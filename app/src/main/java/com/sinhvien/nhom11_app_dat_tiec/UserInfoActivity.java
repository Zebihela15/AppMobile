package com.sinhvien.nhom11_app_dat_tiec;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserInfoActivity extends AppCompatActivity {

    private TextView tvUserEmail, tvUserId, tvUserName, tvOrderHistory, tvChangePassword, tvBookingInfo;
    private Button btnLogout;
    private DatabaseHelper dbHelper;
    private ExecutorService executorService;
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        // Khởi tạo view
        tvUserEmail = findViewById(R.id.tvUserEmail);
        tvUserId = findViewById(R.id.tvUserId);
        tvUserName = findViewById(R.id.tvUserName);
        btnLogout = findViewById(R.id.btnLogout);
        tvOrderHistory = findViewById(R.id.tvOrderHistory);
        tvChangePassword = findViewById(R.id.tvChangePassword);
        tvBookingInfo = findViewById(R.id.tvBookingInfo);

        // Khởi tạo DatabaseHelper và thread pool
        dbHelper = new DatabaseHelper(this);
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());

        // Lấy thông tin từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String email = sharedPreferences.getString("user_email", "Chưa đăng nhập");
        String userIdStr = sharedPreferences.getString("user_id", "-1");
        int userId;
        try {
            userId = Integer.parseInt(userIdStr);
        } catch (NumberFormatException e) {
            userId = -1;
        }
        final int finalUserId = userId;

        // Hiển thị thông tin cơ bản trước
        tvUserEmail.setText(email);
        tvUserId.setText(String.valueOf(userId));

        // Load thông tin người dùng bất đồng bộ
        loadUserInfoAsync(email);

        // Sự kiện nút Logout
        btnLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(UserInfoActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        // Sự kiện xem lịch sử đơn hàng
        tvOrderHistory.setOnClickListener(v -> {
            Intent intent = new Intent(UserInfoActivity.this, OrderHistoryActivity.class);
            startActivity(intent);
        });

        // Sự kiện xem thông tin đặt tiệc
        tvBookingInfo.setOnClickListener(v -> {
            if (finalUserId == -1) {
                Toast.makeText(UserInfoActivity.this, "Vui lòng đăng nhập để xem thông tin đặt tiệc!", Toast.LENGTH_SHORT).show();
                return;
            }

            executorService.execute(() -> {
                List<DatabaseHelper.Booking> bookings = dbHelper.getUserBookings(finalUserId);
                mainHandler.post(() -> {
                    if (bookings.isEmpty()) {
                        Toast.makeText(UserInfoActivity.this, "Bạn chưa có đơn đặt tiệc nào!", Toast.LENGTH_SHORT).show();
                    } else {
                        DatabaseHelper.Booking booking = bookings.get(0);
                        Intent intent = new Intent(UserInfoActivity.this, EditBookingActivity.class);
                        intent.putExtra("booking_id", booking.getId()); // Không cần ép kiểu sang long
                        List<DatabaseHelper.Order> orders = dbHelper.getOrderHistory(finalUserId);
                        for (DatabaseHelper.Order order : orders) {
                            if (order.getBookingId() == booking.getId()) {
                                intent.putExtra("payment_id", order.getPaymentId()); // Không cần ép kiểu sang long
                                intent.putExtra("original_date", order.getDate());
                                break;
                            }
                        }
                        startActivity(intent);
                    }
                });
            });
        });

        // Sự kiện thay đổi mật khẩu (chưa triển khai)
        tvChangePassword.setOnClickListener(v -> {
            Toast.makeText(UserInfoActivity.this, "Tính năng đang cập nhật", Toast.LENGTH_SHORT).show();
        });

        // Cấu hình BottomNavigationView
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

    private void loadUserInfoAsync(String email) {
        executorService.execute(() -> {
            DatabaseHelper.User user = dbHelper.getUserInfo(email);
            mainHandler.post(() -> {
                if (user != null) {
                    tvUserName.setText(user.getName());
                    tvUserEmail.setText(user.getEmail());
                    tvUserId.setText(String.valueOf(user.getId()));
                } else {
                    tvUserName.setText("N/A");
                }
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown(); // Đóng thread pool khi activity bị hủy
    }
}