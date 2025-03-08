package com.sinhvien.nhom11_app_dat_tiec;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserInfoActivity extends AppCompatActivity {

    private TextView tvUserEmail, tvUserId, tvUserName;
    private Button btnLogout;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        // Ánh xạ View
        tvUserEmail = findViewById(R.id.tvUserEmail);
        tvUserId = findViewById(R.id.tvUserId);
        tvUserName = findViewById(R.id.tvUserName);
        btnLogout = findViewById(R.id.btnLogout);

        // Khởi tạo DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Lấy email từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String email = sharedPreferences.getString("user_email", "Chưa đăng nhập");
        String userId = sharedPreferences.getString("user_id", "Chưa có ID");

        // Lấy thông tin người dùng từ database
        DatabaseHelper.User user = dbHelper.getUserInfo(email);
        if (user != null) {
            tvUserName.setText(user.getName());
            tvUserEmail.setText(user.getEmail());
            tvUserId.setText(String.valueOf(user.getId()));
        } else {
            tvUserName.setText("N/A");
            tvUserEmail.setText(email);
            tvUserId.setText(userId);
        }

        // Xử lý sự kiện Đăng xuất
        btnLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(UserInfoActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        // Xử lý BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_person); // Đặt mục "Tài khoản" là đang chọn

            bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    startActivity(new Intent(UserInfoActivity.this, MainActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                } else if (itemId == R.id.nav_notification) {
                    startActivity(new Intent(UserInfoActivity.this, NotificationActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                } else if (itemId == R.id.nav_person) {
                    return true; // Đang ở UserInfoActivity, không cần chuyển
                }
                return false;
            });
        }
    }
}
