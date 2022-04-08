package com.example.online_psychologist.Obj;

public class ConStarted {

    public String Key;
    private String tokenId;
    private String psychologistName;
    private long chat_id;
    private String username;

    public ConStarted() {}

    public ConStarted(String tokenId, String psychologistName, long chat_id, String username) {
        this.tokenId = tokenId;
        this.psychologistName = psychologistName;
        this.chat_id = chat_id;
        this.username = username;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String token) {
        this.tokenId = token;
    }

    public String getPsychologistName() {
        return psychologistName;
    }

    public void setPsychologistName(String psychologistName) {
        this.psychologistName = psychologistName;
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
}
