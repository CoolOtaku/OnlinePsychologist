package com.example.online_psychologist.Activitys;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.online_psychologist.Adapters.MessageAdapter;
import com.example.online_psychologist.App;
import com.example.online_psychologist.Obj.AppAPI;
import com.example.online_psychologist.Obj.Message;
import com.example.online_psychologist.Obj.User;
import com.example.online_psychologist.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.time.Instant;
import java.util.ArrayList;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagesActivity extends AppCompatActivity implements View.OnClickListener {

    private ConstraintLayout MessageWindow;
    private ImageView UserAvatarChat;
    private TextView UserNameChat;
    private RecyclerView ListMessage;
    private ImageButton Btn_emoji;
    private EmojiconEditText editTextMessage;
    private ImageButton Btn_sendMessage;
    private ImageButton Btn_clearChat;

    private EmojIconActions emojIconActions;
    private DatabaseReference refMessage;
    private ArrayList<Message> listMessage = new ArrayList<>();
    private MessageAdapter messageAdapter;
    private String chat_id;
    private String username;

    private Animation animClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        MessageWindow = (ConstraintLayout) findViewById(R.id.MessageWindow);
        UserAvatarChat = (ImageView) findViewById(R.id.UserAvatarChat);
        UserNameChat = (TextView) findViewById(R.id.UserNameChat);
        ListMessage = (RecyclerView) findViewById(R.id.ListMessage);
        Btn_emoji = (ImageButton) findViewById(R.id.Btn_emoji);
        editTextMessage = (EmojiconEditText) findViewById(R.id.editTextMessage);
        Btn_sendMessage = (ImageButton) findViewById(R.id.Btn_sendMessage);
        Btn_clearChat = (ImageButton) findViewById(R.id.Btn_clearChat);

        ListMessage.setHasFixedSize(true);
        ListMessage.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        chat_id = intent.getStringExtra("chat_id");
        username = intent.getStringExtra("username");
        UserNameChat.setText(username);

        emojIconActions = new EmojIconActions(getApplicationContext(), MessageWindow, editTextMessage, Btn_emoji);
        emojIconActions.setUseSystemEmoji(true);

        refMessage = FirebaseDatabase.getInstance(App.DB_URL).getReference(App.CHATS).child(Build.ID.replace('.','-')).child(chat_id);
        getMessages();

        Btn_sendMessage.setOnClickListener(this);
        Btn_clearChat.setOnClickListener(this);
        Btn_emoji.setOnClickListener(this);
        animClick = AnimationUtils.loadAnimation(this, R.anim.btn_anim_click);

        User.ProfileAvatar(Long.parseLong(chat_id), UserAvatarChat);
    }

    private void getMessages(){
        messageAdapter = new MessageAdapter(MessagesActivity.this, listMessage, chat_id);
        ListMessage.setAdapter(messageAdapter);
        refMessage.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listMessage.clear();
                for(DataSnapshot item : snapshot.getChildren()){
                    Message message = item.getValue(Message.class);
                    message.Key = item.getKey();
                    listMessage.add(message);
                }
                try {
                    messageAdapter.notifyDataSetChanged();
                    ListMessage.scrollToPosition(listMessage.size()-1);
                }catch (Exception e){}
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {
        view.startAnimation(animClick);
        switch (view.getId()){
            case R.id.Btn_sendMessage:
                String data = editTextMessage.getText().toString();
                editTextMessage.setText("");
                if(data.isEmpty()){
                    Toast.makeText(MessagesActivity.this, getString(R.string.text_message_is_empty), Toast.LENGTH_LONG).show();
                    break;
                }
                long unixTime = Instant.now().getEpochSecond();
                refMessage.push().setValue(
                    new Message(Build.ID.replace('.','-'),data, unixTime)
                );

                Call<String> call = AppAPI.create().getThemesListData(Long.parseLong(chat_id), data);
                call.enqueue(new Callback<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        System.out.println(response);
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        System.out.println(t);
                    }
                });
                break;
            case R.id.Btn_clearChat:
                AlertDialog.Builder dialog1 = new AlertDialog.Builder(MessagesActivity.this);
                dialog1.setTitle(getString(R.string.clear_chat));
                dialog1.setMessage(getString(R.string.you_want_clear_chat));
                dialog1.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        for (Message message: listMessage) {
                            if(listMessage.get(0).Key != message.Key){
                                refMessage.child(message.Key).removeValue();
                            }
                        }
                    }
                });
                dialog1.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog newDialog1 = dialog1.create();
                newDialog1.show();
                break;
            case R.id.Btn_emoji:
                emojIconActions.ShowEmojIcon();
                break;
        }
    }
}