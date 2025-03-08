package com.sinhvien.nhom11_app_dat_tiec;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText emailInput, passwordInput;
    private TextInputLayout emailInputLayout, passwordInputLayout;
    private MaterialButton loginButton;
    private LinearLayout goSignupScreen;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.input_email);
        passwordInput = findViewById(R.id.password);
        emailInputLayout = findViewById(R.id.email_input_layout);
        passwordInputLayout = findViewById(R.id.password_input_layout);
        loginButton = findViewById(R.id.login_button);
        goSignupScreen = findViewById(R.id.go_signup_screen);

        // Vô hiệu hóa hiệu ứng
        emailInputLayout.setHintAnimationEnabled(false);
        passwordInputLayout.setHintAnimationEnabled(false);

        // Đặt không focus khi chạm vào
        emailInput.setOnFocusChangeListener((v, hasFocus) -> {
            // Giữ hint luôn hiển thị, không di chuyển
            emailInputLayout.setHintEnabled(true);
        });

        passwordInput.setOnFocusChangeListener((v, hasFocus) -> {
            // Giữ hint luôn hiển thị, không di chuyển
            passwordInputLayout.setHintEnabled(true);
        });

        databaseHelper = new DatabaseHelper(this);

        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(email, password);
            }
        });

        // Chuyển sang màn hình đăng ký
        goSignupScreen.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, com.sinhvien.nhom11_app_dat_tiec.SignupActivity.class);
            startActivity(intent);
        });
    }

    private void loginUser(String email, String password) {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (databaseHelper.checkLogin(email, password)) {
            Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

            // Lưu email và user_id vào SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("user_email", email);
            int userId = databaseHelper.getUserId(email, password); // Lấy user_id
            editor.putString("user_id", String.valueOf(userId)); // Lưu user_id dưới dạng String
            editor.apply();

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Email hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
        }
    }
}