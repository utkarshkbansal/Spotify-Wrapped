package com.example.spotifywrap.ui.home;

import android.content.SharedPreferences;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;
public class UserInfo
{
    // the endpoint for getting user info
    private static final String URL = "https://api.spotify.com/v1/me";
    private SharedPreferences sharedPreferences;
    private RequestQueue queue;
    private User user;

    // the constructor requires the
    // Volley request queue and shared preferences
    // from the calling activity
    public UserInfo(RequestQueue queue, SharedPreferences sharedPreferences) {
        this.queue = queue;
        this.sharedPreferences = sharedPreferences;
    }

    public User getUser() {
        return user;
    }

    // method to get the user's data
    public void get(final VolleyCallBack callBack) {

        // We use JsonObjectRequest method of volley library to
        // retrieve a JSONObject response body at a given URL
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URL,
                null, response ->
        {
            // initialize the GSON library
            Gson gson = new Gson();

            // serialize the response into user object
            user = gson.fromJson(response.toString(), User.class);

            // indicate successful call
            callBack.onSuccess();
        }, error -> get(() -> {

        })) {
            // We need to add headers to the call for authorization
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                // get the auth token from shared preferences
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;

                // add it in headers
                headers.put("Authorization", auth);
                return headers;
            }
        };
        // add the JSON object request call to
        // the queue of volley library to make the call
        queue.add(jsonObjectRequest);
    }
}