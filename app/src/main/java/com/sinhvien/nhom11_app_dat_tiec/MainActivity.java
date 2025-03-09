package com.sinhvien.nhom11_app_dat_tiec;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.sinhvien.nhom11_app_dat_tiec.DatabaseHelper.Restaurant;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import android.widget.EditText;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private Handler autoScrollHandler = new Handler();
    private Runnable autoScrollRunnable;
    private RecyclerView featuredRecyclerView;
    private FeaturedRestaurantAdapter featuredRestaurantAdapter;
    private BottomNavigationView bottomNavigationView;
    private EditText searchBar;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Xử lý RecyclerView hiển thị nhà hàng nổi bật
        featuredRecyclerView = findViewById(R.id.featuredRecyclerView);
        featuredRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Lấy danh sách nhà hàng từ database
        List<Restaurant> restaurantList = databaseHelper.getAllRestaurants();
        featuredRestaurantAdapter = new FeaturedRestaurantAdapter(this, restaurantList);
        featuredRecyclerView.setAdapter(featuredRestaurantAdapter);

        // Xử lý ViewPager hiển thị hình ảnh
        viewPager = findViewById(R.id.viewPager);
        List<Integer> imageList = new ArrayList<>();
        imageList.add(R.drawable.dtc1);
        imageList.add(R.drawable.dtc2);
        imageList.add(R.drawable.dtc3);

        ImageAdapter imageAdapter = new ImageAdapter(imageList);
        viewPager.setAdapter(imageAdapter);

        autoScrollRunnable = () -> {
            int currentItem = viewPager.getCurrentItem();
            int totalItems = imageAdapter.getItemCount();
            int nextItem = (currentItem + 1) % totalItems;
            viewPager.setCurrentItem(nextItem, true);
            autoScrollHandler.postDelayed(autoScrollRunnable, 3000);
        };
        autoScrollHandler.postDelayed(autoScrollRunnable, 3000);

        // Xử lý BottomNavigationView
        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_person) {
                    startActivity(new Intent(MainActivity.this, UserInfoActivity.class));
                    return true;
                }
                return false;
            }
        });

        // Xử lý tìm kiếm
        searchBar = findViewById(R.id.searchBar);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString().trim();
                List<Restaurant> filteredList = databaseHelper.searchRestaurants(query);
                featuredRestaurantAdapter.updateList(filteredList);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        autoScrollHandler.removeCallbacks(autoScrollRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        autoScrollHandler.postDelayed(autoScrollRunnable, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        autoScrollHandler.removeCallbacks(autoScrollRunnable);
    }
}