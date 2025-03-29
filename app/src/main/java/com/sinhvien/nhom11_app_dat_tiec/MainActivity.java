package com.sinhvien.nhom11_app_dat_tiec;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.sinhvien.nhom11_app_dat_tiec.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager; // ViewPager2 cho ảnh nhà hàng
    private ViewPager2 featuredDishViewPager; // ViewPager2 cho ảnh món ăn
    private Handler autoScrollHandler = new Handler();
    private Runnable autoScrollRunnable;
    private Handler dishHandler = new Handler();
    private Runnable dishRunnable;
    private RecyclerView featuredRecyclerView;
    private FeaturedRestaurantAdapter featuredRestaurantAdapter;
    private BottomNavigationView bottomNavigationView;
    private EditText searchBar;
    private ImageView notificationIcon; // Khai báo biến notificationIcon
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

        // Xử lý ViewPager hiển thị hình ảnh nhà hàng
        viewPager = findViewById(R.id.viewPager);
        List<Integer> imageList = new ArrayList<>();
        imageList.add(R.drawable.dtc1);
        imageList.add(R.drawable.dtc2);
        imageList.add(R.drawable.dtc3);
        imageList.add(R.drawable.d25);
        imageList.add(R.drawable.d31);

        ImageAdapter imageAdapter = new ImageAdapter(this, imageList, true);
        viewPager.setAdapter(imageAdapter);

        // Tự động chạy ảnh nhà hàng (3 giây/ảnh)
        autoScrollRunnable = () -> {
            int currentItem = viewPager.getCurrentItem();
            int totalItems = imageAdapter.getItemCount();
            int nextItem = (currentItem + 1) % totalItems;
            viewPager.setCurrentItem(nextItem, true);
            autoScrollHandler.postDelayed(autoScrollRunnable, 3000); // 3 giây
        };
        autoScrollHandler.postDelayed(autoScrollRunnable, 3000);

        // Xử lý ViewPager hiển thị hình ảnh món ăn
        featuredDishViewPager = findViewById(R.id.featuredDishViewPager);
        List<Integer> dishImages = new ArrayList<>();
        dishImages.add(R.drawable.ma1);
        dishImages.add(R.drawable.ma2);
        dishImages.add(R.drawable.ma3);

        ImageAdapter dishAdapter = new ImageAdapter(this, dishImages, true);
        featuredDishViewPager.setAdapter(dishAdapter);

        // Tự động chạy ảnh món ăn (6 giây/ảnh)
        dishRunnable = () -> {
            int currentItem = featuredDishViewPager.getCurrentItem();
            int totalItems = dishAdapter.getItemCount();
            int nextItem = (currentItem + 1) % totalItems;
            featuredDishViewPager.setCurrentItem(nextItem, true);
            dishHandler.postDelayed(dishRunnable, 6000); // 6 giây
        };
        dishHandler.postDelayed(dishRunnable, 6000);

        // Xử lý BottomNavigationView
        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_profile) {
                    startActivity(new Intent(MainActivity.this, UserInfoActivity.class));
                    return true;
                }
                return false;
            }
        });

        // Xử lý nút thông báo
        notificationIcon = findViewById(R.id.notificationIcon);
        if (notificationIcon != null) {
            notificationIcon.setOnClickListener(v -> {
                // Chuyển hướng đến NotificationActivity
                Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                startActivity(intent);
            });
        }

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
        // Dừng tự động chạy khi Activity tạm dừng
        autoScrollHandler.removeCallbacks(autoScrollRunnable);
        dishHandler.removeCallbacks(dishRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Tiếp tục tự động chạy khi Activity được resume
        autoScrollHandler.postDelayed(autoScrollRunnable, 3000);
        dishHandler.postDelayed(dishRunnable, 6000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Dừng tự động chạy khi Activity bị hủy
        autoScrollHandler.removeCallbacks(autoScrollRunnable);
        dishHandler.removeCallbacks(dishRunnable);
    }
}