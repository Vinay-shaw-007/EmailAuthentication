package com.example.emailauthentication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;

public class HomeActivity extends AppCompatActivity {

    private LottieAnimationView lottieAnimationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        lottieAnimationView = findViewById(R.id.lottie_animation);
        lottieAnimationView.playAnimation();
        lottieAnimationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!lottieAnimationView.isAnimating()){
                    lottieAnimationView.playAnimation();
                }
            }
        });
    }
}