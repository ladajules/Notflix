package com.ladajules.notflix.data.model;

public class HeroImage{
    private int imageResId;
    private String title;

    public HeroImage(int imageResId, String title){
        this.imageResId = imageResId;
        this.title = title;
    }

    public int getImageResId(){
        return imageResId;
    }

    public String getTitle(){
        return title;
    }
}