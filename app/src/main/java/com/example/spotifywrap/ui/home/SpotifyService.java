package com.example.spotifywrap.ui.home;

import org.json.JSONObject;
import android.content.SharedPreferences;


import com.android.volley.AuthFailureError;
import com.google.gson.Gson;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import android.content.Context;

import java.util.HashMap;
import java.util.Map;


//store access token
public class SpotifyService {
//    @GET("v1/endpoint")
//    Call<ResponseBody> callEndpoint(@Header("Authorization") String accessToken);

    /*
    Endpoints needed :
    - user profile
    - get users playlist + tracks
    - top playlist
    - top artists
    - top songs
     */

    private RequestQueue queue;
    private SharedPreferences sharedPreferences;
    private User user;


    public SpotifyService(RequestQueue queue, SharedPreferences sharedPreferences) {
        this.queue = queue;
        this.sharedPreferences = sharedPreferences;
    }

    /*
    - getTracks()
    - getArtists()
    - getUserInfo()
     */


    public void getUser(final VolleyCallBack callBack) {
        String url = "https://api.spotify.com/v1/me";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,
                null, response ->
        {
            Gson gson = new Gson();
            user = gson.fromJson(response.toString(), User.class);

            callBack.onSuccess();
        }, error -> getUser(() -> {

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






//
//
//    private User user;
//
//    public void get_user(Context context, String accessToken) {
//        String endpoint = "https://api.spotify.com/v1/me";
//        RequestQueue queue = Volley.newRequestQueue(context);
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, endpoint, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Gson gson = new Gson();
//                        user = gson.fromJson(response.toString(), User.class);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // Handle error
////                        CallBack.onError(error); display error in UI
//                    }
//                }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<>();
//                headers.put("Authorization", "Bearer " + accessToken);
//                headers.put("Client-ID", Variables.CLIENT_ID); // Add your Spotify client ID
//                return headers;
//            }
//        };
//
//        queue.add(jsonObjectRequest);
//
//    }
//    public void get_albums(Context context, String accessToken) {
//        String endpoint = "https://api.spotify.com/v1/me/albums";
//        RequestQueue queue = Volley.newRequestQueue(context);
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, endpoint, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Gson gson = new Gson();
//                        user = gson.fromJson(response.toString(), User.class);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // Handle error
////                        CallBack.onError(error); display error in UI
//                    }
//                }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<>();
//                headers.put("Authorization", "Bearer " + accessToken);
//                headers.put("Client-ID", Variables.CLIENT_ID); // Add your Spotify client ID
//                return headers;
//            }
//        };
//
//        queue.add(jsonObjectRequest);
//    }


//
//    public void get_current_user_playlist(Context context, String accessToken) {
//        String endpoint = "https://api.spotify.com/v1/me/playlists";
//        RequestQueue queue = Volley.newRequestQueue(context);
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, endpoint, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Gson gson = new Gson();
//                        user = gson.fromJson(response.toString(), User.class);
////                        CallBack.onSuccess(); update UI
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // Handle error
////                        CallBack.onError(error); display error in UI
//                    }
//                }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<>();
//                headers.put("Authorization", "Bearer " + accessToken);
//                headers.put("Client-ID", Variables.CLIENT_ID); // Add your Spotify client ID
//                return headers;
//            }
//        };
//
//        queue.add(jsonObjectRequest);
//    }
//
//    public void get_user_playlist(Context context, String accessToken, String userID) {
//
//        String endpoint = "https://api.spotify.com/v1/users/{user_id}/playlists";
//        RequestQueue queue = Volley.newRequestQueue(context);
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, endpoint, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Gson gson = new Gson();
//                        user = gson.fromJson(response.toString(), User.class);
////                        CallBack.onSuccess(); update UI
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // Handle error
////                        CallBack.onError(error); display error in UI
//                    }
//                }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<>();
//                headers.put("Authorization", "Bearer " + accessToken);
//                headers.put("Client-ID", Variables.CLIENT_ID); // Add your Spotify client ID
//                return headers;
//            }
//        };
//
//        queue.add(jsonObjectRequest);
//    }
//
//    public void get_user_saved_tracks(Context context, String accessToken) {
//
//        String endpoint = "https://api.spotify.com/v1/me/tracks";
//
//        RequestQueue queue = Volley.newRequestQueue(context);
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, endpoint, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Gson gson = new Gson();
//                        user = gson.fromJson(response.toString(), User.class);
////                        CallBack.onSuccess(); update UI
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // Handle error
////                        CallBack.onError(error); display error in UI
//                    }
//                }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<>();
//                headers.put("Authorization", "Bearer " + accessToken);
//                headers.put("Client-ID", Variables.CLIENT_ID); // Add your Spotify client ID
//                return headers;
//            }
//        };
//
//        queue.add(jsonObjectRequest);
//    }

}









