package com.sinhvien.nhom11_app_dat_tiec;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HaNoiActivity extends AppCompatActivity {

    private RecyclerView featuredRecyclerView;
    private FeaturedRestaurantAdapter featuredRestaurantAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hn);


        featuredRecyclerView = findViewById(R.id.featuredRecyclerView);
        featuredRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        List<Restaurant> restaurantList = new ArrayList<>();
        restaurantList.add(new Restaurant(R.drawable.dtc2, "Tràng An Palace", "Mô tả ngắn về nhà hàng A"));
        restaurantList.add(new Restaurant(R.drawable.hn1, "HaNoiDaewoo Hotel", "Mô tả ngắn về nhà hàng B"));
        restaurantList.add(new Restaurant(R.drawable.hn2, "Queen Bee", "Mô tả ngắn về nhà hàng C"));


        featuredRestaurantAdapter = new FeaturedRestaurantAdapter(this, restaurantList);
        featuredRecyclerView.setAdapter(featuredRestaurantAdapter);
    }
}
