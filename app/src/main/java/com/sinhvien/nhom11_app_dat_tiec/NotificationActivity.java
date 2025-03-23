package com.sinhvien.nhom11_app_dat_tiec;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.Locale;

public class NotificationActivity extends AppCompatActivity {

    private TextView tvCountdown, tvTableCount, tvDate, tvTime, tvFullName, tvPhone, tvEmail;
    private Button btnBack;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_notification);

        dbHelper = new DatabaseHelper(this);

        tvCountdown = findViewById(R.id.tvCountdown);
        tvTableCount = findViewById(R.id.tvTableCount);
        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);
        tvFullName = findViewById(R.id.tvFullName);
        tvPhone = findViewById(R.id.tvPhone);
        tvEmail = findViewById(R.id.tvEmail);
        btnBack = findViewById(R.id.btnBack);

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("user_id", null);

        if (userId == null) {
            Toast.makeText(this, "Không tìm thấy user ID. Vui lòng đăng nhập lại!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("booking_id")) {
            int bookingId = extras.getInt("booking_id", -1);
            if (bookingId != -1) {
                loadBookingDetailsFromDatabase(bookingId);
            } else {
                loadFromIntent(extras);
            }
        } else if (extras != null) {
            loadFromIntent(extras);
        } else {
            loadLatestBookingFromDatabase(userId);
        }

        btnBack.setOnClickListener(v -> finish());
    }

    private void loadFromIntent(Bundle extras) {
        String date = extras.getString("date");
        String time = extras.getString("time");
        int tableCount = extras.getInt("tableCount");
        String fullName = extras.getString("fullName");
        String phone = extras.getString("phone");
        String email = extras.getString("email");

        Log.d("Notification", "Nhận dữ liệu từ Intent: date=" + date + ", time=" + time + ", tableCount=" + tableCount);

        if (date != null && time != null && fullName != null && phone != null && email != null) {
            displayBookingDetails(date, time, tableCount, fullName, phone, email);
        } else {
            tvCountdown.setText("Dữ liệu từ Intent không đầy đủ!");
        }
    }

    private void loadBookingDetailsFromDatabase(int bookingId) {
        DatabaseHelper.Booking booking = dbHelper.getBookingById(bookingId);
        if (booking != null) {
            DatabaseHelper.ThanhToan thanhToan = dbHelper.getThanhToanByBookingId(bookingId);
            if (thanhToan != null) {
                String date = booking.getDate();
                String time = booking.getTime();
                int tableCount = booking.getTableCount();
                String fullName = thanhToan.getHoTen();
                String phone = thanhToan.getSoDienThoai();
                String email = thanhToan.getEmail();

                displayBookingDetails(date, time, tableCount, fullName, phone, email);
            } else {
                tvCountdown.setText("Không tìm thấy thông tin thanh toán cho booking ID: " + bookingId);
            }
        } else {
            tvCountdown.setText("Không tìm thấy booking với ID: " + bookingId);
        }
    }

    private void loadLatestBookingFromDatabase(String userId) {
        List<DatabaseHelper.Booking> bookings = dbHelper.getUserBookings(userId);
        if (bookings != null && !bookings.isEmpty()) {
            DatabaseHelper.Booking latestBooking = bookings.get(bookings.size() - 1);
            int bookingId = latestBooking.getId();
            DatabaseHelper.ThanhToan thanhToan = dbHelper.getThanhToanByBookingId(bookingId);
            if (thanhToan != null) {
                String date = latestBooking.getDate();
                String time = latestBooking.getTime();
                int tableCount = latestBooking.getTableCount();
                String fullName = thanhToan.getHoTen();
                String phone = thanhToan.getSoDienThoai();
                String email = thanhToan.getEmail();

                displayBookingDetails(date, time, tableCount, fullName, phone, email);
            } else {
                tvCountdown.setText("Không tìm thấy thông tin thanh toán cho booking gần nhất!");
            }
        } else {
            tvCountdown.setText("Bạn chưa có booking nào!");
        }
    }

    private void displayBookingDetails(String date, String time, int tableCount, String fullName, String phone, String email) {
        tvTableCount.setText("Số bàn: " + tableCount);
        tvDate.setText("Ngày: " + date);
        tvTime.setText("Giờ: " + time);
        tvFullName.setText("Tên khách hàng: " + fullName);
        tvPhone.setText("Số điện thoại: " + phone);
        tvEmail.setText("Email: " + email);

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date orderDate = sdf.parse(date);
            Date currentDate = new Date();

            long diffInMillies = orderDate.getTime() - currentDate.getTime();
            long daysLeft = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            if (daysLeft > 0) {
                tvCountdown.setText("Còn " + daysLeft + " ngày");
            } else if (daysLeft == 0) {
                tvCountdown.setText("Hôm nay là ngày đặt tiệc!");
            } else {
                tvCountdown.setText("Đã qua ngày đặt tiệc!");
            }
        } catch (ParseException e) {
            Log.e("Notification", "Lỗi định dạng ngày: ", e);
            tvCountdown.setText("Không thể tính thời gian");
        }
    }
}