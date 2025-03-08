package com.sinhvien.nhom11_app_dat_tiec;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DaNangActivity extends AppCompatActivity {

    private RecyclerView featuredRecyclerView;
    private FeaturedRestaurantAdapter featuredRestaurantAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dn);

        // Ánh xạ RecyclerView
        featuredRecyclerView = findViewById(R.id.featuredRecyclerView);
        featuredRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Tạo danh sách nhà hàng nổi bật tại TP. Hồ Chí Minh
        List<Restaurant> restaurantList = new ArrayList<>();
        restaurantList.add(new Restaurant(R.drawable.dtc3, "New Orient Hotel", "Mô tả ngắn về nhà hàng A"));
        restaurantList.add(new Restaurant(R.drawable.dn1, "Golden Phoenix", "Mô tả ngắn về nhà hàng B"));
        restaurantList.add(new Restaurant(R.drawable.dn2, "Elden Plaza", "Mô tả ngắn về nhà hàng C"));

        // Tạo Adapter và gán vào RecyclerView
        featuredRestaurantAdapter = new FeaturedRestaurantAdapter(this, restaurantList);
        featuredRecyclerView.setAdapter(featuredRestaurantAdapter);
    }
}
