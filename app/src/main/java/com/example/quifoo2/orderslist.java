package com.example.quifoo2;

import static com.example.quifoo2.login.actual_email;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class orderslist extends AppCompatActivity {
    RecyclerView LCOrderView, HCCOrderView, JCOrderView, RCOrderView;
    ViewOrdersAdapter viewOrdersAdapter1, viewOrdersAdapter2, viewOrdersAdapter3, viewOrdersAdapter4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderslist);
        LCOrderView = findViewById(R.id.LC_orders_view);
        HCCOrderView = findViewById(R.id.HCC_orders_view);
        JCOrderView = findViewById(R.id.JC_orders_view);
        RCOrderView = findViewById(R.id.RC_orders_view);

        setOrderView();
        setOrderView2();
        setOrderView3();
        setOrderView4();
    }
    private void setOrderView()
    {
        FirebaseRecyclerOptions<ViewOrdersModel> options = new FirebaseRecyclerOptions.Builder<ViewOrdersModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Orders").child("Leeways Canteen").orderByChild("User").equalTo(actual_email),ViewOrdersModel.class)
                .build();

        viewOrdersAdapter1 = new ViewOrdersAdapter(options);
        LCOrderView.setLayoutManager(new LinearLayoutManager(this));
        LCOrderView.setAdapter(viewOrdersAdapter1);
    }
    private void setOrderView2()
    {
        FirebaseRecyclerOptions<ViewOrdersModel> options = new FirebaseRecyclerOptions.Builder<ViewOrdersModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Orders").child("Hot Chat Corner").orderByChild("User").equalTo(actual_email),ViewOrdersModel.class)
                .build();

        viewOrdersAdapter2 = new ViewOrdersAdapter(options);
        HCCOrderView.setLayoutManager(new LinearLayoutManager(this));
        HCCOrderView.setAdapter(viewOrdersAdapter2);
    }

    private void setOrderView3()
    {
        FirebaseRecyclerOptions<ViewOrdersModel> options = new FirebaseRecyclerOptions.Builder<ViewOrdersModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Orders").child("Juice Corner").orderByChild("User").equalTo(actual_email),ViewOrdersModel.class)
                .build();

        viewOrdersAdapter3 = new ViewOrdersAdapter(options);
        JCOrderView.setLayoutManager(new LinearLayoutManager(this));
        JCOrderView.setAdapter(viewOrdersAdapter3);
    }

    private void setOrderView4()
    {
        FirebaseRecyclerOptions<ViewOrdersModel> options = new FirebaseRecyclerOptions.Builder<ViewOrdersModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Orders").child("Royal Cafe").orderByChild("User").equalTo(actual_email),ViewOrdersModel.class)
                .build();

        viewOrdersAdapter4= new ViewOrdersAdapter(options);
        RCOrderView.setLayoutManager(new LinearLayoutManager(this));
        RCOrderView.setAdapter(viewOrdersAdapter4);
    }

    @Override
    public void onStart() {
        super.onStart();
        viewOrdersAdapter1.startListening();
        viewOrdersAdapter2.startListening();
        viewOrdersAdapter3.startListening();
        viewOrdersAdapter4.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        viewOrdersAdapter1.stopListening();
        viewOrdersAdapter2.stopListening();
        viewOrdersAdapter3.stopListening();
        viewOrdersAdapter4.stopListening();
    }


    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(orderslist.this,shopselection.class);
        startActivity(intent);
        finish();
    }
}