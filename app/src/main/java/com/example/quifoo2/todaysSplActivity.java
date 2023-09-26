package com.example.quifoo2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class todaysSplActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    MainAdapter mainAdapter;
    Button viewCartBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays_spl);

        recyclerView = findViewById(R.id.todaySpecialView);
        viewCartBtn = findViewById(R.id.viewCart_btn2);

        viewCartBtn.setOnClickListener(view -> {
            Intent intent = new Intent(todaysSplActivity.this, viewCart.class);
            startActivity(intent);
        });

        setFood();
    }

    private void setFood()
    {
        FirebaseRecyclerOptions<MainModel> options = new FirebaseRecyclerOptions.Builder<MainModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Food Items").child(shopselection.selectedShop).orderByChild("DishType").equalTo("Special Dish"), MainModel.class)
                .build();

        mainAdapter = new MainAdapter(options);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mainAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        mainAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mainAdapter.stopListening();
    }


}