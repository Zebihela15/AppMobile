package com.sinhvien.nhom11_app_dat_tiec;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AccountActivity extends AppCompatActivity {

    private ImageView btnBack, avatar;
    private EditText etUserName, etPhone, etEmail;
    private Spinner spGender;
    private Button btnSave;
    private DatabaseHelper dbHelper;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_info);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://nhom11-app-tiec-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users");
        dbHelper = new DatabaseHelper(this);

        btnBack = findViewById(R.id.btnBack);
        avatar = findViewById(R.id.avatar);
        etUserName = findViewById(R.id.etUserName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        spGender = findViewById(R.id.spGender);
        btnSave = findViewById(R.id.btnSave);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGender.setAdapter(adapter);

        loadUserInfo();

        btnBack.setOnClickListener(v -> finish());
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AccountActivity", "Save button clicked");
                saveUserInfo();
            }
        });
    }

    private void loadUserInfo() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            String email = prefs.getString("user_email", currentUser.getEmail());

            Log.d("AccountActivity", "Loading info - User ID: " + userId + ", Email: " + email);

            mDatabase.child(userId).get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult().exists()) {
                    String name = task.getResult().child("name").getValue(String.class);
                    String phone = task.getResult().child("phone").getValue(String.class);
                    String emailFromDb = task.getResult().child("email").getValue(String.class);
                    String gender = task.getResult().child("gender").getValue(String.class);

                    Log.d("AccountActivity", "Firebase data - Name: " + name + ", Phone: " + phone + ", Email: " + emailFromDb + ", Gender: " + gender);

                    // Đồng bộ email từ Authentication vào Realtime Database
                    if (emailFromDb == null || !emailFromDb.equals(currentUser.getEmail())) {
                        mDatabase.child(userId).child("email").setValue(currentUser.getEmail());
                        emailFromDb = currentUser.getEmail();
                    }

                    etUserName.setText(name != null ? name : "");
                    etPhone.setText(phone != null ? phone : "");
                    etEmail.setText(emailFromDb != null ? emailFromDb : email);
                    spGender.setSelection(gender != null && gender.equals("Nữ") ? 1 : 0);
                } else {
                    Log.d("AccountActivity", "No Firebase data, checking SQLite...");
                    DatabaseHelper.User localUser = dbHelper.getUserByUserId(userId);
                    if (localUser != null) {
                        etUserName.setText(localUser.getName() != null ? localUser.getName() : "");
                        etPhone.setText(localUser.getPhone() != null ? localUser.getPhone() : "");
                        etEmail.setText(localUser.getEmail() != null ? localUser.getEmail() : email);
                        spGender.setSelection(0);
                        Log.d("AccountActivity", "SQLite data - Name: " + localUser.getName() + ", Phone: " + localUser.getPhone() + ", Email: " + localUser.getEmail());
                    } else {
                        etEmail.setText(email);
                        spGender.setSelection(0);
                        Log.d("AccountActivity", "No data in Firebase or SQLite, using default email: " + email);
                    }
                }
            });
        } else {
            Toast.makeText(this, "Vui lòng đăng nhập!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    private void saveUserInfo() {
        Log.d("AccountActivity", "saveUserInfo() started");

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Vui lòng đăng nhập!", Toast.LENGTH_SHORT).show();
            Log.e("AccountActivity", "No current user");
            return;
        }

        String userId = currentUser.getUid();
        String name = etUserName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String gender = spGender.getSelectedItem().toString();

        Log.d("AccountActivity", "Input data - Name: " + name + ", Phone: " + phone + ", Email: " + email + ", Gender: " + gender);

        if (name.isEmpty()) {
            Toast.makeText(this, "Tên không được để trống!", Toast.LENGTH_SHORT).show();
            Log.d("AccountActivity", "Validation failed: Name is empty");
            return;
        }
        if (phone.isEmpty()) {
            Toast.makeText(this, "Số điện thoại không được để trống!", Toast.LENGTH_SHORT).show();
            Log.d("AccountActivity", "Validation failed: Phone is empty");
            return;
        }
        if (email.isEmpty()) {
            Toast.makeText(this, "Email không được để trống!", Toast.LENGTH_SHORT).show();
            Log.d("AccountActivity", "Validation failed: Email is empty");
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email không hợp lệ!", Toast.LENGTH_SHORT).show();
            Log.d("AccountActivity", "Validation failed: Invalid email");
            return;
        }
        if (!phone.matches("^0\\d{9,10}$")) {
            Toast.makeText(this, "Số điện thoại không hợp lệ! (Phải bắt đầu bằng 0, 10-11 số)", Toast.LENGTH_SHORT).show();
            Log.d("AccountActivity", "Validation failed: Invalid phone");
            return;
        }

        // Lưu dữ liệu lên Firebase Realtime Database (không lưu email ngay)
        Log.d("AccountActivity", "Saving to Firebase for user: " + userId);
        DatabaseReference userRef = mDatabase.child(userId);
        userRef.child("name").setValue(name);
        userRef.child("phone").setValue(phone);
        userRef.child("gender").setValue(gender).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("AccountActivity", "Firebase Realtime Database save successful");

                // Kiểm tra email có thay đổi không
                if (!email.equals(currentUser.getEmail())) {
                    // Kiểm tra email ban đầu đã được xác minh chưa
                    if (!currentUser.isEmailVerified()) {
                        currentUser.sendEmailVerification().addOnCompleteListener(verifyTask -> {
                            if (verifyTask.isSuccessful()) {
                                Log.d("AccountActivity", "Verification email sent for original email: " + currentUser.getEmail());
                                Toast.makeText(this, "Vui lòng xác minh email ban đầu trước khi thay đổi email!", Toast.LENGTH_LONG).show();
                                mAuth.signOut();
                                finish();
                            } else {
                                String errorMsg = verifyTask.getException() != null ? verifyTask.getException().getMessage() : "Lỗi không xác định";
                                Log.e("AccountActivity", "Failed to send verification email for original email: " + errorMsg);
                                Toast.makeText(this, "Không thể gửi email xác minh: " + errorMsg, Toast.LENGTH_LONG).show();
                            }
                        });
                        return;
                    }

                    // Yêu cầu người dùng nhập lại mật khẩu để xác minh
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Xác minh danh tính");
                    builder.setMessage("Vui lòng nhập mật khẩu để xác minh trước khi thay đổi email:");

                    final EditText input = new EditText(this);
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    builder.setView(input);

                    builder.setPositiveButton("Xác minh", (dialog, which) -> {
                        String password = input.getText().toString().trim();
                        if (password.isEmpty()) {
                            Toast.makeText(this, "Vui lòng nhập mật khẩu!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Xác minh lại thông tin đăng nhập
                        AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail(), password);
                        currentUser.reauthenticate(credential).addOnCompleteListener(reauthTask -> {
                            if (reauthTask.isSuccessful()) {
                                Log.d("AccountActivity", "Reauthentication successful");

                                // Gửi email xác minh đến email mới
                                currentUser.verifyBeforeUpdateEmail(email).addOnCompleteListener(verifyTask -> {
                                    if (verifyTask.isSuccessful()) {
                                        Log.d("AccountActivity", "Verification email sent to: " + email);
                                        // Không lưu email vào Realtime Database ngay, chờ người dùng xác minh
                                        saveToLocal(userId, name, phone, currentUser.getEmail()); // Lưu email cũ tạm thời
                                        Toast.makeText(this, "Lưu thông tin thành công! Vui lòng kiểm tra email mới để xác minh.", Toast.LENGTH_LONG).show();
                                        mAuth.signOut(); // Đăng xuất để người dùng đăng nhập lại sau khi xác minh
                                        finish(); // Quay lại UserInfoActivity
                                    } else {
                                        String errorMsg = verifyTask.getException() != null ? verifyTask.getException().getMessage() : "Lỗi không xác định";
                                        Log.e("AccountActivity", "Failed to send verification email: " + errorMsg);
                                        Toast.makeText(this, "Gửi email xác minh thất bại: " + errorMsg, Toast.LENGTH_LONG).show();
                                    }
                                });
                            } else {
                                String errorMsg = reauthTask.getException() != null ? reauthTask.getException().getMessage() : "Lỗi không xác định";
                                Log.e("AccountActivity", "Reauthentication failed: " + errorMsg);
                                Toast.makeText(this, "Xác minh thất bại: " + errorMsg, Toast.LENGTH_LONG).show();
                            }
                        });
                    });

                    builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());
                    builder.show();
                } else {
                    // Nếu email không thay đổi, lưu email hiện tại vào Realtime Database
                    userRef.child("email").setValue(currentUser.getEmail());
                    saveToLocal(userId, name, phone, currentUser.getEmail());
                    Toast.makeText(this, "Lưu thông tin thành công!", Toast.LENGTH_SHORT).show();
                    finish(); // Quay lại UserInfoActivity
                }
            } else {
                String errorMsg = task.getException() != null ? task.getException().getMessage() : "Lỗi không xác định";
                Log.e("AccountActivity", "Firebase Realtime Database save failed: " + errorMsg);
                if (!isNetworkAvailable()) {
                    Log.d("AccountActivity", "No network, saving locally");
                    saveToLocal(userId, name, phone, currentUser.getEmail());
                    Toast.makeText(this, "Không có mạng, đã lưu tạm vào thiết bị!", Toast.LENGTH_SHORT).show();
                    finish(); // Quay lại UserInfoActivity
                } else {
                    Toast.makeText(this, "Lưu thất bại: " + errorMsg, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void saveToLocal(String userId, String name, String phone, String email) {
        DatabaseHelper.User localUser = dbHelper.getUserByUserId(userId);
        if (localUser != null) {
            dbHelper.updateUser(userId, name, phone, email);
        } else {
            dbHelper.addUser(userId, name, phone, email);
        }

        SharedPreferences.Editor editor = getSharedPreferences("UserPrefs", MODE_PRIVATE).edit();
        editor.putString("user_email", email);
        editor.apply();
        Log.d("AccountActivity", "Data saved locally for user: " + userId);
    }
}