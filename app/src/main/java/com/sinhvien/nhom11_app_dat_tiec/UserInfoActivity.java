package com.sinhvien.nhom11_app_dat_tiec;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserInfoActivity extends AppCompatActivity {

    private TextView tvUserEmail, tvUserId, tvUserName, tvOrderHistory, tvChangePassword, tvBookingInfo;
    private Button btnLogout;
    private DatabaseHelper dbHelper;
    private ExecutorService executorService;
    private Handler mainHandler;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        // Khởi tạo Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Khởi tạo view
        tvUserEmail = findViewById(R.id.tvUserEmail);
        tvUserId = findViewById(R.id.tvUserId);
        tvUserName = findViewById(R.id.tvUserName);
        btnLogout = findViewById(R.id.btnLogout);
        tvOrderHistory = findViewById(R.id.tvOrderHistory);
        tvChangePassword = findViewById(R.id.tvChangePassword);
        tvBookingInfo = findViewById(R.id.tvAccountInfo);

        // Khởi tạo DatabaseHelper và thread pool
        dbHelper = new DatabaseHelper(this);
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());

        // Lấy thông tin từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String email = sharedPreferences.getString("user_email", "Chưa đăng nhập");
        String userId = sharedPreferences.getString("user_id", "N/A");

        // Hiển thị thông tin cơ bản trước
        tvUserEmail.setText(email);
        tvUserId.setText(userId);

        // Load thông tin người dùng bất đồng bộ
        loadUserInfoAsync(email);

        // Sự kiện nút Logout
        btnLogout.setOnClickListener(v -> logoutUser());

        // Sự kiện xem lịch sử đơn hàng
        tvOrderHistory.setOnClickListener(v -> {
            Intent intent = new Intent(UserInfoActivity.this, OrderHistoryActivity.class);
            startActivity(intent);
        });

        // Sự kiện xem thông tin cá nhân
        tvBookingInfo.setOnClickListener(v -> {
            if (userId.equals("N/A")) {
                Toast.makeText(UserInfoActivity.this, "Vui lòng đăng nhập để xem thông tin cá nhân!", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(UserInfoActivity.this, AccountActivity.class);
            startActivity(intent);
        });

        // Sự kiện thay đổi mật khẩu
        tvChangePassword.setOnClickListener(v -> showChangePasswordDialog());

        // Cấu hình BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_profile);

            bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    startActivity(new Intent(UserInfoActivity.this, MainActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                } else if (itemId == R.id.navigation_profile) {
                    return true;
                }
                return false;
            });
        }
    }

    // Hàm xử lý đăng xuất
    private void logoutUser() {
        mAuth.signOut();

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(UserInfoActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    // Hàm load thông tin người dùng bất đồng bộ
    private void loadUserInfoAsync(String email) {
        executorService.execute(() -> {
            DatabaseHelper.User user = dbHelper.getUserInfo(email);
            mainHandler.post(() -> {
                if (user != null) {
                    tvUserName.setText(user.getName());
                    tvUserEmail.setText(user.getEmail());
                    tvUserId.setText(user.getUserId());
                } else {
                    tvUserName.setText("N/A");
                }
            });
        });
    }

    // Hàm hiển thị dialog đổi mật khẩu
    private void showChangePasswordDialog() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Vui lòng đăng nhập để thay đổi mật khẩu!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Inflate layout cho dialog
        View dialogView = LayoutInflater.from(this).inflate(R.layout.activity_doi_mk, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        // Khởi tạo các view trong dialog
        EditText etNewPassword = dialogView.findViewById(R.id.etNewPassword);
        EditText etConfirmPassword = dialogView.findViewById(R.id.etConfirmPassword);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnConfirm = dialogView.findViewById(R.id.btnConfirm);

        AlertDialog dialog = builder.create();

        // Sự kiện nút Hủy
        btnCancel.setOnClickListener(v -> dialog.dismiss());

        // Sự kiện nút Xác nhận
        btnConfirm.setOnClickListener(v -> {
            String newPassword = etNewPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            // Kiểm tra dữ liệu nhập vào
            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(UserInfoActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(UserInfoActivity.this, "Mật khẩu xác nhận không khớp!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (newPassword.length() < 6) {
                Toast.makeText(UserInfoActivity.this, "Mật khẩu phải có ít nhất 6 ký tự!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Cập nhật mật khẩu trên Firebase
            currentUser.updatePassword(newPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(UserInfoActivity.this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(UserInfoActivity.this, "Đổi mật khẩu thất bại: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });

        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}