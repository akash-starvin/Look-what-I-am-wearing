package com.example.android.lookwhatiamwaring2;

public class FBInsertData {

    String des, date, email, url;

    public FBInsertData(String des, String date, String email, String url) {
        this.des = des;
        this.date = date;
        this.email = email;
        this.url = url;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
