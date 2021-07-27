package com.sp.carbonless;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignLog extends AppCompatActivity {
    public Button Sign, Log;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_new);
        Sign = findViewById(R.id.SignUp);
        Log = findViewById(R.id.LoginButton);
        mFirebaseAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if (mFirebaseUser != null) {
                    Toast.makeText(SignLog.this, "You are logged in", Toast.LENGTH_LONG);
                    Intent i = new Intent(SignLog.this, HomeActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(SignLog.this, "Please log in", Toast.LENGTH_LONG);
                }
            }
        };
        Sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intSignUp = new Intent(SignLog.this, MainActivity.class);
                startActivity(intSignUp);
            }
        });
        Log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intSignUp1 = new Intent(SignLog.this, MainActivity.class);
                startActivity(intSignUp1);
            }
        });

    }
}