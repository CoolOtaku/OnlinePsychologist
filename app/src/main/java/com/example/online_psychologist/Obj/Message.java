package com.example.online_psychologist.Obj;

public class Message {

    public String Key;
    private String from;
    private String text;
    private long date;

    public Message() { }

    public Message(String from, String text, long date) {
        this.from = from;
        this.text = text;
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
