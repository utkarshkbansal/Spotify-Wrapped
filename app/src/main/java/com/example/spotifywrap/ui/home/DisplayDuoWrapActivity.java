package com.example.spotifywrap.ui.home;

import static com.google.android.material.internal.ViewUtils.getContentView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotifywrap.R;
import com.example.spotifywrap.databinding.ActivityDisplayDuoWrapBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DisplayDuoWrapActivity extends AppCompatActivity {

    private ActivityDisplayDuoWrapBinding binding;
    private FirebaseFirestore db;
    private ImageButton backButton ;
    private FirebaseFunction firebaseFunction ;
    private RecyclerView recyclerView;
    private ImageButton nextButton;
    private SongService songService;
    private SongAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_duo_wrap);
        backButton = findViewById(R.id.backDuo);
        firebaseFunction = new FirebaseFunction();

        recyclerView = findViewById(R.id.recyclerViewDuo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        String selectedUser = getIntent().getStringExtra("selectedUser");


        ArrayList<String> users = new ArrayList<String>();
        users.add(getSharedPreferences("SPOTIFY", MODE_PRIVATE).getString("username",""));
        users.add(selectedUser);

        firebaseFunction.getWrapsForUsernames(users, new FirebaseFunction.OnUserWrapResultListener() {
            @Override
            public void onUserWrapResult(ArrayList<Song> wraps) {
                Log.d("GOT DUO WRAP" , wraps.toString());
                updateDuo(wraps);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void updateDuo(ArrayList<Song> duoWrap) {
        if (duoWrap.size() > 0) {
            adapter = new SongAdapter(getApplicationContext(), duoWrap);
            recyclerView.setAdapter(adapter);
        }

    }
}
