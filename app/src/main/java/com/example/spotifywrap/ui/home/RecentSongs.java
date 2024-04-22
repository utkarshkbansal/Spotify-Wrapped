package com.example.spotifywrap.ui.home;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotifywrap.databinding.ActivityRecentSongsBinding;

import com.example.spotifywrap.R;

import java.util.ArrayList;

public class RecentSongs extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityRecentSongsBinding binding;
    SongService songService ;
    private SongAdapter adapter;
    private ArrayList<Song> recentlyPlayedTracks;
    private RecyclerView recyclerView;
    private ImageButton backButton ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_songs);

        songService = new SongService(getApplicationContext());

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getTracks();
        backButton = findViewById(R.id.backRecentSongs);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void getTracks() {
        songService.getRecentlyPlayedTracks(() -> {
            recentlyPlayedTracks = songService.getSongs();
            updateSong();
        });
    }

    private void updateSong() {
        if (recentlyPlayedTracks.size() > 0) {
            adapter = new SongAdapter(this, recentlyPlayedTracks);
            recyclerView.setAdapter(adapter);
        }
    }


}