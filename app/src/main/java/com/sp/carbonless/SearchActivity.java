package com.sp.carbonless;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sp.carbonless.Model.Products;
import com.sp.carbonless.ViewHolder.ProductViewHolder;
import com.squareup.picasso.Picasso;

public class SearchActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Button searchButton;
    private TextView goBack;
    private EditText searchInput;
    private RecyclerView searchList;
    private String searchInputTxt;
    private Spinner spinner_search;
    private String searchCategory = "";


    @Override
    protected void  onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        goBack = findViewById(R.id.go_back_btn1);
        spinner_search = findViewById(R.id.search_spinner);
        searchButton = findViewById(R.id.search_button);
        searchInput = findViewById(R.id.search_item_name);
        searchList = findViewById(R.id.search_list);
        searchList.setLayoutManager(new GridLayoutManager(SearchActivity.this, 2));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.search_category, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner_search.setAdapter(adapter);

        spinner_search.setOnItemSelectedListener(SearchActivity.this);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchInputTxt = searchInput.getText().toString();
                onStart();
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

        parent.getItemAtPosition(pos);
        switch (pos) {
            case 0:
                searchCategory = "";
                break;
            case 1:
                searchCategory = "Smart phones";
                break;
            case 2:
                searchCategory = "Computers & Laptops";
                break;
            case 3:
                searchCategory = "Computer peripherals";
                break;
            case 4:
                searchCategory = "Printers";
                break;
            case 5:
                searchCategory = "Monitors & Televisions";
                break;
            case 6:
                searchCategory = "Consoles";
                break;
            case 7:
                searchCategory = "Wearables & Smart watches";
                break;
            case 8:
                searchCategory = "Smart phone accessories";
                break;
            case 9:
                searchCategory = "Others";
                break;
        }
        searchSpinnerGo();
    }

    private void searchSpinnerGo() {
        DatabaseReference reference = FirebaseDatabase.getInstance("https://nsmen-514df-default-rtdb.firebaseio.com/").getReference().child("Products");

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>().setQuery(reference.orderByChild("category").startAt(searchCategory), Products.class).build();


        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model)
                    {

                        holder.txtProductName.setText(model.getPname());
                        holder.txtSellerName.setText("By: " + model.getUsername());
                        holder.txtProductPrice.setText("$ " + model.getPrice());
                        holder.txtProductTime.setText(model.getDate());
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(SearchActivity.this, MarketListingDetailsActivity.class);
                                intent.putExtra("pid", model.getPid());
                                intent.putExtra("username", model.getUsername());
                                intent.putExtra("email", model.getEmail());
                                intent.putExtra("pname", model.getPname());
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.market_cardview, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        searchList.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference reference = FirebaseDatabase.getInstance("https://nsmen-514df-default-rtdb.firebaseio.com/").getReference().child("Products");

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>().setQuery(reference.orderByChild("pname").startAt(searchInputTxt), Products.class).build();


        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model)
                    {

                        holder.txtProductName.setText(model.getPname());
                        holder.txtSellerName.setText("By: " + model.getUsername());
                        holder.txtProductPrice.setText("$ " + model.getPrice());
                        holder.txtProductTime.setText(model.getDate());
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(SearchActivity.this, MarketListingDetailsActivity.class);
                                intent.putExtra("pid", model.getPid());
                                intent.putExtra("username", model.getUsername());
                                intent.putExtra("email", model.getEmail());
                                intent.putExtra("pname", model.getPname());
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.market_cardview, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        searchList.setAdapter(adapter);
        adapter.startListening();
    }
}
