package com.example.spotifywrap.ui.home;

import android.os.Parcelable;

import java.util.ArrayList;


public class Artist  {
    private String name ;
    private String imageUrl = "" ;
    private ArrayList<String> genre;

    public Artist(String name) {
        this.name = name ;

    }
    public Artist(String name, String imageUrl) {
        this.name = name ;
        this.imageUrl = imageUrl;
    }
    public Artist(String name, String imageUrl, ArrayList<String> genre) {
        this.name = name ;
        this.imageUrl = imageUrl;
        this.genre = genre ;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getImageUrl() { return this.imageUrl; }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl ;
    }
    public ArrayList<String> getGenre() {
        return this.genre;
    }
    public void setGenre(ArrayList<String> genre) {
        this.genre = genre;
    }


    @Override
    public String toString() {
        return "Artist{" +
                "name='" + name + '\'' +
                '}';
    }
}
