package com.example.android.lookwhatiamwaring2;

public class ModelData {
    String url, date, des, key;

    public ModelData(String url, String date, String des, String key) {
        this.url = url;
        this.date = date;
        this.des = des;
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getDBKey() {
        return key;
    }

    public void setDBKey(String key) {
        this.key = key;
    }
}
