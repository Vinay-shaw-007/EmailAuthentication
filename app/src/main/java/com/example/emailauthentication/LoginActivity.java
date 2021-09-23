package com.example.emailauthentication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private TextView loginToSignUp;
    private Button login;
    private EditText email, password;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        hooks();
        eventHandler();
    }

    private void eventHandler() {
        loginToSignUp.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, SignUp.class)));

        login.setOnClickListener(v -> {
            String emailInput=email.getText().toString();
            String passwordInput=password.getText().toString();
            if(TextUtils.isEmpty(emailInput)||TextUtils.isEmpty(passwordInput)){
                Toast.makeText(LoginActivity.this, "Please fill all the information", Toast.LENGTH_SHORT).show();
            }
            else if(passwordInput.length()<6){
                Toast.makeText(LoginActivity.this, "Password must contain at least 6 characters", Toast.LENGTH_SHORT).show();
            }
            else{
                if (validateEmail()) loginUser(emailInput, passwordInput);
                else Toast.makeText(LoginActivity.this, "Enter correct Email.", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void loginUser(String emailInput, String passwordInput) {
        auth.signInWithEmailAndPassword(emailInput, passwordInput).addOnSuccessListener(authResult -> {
            if (Objects.requireNonNull(auth.getCurrentUser()).isEmailVerified()){
                Toast.makeText(LoginActivity.this, "log-in successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();
            } else {
                Toast.makeText(getApplicationContext(),"Please Verify Your Email",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private boolean validateEmail() {
        String val = email.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";

        if (val.isEmpty()) {
            email.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            email.setError("Invalid email address");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }

    private void hooks() {
        login = findViewById(R.id.btn_login);
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        loginToSignUp = findViewById(R.id.login_signUp_btn);
        auth = FirebaseAuth.getInstance();
    }
}