package com.example.spotifywrap.ui.home;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotifywrap.MainActivity;
import com.example.spotifywrap.R;
import java.util.ArrayList;
import com.example.spotifywrap.ui.home.Song;
import com.example.spotifywrap.ui.home.SongAdapter;
import com.example.spotifywrap.ui.home.SongService;
public class TopTracksActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageButton backButton;
    private ImageButton nextButton;
    private SongService songService;
    private SongAdapter adapter;
    private ArrayList<Song> topTracks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_tracks);

        songService = new SongService(getApplicationContext());


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        backButton = findViewById(R.id.backTopTrack);
        nextButton = findViewById(R.id.nextArtistButton);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TopTracksActivity.this, TopArtists.class);
                startActivity(intent);
            }
        });

        getTopTracks();
    }

    private void getTopTracks() {
        songService.getTopTracks(() -> {
            topTracks = songService.getSongs();
            updateSong();
        });
    }

    private void updateSong() {
        if (topTracks.size() > 0) {
            adapter = new SongAdapter(this, topTracks);
            recyclerView.setAdapter(adapter);
        } else {
            // Show a message or handle the case where no songs are retrieved
            Toast.makeText(this, "No songs found", Toast.LENGTH_SHORT).show();
        }
    }
}
