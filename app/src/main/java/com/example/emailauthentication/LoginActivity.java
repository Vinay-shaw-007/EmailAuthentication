package com.example.emailauthentication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private TextView loginToSignUp, forgotPassword;
    private Button login;
    private EditText email, password;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Initializing
        hooks();
        //Setting onClickListeners
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

        forgotPassword.setOnClickListener(this::forgot_password);
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

    private Boolean validateEmailDialogBox(EditText reset,String val) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";

        if (val.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Email not be empty",Toast.LENGTH_SHORT).show();
            return false;
        } else if (!val.matches(emailPattern)) {
            Toast.makeText(LoginActivity.this, "Invalid Email",Toast.LENGTH_SHORT).show();
            return false;
        } else {
            reset.setError(null);
            return true;
        }
    }

    private void forgot_password(View v) {

        EditText resetmail = new EditText(v.getContext());
        resetmail.setBackgroundColor(getResources().getColor(R.color.textfieldcolor));
        resetmail.setHint("Email");
        resetmail.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        resetmail.setTextColor(getResources().getColor(R.color.colorAssent));
        resetmail.setHintTextColor(getResources().getColor(R.color.colorAssent));
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext(),R.style.MyDialogTheme);
        alertDialog.setTitle(Html.fromHtml("<font color='#A8A8A8'>Reset Password?</font>"));
        alertDialog.setMessage(Html.fromHtml("<font color='#A8A8A8'>Enter Your Email</font>"));
        alertDialog.setView(resetmail);

        alertDialog.setPositiveButton(Html.fromHtml("<font color='#A8A8A8'>YES</font>"), (dialog, which) -> {
            //Extract the email and sent reset link
            String mail = resetmail.getText().toString().trim();
            if(validateEmailDialogBox(resetmail,mail)) {
                auth.sendPasswordResetEmail(mail).addOnCompleteListener(task -> Toast.makeText(LoginActivity.this, "Reset Link Has been Sent.", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(LoginActivity.this, "Error! Reset Link is Not Sent. " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }else {
                Toast.makeText(LoginActivity.this, "Not Sent",Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.setNegativeButton(Html.fromHtml("<font color='#A8A8A8'>NO</font>"), (dialog, which) -> {
            //Back to the login screen
        });

        alertDialog.create().show();


    }

    private void hooks() {
        login = findViewById(R.id.btn_login);
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        loginToSignUp = findViewById(R.id.login_signUp_btn);
        forgotPassword = findViewById(R.id.forgot_password);
        auth = FirebaseAuth.getInstance();
    }
}