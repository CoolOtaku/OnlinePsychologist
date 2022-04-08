package com.example.online_psychologist.Services;


import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;
import com.example.online_psychologist.App;
import com.example.online_psychologist.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class FireBaseMessageService extends FirebaseMessagingService {

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        String title = remoteMessage.getNotification().getTitle();
        String text = remoteMessage.getNotification().getBody();
        final  String CHANNEL_ID = "FBNotificationChannelOP";
        NotificationChannel channel = null;
            channel = new NotificationChannel(
                    CHANNEL_ID,"This is channel from bot message",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification.Builder notifi = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.logo)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSound(soundUri)
                .setAutoCancel(true);
        NotificationManagerCompat.from(this).notify(1,notifi.build());
        super.onMessageReceived(remoteMessage);
    }

    @Override
    public void onNewToken(@NonNull String s) {
        App.SaveToken(s);
        String deviseId = Build.ID.replace('.','-');
        Task<Void> ref = FirebaseDatabase.getInstance(App.DB_URL)
                .getReference(App.TOKENS+"/"+deviseId).setValue(s);
        super.onNewToken(s);
    }
}
