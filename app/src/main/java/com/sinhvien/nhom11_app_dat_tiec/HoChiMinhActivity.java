package com.sinhvien.nhom11_app_dat_tiec;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HoChiMinhActivity extends AppCompatActivity {

    private RecyclerView featuredRecyclerView;
    private FeaturedRestaurantAdapter featuredRestaurantAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hcm);

        featuredRecyclerView = findViewById(R.id.featuredRecyclerView);
        featuredRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        List<Restaurant> restaurantList = new ArrayList<>();
        restaurantList.add(new Restaurant(R.drawable.dtc1, "Nhà Hàng MiaSaigon", "Mô tả ngắn về nhà hàng A"));
        restaurantList.add(new Restaurant(R.drawable.nhahang1, "Nhà hàng Capella Park View", "Mô tả ngắn về nhà hàng B"));
        restaurantList.add(new Restaurant(R.drawable.nhahang2, "Nhà hàng AquaJardin", "Mô tả ngắn về nhà hàng C"));


        featuredRestaurantAdapter = new FeaturedRestaurantAdapter(this, restaurantList);
        featuredRecyclerView.setAdapter(featuredRestaurantAdapter);
    }
}
