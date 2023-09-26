package com.example.quifoo2;

import static android.content.ContentValues.TAG;

import static com.example.quifoo2.shopselection.selectedShop;
import static com.example.quifoo2.viewCart.totalPrice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class order_status extends AppCompatActivity {

    RecyclerView recyclerView;
    OrderItemsAdapter orderItemsAdapter;
    TextView orderId, amountPaid;
    DatabaseReference orderRef,foodItemRef;
    ImageView orderPlaced, orderAccepted, orderFinished;
    View statusBar1, statusBar2;
    Button cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        recyclerView = findViewById(R.id.orderItemsView);
        orderId = findViewById(R.id.orderId);
        amountPaid = findViewById(R.id.amountPaid);
        statusBar1 = findViewById(R.id.status_bar_1);
        statusBar2 = findViewById(R.id.status_bar_2);
        orderPlaced = findViewById(R.id.imageView);
        orderAccepted = findViewById(R.id.imageView3);
        orderFinished = findViewById(R.id.imageView4);
        cancelBtn = findViewById(R.id.cancelBtn);
        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(selectedShop).child(scanQr_activity.orderId);
        foodItemRef = FirebaseDatabase.getInstance().getReference().child("Food Items").child(selectedShop);

        cancelBtn.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(order_status.this);
            builder.setTitle("Cancel Order ?");
            builder.setMessage("Are you sure you want to cancel order?");
            builder.setPositiveButton("Yes", ((dialogInterface, i) -> cancelOrder()));
            builder.setNegativeButton("No", ((dialogInterface, i) -> {}));
            builder.show();


        });

        setCancelBtn();
        setOrderId();
        setAmountPaid();
        setOrderedItems();
        setStatusBar();
    }

    private void setCancelBtn()
    {
        orderRef.child("OrderItems").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    if(snapshot1.child("DishType").getValue().equals("Cancelled Food"))
                        cancelBtn.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void setOrderedItems()
    {
        FirebaseRecyclerOptions<OrderItemsModel> options = new FirebaseRecyclerOptions.Builder<OrderItemsModel>()
                .setQuery(orderRef.child("OrderItems"), OrderItemsModel.class)
                .build();

        orderItemsAdapter = new OrderItemsAdapter(options);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(orderItemsAdapter);
    }

    private void setOrderId()
    {
        orderRef.child("OrderId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String orderIdNumber = snapshot.getValue(String.class);
                orderId.setText(String.valueOf(orderIdNumber));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setAmountPaid()
    {
        orderRef.child("OrderTotalPrice").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String amt = snapshot.getValue(String.class);
                amountPaid.setText(String.valueOf(amt));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void setStatusBar()
    {
        orderRef.child("OrderStatus").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String status = snapshot.getValue(String.class);
                if(status != null) {
                    switch (status) {
                        case "Placed":
                            orderPlaced.setBackgroundResource(R.drawable.orange_circle);

                            break;

                        case "Accepted":
                            orderPlaced.setBackgroundResource(R.drawable.orange_circle);
                            statusBar1.setVisibility(View.VISIBLE);
                            orderAccepted.setBackgroundResource(R.drawable.orange_circle);
                            break;

                        case "Finished":
                            orderPlaced.setBackgroundResource(R.drawable.orange_circle);
                            statusBar1.setVisibility(View.VISIBLE);
                            orderAccepted.setBackgroundResource(R.drawable.orange_circle);
                            statusBar2.setVisibility(View.VISIBLE);
                            orderFinished.setBackgroundResource(R.drawable.orange_circle);
                            break;

                        case "Delivered":
                            Intent intent = new Intent(order_status.this, order.class);
                            startActivity(intent);




                    }
                }


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void cancelOrder()
    {
        orderRef.child("OrderStatus").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Map<String, Object> map = new HashMap<>();
                String s = snapshot.getValue(String.class);
                if(s != null) {
                    if(s.equals("Placed"))
                        map.put("OrderTotalPrice", String.valueOf(totalPrice));
                    if (s.equals("Accepted") || s.equals("Finished")) {
                        map.put("OrderTotalPrice", String.valueOf(Math.round(totalPrice * 0.5)));
                        setCancelledFood();
                    }
                }
                orderRef.updateChildren(map);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Map<String,Object> status = new HashMap<>();
        status.put("OrderStatus","Cancelled");
        orderRef.updateChildren(status);
        Toast.makeText(order_status.this, "Your order is cancelled", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(order_status.this, order.class);
        startActivity(intent);

    }

    private void setCancelledFood()
    {
        orderRef.child("OrderItems").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Random random = new Random();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    String price = dataSnapshot.child("Price").getValue().toString();
                    int quantity = Integer.parseInt(dataSnapshot.child("Quantity").getValue().toString());

                    while(quantity != 0)
                    {
                        String name = dataSnapshot.child("Name").getValue().toString();
                        String id = "CancelledFood" + random.nextInt(300);
                        foodItemRef.child(id)
                                .setValue(dataSnapshot.getValue(), ((error, ref) -> {
                                    if (error != null) {
                                        Log.d(TAG, "Copy Failed");
                                    }
                                    else {
                                        Log.d(TAG, "Successful");
                                    }
                                }));

                        foodItemRef.child(id).child("Quantity").removeValue();
                        for(int i = 0; i < quantity; i++)
                        {
                            name = name + " ";
                        }
                        Map<String, Object> map = new HashMap<>();
                        map.put("Name",name);
                        map.put("Price",Math.round(Integer.parseInt(price)*(0.5)));
                        map.put("Available", true);
                        map.put("DishType", "Cancelled Food");

                        foodItemRef.child(id).updateChildren(map);
                        quantity--;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    @Override
    public void onStart() {
        super.onStart();
        orderItemsAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        orderItemsAdapter.stopListening();
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, orderslist.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}