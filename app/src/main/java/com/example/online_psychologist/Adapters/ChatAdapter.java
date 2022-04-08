package com.example.online_psychologist.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.online_psychologist.Activitys.MessagesActivity;
import com.example.online_psychologist.App;
import com.example.online_psychologist.Obj.AppAPI;
import com.example.online_psychologist.Obj.User;
import com.example.online_psychologist.R;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.example.online_psychologist.Obj.Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private Context context;
    private ArrayList<Chat> list;

    private Animation animClick;

    public ChatAdapter(Context context, ArrayList<Chat> list) {
        this.context = context;
        this.list = list;
        animClick = AnimationUtils.loadAnimation(context, R.anim.btn_anim_click);
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_item, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chat = list.get(position);
        User.ProfileAvatar(Long.parseLong(chat.getChat_id()),holder.UserAvatar);
        holder.NicNameText.setText(chat.getUsername());

        holder.ChatContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animClick);
                Intent intent = new Intent();
                intent.setClass(context, MessagesActivity.class);
                intent.putExtra("chat_id",chat.getChat_id());
                intent.putExtra("username",chat.getUsername());
                context.startActivity(intent);
            }
        });
        holder.Btn_deleteChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animClick);
                AlertDialog.Builder dialog1 = new AlertDialog.Builder(context);
                dialog1.setTitle(context.getString(R.string.delete_chat));
                dialog1.setMessage(context.getString(R.string.you_want_delete_chat));
                dialog1.setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance(App.DB_URL).getReference(App.CHATS).child(Build.ID.replace('.','-'))
                                .child(chat.getChat_id()).removeValue();
                        FirebaseDatabase.getInstance(App.DB_URL).getReference(App.CON_STARTED).child(chat.getChat_id()).removeValue();
                        Call<String> call = AppAPI.create().getThemesListData(Long.parseLong(chat.getChat_id()), "Психолог "+ FirebaseAuth.getInstance().getCurrentUser()
                                .getDisplayName()+" закінчує з вами спілкування!");
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
                        int newPos = holder.getAdapterPosition();
                        if(!list.isEmpty()) {
                            list.remove(newPos);
                        }
                        notifyItemRemoved(newPos);
                    }
                });
                dialog1.setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog newDialog1 = dialog1.create();
                newDialog1.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout ChatContainer;
        ImageView UserAvatar;
        TextView NicNameText;
        ImageButton Btn_deleteChat;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            ChatContainer = itemView.findViewById(R.id.ChatContainer);
            UserAvatar = itemView.findViewById(R.id.UserAvatar);
            NicNameText = itemView.findViewById(R.id.NicNameText);
            Btn_deleteChat = itemView.findViewById(R.id.Btn_deleteChat);
        }
    }
}
