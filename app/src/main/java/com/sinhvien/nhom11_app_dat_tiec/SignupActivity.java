package com.sinhvien.nhom11_app_dat_tiec;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignupActivity extends AppCompatActivity {

    private ImageView backButton;
    private TextInputEditText nameInput, phoneInput, emailInput, passwordInput, confirmPasswordInput;
    private MaterialButton signupButton;
    private LinearLayout goLoginScreen;
    private FirebaseAuth mAuth;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        backButton = findViewById(R.id.back_button);
        nameInput = findViewById(R.id.name);
        phoneInput = findViewById(R.id.phone);
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        confirmPasswordInput = findViewById(R.id.confirm_password);
        signupButton = findViewById(R.id.signup_button);
        goLoginScreen = findViewById(R.id.go_login_screen);

        mAuth = FirebaseAuth.getInstance();
        databaseHelper = new DatabaseHelper(this);

        backButton.setOnClickListener(v -> onBackPressed());

        signupButton.setOnClickListener(v -> handleSignup());

        goLoginScreen.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void handleSignup() {
        String name = nameInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();

        // Kiểm tra các trường không được để trống
        if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra số điện thoại: bắt đầu bằng 0, đúng 10 số
        if (!phone.matches("^0\\d{9}$")) {
            Toast.makeText(this, "Số điện thoại phải bắt đầu bằng 0 và có đúng 10 số!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra số điện thoại trùng trong SQLite
        if (databaseHelper.isPhoneExists(phone)) {
            Toast.makeText(this, "Số điện thoại đã được sử dụng! Vui lòng dùng số khác.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra email: phải đúng định dạng và có đuôi hợp lệ
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ||
                !email.matches(".*\\.(com|vn|org|net|edu)$")) {
            Toast.makeText(this, "Email phải đúng định dạng và có đuôi như .com, .vn, .org, .net, .edu!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra mật khẩu: ít nhất 6 ký tự
        if (password.length() < 6) {
            Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra mật khẩu xác nhận
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu xác nhận không khớp!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Đăng ký với Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String userId = mAuth.getCurrentUser().getUid();

                        // Lưu thông tin vào SQLite
                        boolean insertSuccess = databaseHelper.addUser(userId, name, phone, email);
                        if (insertSuccess) {
                            Toast.makeText(this, "Đăng ký thành công! Vui lòng đăng nhập.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(this, "Lưu thông tin vào SQLite thất bại!", Toast.LENGTH_SHORT).show();
                            mAuth.getCurrentUser().delete(); // Xóa tài khoản Firebase nếu lưu SQLite thất bại
                        }
                    } else {
                        // Xử lý lỗi cụ thể
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(this, "Email đã được sử dụng! Vui lòng dùng email khác.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(this, "Đăng ký thất bại: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}