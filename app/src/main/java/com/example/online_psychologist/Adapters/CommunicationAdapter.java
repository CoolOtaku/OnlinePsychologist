package com.example.online_psychologist.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.example.online_psychologist.App;
import com.example.online_psychologist.Obj.AppAPI;
import com.example.online_psychologist.Obj.ConStarted;
import com.example.online_psychologist.Obj.Message;
import com.example.online_psychologist.Obj.User;
import com.example.online_psychologist.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommunicationAdapter extends RecyclerView.Adapter<CommunicationAdapter.UsersViewHolder> {

    private Context context;
    private ArrayList<User> list;
    private DatabaseReference refCommunication;
    private AlertDialog dialogList;

    private Animation animClick;

    public CommunicationAdapter(Context context, ArrayList<User> list, DatabaseReference refCommunication, AlertDialog dialog) {
        this.context = context;
        this.list = list;
        this.refCommunication = refCommunication;
        this.dialogList = dialog;
        animClick = AnimationUtils.loadAnimation(context, R.anim.btn_anim_click);
    }

    @NonNull
    @Override
    public CommunicationAdapter.UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new CommunicationAdapter.UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunicationAdapter.UsersViewHolder holder, int position) {
        User user = list.get(position);
        User.ProfileAvatar(user.getChat_id(),holder.UserAvatar);
        holder.NicNameUser.setText(user.getUsername());

        Date date = new Date(user.getDate()*1000L);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd.MM.yy");
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Kiev"));
        holder.DateCommunicateUser.setText(sdf.format(date));

        holder.UserContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animClick);
                AlertDialog.Builder dialogExit = new AlertDialog.Builder(context);
                dialogExit.setTitle(context.getString(R.string.go_to_communicate));
                dialogExit.setMessage(context.getString(R.string.con_started));
                dialogExit.setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        dialogList.cancel();
                        refCommunication.child(user.Key).removeValue();
                        DatabaseReference ref = FirebaseDatabase.getInstance(App.DB_URL).getReference(App.CON_STARTED);
                        ref.child(user.Key).setValue(
                                new ConStarted(Build.ID.replace('.','-'), FirebaseAuth.getInstance().getCurrentUser()
                                .getDisplayName(), user.getChat_id(), user.getUsername())
                        );
                        long unixTime = Instant.now().getEpochSecond();
                        DatabaseReference ref2 = FirebaseDatabase.getInstance(App.DB_URL).getReference(App.CHATS);
                        ref2.child(Build.ID.replace('.','-')).child(user.Key).push().setValue(
                                new Message(Build.ID.replace('.','-'),"Я готовий спілкуватися!", unixTime)
                        );
                        Call<String> call = AppAPI.create().getThemesListData(user.getChat_id(), "Психолог "+FirebaseAuth.getInstance().getCurrentUser()
                                .getDisplayName()+" готовий з вами спілкуватися!");
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
                    }
                });
                dialogExit.setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog newDialog2 = dialogExit.create();
                newDialog2.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout UserContainer;
        ImageView UserAvatar;
        TextView NicNameUser, DateCommunicateUser;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            UserContainer = itemView.findViewById(R.id.UserContainer);
            UserAvatar = itemView.findViewById(R.id.UserAvatar);
            NicNameUser = itemView.findViewById(R.id.NicNameUser);
            DateCommunicateUser = itemView.findViewById(R.id.DateCommunicateUser);
        }
    }
}
