package com.sp.carbonless;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sp.carbonless.Model.Users;
import com.sp.carbonless.Prevalent.Prevalent;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    public Button Sign, Log;
    FirebaseAuth mFirebaseAuth;
    private ProgressDialog loadingBar;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_new);
        Sign = findViewById(R.id.SignUp);
        Log = findViewById(R.id.LoginButton);
        mFirebaseAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);
        Paper.init(this);

        Sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intSignUp = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intSignUp);
            }
        });
        Log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intSignUp1 = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intSignUp1);
            }
        });

        String UserPhoneKey = Paper.book().read(Prevalent.UserNameKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);

        if (UserPhoneKey != "" && UserPasswordKey != "")
        {
            if (!TextUtils.isEmpty(UserPhoneKey)  &&  !TextUtils.isEmpty(UserPasswordKey))
            {
                AllowAccess(UserPhoneKey, UserPasswordKey);

                loadingBar.setTitle("Already Logged in");
                loadingBar.setMessage("Please wait.....");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }
    }


        private void AllowAccess ( final String username, final String password)
        {
            final DatabaseReference RootRef;
            RootRef = FirebaseDatabase.getInstance("https://carbonless-d32c3-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();


            RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("Users").child(username).exists()) {
                        Users usersData = dataSnapshot.child("Users").child(username).getValue(Users.class);

                        if (usersData.getUserName().equals(username)) {
                            if (usersData.getPassword().equals(password)) {
                                Toast.makeText(MainActivity.this, "Please wait, you are already logged in...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(MainActivity.this, HomeActivity2.class);
                                Prevalent.currentOnlineUser = usersData;
                                startActivity(intent);
                            } else {
                                loadingBar.dismiss();
                                Toast.makeText(MainActivity.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Account with this " + username + " number do not exists.", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

}