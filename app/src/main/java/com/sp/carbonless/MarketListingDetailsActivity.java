package com.sp.carbonless;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sp.carbonless.Model.Products;
import com.sp.carbonless.Prevalent.Prevalent;
import com.squareup.picasso.Picasso;

public class MarketListingDetailsActivity extends AppCompatActivity {
    private TextView listingName, listingDetails, listingPrice, backTextButton, deleteTextButton;
    private ImageView listingImage;
    private Button emailNow;
    private String listingId = "";
    private String username1;
    private String email = "";
    private String subject = "";

    @Override
    protected void  onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.market_listings_details);

        listingId = getIntent().getStringExtra("pid");
        username1 = getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");
        subject = getIntent().getStringExtra("pname");


        listingImage = (ImageView) findViewById(R.id.market_listing_image);
        listingName = (TextView) findViewById(R.id.market_listing_name);
        listingDetails = (TextView) findViewById(R.id.market_listing_details);
        listingPrice = (TextView) findViewById(R.id.market_listing_price);
        emailNow = (Button) findViewById(R.id.chat_now_button);
        backTextButton = (TextView) findViewById(R.id.go_back_btn);
        deleteTextButton = (TextView) findViewById(R.id.delete_btn);

        backTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        emailNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });

        deleteTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                    CharSequence options[] = new CharSequence[]
                            {
                                    "Yes", "No"
                            };

                    AlertDialog.Builder builder = new AlertDialog.Builder(MarketListingDetailsActivity.this);
                    builder.setTitle("Are you sure you want to delete this listing?");

                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            if (i == 0) {
                                if (Prevalent.currentOnlineUser.getUserName() == username1) {
                                    deleteListing(listingId);
                                } else {
                                    Toast.makeText(MarketListingDetailsActivity.this, "You can only delete your own listings", Toast.LENGTH_SHORT).show();
                                }
                            }
                            if (i == 1) {
                                Toast.makeText(MarketListingDetailsActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.show();
                }
        });

        getListingId(listingId);
    }

    private void sendMail() {

        String message = "Choose an email client";
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.putExtra(Intent.EXTRA_EMAIL, email);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, subject);

        intent.setData(Uri.parse("mailto:"));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(MarketListingDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteListing(String listingId1) {

        DatabaseReference listingsRef = FirebaseDatabase.getInstance("https://nsmen-514df-default-rtdb.firebaseio.com/").getReference().child("UserListings").child(Prevalent.currentOnlineUser.getUserName());
        DatabaseReference listingsRef1 = FirebaseDatabase.getInstance("https://nsmen-514df-default-rtdb.firebaseio.com/").getReference().child("Products").child(listingId1);

        listingsRef1.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(MarketListingDetailsActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                }
            }

        });

        listingsRef.child(listingId1).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(MarketListingDetailsActivity.this, "Deleted", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MarketListingDetailsActivity.this, HomeActivity2.class);
                    startActivity(intent);
                }
            }

        });

    }

    private void getListingId(String listingId) {
        DatabaseReference listingsRef = FirebaseDatabase.getInstance("https://nsmen-514df-default-rtdb.firebaseio.com/").getReference().child("Products");

        listingsRef.child(listingId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    Products products = snapshot.getValue(Products.class);

                    listingName.setText(products.getPname() + "  | By: " + products.getUsername() + " | On: " + products.getDate());
                    listingDetails.setText(products.getDescription());
                    listingPrice.setText("Category: " + products.getCategory() + "\nPrice: $" + products.getPrice() + "\nEmail: " + products.getEmail() + "\nWhere to collect: " + products.getAddress());
                    Picasso.get().load(products.getImage()).into(listingImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
