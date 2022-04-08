package com.example.online_psychologist.Obj;

public class Chat {

    private String chat_id;
    private String username;

    public Chat() { }

    public Chat(String chat_id, String username) {
        this.chat_id = chat_id;
        this.username = username;
    }

    public String getChat_id() {
        return chat_id;
    }

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
