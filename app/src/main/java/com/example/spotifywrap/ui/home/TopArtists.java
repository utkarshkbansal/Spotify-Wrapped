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

import java.util.ArrayList;import java.util.HashSet;
import java.util.stream.Collectors;


public class TopArtists extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageButton backButton;
    private ImageButton nextButton;

    private ArtistAdapter adapter;
    private ArrayList<Artist> topArtists;
    private SongService songService ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_artists);


        recyclerView = findViewById(R.id.recyclerViewTopArtists);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        backButton = findViewById(R.id.backButtonArtist);
        nextButton = findViewById(R.id.nextGenreButton);

        songService = new SongService(getApplicationContext());
        getTopArtists();
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(TopArtists.this, TopGenre.class);
                    HashSet<String> set = new HashSet<>();

                    for (Artist artist : topArtists) {
                        set.addAll(artist.getGenre());
                    }

                    String s = set.stream().collect(Collectors.joining(", "));
                Log.d("current s", s);


                intent.putExtra("genresString", s);
                    startActivity(intent);

            }
        });



    }

    private void getTopArtists() {
        songService.getTopArtists(() -> {
            topArtists = songService.getArtists();
            Log.d("GOT ARTISTS", topArtists.get(0).getGenre().toString());
            updateArtists();

        });

    }

    private void updateArtists() {
        if (topArtists.size()>0) {
            adapter = new ArtistAdapter(this, topArtists);
            recyclerView.setAdapter(adapter);

        } else {
            Toast.makeText(this, "No artists found", Toast.LENGTH_SHORT).show();
        }
    }

}
