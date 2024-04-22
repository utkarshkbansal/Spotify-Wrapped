package com.example.spotifywrap.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.WindowDecorActionBar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.spotifywrap.R;
import com.example.spotifywrap.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    TextView userDetails;
    Button submitButton;
    SharedPreferences sharedPreferences;
    private RequestQueue queue;
    private SongService songService;
    private TextView userView;
    private TextView songView;
    private Button addBtn;
    private Song song;
    private ArrayList<Song> tracks;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        userView = view.findViewById(R.id.user);
        songView = view.findViewById(R.id.song);
        addBtn = view.findViewById(R.id.add);

        sharedPreferences = requireActivity().getSharedPreferences(getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("display_name", "Couldn't retrieve name");
        String email = sharedPreferences.getString("email", "Couldn't get email");


        // Display the details in the text view
        userDetails.setText("Name: " + name);
        userDetails.append("\nEmail ID: " + email);
        userView.setText(sharedPreferences.getString("userid", "No User"));

        getTracks();

        return view;

    }

    private void getTracks() {
        // call spotifyService endpoint
        songService = new SongService(requireContext().getApplicationContext());

        VolleyCallBack callBack ;
        songService.getTopTracks(() -> {
            tracks = songService.getSongs();
            updateSong();

        });
    }
    private void updateSong() {
        if (tracks.size() > 0) {
            songView.setText(tracks.get(0).getName());
            song = tracks.get(0);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
