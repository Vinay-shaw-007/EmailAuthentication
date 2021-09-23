package com.example.emailauthentication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class SignUp extends AppCompatActivity {

    private EditText email, password, username;
    private Button signUp;
    ImageView back;
    private FirebaseAuth auth;
    private DatabaseReference userdata;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //Initializing
        hooks();
        //Setting onClickListeners
        eventHandler();
    }

    private void eventHandler() {
        signUp.setOnClickListener(v -> {
            String emailInput=email.getText().toString();
            String passwordInput=password.getText().toString();
            String usernameInput=username.getText().toString();
            if(TextUtils.isEmpty(emailInput)||TextUtils.isEmpty(passwordInput)||TextUtils.isEmpty(usernameInput)){
                Toast.makeText(SignUp.this, "Please fill all the information", Toast.LENGTH_SHORT).show();
            }
            else if(passwordInput.length()<6){
                Toast.makeText(SignUp.this, "Password must contain at least 6 characters", Toast.LENGTH_SHORT).show();
            }
            else{
                if (validateEmail()) registerUser( emailInput, passwordInput);
                else Toast.makeText(SignUp.this, "Enter correct Email.", Toast.LENGTH_SHORT).show();

            }
        });

        back.setOnClickListener(v -> onBackPressed());
    }

    private Boolean validateEmail() {
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

    private void registerUser(String emailInput, String passwordInput) {
        auth.createUserWithEmailAndPassword(emailInput,passwordInput).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Objects.requireNonNull(auth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Verification Link sent successfully", Toast.LENGTH_SHORT).show();
                        user = FirebaseAuth.getInstance().getCurrentUser();
                        userdata = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("userid", user.getUid());
                        userMap.put("username", username.getText().toString().trim());
                        userMap.put("email", Objects.requireNonNull(user.getEmail()).trim());
                        userdata.setValue(userMap);
                        startActivity(new Intent(SignUp.this, LoginActivity.class));
                        finish();
                    } else Toast.makeText(getApplicationContext(), Objects.requireNonNull(task1.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                });
            }if(!task.isSuccessful()){
                Toast.makeText(SignUp.this, ""+ Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hooks() {
        email = findViewById(R.id.signUp_email);
        password = findViewById(R.id.signUp_password);
        username = findViewById(R.id.signUp_user_name);
        signUp = findViewById(R.id.btn_signUp);
        back = findViewById(R.id.btn_back);
        auth = FirebaseAuth.getInstance();

    }
}