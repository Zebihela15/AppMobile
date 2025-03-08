package com.sinhvien.nhom11_app_dat_tiec;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class InfoNhaHang1Activity extends AppCompatActivity {

    private Dialog fullIntroDialog;
    private ScrollView scrollView;
    private TextView introSection, gallerySection;
    private RecyclerView menuSection;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_nhahang1);

        // Khởi tạo các view
        NestedScrollView scrollView = findViewById(R.id.scrollView);

        introSection = findViewById(R.id.introSection);
        gallerySection = findViewById(R.id.gallerySection);
        menuSection = findViewById(R.id.menuSection);
        bottomNavigationView = findViewById(R.id.navigation);

        // Khởi tạo Dialog
        fullIntroDialog = new Dialog(this);
        fullIntroDialog.setContentView(R.layout.dialog_intro);
        fullIntroDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        // Khởi tạo RecyclerView
        int[] imageResources = {
                R.drawable.d11, R.drawable.d12, R.drawable.d13,
                R.drawable.d14, R.drawable.d15
        };
        menuSection.setLayoutManager(new GridLayoutManager(this, 3));
        menuSection.setAdapter(new ImageAdapter(this, imageResources));

        // Xử lý BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_person); // Đặt mục đang chọn nếu cần

            bottomNavigationView.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    startActivity(new Intent(InfoNhaHang1Activity.this, MainActivity.class));
                    overridePendingTransition(0, 0);
                    finish(); // Đóng InfoNhaHang1Activity
                    return true;
                } else if (itemId == R.id.nav_notification) {
                    startActivity(new Intent(InfoNhaHang1Activity.this, NotificationActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                } else if (itemId == R.id.nav_person) {
                    startActivity(new Intent(InfoNhaHang1Activity.this, UserInfoActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                }
                return false;
            });
        }

    }

    public void toggleIntro(View view) {
        fullIntroDialog.show();
    }

    public void closeDialog(View view) {
        fullIntroDialog.dismiss();
    }

    public void scrollToIntro(View view) {
        scrollView.smoothScrollTo(0, introSection.getTop());
    }

    public void scrollToMenu(View view) {
        scrollView.smoothScrollTo(0, menuSection.getTop());
    }

    public void scrollToGallery(View view) {
        scrollView.smoothScrollTo(0, gallerySection.getTop());
    }

    public void onBookingButtonClick(View view) {
        Intent intent = new Intent(this, BookingActivity.class);
        startActivity(intent);
    }
}