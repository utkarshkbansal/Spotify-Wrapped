package com.example.spotifywrap.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SongService {
    private ArrayList<Song> songs = new ArrayList<>();
    private ArrayList<Artist> artists = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private RequestQueue queue;

    public SongService(Context context) {
        sharedPreferences = context.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(context);
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }
    public ArrayList<Artist> getArtists() {return artists;}

    public ArrayList<Song> getRecentlyPlayedTracks(final VolleyCallBack callBack) {
        String endpoint = "https://api.spotify.com/v1/me/player/recently-played";
        //filter get unique one -> wrap up
        songs.clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    Gson gson = new Gson();
                    JSONArray jsonArray = response.optJSONArray("items");
                    Log.d("STARTING", "list of songs" + jsonArray);


                    for (int n = 0; n < jsonArray.length(); n++) {
                        try {
                            JSONObject object = jsonArray.getJSONObject(n);
                            object = object.optJSONObject("track");
                            JSONObject albumObject = object.getJSONObject("album");
                            JSONArray artistsArray = object.getJSONArray("artists");
                            List<Artist> artists = new ArrayList<>();
                            for (int i = 0; i < artistsArray.length(); i++) {
                                JSONObject artistObject = artistsArray.getJSONObject(i);
                                String artistName = artistObject.getString("name");
                                String imageUrl = ""; // You can add image URL retrieval here if needed
                                Artist artist = new Artist(artistName, imageUrl);
                                artists.add(artist);
                            }

//                            JSONArray artistsArray = object.getJSONArray("artists");
//                            String artistName = artistsArray.getJSONObject(0).getString("name");
//                            Log.d("STARTING", "ARTIST" + artistName);
//
//                            Artist a = new Artist(artistName) ;
                            JSONArray imagesArray = albumObject.getJSONArray("images");
                            String imageUrl = imagesArray.getJSONObject(0).getString("url");
                            Log.d("STARTING", "GOT SONGS" + object.toString());

                            Song song = gson.fromJson(object.toString(), Song.class);


//                            song.setArtist(a);
                            song.setArtists(artists);

                            song.setImageUrl(imageUrl);

                            songs.add(song);
                            Log.d("STARTING", "Added songs");

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("STARTING", "oops error" + e.toString());
                        }
                    }
                    callBack.onSuccess();
                }, error -> {

                }) {
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
        return songs;
    }

    public ArrayList<Song> getTopTracks(final VolleyCallBack callBack, String timeRange, int limit ) {

        String url = "https://api.spotify.com/v1/me/top/tracks?type=artists&time_range=" + timeRange + "&limit=" + limit;
        songs.clear(); // Clear the existing list before adding new songs
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    Gson gson = new Gson();
                    JSONArray jsonArray = response.optJSONArray("items");
                    Log.d("STARTING", "list of songs" + jsonArray);

                    for (int n = 0; n < jsonArray.length(); n++) {
                        try {
                            JSONObject object = jsonArray.getJSONObject(n);
                            Log.d("STARTING", "Object" + object.toString());
                            JSONObject albumObject = object.getJSONObject("album");
                            JSONArray artistsArray = albumObject.getJSONArray("artists");
                            List<Artist> artists = new ArrayList<>();
                            for (int i = 0; i < artistsArray.length(); i++) {
                                JSONObject artistObject = artistsArray.getJSONObject(i);
                                String artistName = artistObject.getString("name");
                                Artist artist = new Artist(artistName);
                                artists.add(artist);
                            }

                            Log.d("STARTING", "Album" + albumObject.toString()) ;
                            Log.d("STARTING", "artists" + artistsArray.toString()) ;


                            JSONArray imagesArray = albumObject.getJSONArray("images");
                            Log.d("STARTING", "Img url" + imagesArray.toString());

                            String imageUrl = imagesArray.getJSONObject(0).getString("url");
                            Log.d("STARTING", "GOT SONGS" + albumObject.toString());

                            Song song = gson.fromJson(albumObject.toString(), Song.class);
                            song.setArtists(artists);
                            song.setImageUrl(imageUrl);

                            songs.add(song); // Add the song to the list

                            Log.d("STARTING", "Song name" +song.getName()+song.getImageUrl() + song.getArtists().get(0).getName().toString()) ;

                            Log.d("STARTING", "Added songs");

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("STARTING", "oops error" + e.toString());
                        }
                    }
                    callBack.onSuccess(); // Pass the list of songs to the callback
                }, error -> {

                }) {
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
        return songs;
    }
    public ArrayList<Song> getTopTracks(final VolleyCallBack callBack ) {
        String timeRange = "long_term"; // Set the time range to long_term
        int limit = 20; // Limit the result to 5 artists

        String url = "https://api.spotify.com/v1/me/top/tracks?type=artists&time_range=" + timeRange + "&limit=" + limit;
        songs.clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    Gson gson = new Gson();
                    JSONArray jsonArray = response.optJSONArray("items");
                    Log.d("STARTING", "list of songs" + jsonArray);

                    for (int n = 0; n < jsonArray.length(); n++) {
                        try {
                            JSONObject object = jsonArray.getJSONObject(n);
                            Log.d("STARTING", "Object" + object.toString());
                            JSONObject albumObject = object.getJSONObject("album");
                            JSONArray artistsArray = albumObject.getJSONArray("artists");
                            List<Artist> artists = new ArrayList<>();
                            for (int i = 0; i < artistsArray.length(); i++) {
                                JSONObject artistObject = artistsArray.getJSONObject(i);
                                String artistName = artistObject.getString("name");
                                Artist artist = new Artist(artistName);
                                artists.add(artist);
                            }

                            Log.d("STARTING", "Album" + albumObject.toString()) ;
                            Log.d("STARTING", "artists" + artistsArray.toString()) ;


                            JSONArray imagesArray = albumObject.getJSONArray("images");
                            Log.d("STARTING", "Img url" + imagesArray.toString());

                            String imageUrl = imagesArray.getJSONObject(0).getString("url");
                            Log.d("STARTING", "GOT SONGS" + albumObject.toString());

                            Song song = gson.fromJson(albumObject.toString(), Song.class);
                            song.setArtists(artists);
                            song.setImageUrl(imageUrl);

                            songs.add(song); // Add the song to the list

                            Log.d("STARTING", "Song name" +song.getName()+song.getImageUrl() + song.getArtists().get(0).getName().toString()) ;

                            Log.d("STARTING", "Added songs");

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("STARTING", "oops error" + e.toString());
                        }
                    }
                    callBack.onSuccess(); // Pass the list of songs to the callback
                }, error -> {

                }) {
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
        return songs;
    }

    public ArrayList<Artist> getTopArtists(final VolleyCallBack callBack) {
        String timeRange = "long_term"; // Set the time range to long_term
        int limit = 10; // Limit the result to 5 artists

        String url = "https://api.spotify.com/v1/me/top/artists?type=artists&time_range=" + timeRange + "&limit=" + limit;
//        String url = "https://api.spotify.com/v1/me/top/artists/";
        artists.clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    Gson gson = new Gson();
                    JSONArray jsonArray = response.optJSONArray("items");

                    for (int n = 0; n < jsonArray.length(); n++) {
                        try {
                            JSONObject object = jsonArray.getJSONObject(n);
                            Log.d("STARTING", "Object" + object.toString());
                            String artistName = object.getString("name");
                            JSONArray jsonGenre = object.getJSONArray("genres") ;

                            ArrayList<String> genre = new ArrayList<String>();
                            for (int i = 0 ;i < jsonGenre.length(); i ++) {
                                genre.add(jsonGenre.getString(i));
                            }
                            String imageUrl = object.getJSONArray("images").getJSONObject(0).getString("url");
                            Artist artist = new Artist(artistName, imageUrl, genre);
                            artists.add(artist);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("STARTING", "oops error" + e.toString());
                        }
                    }
                    callBack.onSuccess(); // Pass the list of songs to the callback
                }, error -> {

                }) {
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
        return artists; // Return the class-level ArrayList of songs
    }



    public interface VolleyCallBack {
        void onSuccess();
    }

    private HashMap<String, Object> createResult(ArrayList<Song> tracks, ArrayList<Artist> artists, HashSet<String> genres) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("tracks", new ArrayList<>(tracks));
        result.put("artists", new ArrayList<>(artists));
        result.put("genres", new ArrayList<>(genres));
        return result;
    }

}

