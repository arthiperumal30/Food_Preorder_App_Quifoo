package com.example.quifoo2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class payment extends AppCompatActivity {
    TextView viewScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        viewScan = findViewById(R.id.link4);

        viewScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(payment.this,scanQr_activity.class);
                startActivity(intent);
            }
        });
    }
}