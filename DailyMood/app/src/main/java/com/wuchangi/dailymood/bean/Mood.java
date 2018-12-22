package com.wuchangi.dailymood.bean;

/**
 * Created by WuchangI on 2018/12/21.
 */

public class Mood {

    private String date;
    private String description;
    private String picturePath;

    public Mood(){

    }

    public Mood(String date, String description, String picturePath) {
        this.date = date;
        this.description = description;
        this.picturePath = picturePath;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }
}
