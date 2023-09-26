package com.example.quifoo2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

public class MainAdapter extends FirebaseRecyclerAdapter<MainModel, MainAdapter.ViewHolder> {
    public MainAdapter(@NonNull FirebaseRecyclerOptions<MainModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder,final int position, @NonNull MainModel model) {
        holder.name.setText(model.getName());
        holder.price.setText(String.valueOf(model.getPrice()));
        holder.category.setText(model.getCategory());

        setButton(holder, model);

        if((model.getDishType()).equals("Special Dish"))
        {
            holder.starImg.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.starImg.setVisibility(View.INVISIBLE);
        }

        if(model.getAvailable().equals(false))
        {
            holder.availabilityStatus.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.availabilityStatus.setVisibility(View.INVISIBLE);
        }
        if((model.getDishType()).equals("Cancelled Food"))
        {
            holder.offerImg.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.offerImg.setVisibility(View.INVISIBLE);
        }


        Glide.with(holder.img.getContext())
                .load(model.getImage())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img);

        holder.addToCart.setOnClickListener(view -> {

            Map<String,Object> map = new HashMap<>();
            map.put("Name", model.getName());
            map.put("Price",model.getPrice());
            map.put("Image",model.getImage());
            map.put("Category",model.getCategory());
            map.put("DishType",model.getDishType());
            map.put("Quantity",Integer.parseInt("1"));

            FirebaseDatabase.getInstance().getReference().child("Cart").child(login.email).child(shopselection.selectedShop).push()
                    .setValue(map).addOnSuccessListener(unused -> Toast.makeText(holder.name.getContext(), "Added to cart", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(holder.name.getContext(), "Failed", Toast.LENGTH_SHORT).show());

        });

        holder.addedToCart.setOnClickListener(v -> {
            FirebaseRecyclerOptions<ViewCartModel> options = new FirebaseRecyclerOptions.Builder<ViewCartModel>()
                    .setQuery(FirebaseDatabase.getInstance().getReference().child("Cart").child(login.email).child(shopselection.selectedShop),ViewCartModel.class)
                    .build();

            ViewCartAdapter viewCartAdapter = new ViewCartAdapter(options);
            viewCartAdapter.delete(model.getName());
        });



    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new ViewHolder(view);
    }

    public void setButton(ViewHolder holder, MainModel model)
    {
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("Cart").child(login.email).child(shopselection.selectedShop);
        cartRef.orderByChild("Name").equalTo(model.getName()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                boolean present = false;
                for(DataSnapshot snapshot : datasnapshot.getChildren())
                {
                    present = true;
                }

                if(present)
                {
                    holder.addedToCart.setVisibility(View.VISIBLE);
                    holder.addToCart.setVisibility(View.GONE);
                }
                else
                {
                    holder.addToCart.setVisibility(View.VISIBLE);
                    holder.addedToCart.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img, starImg, offerImg;
        TextView name,price,category,availabilityStatus;
        ImageButton addToCart, addedToCart;



        public ViewHolder(@NonNull View itemView) {super(itemView);
            img = itemView.findViewById(R.id.img1);
            name = itemView.findViewById(R.id.dish_name);
            price = itemView.findViewById(R.id.dish_price);
            category = itemView.findViewById(R.id.dish_category);
            addToCart = itemView.findViewById(R.id.addtocart_btn);
            addedToCart = itemView.findViewById(R.id.addedtocart_btn);
            availabilityStatus = itemView.findViewById(R.id.availabilityStatus);
            starImg = itemView.findViewById(R.id.starImg);
            offerImg = itemView.findViewById(R.id.offeredFood_ic);
        }
    }
}
