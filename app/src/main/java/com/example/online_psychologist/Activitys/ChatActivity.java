package com.example.online_psychologist.Activitys;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.online_psychologist.Adapters.ChatAdapter;
import com.example.online_psychologist.Adapters.CommunicationAdapter;
import com.example.online_psychologist.App;
import com.example.online_psychologist.Obj.Chat;
import com.example.online_psychologist.Obj.ConStarted;
import com.example.online_psychologist.Obj.User;
import com.example.online_psychologist.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    private ChatAdapter adapter;
    private RecyclerView MessageListView;
    private ImageButton Btn_new_communication;
    private ImageButton Btn_exit;
    private ImageView Logo;

    private DatabaseReference ref;
    private DatabaseReference refCommunication;
    private ArrayList<Chat> dataList = new ArrayList<>();
    private ArrayList<User> arr_users_list = new ArrayList<>();
    private CommunicationAdapter communicationAdapter;
    private Animation animClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        MessageListView = (RecyclerView) findViewById(R.id.MessageListView);
        Btn_new_communication = (ImageButton) findViewById(R.id.Btn_new_communication);
        Btn_exit = (ImageButton) findViewById(R.id.Btn_exit);
        Logo = (ImageView) findViewById(R.id.Logo);

        MessageListView.setHasFixedSize(true);
        MessageListView.setLayoutManager(new LinearLayoutManager(this));

        ref = FirebaseDatabase.getInstance(App.DB_URL).getReference(App.CHATS).child(Build.ID.replace('.','-'));
        displayAllChats();
        refCommunication = FirebaseDatabase.getInstance(App.DB_URL).getReference(App.COMMUNICATE);
        checkNewCommunication();

        Btn_new_communication.setOnClickListener(this);
        Btn_exit.setOnClickListener(this);
        Logo.setOnClickListener(this);
        animClick = AnimationUtils.loadAnimation(this, R.anim.btn_anim_click);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        view.startAnimation(animClick);
        switch (view.getId()){
            case R.id.Btn_new_communication:
                AlertDialog.Builder dialogComm = new AlertDialog.Builder(ChatActivity.this);
                dialogComm.setTitle(getString(R.string.users_who_want_to_communicate));
                View v = View.inflate(ChatActivity.this,R.layout.list_users,null);
                dialogComm.setView(v);
                AlertDialog newDialog = dialogComm.create();
                RecyclerView ListNewUserComm = (RecyclerView) v.findViewById(R.id.ListNewUserComm);
                LinearLayoutManager llm = new LinearLayoutManager(ChatActivity.this);
                ListNewUserComm.setLayoutManager(llm);
                communicationAdapter = new CommunicationAdapter(ChatActivity.this, arr_users_list, refCommunication, newDialog);
                ListNewUserComm.setAdapter(communicationAdapter);
                newDialog.show();
                break;
            case R.id.Btn_exit:
                AlertDialog.Builder dialogExit = new AlertDialog.Builder(ChatActivity.this);
                dialogExit.setTitle(getString(R.string.log_out));
                dialogExit.setMessage(getString(R.string.log_out_enable));
                dialogExit.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        AuthUI.getInstance().signOut(ChatActivity.this);
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent();
                        intent.setClass(ChatActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                dialogExit.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog newDialog2 = dialogExit.create();
                newDialog2.show();
                break;
            case R.id.Logo:
                AlertDialog.Builder dialogContacts = new AlertDialog.Builder(ChatActivity.this);
                View vv = View.inflate(ChatActivity.this,R.layout.contacts_layout,null);
                dialogContacts.setView(vv);
                ImageView btn_instagram = vv.findViewById(R.id.btn_instagram);
                ImageView btn_facebook = vv.findViewById(R.id.btn_facebook);
                View.OnClickListener onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.startAnimation(animClick);
                        Intent intent = new Intent();
                        Uri uri = null;
                        switch(v.getId()){
                            case R.id.btn_instagram:
                                uri = Uri.parse(getString(R.string.instagram_url));
                                intent.setPackage("com.instagram.android");
                                break;
                            case R.id.btn_facebook:
                                uri = Uri.parse(getString(R.string.facebook_url));
                                intent.setPackage("com.facebook.android");
                                break;
                        }
                        try {
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setData(uri);
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW,uri));
                        }
                    }
                };
                btn_instagram.setOnClickListener(onClickListener);
                btn_facebook.setOnClickListener(onClickListener);
                TextView textViewPlayerTrackInfo = vv.findViewById(R.id.textViewPlayerTrackInfo);
                textViewPlayerTrackInfo.setSelected(true);

                AlertDialog Dialog = dialogContacts.create();
                Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                Dialog.show();
                break;
        }
    }
    private void displayAllChats(){
        dataList = App.LoadChatsOnStorage();
        adapter = new ChatAdapter(ChatActivity.this, dataList);
        MessageListView.setAdapter(adapter);

        ref.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for(DataSnapshot item : snapshot.getChildren()){
                    String key = item.getKey();
                    Task<DataSnapshot> loc_ref = FirebaseDatabase.getInstance(App.DB_URL)
                    .getReference(App.CON_STARTED).child(key).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            ConStarted res = task.getResult().getValue(ConStarted.class);
                            Chat chat = new Chat(key,res.getUsername());
                            dataList.add(chat);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
                App.SaveChatsInStorage(dataList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
    private void checkNewCommunication(){
        refCommunication.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.hasChildren()){
                    Btn_new_communication.setVisibility(View.GONE);
                    return;
                }
                arr_users_list.clear();
                Btn_new_communication.setVisibility(View.VISIBLE);
                for(DataSnapshot item : snapshot.getChildren()){
                    User user = item.getValue(User.class);
                    user.Key = item.getKey();
                    arr_users_list.add(user);
                }
                try {
                    communicationAdapter.notifyDataSetChanged();
                }catch (Exception e){}
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
}