package com.example.quifoo2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class shopselection extends AppCompatActivity {
    CardView hotChatCorner, royalCafe, juiceCorner, leewaysCanteen;
    ImageView yourOrdersBtn;
    static String selectedShop;
    private static final int TIME_INTERVAL = 2000;
    private long backPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopselection);
        hotChatCorner = findViewById(R.id.hotChatCorner);
        royalCafe = findViewById(R.id.royalCafe);
        juiceCorner = findViewById(R.id.juiceCorner);
        leewaysCanteen = findViewById(R.id.leewaysCanteen);
        yourOrdersBtn = findViewById(R.id.yourOrdersBtn);

        hotChatCorner.setOnClickListener(v -> {
            selectedShop = "Hot Chat Corner";
            Intent intent = new Intent(shopselection.this,order.class);
            startActivity(intent);
        });

        royalCafe.setOnClickListener(v -> {
            selectedShop = "Royal Cafe";
            Intent intent = new Intent(shopselection.this,order.class);
            startActivity(intent);
        });

        juiceCorner.setOnClickListener(v -> {
            selectedShop = "Juice Corner";
            Intent intent = new Intent(shopselection.this,order.class);
            startActivity(intent);
        });

        leewaysCanteen.setOnClickListener(v -> {
            selectedShop = "Leeways Canteen";
            Intent intent = new Intent(shopselection.this,order.class);
            startActivity(intent);
        });

        yourOrdersBtn.setOnClickListener(v -> {
            Intent intent = new Intent(shopselection.this,orderslist.class);
            startActivity(intent);
        });

    }

}