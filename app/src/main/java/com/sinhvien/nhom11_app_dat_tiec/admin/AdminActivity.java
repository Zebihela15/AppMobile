package com.sinhvien.nhom11_app_dat_tiec.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sinhvien.nhom11_app_dat_tiec.DatabaseHelper;
import com.sinhvien.nhom11_app_dat_tiec.R;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_main);

        Button btnRestaurants = findViewById(R.id.btn_restaurants);
        Button btnMenus = findViewById(R.id.btn_menus);
        Button btnServices = findViewById(R.id.btn_services);

        btnRestaurants.setOnClickListener(v -> {
            startActivity(new Intent(this, RestaurantActivity.class));
        });


    }
}