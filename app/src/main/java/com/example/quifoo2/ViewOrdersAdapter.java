package com.example.quifoo2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class ViewOrdersAdapter extends FirebaseRecyclerAdapter<ViewOrdersModel,ViewOrdersAdapter.ViewHolder> {
    Context context;
    public ViewOrdersAdapter(@NonNull FirebaseRecyclerOptions<ViewOrdersModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewOrdersAdapter.ViewHolder holder, int position, @NonNull ViewOrdersModel model) {
        holder.orderId.setText(model.getOrderId());
        holder.counterName.setText(model.getCounterName());
        context = holder.cardView.getContext();

        holder.cardView.setOnClickListener(v -> {
            shopselection.selectedShop = model.CounterName;
            scanQr_activity.orderId = model.OrderId;
            Intent intent = new Intent(context,order_status.class);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public ViewOrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_orders_holder, parent, false);
        return new ViewHolder(view);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView orderId, counterName;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orderId);
            counterName = itemView.findViewById(R.id.counterName);
            cardView = itemView.findViewById(R.id.orderView);
        }
    }
}
