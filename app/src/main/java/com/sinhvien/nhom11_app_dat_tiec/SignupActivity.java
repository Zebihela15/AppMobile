package com.sinhvien.nhom11_app_dat_tiec;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class SignupActivity extends AppCompatActivity {

    private ImageView backButton;
    private TextInputEditText nameInput, phoneInput, emailInput, passwordInput, confirmPasswordInput;
    private MaterialButton signupButton;
    private LinearLayout goLoginScreen;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Ánh xạ ID từ XML
        backButton = findViewById(R.id.back_button);
        nameInput = findViewById(R.id.name);
        phoneInput = findViewById(R.id.phone);
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        confirmPasswordInput = findViewById(R.id.confirm_password);
        signupButton = findViewById(R.id.signup_button);
        goLoginScreen = findViewById(R.id.go_login_screen);

        databaseHelper = new DatabaseHelper(this);

        // Xử lý sự kiện nút quay lại
        backButton.setOnClickListener(v -> onBackPressed());

        // Xử lý sự kiện nút đăng ký
        signupButton.setOnClickListener(v -> handleSignup());

        // Chuyển đến màn hình đăng nhập khi bấm vào "Login"
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

        if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email không hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!phone.matches("^0\\d{9,10}$")) {
            Toast.makeText(this, "Số điện thoại không hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu nhập lại không khớp!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (databaseHelper.checkUserExists(email)) {
            Toast.makeText(this, "Người dùng đã tồn tại! Vui lòng đăng nhập.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean insertSuccess = databaseHelper.addUser(name, phone, email, password);
        if (insertSuccess) {
            Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}
