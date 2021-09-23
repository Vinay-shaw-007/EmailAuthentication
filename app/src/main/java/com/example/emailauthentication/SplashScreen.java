package com.example.emailauthentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        new Handler().postDelayed(() -> {
            //Checking  if current is user is registered or not
            if (user != null){
                //If user is verified
                if (user.isEmailVerified()) {
                    startActivity(new Intent(SplashScreen.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                }
            }else startActivity(new Intent(SplashScreen.this, LoginActivity.class));//If it's a new user
            finish();
        }, 3000);
    }
}