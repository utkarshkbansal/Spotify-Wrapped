package com.example.spotifywrap.ui.home;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spotifywrap.R;

import java.util.ArrayList;
import java.util.List;

public class DuoWrap extends AppCompatActivity {

    private AutoCompleteTextView autoCompleteTextView;
    private Button buttonSearch;
    private ListView listViewUsers;
    private FirebaseFunction firebaseFunction;
    private ArrayAdapter<String> usersAdapter;
    private ImageButton backButton ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duo_wrap);
        backButton = findViewById(R.id.backDuoSearch);
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        buttonSearch = findViewById(R.id.buttonSearch);
        listViewUsers = findViewById(R.id.listViewUsers);
        firebaseFunction = new FirebaseFunction();

        firebaseFunction.getAllUsers(new FirebaseFunction.OnUserListListener() {
            @Override
            public void onUserListReceived(List<String> userList) {
                ArrayAdapter<String> usersAdapter = new ArrayAdapter<>(DuoWrap.this, android.R.layout.simple_dropdown_item_1line, userList);
                autoCompleteTextView.setAdapter(usersAdapter);

                ArrayAdapter<String> listAdapter = new ArrayAdapter<>(DuoWrap.this, android.R.layout.simple_list_item_1, userList);
                listViewUsers.setAdapter(listAdapter);
            }
        });


        listViewUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedUser = (String) parent.getItemAtPosition(position);
                Intent intent = new Intent(DuoWrap.this, DisplayDuoWrapActivity.class);
                intent.putExtra("selectedUser", selectedUser);
                startActivity(intent);
            }
        });

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedUser = autoCompleteTextView.getText().toString();
                if (!selectedUser.isEmpty()) {
                    Intent intent = new Intent(DuoWrap.this, DisplayDuoWrapActivity.class);
                    intent.putExtra("selectedUser", selectedUser);
                    startActivity(intent);
                } else {
                    Toast.makeText(DuoWrap.this, "Please select a user", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
