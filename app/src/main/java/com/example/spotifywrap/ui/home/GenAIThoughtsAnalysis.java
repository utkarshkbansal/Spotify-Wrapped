package com.example.spotifywrap.ui.home;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spotifywrap.R;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GenAIThoughtsAnalysis {
    private TextView resultTextView;
    private SongService songService;
    private ArrayList<Song> topTracks;
    private StringBuilder response;
    private Handler mainHandler;

    public GenAIThoughtsAnalysis(TextView resultTextView, SongService songService, Handler mainHandler) {
        this.resultTextView = resultTextView;
        this.songService = songService;
        this.response = new StringBuilder();
        this.mainHandler = mainHandler;
        getTopTracks();
    }

    private void getTopTracks() {
        songService.getTopTracks(() -> {
            topTracks = songService.getSongs();
            callFlaskAPI();
        });
    }

    private void callFlaskAPI() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url("http://10.91.0.94:8888/api/user/thoughts?music_taste=\"" + topTracks.toString() + "\"").build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("GenAIAnalysis", "API Failure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseBody = response.body().string();
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        resultTextView.setText(responseBody);
                    }
                });
            }
        });
        Log.d("GenAIAnalysis", "topTracks: " + topTracks.toString());
//        onPostExecute(response.toString());
    }

    protected void onPostExecute(String result) {
        Log.d("GenAIAnalysis", "Response from API: " + response);
        resultTextView.setText(result);
    }
}
