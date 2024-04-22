package com.example.spotifywrap.ui.home;

import androidx.annotation.NonNull;

import java.util.List;

public class Song {

    private String id;
    private String name;

    private String imageUrl ;
    private List<Artist> artists;

    public Song(String id, String name, String imageUrl) {
        this.name = name;
        this.id = id;
        this.imageUrl = imageUrl;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
    public List<Artist> getArtists() {
        return this.artists;
    }
    public void setArtists(List<Artist> a) {
        this.artists = a ;
    }

    @Override
    public String toString() {
        return "Song{" +
                "name='" + name + '\'' +
                ", artists=" + artists +
                '}';
    }
}