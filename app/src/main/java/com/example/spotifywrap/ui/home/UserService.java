package com.example.spotifywrap.ui.home;

import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class UserService {
    private User user ;
    private SharedPreferences sharedPreferences;
    private RequestQueue queue;
    public UserService(RequestQueue queue, SharedPreferences sharedPreferences) {
        this.queue = queue;
        this.sharedPreferences = sharedPreferences;
    }
    public User getUser() {
        return user;
    }

    public void get(final VolleyCallBack callBack) {
        String url = "https://api.spotify.com/v1/me";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,
                null, response ->
        {
            Gson gson = new Gson();
            user = gson.fromJson(response.toString(), User.class);

            callBack.onSuccess();
        }, error -> get(() -> {

        })) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;


                headers.put("Authorization", auth);
                return headers;
            }
        };

        queue.add(jsonObjectRequest);
    }
}
