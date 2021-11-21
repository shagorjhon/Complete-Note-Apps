package com.example.noteapplication;

import android.graphics.Bitmap;

import java.io.Serializable;

public class ModelClass implements Serializable {
    int id;
    String title;
    String description;
    String date;
    byte[] image;

    public ModelClass(int id, String title, String description, String date, byte[] image) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.image = image;
    }

    public ModelClass(String title, String description, String date, byte[] image) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.image = image;
    }

    public ModelClass(int id,String title, String description) {
        this.title = title;
        this.description = description;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
