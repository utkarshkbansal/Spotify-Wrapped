package com.example.spotifywrap.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.spotifywrap.MainActivity;
import com.example.spotifywrap.R;
import com.example.spotifywrap.databinding.ActivityMainBinding;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;
import androidx.fragment.app.FragmentTransaction;
import com.example.spotifywrap.databinding.ActivityMainBinding;

public class Login extends AppCompatActivity{

    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    private RequestQueue requestQueue;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page); //mb logging in load page


        AuthorizationRequest.Builder builder = new AuthorizationRequest.Builder(Variables.CLIENT_ID, AuthorizationResponse.Type.TOKEN, Variables.REDIRECT_URI)
                .setShowDialog(true);
        builder.setScopes(new String[]{Variables.SCOPES});
        AuthorizationRequest request = builder.build();
        AuthorizationClient.openLoginActivity(this, Variables.REQUEST_CODE, request);

        sharedPreferences = getSharedPreferences("SPOTIFY", 0);
        requestQueue = Volley.newRequestQueue(this);

    }

    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == Variables.REQUEST_CODE) {
            AuthorizationResponse response
                    = AuthorizationClient.getResponse(
                    resultCode, intent);

            switch (response.getType()) {
                case TOKEN:
                    editor = getSharedPreferences("SPOTIFY",
                            0).edit();
                    Log.d("STARTING", "GOT AUTH TOKEN");

                    editor.putString("token",
                            response.getAccessToken());

                    editor.apply();

                    UserService userService = new UserService(requestQueue, sharedPreferences);
                    userService.get(() -> {
                        User user = userService.getUser();
                        editor = getSharedPreferences("SPOTIFY", 0).edit();
                        editor.putString("userid", user.id);
                        editor.putString("username", user.display_name);
                        editor.putString("email",user.email);
                        Log.d("STARTING", "GOT USER INFORMATION" + user.display_name);
                        editor.commit();

                    });
                    Intent newintent = new Intent(Login.this, MainActivity.class);
                    newintent.putExtra("isAuthenticated", true);
                    setResult(RESULT_OK, newintent);
                    finish();

                    Toast.makeText(this, "Auth successful",Toast.LENGTH_SHORT)
                            .show();

                    break;

                case ERROR:
                    Log.d("LoginActivity", response.getError());
                    Intent mainIntentError = new Intent(Login.this, MainActivity.class);
                    mainIntentError.putExtra("isAuthenticated", false);
                    setResult(RESULT_CANCELED, mainIntentError);
                    finish() ;
                    break;
            }

        }
    }
    protected void onDestroy() {
        super.onDestroy();
    }

}
