package com.example.spotifywrap;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {
    private static final String PREF_NAME = "Spotify Wrap";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static SharedPreferencesManager instance;

    private SharedPreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static synchronized SharedPreferencesManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesManager(context.getApplicationContext());
        }
        return instance;
    }

    public void saveAuthToken(String token) {
        editor.putString("authToken", token);
        editor.apply();
    }

    public String getAuthToken() {
        return sharedPreferences.getString("authToken", null);
    }

    public void saveUserId(String userId) {
        editor.putString("userId", userId);
        editor.apply();
    }

    public String getUserId() {
        return sharedPreferences.getString("userId", null);
    }
}
