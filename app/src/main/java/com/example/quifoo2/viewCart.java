package com.example.quifoo2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class viewCart extends AppCompatActivity {
    RecyclerView recyclerSelectedItems;
    ViewCartAdapter viewCartAdapter;
    TextView totPrice;
    Button checkout;
    static int totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);

        recyclerSelectedItems = findViewById(R.id.selected_food_view);
        totPrice = findViewById(R.id.totalPrice);
        checkout = findViewById(R.id.Checkout_btn);

        checkout.setOnClickListener(view -> {
            totalPrice = Integer.parseInt(totPrice.getText().toString());
            Intent intent = new Intent(viewCart.this,payment.class);
            startActivity(intent);
        });

        setSelectedFoodItems();
        calculateTotalPrice();

    }

    private void setSelectedFoodItems()
    {
        FirebaseRecyclerOptions<ViewCartModel> options = new FirebaseRecyclerOptions.Builder<ViewCartModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Cart").child(login.email).child(shopselection.selectedShop),ViewCartModel.class)
                .build();

        viewCartAdapter = new ViewCartAdapter(options);
        recyclerSelectedItems.setLayoutManager(new LinearLayoutManager(this));
        recyclerSelectedItems.setAdapter(viewCartAdapter);

    }

    public void calculateTotalPrice()
    {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Cart").child(login.email).child(shopselection.selectedShop);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                int total = 0;
                for(DataSnapshot snapshot : datasnapshot.getChildren())
                {
                    total = total + Integer.parseInt(snapshot.child("Price").getValue().toString()) * Integer.parseInt(snapshot.child("Quantity").getValue().toString());
                }
                totPrice.setText(String.valueOf(total));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        viewCartAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        viewCartAdapter.stopListening();
    }

}