package com.example.spotifywrap;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spotifywrap.R;
import com.example.spotifywrap.ui.home.ChangeAccount;
import com.example.spotifywrap.ui.home.DuoWrap;
import com.example.spotifywrap.ui.home.FirebaseFunction;
import com.example.spotifywrap.ui.home.GenAIUserActionsActivity;
import com.example.spotifywrap.ui.home.Login;
import com.example.spotifywrap.ui.home.RecentSongs;
import com.example.spotifywrap.ui.home.Song;
import com.example.spotifywrap.ui.home.SongService;
import com.example.spotifywrap.ui.home.TopArtists;
import com.example.spotifywrap.ui.home.TopTracksActivity;
import com.example.spotifywrap.ui.home.User;
import com.example.spotifywrap.ui.home.YourWrap;
import com.google.firebase.FirebaseApp;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button buttonSpotifyLogin;
    private TextView greetingMessage;
    boolean isAuthenticated = false;
    private static final int LOGIN_REQUEST_CODE = 1001;
    private SharedPreferences sharedPreferences;
    private Button buttonRecentlyPlayedSong;

    private Button buttonMusicTaste;
    private Button buttonDuoWrapped;
    private Button buttonTopTracks;
    private Button buttonAccount ;

    private User user;

    FirebaseFunction firebaseFunction;
    SongService songService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this);
        }
        songService = new SongService(getApplicationContext());

        buttonAccount = findViewById(R.id.accountButton);
        buttonSpotifyLogin = findViewById(R.id.buttonSpotifyLogin);
        greetingMessage = findViewById(R.id.greetingMessage);
        buttonRecentlyPlayedSong = findViewById(R.id.buttonRecentlyPlayedSong);
        buttonMusicTaste = findViewById(R.id.buttonMusicTaste);
        buttonDuoWrapped = findViewById(R.id.buttonDuoWrapped);
        sharedPreferences = getSharedPreferences("SPOTIFY", MODE_PRIVATE);
        buttonTopTracks = findViewById(R.id.buttonTopTracks);


        SharedPreferences sharedPreferences = getSharedPreferences("SPOTIFY", 0);
        greetingMessage.setText(sharedPreferences.getString("userid", "No User"));


        buttonSpotifyLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                intent.putExtra("LOGIN_REQUEST_CODE", LOGIN_REQUEST_CODE);
                startActivityForResult(intent, LOGIN_REQUEST_CODE);
            }
        });

        buttonRecentlyPlayedSong.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, RecentSongs.class);
            startActivityForResult(intent, 1);
        }

        });

        buttonTopTracks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TopTracksActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        buttonMusicTaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GenAIUserActionsActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        buttonDuoWrapped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DuoWrap.class);
                startActivity(intent);
            }
        });
        buttonAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChangeAccount.class);
                startActivityForResult(intent, 300);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOGIN_REQUEST_CODE) {

            isAuthenticated = data.getBooleanExtra("isAuthenticated", false);
            if (isAuthenticated) {
                Log.d("STARTING", "Got authenticated");
                showGreetingMessage();
                sharedPreferences = getSharedPreferences("SPOTIFY", 0);
                isAuthenticated = sharedPreferences.getBoolean("isAuthenticated", false);
                String token = sharedPreferences.getString("token", "");
                String userId = sharedPreferences.getString("userid", "");
                String email = sharedPreferences.getString("email", "");

                firebaseFunction = new FirebaseFunction();
                Log.d("USERNAME IS", sharedPreferences.getString("username", "No User"));
                firebaseFunction.getUsername(email, new FirebaseFunction.OnUsernameResultListener() {
                    @Override
                    public void onUsernameResult(String name) {
                        String username;

                        Log.d("USERNAME", name);

                        if (name == null) {
                            username = sharedPreferences.getString("username", "No User");
                            Log.d("NOO", sharedPreferences.getString("username", "No User"));


                        } else {
                            username = name;
                            Log.d("STARTING", "Found username");
                        }
                        greetingMessage.setText("Welcome," + username);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username", username);
                        editor.apply();
                    }
                });

                songService.getTopTracks(() -> {
                    ArrayList<Song> topTracks = songService.getSongs();
                    firebaseFunction.storeUser(getApplicationContext(), sharedPreferences.getString("username", ""),
                            email,
                            topTracks
                    );
                });


                showAuthenticatedViews();

            } else {
                Log.d("STARTING", "Not authenticated");
                showLoginButton();
            }
        } else if (requestCode == 300) {
            if (resultCode == RESULT_OK) {
                boolean isLogout = data.getBooleanExtra("isLogout", false);
                String newUsername = data.getStringExtra("newUsername");

                if (isLogout) {
                    performLogout();

                } else if (newUsername != null) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", newUsername);
                    editor.apply();
                    greetingMessage.setText("Welcome, " + newUsername);
                }
            }
        } else {
            showAuthenticatedViews();
        }
    }

    private void showAuthenticatedViews() {
        greetingMessage.setVisibility(View.VISIBLE);
        greetingMessage.setText("Welcome, " + sharedPreferences.getString("username", "No User"));
        buttonSpotifyLogin.setVisibility(View.GONE);
        buttonRecentlyPlayedSong.setVisibility(View.VISIBLE);
        buttonMusicTaste.setVisibility(View.VISIBLE);
        buttonDuoWrapped.setVisibility(View.VISIBLE);
        buttonTopTracks.setVisibility(View.VISIBLE);
        buttonAccount.setVisibility(View.VISIBLE);
    }
    private void showGreetingMessage() {
        greetingMessage.setVisibility(View.VISIBLE);
        buttonSpotifyLogin.setVisibility(View.GONE);
    }

    private void showLoginButton() {
        greetingMessage.setVisibility(View.GONE);
        buttonSpotifyLogin.setVisibility(View.VISIBLE);
        buttonRecentlyPlayedSong.setVisibility(View.GONE);
        buttonMusicTaste.setVisibility(View.GONE);
        buttonDuoWrapped.setVisibility(View.GONE);
        buttonTopTracks.setVisibility(View.GONE);
        buttonAccount.setVisibility(View.GONE);

    }
    private void performLogout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isAuthenticated", false);
        editor.remove("token");
        editor.remove("userid");
        editor.remove("username");
        editor.remove("email");
        editor.apply();


        showLoginButton();


    }
    protected void onDestroy() {
        super.onDestroy();
    }
    private void storeUser(String username, String email, ArrayList<Song> topTracks) {
        firebaseFunction.storeUser(getApplicationContext(),username, email, topTracks);
    }


}
