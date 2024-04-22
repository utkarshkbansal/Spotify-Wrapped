package com.example.spotifywrap.ui.home;
import android.content.Context;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseFunction {
    private static final String TAG = "FirebaseFunction";
    private FirebaseFirestore db ;
    private CollectionReference usersRef ;
    public FirebaseFunction() {
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("Users");
    }


    public void getAllUsers(OnUserListListener userListListener) {
        ArrayList<String> userList = new ArrayList<String>();

        db.collection("Users")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        String username = document.getString("username");
                        userList.add(username);
                    }
                    Log.d(TAG, "users list" + userList.toString());
                    if (userListListener != null) {
                        userListListener.onUserListReceived(userList);
                    }

                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error getting documents: " + e);
                });
    }
    public void getUsername(String email,OnUsernameResultListener listener ) {
        Log.d("STARTING", "GETTING USERNAME for mail"+email);

        usersRef.whereEqualTo("email", email.toLowerCase())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        String username = document.getString("username");
                        if (username == null) {
                            listener.onUsernameResult(null);
                            return;
                        } else {
                            listener.onUsernameResult(username);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error getting documents: " + e);
                });
    }

    public void getUserWrap(String username, OnUserWrapResultListener listener) {
        usersRef.whereEqualTo("username", username.toLowerCase())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<Song> userWrap = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        List<Map<String, Object>> wrap = (List<Map<String, Object>>) document.get("wrap");
                        if (wrap != null) {
                            for (Map<String, Object> songMap : wrap) {
                                String id = (String) songMap.get("id");
                                String name = (String) songMap.get("name");
                                String imageUrl = (String) songMap.get("imageUrl");

                                List<Artist> artists = new ArrayList<>();
                                List<String> artistNames = (List<String>) songMap.get("artists");
                                if (artistNames != null) {
                                    for (String artistName : artistNames) {
                                        artists.add(new Artist(artistName));
                                    }
                                }

                                Song song = new Song(id, name, imageUrl);
                                song.setArtists(artists);
                                userWrap.add(song);
                            }
                        }
                    }
                    listener.onUserWrapResult(userWrap);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error getting documents: " + e);
                });
    }
    public void getWrapsForUsernames(List<String> usernames, OnUserWrapResultListener listener) {
        db.collection("Users")
                .whereIn("username", usernames)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Map<String, List<Song>> wrapsMap = new HashMap<>();
                        ArrayList<Song> wrap = new ArrayList<>();

                        for (DocumentSnapshot document : task.getResult()) {
                            String username = document.getString("username");
                            List<Map<String, Object>> wrapData = (List<Map<String, Object>>) document.get("wrap");

                            if (wrapData != null) {
                                for (Map<String, Object> songMap : wrapData) {
                                    String id = (String) songMap.get("id");
                                    String name = (String) songMap.get("name");
                                    String imageUrl = (String) songMap.get("imageUrl");

                                    List<Artist> artists = new ArrayList<>();
                                    List<String> artistNames = (List<String>) songMap.get("artists");
                                    if (artistNames != null) {
                                        for (String artistName : artistNames) {
                                            artists.add(new Artist(artistName));
                                        }
                                    }

                                    Song song = new Song(id, name, imageUrl);
                                    song.setArtists(artists);
                                    boolean songAlreadyExists = false;
                                    for (Song existingSong : wrap) {
                                        if (existingSong.getId().equals(song.getId())) {
                                            songAlreadyExists = true;
                                            break;
                                        }
                                    }
                                    if (!songAlreadyExists) {
                                        wrap.add(song);
                                    }

                                }

                                wrapsMap.put(username, wrap);
                            }
                        }

                        //process list of songs


                        listener.onUserWrapResult(wrap);
                    } else {
                        Log.e("DisplayDuoWrapActivity", "Error getting documents: ", task.getException());
                    }
                });
    }

    public void storeUser(Context context, String username, String email, ArrayList<Song> wrap) {
        Log.d("STORING", username.toString());
        usersRef.document(email)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        updateUserData(context, username, email, wrap);
                    } else {
                        createNewUser(username, email, wrap);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error checking document existence: " + e);
                });
    }

    private void updateUserData(Context context, String username, String email, ArrayList<Song> wrap) {
        Log.d("STARTING", "EMAILZ"+email);
        Map<String, Object> user = new HashMap<>();
        if (username != null && !username.isEmpty()) {
            user.put("username", username);
        }
        if (email != null && !email.isEmpty()) {
            user.put("email", email);
        }
        if (wrap != null && !wrap.isEmpty()) {
            List<Map<String, Object>> songsList = new ArrayList<>();
            for (Song song : wrap) {
                Map<String, Object> songMap = new HashMap<>();
                songMap.put("id", song.getId());
                songMap.put("name", song.getName());
                songMap.put("imageUrl", song.getImageUrl());

                List<String> artistNames = new ArrayList<>();
                for (Artist artist : song.getArtists()) {
                    artistNames.add(artist.getName());
                }
                songMap.put("artists", artistNames);

                songsList.add(songMap);
            }
            user.put("wrap", songsList);
        } else {
            SongService songService = new SongService(context);
            songService.getTopTracks(() -> {
                ArrayList<Song> topTracks = songService.getSongs();
                List<Map<String, Object>> songsList = new ArrayList<>();
                for (Song song : topTracks) {
                    Map<String, Object> songMap = new HashMap<>();
                    songMap.put("id", song.getId());
                    songMap.put("name", song.getName());
                    songMap.put("imageUrl", song.getImageUrl());

                    // Convert list of artists into a list of artist names
                    List<String> artistNames = new ArrayList<>();
                    for (Artist artist : song.getArtists()) {
                        artistNames.add(artist.getName());
                    }
                    songMap.put("artists", artistNames);
                    songsList.add(songMap);
                }
                user.put("wrap", songsList);

            });
        }

        usersRef.document(email)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "User data updated successfully");
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error updating user data: " + e);
                });
    }

    private void createNewUser(String username, String email, ArrayList<Song> wrap) {
        Map<String, Object> user = new HashMap<>();
        if (username != null && !username.isEmpty()) {
            user.put("username", username);
        }
        if (email != null && !email.isEmpty()) {
            user.put("email", email);
        }
        if (wrap != null && !wrap.isEmpty()) {
            List<Map<String, Object>> songsList = new ArrayList<>();
            for (Song song : wrap) {
                Map<String, Object> songMap = new HashMap<>();
                songMap.put("id", song.getId());
                songMap.put("name", song.getName());
                songMap.put("imageUrl", song.getImageUrl());

                List<String> artistNames = new ArrayList<>();
                for (Artist artist : song.getArtists()) {
                    artistNames.add(artist.getName());
                }
                songMap.put("artists", artistNames);

                songsList.add(songMap);
            }
            user.put("wrap", songsList);
        }

        usersRef.document(email)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "New user data stored successfully");
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error storing new user data: " + e);
                });
    }

    public void deleteUserAccount(String email, OnUserAccountDeletedListener listener) {
        usersRef.document(email)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "User account deleted successfully");
                    if (listener != null) {
                        listener.onUserAccountDeleted(true); // Notify listener about successful deletion
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error deleting user account: " + e);
                    if (listener != null) {
                        listener.onUserAccountDeleted(false); // Notify listener about failure
                    }
                });
    }


    public interface OnUserAccountDeletedListener {
        void onUserAccountDeleted(boolean success);
    }

    public interface OnUsernameResultListener {
        void onUsernameResult(String username);
    }
    public interface OnUserListListener {
        void onUserListReceived(List<String> userList);
    }

    public interface OnUserWrapResultListener {
        void onUserWrapResult(ArrayList<Song> userWrap);
    }
}
