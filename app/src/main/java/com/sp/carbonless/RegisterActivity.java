package com.sp.carbonless;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    public EditText usernameId, emailId, password, CfmPassword, fullnameId, addressId;
    public Button buttonSignUp;
    public TextView tvSignIn;
    private ProgressDialog loadingBar;
    FirebaseAuth mFirebaseAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://nsmen-514df-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mFirebaseAuth = FirebaseAuth.getInstance();
        fullnameId = findViewById(R.id.editTextFullNameLogin);
        addressId = findViewById(R.id.editTextAddress);
        usernameId = findViewById(R.id.editTextUsername);
        emailId = findViewById(R.id.editTextUsernameLogin);
        password = findViewById(R.id.editTextPassword);
        CfmPassword = findViewById(R.id.editTextCfmPassword);
        tvSignIn = findViewById(R.id.noacc);
        buttonSignUp = findViewById(R.id.cirLoginButton);
        loadingBar = new ProgressDialog(this);



        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                SignUp();
            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });

    }

    private void SignUp()
    {
        String username = usernameId.getText().toString();
        String fullname = fullnameId.getText().toString();
        String address = addressId.getText().toString();
        String email = emailId.getText().toString();
        String password1 = password.getText().toString();
        String passwordCfm = CfmPassword.getText().toString();

        if (TextUtils.isEmpty(username))
        {
            Toast.makeText(this, "Please write your username", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please write your email", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(fullname))
        {
            Toast.makeText(this, "Please write your full name", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(address))
        {
            Toast.makeText(this, "Please write your address", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password1))
        {
            Toast.makeText(this, "Please write your password", Toast.LENGTH_SHORT).show();
        }else if (!password1.equals(passwordCfm))
        {
            password.setError("Password is not the same!");
            password.setError("Confirm Password is not the same!");
            password.requestFocus();
        }
        else
        {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidatephoneNumber(username, email, password1, fullname, address);
        }
    }

    private void ValidatephoneNumber(final String username, final String email, final String password, final String fullname, final String address)
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance("https://nsmen-514df-default-rtdb.firebaseio.com/").getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (!(dataSnapshot.child("Users").child(username).exists()))
                {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("username", username);
                    userdataMap.put("password", password);
                    userdataMap.put("email", email);
                    userdataMap.put("fullname", fullname);
                    userdataMap.put("address", address);


                    RootRef.child("Users").child(username).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(RegisterActivity.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        loadingBar.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "This username " + email + " already exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Please try again using another phone number.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}