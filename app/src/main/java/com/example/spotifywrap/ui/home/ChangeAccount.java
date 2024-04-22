package com.example.spotifywrap.ui.home;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.spotifywrap.MainActivity;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.spotifywrap.databinding.ActivityChangeAccountBinding;

import com.example.spotifywrap.R;
import com.spotify.sdk.android.auth.LoginActivity;

import java.util.ArrayList;

public class ChangeAccount extends AppCompatActivity {

    private Button btnLogout, btnChangeUsername, btnDeleteAccount;
    private FirebaseFunction firebaseFunction ;
    private SharedPreferences sharedPreferences ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_account);
        firebaseFunction = new FirebaseFunction();
        sharedPreferences = getSharedPreferences("SPOTIFY", MODE_PRIVATE);

        btnLogout = findViewById(R.id.btnLogout);
        btnChangeUsername = findViewById(R.id.btnChangeUsername);
        btnDeleteAccount = findViewById(R.id.btnDeleteAccount);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logout();
            }
        });

        btnChangeUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changeUsername();
            }
        });



        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccount();
                logout();
            }
        });
    }

    private void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isAuthenticated", false);
        editor.remove("token");
        editor.remove("userid");
        editor.remove("username");
        editor.remove("email");
        editor.apply();

        Intent resultIntent = new Intent();
        resultIntent.putExtra("isLogout", true);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void changeUsername() {
        EditText editTextNewUsername = findViewById(R.id.editTextNewUsername);
        String newUsername = editTextNewUsername.getText().toString().trim();
        String email = sharedPreferences.getString("email","");

        if (!newUsername.isEmpty()) {
            firebaseFunction.storeUser(getApplicationContext(),newUsername, email, null);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", newUsername);
            editor.apply();

            // Set the result with the new username
            Intent resultIntent = new Intent();
            resultIntent.putExtra("newUsername", newUsername);
            setResult(RESULT_OK, resultIntent);
            finish();
        } else {
            Snackbar.make(findViewById(android.R.id.content), "Please enter a new username", Snackbar.LENGTH_SHORT).show();
        }
    }


    private void deleteAccount() {
        String email = getSharedPreferences("SPOTIFY", MODE_PRIVATE).getString("username","");

        firebaseFunction.deleteUserAccount(email, new FirebaseFunction.OnUserAccountDeletedListener() {

            @Override
            public void onUserAccountDeleted(boolean success) {
                if (success == true) {
                    logout();

                } else {
                    Toast.makeText(ChangeAccount.this, "Failed to delete account", Toast.LENGTH_SHORT).show();
                }


            }

        });



    }
}