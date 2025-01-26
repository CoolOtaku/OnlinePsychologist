package com.example.online_psychologist;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.online_psychologist.Obj.Chat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class App extends Application {

    public static SharedPreferences sp;
    public static int SIGN_IN_CODE = 1;

    public static final String BOT_TOKEN = "[TELEGRAM_BOT_TOKEN]";
    public static final String TELEGRAM_BOT_URL = "https://api.telegram.org/bot"+BOT_TOKEN+"/";
    public static final String TELEGRAM_FILE_URL = "https://api.telegram.org/file/bot"+BOT_TOKEN+"/";

    public static String DB_URL = "[FIREBASE_DATA_BASE_URL]";
    public static String TOKENS = "AndroidTokens";
    public static String CHATS = "chats";
    public static String COMMUNICATE = "communicate";
    public static String CON_STARTED = "conversation_started";

    @Override
    public void onCreate(){
        super.onCreate();
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    public static void SaveToken(String token){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("token", token);
        editor.commit();
    }
    public static ArrayList<Chat> LoadChatsOnStorage(){
        Gson gson = new Gson();
        String jsonChats = sp.getString("chats", "");
        if(jsonChats.isEmpty()){
            return new ArrayList<Chat>();
        }
        Type type = new TypeToken<ArrayList<Chat>>(){}.getType();
        return gson.fromJson(jsonChats, type);
    }
    public static void SaveChatsInStorage(ArrayList<Chat> list){
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String jsonChats = gson.toJson(list);
        editor.putString("chats", jsonChats);
        editor.apply();
    }
    public static void SaveUserAvatar(String user_id, String url){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("UserAvatar"+user_id, url);
        editor.commit();
    }
}
