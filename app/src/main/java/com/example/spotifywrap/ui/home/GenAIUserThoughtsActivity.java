package com.example.spotifywrap.ui.home;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spotifywrap.R;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GenAIUserThoughtsActivity extends AppCompatActivity {

    private Button nextButton;
    private SongService songService;
    private ArrayList<Song> topTracks;
    private Button buttonUserThoughtsNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.genai_user_thoughts);

        TextView resultTextView = findViewById(R.id.text_user_thought_prediction);
        SongService songService = new SongService(getApplicationContext());
        Handler mainHandler = new Handler(Looper.getMainLooper()); // Get main thread's Handler

        GenAIThoughtsAnalysis genAIAnalysis = new GenAIThoughtsAnalysis(resultTextView, songService, mainHandler);
        buttonUserThoughtsNext = findViewById(R.id.button_user_thought_next);
        buttonUserThoughtsNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GenAIUserThoughtsActivity.this, GenAIUserDressActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }
}