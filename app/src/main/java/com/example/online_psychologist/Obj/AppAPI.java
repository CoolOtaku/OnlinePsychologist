package com.example.online_psychologist.Obj;

import com.example.online_psychologist.App;
import com.example.online_psychologist.Intarfaces.Server;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppAPI {
    public static Server create(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(App.TELEGRAM_BOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Server api = retrofit.create(Server.class);
        return api;
    }
}
