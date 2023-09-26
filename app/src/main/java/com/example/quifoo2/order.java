package com.example.quifoo2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class order extends AppCompatActivity {
    RecyclerView recyclerView, recyclerCategories;
    MainAdapter mainAdapter;
    TextView shopName, categories;
    Button viewCartBtn;
    String category;
    private ImageSlider imageSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerCategories = findViewById(R.id.foodCategory);
        viewCartBtn = findViewById(R.id.viewCart_btn);
        imageSlider = findViewById(R.id.imageSlider);
        categories = findViewById(R.id.textView2);
        shopName = findViewById(R.id.shopName);
        shopName.setText(shopselection.selectedShop);

        viewCartBtn.setOnClickListener(view -> {
            Intent intent = new Intent(order.this, viewCart.class);
            startActivity(intent);
        });

        setBanner();
        imageSlider.setItemClickListener(i -> {
            Intent intent;
            switch (i)
            {
                case 0:
                    intent = new Intent(order.this, todaysSplActivity.class);
                    startActivity(intent);
                    break;


                case 1:
                    intent = new Intent(order.this, splOfferActivity.class);
                    startActivity(intent);
                    break;
            }
        });

        setCategories();
        setFoodItem(0);
    }

    private void setBanner()
    {
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.tdaysspecialbanner, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.splofferbanner, ScaleTypes.FIT));

        imageSlider.setImageList(slideModels, ScaleTypes.FIT);
    }

    private void setFoodItem(int pos)
    {
        if(shopselection.selectedShop.equals("Leeways Canteen"))
        {
            switch (pos)
            {
                case 0:
                    category = "Lunch";
                    break;

                case 1:
                    category = "Tiffin";
                    break;
            }
        }
        else if(shopselection.selectedShop.equals("Royal Cafe")) {
            switch (pos) {
                case 0:
                    category = "Sandwich";
                    break;

                case 1:
                    category = "Snacks";
                    break;

                case 2:
                    category = "Beverage";
                    break;
            }
        }
        else if(shopselection.selectedShop.equals("Juice Corner")){
            category = "Beverage";
        }
        else
        {
            category = "Snacks";
        }


        FirebaseRecyclerOptions<MainModel> options = new FirebaseRecyclerOptions.Builder<MainModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Food Items").child(shopselection.selectedShop).orderByChild("Category").equalTo(category), MainModel.class)
                .build();

        mainAdapter = new MainAdapter(options);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mainAdapter);
        mainAdapter.startListening();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setCategories(){
        List<FoodCategoryModel> data = new ArrayList<>();

        if(shopselection.selectedShop.equals("Leeways Canteen"))
        {
            FoodCategoryModel foodCategoryModel = new FoodCategoryModel("Lunch", R.drawable.lunch_ic);
            FoodCategoryModel foodCategoryModel2 = new FoodCategoryModel("Tiffin", R.drawable.tiffin_ic);

            data.add(foodCategoryModel);
            data.add(foodCategoryModel2);
        }
        else if(shopselection.selectedShop.equals("Royal Cafe"))
        {
            FoodCategoryModel foodCategoryModel = new FoodCategoryModel("Sandwich", R.drawable.tiffin_ic);
            FoodCategoryModel foodCategoryModel2 = new FoodCategoryModel("Snacks", R.drawable.snacks2_ic);
            FoodCategoryModel foodCategoryModel3 = new FoodCategoryModel("Beverages", R.drawable.beverage_ic);

            data.add(foodCategoryModel);
            data.add(foodCategoryModel2);
            data.add(foodCategoryModel3);
        }
        else
        {
            recyclerCategories.setVisibility(View.GONE);
            categories.setVisibility(View.GONE);
        }

        FoodCategoryAdapter foodCategoryAdapter = new FoodCategoryAdapter(data, order.this, this::setFoodItem);

        recyclerCategories.setLayoutManager(new LinearLayoutManager(order.this, RecyclerView.HORIZONTAL, false));
        recyclerCategories.setAdapter(foodCategoryAdapter);
        foodCategoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
        mainAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mainAdapter.stopListening();
    }
}