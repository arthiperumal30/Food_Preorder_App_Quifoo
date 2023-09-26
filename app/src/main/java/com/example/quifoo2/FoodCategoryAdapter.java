package com.example.quifoo2;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class FoodCategoryAdapter extends RecyclerView.Adapter<FoodCategoryAdapter.CategoryHolder> {

    List<FoodCategoryModel> data;
    Context context;
    int selectedItem = 0;
    OnCategoryClick onCategoryClick;

    public interface OnCategoryClick{
        void onClick(int pos);
    }

    public FoodCategoryAdapter(List<FoodCategoryModel> data, Context context, OnCategoryClick onCategoryClick) {
        this.data = data;
        this.context = context;
        this.onCategoryClick = onCategoryClick;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.category_holder,parent,false);
        return new CategoryHolder(view);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        holder.image.setImageResource(data.get(position).getImages());
        holder.title.setText(data.get(position).getName());
        if(position == selectedItem){
            //make card selected
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                holder.cardView.setOutlineSpotShadowColor(context.getColor(R.color.orange));
                holder.cardView.setOutlineAmbientShadowColor(context.getColor(R.color.orange));
            }
            holder.cardView.setStrokeWidth(2);
            holder.title.setTextColor(context.getColor(R.color.orange));
            holder.image.setColorFilter(ContextCompat.getColor(context,R.color.orange), PorterDuff.Mode.SRC_IN);
        }
        else
        {
            //make card inactive
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                holder.cardView.setOutlineSpotShadowColor(context.getColor(R.color.gray));
                holder.cardView.setOutlineAmbientShadowColor(context.getColor(R.color.gray));
            }
            holder.cardView.setStrokeWidth(0);
            holder.title.setTextColor(context.getColor(R.color.gray));
            holder.image.setColorFilter(ContextCompat.getColor(context,R.color.gray), PorterDuff.Mode.SRC_IN);

        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class CategoryHolder extends RecyclerView.ViewHolder{
        TextView title;
        ImageView image;
        MaterialCardView cardView;
        @SuppressLint("NotifyDataSetChanged")
        public CategoryHolder(View holder){
            super(holder);
            title = holder.findViewById(R.id.category_name);
            image = holder.findViewById(R.id.category_ic);
            cardView = holder.findViewById(R.id.card_view);

            cardView.setOnClickListener(view -> {
                selectedItem = getAdapterPosition();
                if(onCategoryClick != null) {
                    onCategoryClick.onClick(getAdapterPosition());
                }
                notifyDataSetChanged();
            });
        }

    }

}

