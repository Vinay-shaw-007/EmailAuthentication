package com.example.emailauthentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    private LottieAnimationView lottieAnimationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        lottieAnimationView = findViewById(R.id.lottie_animation);
        lottieAnimationView.playAnimation();
        lottieAnimationView.setOnClickListener(v -> {
            if (!lottieAnimationView.isAnimating()){
                lottieAnimationView.playAnimation();
            }
        });
        Button logout = findViewById(R.id.btn_logout);
        logout.setOnClickListener(v -> {
            auth.signOut();
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
        });
    }
}