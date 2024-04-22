package com.example.spotifywrap.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotifywrap.R;
import com.example.spotifywrap.databinding.ActivityYourWrapBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;


public class YourWrap extends AppCompatActivity {

    private ActivityYourWrapBinding binding;
    private SongService songService;
    private SongAdapter songAdapter;
    private ArtistAdapter artistAdapter;
    private TextView genresTextView; // Add this line
    private ArrayList<Song> topTracks;

    private ArrayList<Artist> topArtists;
    private RecyclerView songrecyclerView;
    private RecyclerView artistrecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_wrap);
        genresTextView = findViewById(R.id.genresTextView);

        songService = new SongService(getApplicationContext());
//        songrecyclerView = findViewById(R.id.recyclerViewWrapTopTracks);
//        artistrecyclerView = findViewById(R.id.recyclerViewWrapTopArtists);
        songrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        artistrecyclerView.setLayoutManager(new LinearLayoutManager(this));



        populateData();
    }


    private void populateData() {
        songService.getTopTracks(() -> {
            topTracks = songService.getSongs();
            if (topTracks != null) {
                Log.d("error", "got song" + topTracks.toString());
            } else {
                Log.e("error", "Failed to get top tracks");
            }
            getTopArtists();
        });


        displayTopTracksAndArtists();
    }
    private void getTopArtists() {
        songService.getTopArtists(() -> {
            topArtists = songService.getArtists();
            if (topArtists != null) {
                displayTopArtists();
                updateGenresTextView(topArtists);
                Log.d("error", "got these" + topTracks.toString() + topArtists.toString());
            } else {
                Log.e("error", "Failed to get top artists");
            }
        });
    }
    private void displayTopTracksAndArtists() {
        synchronized (this) {
            if (topTracks.size() > 0) {
                songAdapter = new SongAdapter(this, topTracks);
                songrecyclerView.setAdapter(songAdapter);
            } else {
                Toast.makeText(this, "No songs found", Toast.LENGTH_SHORT).show();
            }

            if (topArtists.size() > 0) {
                artistAdapter = new ArtistAdapter(this, topArtists);
                artistrecyclerView.setAdapter(artistAdapter);
            } else {
                Toast.makeText(this, "No artists found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateGenresTextView(ArrayList<Artist> topArtists) {
        HashSet<String> genres = new HashSet<>();
        for (Artist artist : topArtists) {
            genres.addAll(artist.getGenre());
        }
        StringBuilder genresBuilder = new StringBuilder();
        for (String genre : genres) {
            genresBuilder.append(genre).append(", ");
        }
        String genresText = genresBuilder.substring(0, genresBuilder.length() - 2);
        genresTextView.setText(genresText);
    }

    private void displayTopTracks() {
        if (topTracks.size() > 0) {
            songAdapter = new SongAdapter(this, topTracks);
            songrecyclerView.setAdapter(songAdapter);
        } else {
            Toast.makeText(this, "No songs found", Toast.LENGTH_SHORT).show();
        }
    }
    private void displayTopArtists() {

        if (topArtists.size() > 0) {
            artistAdapter = new ArtistAdapter(this, topArtists);
            artistrecyclerView.setAdapter(artistAdapter);
        } else {
            Toast.makeText(this, "No artists found", Toast.LENGTH_SHORT).show();
        }

    }

}