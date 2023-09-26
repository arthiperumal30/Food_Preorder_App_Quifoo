package com.example.quifoo2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ViewCartAdapter extends FirebaseRecyclerAdapter<ViewCartModel, ViewCartAdapter.ViewHolder> {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Cart").child(login.email).child(shopselection.selectedShop);

    public ViewCartAdapter(@NonNull FirebaseRecyclerOptions<ViewCartModel> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, final int position, @NonNull ViewCartModel model) {
        holder.name2.setText(model.getName());
        holder.price2.setText(String.valueOf(model.getPrice()));
        holder.qtt.setText(String.valueOf(model.getQuantity()));

        if((model.getCategory()).equals("Cancelled Food"))
        {
            holder.offerImg.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.offerImg.setVisibility(View.INVISIBLE);
        }

        if((model.getDishType()).equals("Special Dish"))
        {
            holder.starImg.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.starImg.setVisibility(View.INVISIBLE);
        }
        if((model.getCategory()).equals("Cancelled Food"))
        {
            if((model.getQuantity())==(model.getQtyAvailable()))
            {
                holder.add.setEnabled(false);
            }
        }
        holder.add.setOnClickListener(view -> {
            int quantity_upd = Integer.parseInt(holder.qtt.getText().toString());
            if(!model.getDishType().equals("Cancelled Food"))
            {
                quantity_upd++;
                Map<String,Object> map = new HashMap<>();
                map.put("Quantity",(quantity_upd));
                updateCount(model, map);
            }
        });

        holder.sub.setOnClickListener(view -> {
            if(model.getQuantity()!=1) {
                int quantity_upd = Integer.parseInt(holder.qtt.getText().toString());
                quantity_upd--;
                Map<String, Object> map = new HashMap<>();
                map.put("Quantity", (quantity_upd));

                updateCount(model, map);
            }
        });

        holder.delBtn.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(holder.name2.getContext());
            builder.setTitle("Remove "+model.getName()+" ?");
            builder.setMessage("Do you want to remove "+model.getName()+" from the cart?");
            builder.setPositiveButton("Remove", ((dialogInterface, i) -> delete(model.getName())));

            builder.setNegativeButton("Cancel", (dialog, which) -> {

            });
            builder.show();

        });


        Glide.with(holder.img2.getContext())
                .load(model.getImage())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img2);
    }

     public void delete (String name)
    {
        databaseReference.orderByChild("Name").equalTo(name)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snapshot1 : snapshot.getChildren())
                        {
                            databaseReference.child(snapshot1.getKey()).removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void  updateCount (ViewCartModel model,Map<String,Object> map)
    {
        databaseReference.orderByChild("Name").equalTo(model.getName())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren())
                        {
                            databaseReference.child(snapshot1.getKey()).updateChildren(map);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_cart_item_holder, parent, false);
        return new ViewHolder(view);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img2, starImg, offerImg;
        TextView name2, price2, add, sub, qtt;
        ImageButton delBtn;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img2 = itemView.findViewById(R.id.img2);
            name2 = itemView.findViewById(R.id.dish_name2);
            price2 = itemView.findViewById(R.id.dish_price2);
            add = itemView.findViewById(R.id.add);
            sub = itemView.findViewById(R.id.subtract);
            qtt = itemView.findViewById(R.id.qtt);
            delBtn = itemView.findViewById(R.id.delete_btn);
            starImg = itemView.findViewById(R.id.specialFood_ic);
            offerImg = itemView.findViewById(R.id.offeredFood_ic);
        }
    }
}

