package com.example.quifoo2;

import static android.content.ContentValues.TAG;

import static com.example.quifoo2.shopselection.selectedShop;
import static com.example.quifoo2.viewCart.totalPrice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class scanQr_activity extends AppCompatActivity {
    Button copyUpiBtn,proceed_btn;
    TextView textview;
    static String orderId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

        copyUpiBtn =findViewById(R.id.copyupi);
        textview=findViewById(R.id.txt);
        proceed_btn = findViewById(R.id.Proceed_btn);

        copyUpiBtn.setOnClickListener(view -> {
            ClipboardManager clipboardManager= null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

                clipboardManager = (ClipboardManager) getSystemService (Context.CLIPBOARD_SERVICE);
            }
            ClipData clipdata =ClipData.newPlainText("COPY",textview.getText().toString());
            clipboardManager.setPrimaryClip(clipdata);
            Toast.makeText(scanQr_activity.this,"copied",Toast.LENGTH_SHORT).show();
        });

        proceed_btn.setOnClickListener(view -> {
            insertDatabase();
            deleteDatabase();
            Intent intent = new Intent(scanQr_activity.this, order_status.class);
            startActivity(intent);
        });
    }

    private void insertDatabase()
    {
        String counter;
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        final int random = new Random().nextInt(900) + 100;

        if(selectedShop.equals("Leeways Canteen"))
            counter = "LC";
        else if(selectedShop.equals("Hot Chat Corner"))
            counter = "HCC";
        else if(selectedShop.equals("Juice Corner"))
            counter = "JC";
        else
            counter = "RC";

        orderId = counter+"-"+ currentDate +"-"+random;


        DatabaseReference cartReference = FirebaseDatabase.getInstance().getReference().child("Cart").child(login.email).child(selectedShop);
        DatabaseReference orderReference = FirebaseDatabase.getInstance().getReference().child("Orders").child(selectedShop).child(orderId);
        DatabaseReference foodReference = FirebaseDatabase.getInstance().getReference().child("Food Items").child(selectedShop);


        Map<Object,String> map = new HashMap<>();
        map.put("User",login.actual_email);
        map.put("OrderDate",currentDate);
        map.put("OrderTime",currentTime);
        map.put("OrderId",orderId);
        map.put("OrderTotalPrice", String.valueOf(totalPrice));
        map.put("OrderStatus", "Placed");
        map.put("OrderItems",null);
        map.put("CounterName",selectedShop);

        orderReference.setValue(map);

        cartReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    String name = dataSnapshot.child("Name").getValue().toString();
                    String dishtype = dataSnapshot.child("DishType").getValue().toString();
                    if(dishtype.equals("Cancelled Food"))
                    {
                        foodReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot dataSnapshot1 : snapshot.getChildren())
                                {
                                    String fname = dataSnapshot1.child("Name").getValue().toString();
                                    if(fname.equals(name))
                                    {
                                        String id = dataSnapshot1.getKey().toString();
                                        foodReference.child(id).removeValue();
                                        break;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }

                }
                orderReference.child("OrderItems").setValue(snapshot.getValue(), ((error, ref) -> {
                    if(error != null){
                        Log.d(TAG, "Copy Failed");
                    }
                    else{
                        Log.d(TAG, "Successful");
                    }
                }));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }

    private void deleteDatabase()
    {
        FirebaseDatabase.getInstance().getReference().child("Cart").child(login.email).child(selectedShop).removeValue();
        DatabaseReference foodItemsReference = FirebaseDatabase.getInstance().getReference().child("Food Items").child(selectedShop);

            }
}