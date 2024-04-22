package com.example.spotifywrap.ui.home;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spotifywrap.R;

import java.util.Arrays;
import java.util.List;

public class TopGenre extends AppCompatActivity {

    private ImageButton backButton;
    private TextView genresTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_wrap);

        backButton = findViewById(R.id.backGenre);
        genresTextView = findViewById(R.id.genresTextView);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close the activity and return to the previous one
            }
        });

        // Get the comma-separated genres string from intent extra
        String genresString = getIntent().getStringExtra("genresString");

        if (genresString != null && !genresString.isEmpty()) {
            // Split the string into individual genres
            List<String> genresList = Arrays.asList(genresString.split(", "));

            // Join the genres with newline characters to display vertically
            String genresText = String.join("\n", genresList);

            // Set the genres text to the TextView
            genresTextView.setText(genresText);
        } else {
            // Handle the case where genresString is empty or null
            genresTextView.setText("No genres found");
        }
    }
}
