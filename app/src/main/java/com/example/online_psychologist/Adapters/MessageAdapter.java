package com.example.online_psychologist.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daasuu.bl.ArrowDirection;
import com.daasuu.bl.BubbleLayout;
import com.example.online_psychologist.Obj.Message;
import com.example.online_psychologist.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{

    private Context context;
    private ArrayList<Message> list;
    private String chat_id;

    public MessageAdapter(Context context, ArrayList<Message> list, String chat_id) {
        this.context = context;
        this.list = list;
        this.chat_id = chat_id;
    }

    @NonNull
    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false);
        return new MessageAdapter.MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MessageViewHolder holder, int position) {
        Message message = list.get(position);

        if(message.getFrom().equals(chat_id)){
            holder.MessageContainer.setPadding(0,0,40,0);
            holder.MessageContainer.setGravity(Gravity.LEFT);
            holder.MessageBubble.setArrowDirection(ArrowDirection.LEFT);
        }else if(message.getFrom().equals(Build.ID.replace('.','-'))){
            holder.MessageContainer.setPadding(40,0,0,0);
            holder.MessageContainer.setGravity(Gravity.RIGHT);
            holder.MessageBubble.setArrowDirection(ArrowDirection.RIGHT);
        }

        holder.DataMessageText.setText(message.getText());

        Date date = new Date(message.getDate()*1000L);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd.MM.yy");
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Kiev"));
        holder.DateMessageText.setText(sdf.format(date));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        LinearLayout MessageContainer;
        BubbleLayout MessageBubble;
        TextView DataMessageText;
        TextView DateMessageText;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            MessageContainer = itemView.findViewById(R.id.MessageContainer);
            MessageBubble = itemView.findViewById(R.id.MessageBubble);
            DataMessageText = itemView.findViewById(R.id.DataMessageText);
            DateMessageText = itemView.findViewById(R.id.DateMessageText);
        }
    }
}
