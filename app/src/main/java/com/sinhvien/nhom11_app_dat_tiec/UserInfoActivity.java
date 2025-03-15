package com.sinhvien.nhom11_app_dat_tiec;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class UserInfoActivity extends AppCompatActivity {

    private TextView tvUserEmail, tvUserId, tvUserName, tvOrderHistory, tvChangePassword, tvBookingInfo;
    private Button btnLogout;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        tvUserEmail = findViewById(R.id.tvUserEmail);
        tvUserId = findViewById(R.id.tvUserId);
        tvUserName = findViewById(R.id.tvUserName);
        btnLogout = findViewById(R.id.btnLogout);
        tvOrderHistory = findViewById(R.id.tvOrderHistory);
        tvChangePassword = findViewById(R.id.tvChangePassword);
        tvBookingInfo = findViewById(R.id.tvBookingInfo);

        dbHelper = new DatabaseHelper(this);

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String email = sharedPreferences.getString("user_email", "Chưa đăng nhập");
        String userId = sharedPreferences.getString("user_id", "Chưa có ID");

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

        btnLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(UserInfoActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        tvOrderHistory.setOnClickListener(v -> {
            Intent intent = new Intent(UserInfoActivity.this, OrderHistoryActivity.class);
            startActivity(intent);
        });

        // Sửa sự kiện cho tvBookingInfo
        tvBookingInfo.setOnClickListener(v -> {
            List<DatabaseHelper.Booking> bookings = dbHelper.getUserBookings(email);
            if (bookings.isEmpty()) {
                Toast.makeText(UserInfoActivity.this, "Bạn chưa có đơn đặt tiệc nào!", Toast.LENGTH_SHORT).show();
            } else {
                // Lấy đơn đặt tiệc đầu tiên (hoặc hiển thị danh sách để chọn)
                DatabaseHelper.Booking booking = bookings.get(0); // Ví dụ: chọn đơn đầu tiên
                Intent intent = new Intent(UserInfoActivity.this, EditBookingActivity.class);
                intent.putExtra("booking_id", (long) booking.getId()); // Truyền booking_id
                startActivity(intent);
            }
        });

        tvChangePassword.setOnClickListener(v -> {
            Toast.makeText(UserInfoActivity.this, "Tính năng đang cập nhật", Toast.LENGTH_SHORT).show();
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_person);

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
                    return true;
                }
                return false;
            });
        }
    }
}