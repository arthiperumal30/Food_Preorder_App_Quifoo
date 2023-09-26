package com.example.quifoo2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class OrderItemsAdapter extends FirebaseRecyclerAdapter<OrderItemsModel, OrderItemsAdapter.ViewHolder> {

    public OrderItemsAdapter(@NonNull FirebaseRecyclerOptions<OrderItemsModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull OrderItemsAdapter.ViewHolder holder, int position, @NonNull OrderItemsModel model) {
        holder.name3.setText(model.getName());
        holder.quantity.setText(String.valueOf(model.getQuantity()));
        Glide.with(holder.img3.getContext())
                .load(model.getImage())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img3);

    }

    @NonNull
    @Override
    public OrderItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_items_holder, parent, false);
        return new ViewHolder(view);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img3;
        TextView name3, quantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img3 = itemView.findViewById(R.id.img3);
            name3 = itemView.findViewById(R.id.dish_name3);
            quantity = itemView.findViewById(R.id.dish_quantity);
        }
    }
}

