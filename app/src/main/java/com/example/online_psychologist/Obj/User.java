package com.example.online_psychologist.Obj;

import android.os.Build;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import com.example.online_psychologist.App;
import com.example.online_psychologist.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class User {

    public String Key;
    private long chat_id;
    private String username;
    private long date;

    public User(){}

    public User(long chat_id, String username, long date) {
        this.chat_id = chat_id;
        this.username = username;
        this.date = date;
    }

    public long getChat_id() {
        return chat_id;
    }

    public void setChat_id(long chat_id) {
        this.chat_id = chat_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public static void ProfileAvatar(long user_id, ImageView imageView){
        String url = App.sp.getString("UserAvatar"+user_id, "");
        if(!url.isEmpty()){
            Picasso.get().load(url).transform(new CircleTransform()).error(R.drawable.icon_telegram)
                    .placeholder(R.drawable.icon_telegram).into(imageView);
        }
        Call<ProfilePhotos> call = AppAPI.create().getUserProfilePhotos(user_id, 1);
        call.enqueue(new Callback<ProfilePhotos>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ProfilePhotos> call, Response<ProfilePhotos> response) {
                ProfilePhotos.Photos file_id = response.body().getResult().photos.get(0).get(0);
                System.out.println(file_id.getFile_id());//AgACAgIAAxUAAWIzO6HyLUvSnWeFOFjfE76y0xAhAAKupzEb4WVwIskpxFCatbCMAQADAgADYQADIwQ
                Call<ProfilePhotos.UserImageURL> call1 = AppAPI.create().getFile(file_id.getFile_id());
                call1.enqueue(new Callback<ProfilePhotos.UserImageURL>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(Call<ProfilePhotos.UserImageURL> call1, Response<ProfilePhotos.UserImageURL> response1) {
                        ProfilePhotos.UserImageURL userImageURL = response1.body();
                        Picasso.get().load(App.TELEGRAM_FILE_URL+userImageURL.result.getFile_path()).transform(new CircleTransform()).error(R.drawable.icon_telegram)
                                .placeholder(R.drawable.icon_telegram).into(imageView);
                        App.SaveUserAvatar(String.valueOf(user_id), App.TELEGRAM_FILE_URL+userImageURL.result.getFile_path());
                    }
                    @Override
                    public void onFailure(Call<ProfilePhotos.UserImageURL> call1, Throwable t) {
                        System.out.println(t);
                    }
                });
            }
            @Override
            public void onFailure(Call<ProfilePhotos> call, Throwable t) {
                System.out.println(t);
            }
        });
    }
}
