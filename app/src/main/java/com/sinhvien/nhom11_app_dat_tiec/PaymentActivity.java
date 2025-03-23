package com.sinhvien.nhom11_app_dat_tiec;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PaymentActivity extends AppCompatActivity {
    private TextView tvFullName, tvPhone, tvEmail, tvDate, tvTime, tvTableCount, tvTotalPrice;
    private TextView tvSubTotalPrice, tvDiscount, tvTax;
    private RadioGroup rgPaymentMethod;
    private RadioButton rbFullPayment, rbDeposit;
    private Button btnConfirmPayment;
    private DatabaseHelper dbHelper;
    private double totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        dbHelper = new DatabaseHelper(this);
        initViews();
        setupPaymentDetails();
        setupListeners();
    }

    private void initViews() {
        tvFullName = findViewById(R.id.tvFullName);
        tvPhone = findViewById(R.id.tvPhone);
        tvEmail = findViewById(R.id.tvEmail);
        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);
        tvTableCount = findViewById(R.id.tvTableCount);
        tvSubTotalPrice = findViewById(R.id.tvSubTotalPrice);
        tvDiscount = findViewById(R.id.DisFeeTxt);
        tvTax = findViewById(R.id.thueTxt);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        rgPaymentMethod = findViewById(R.id.rgPaymentMethod);
        rbFullPayment = findViewById(R.id.rbFullPayment);
        rbDeposit = findViewById(R.id.rbDeposit);
        btnConfirmPayment = findViewById(R.id.btnConfirmPayment);
    }

    private void setupPaymentDetails() {
        Intent intent = getIntent();
        tvFullName.setText("Tên khách hàng: " + intent.getStringExtra("full_name"));
        tvPhone.setText("Số điện thoại: " + intent.getStringExtra("phone"));
        tvEmail.setText("Email: " + intent.getStringExtra("email"));
        tvDate.setText("Ngày: " + intent.getStringExtra("date"));
        tvTime.setText("Giờ: " + intent.getStringExtra("time"));

        int tableCount = intent.getIntExtra("table_count", 0);
        tvTableCount.setText("Số bàn: " + tableCount);
        Log.d("Payment", "Table count received: " + tableCount);

        int menuId = intent.getIntExtra("menu_id", 1);
        ArrayList<Integer> serviceIds = intent.getIntegerArrayListExtra("service_ids");

        double menuPrice = (menuId == 1) ? 3550000 : 5000000;
        double serviceCost = 0;
        if (serviceIds != null) {
            for (int serviceId : serviceIds) {
                switch (serviceId) {
                    case 1: serviceCost += 100000; break;
                    case 2: serviceCost += 150000; break;
                    case 3: serviceCost += 200000; break;
                }
            }
        }

        totalPrice = (tableCount * menuPrice) + serviceCost;
        double subTotal = totalPrice;
        double discount = 0;
        double tax = 0;

        DecimalFormat formatter = new DecimalFormat("#,###");
        tvSubTotalPrice.setText("Tổng phụ: " + formatter.format(subTotal) + " VNĐ");
        tvDiscount.setText("Giảm giá: " + formatter.format(discount) + " VNĐ");
        tvTax.setText("Thuế: " + formatter.format(tax) + " VNĐ");
        tvTotalPrice.setText("Tổng cộng: " + formatter.format(totalPrice) + " VNĐ");
    }

    private void setupListeners() {
        btnConfirmPayment.setOnClickListener(this::processPayment);
    }

    private void processPayment(View v) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("user_id", null);

        Intent intent = getIntent();
        int restaurantId = intent.getIntExtra("restaurant_id", 1);
        int tableCount = intent.getIntExtra("table_count", 0);
        String date = tvDate.getText().toString().replace("Ngày: ", "");
        String time = tvTime.getText().toString().replace("Giờ: ", "");
        int menuId = intent.getIntExtra("menu_id", 1);
        ArrayList<Integer> serviceIds = intent.getIntegerArrayListExtra("service_ids");

        String fullName = tvFullName.getText().toString().replace("Tên khách hàng: ", "").trim();
        String phone = tvPhone.getText().toString().replace("Số điện thoại: ", "").trim();
        String email = tvEmail.getText().toString().replace("Email: ", "").trim();
        String paymentMethod = rbFullPayment.isChecked() ? "Thanh toán toàn bộ" : "Đặt cọc 50%";
        String paymentStatus = rbFullPayment.isChecked() ? "Đã thanh toán toàn bộ" : "Đã đặt cọc";
        double amountPaid = rbDeposit.isChecked() ? totalPrice * 0.5 : totalPrice;
        String note = "";

        Log.d("Payment", "userId: " + userId + ", restaurantId: " + restaurantId + ", tableCount: " + tableCount);
        Log.d("Payment", "date: " + date + ", time: " + time + ", menuId: " + menuId + ", serviceIds: " + serviceIds);

        if (userId == null) {
            Toast.makeText(this, "Lỗi: Không tìm thấy User ID. Vui lòng đăng nhập lại!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (fullName.isEmpty() || phone.isEmpty() || email.isEmpty()) {
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
        if (tableCount < 5 || tableCount > 50) {
            Toast.makeText(this, "Lỗi: Số bàn phải từ 5 đến 50!", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar minDate = Calendar.getInstance();
        minDate.add(Calendar.DAY_OF_MONTH, 14);
        Date selectedDate;
        try {
            selectedDate = sdf.parse(date);
            if (selectedDate.before(minDate.getTime())) {
                Toast.makeText(this, "Lỗi: Ngày đặt tiệc phải cách ít nhất 14 ngày từ hôm nay!", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (ParseException e) {
            Toast.makeText(this, "Lỗi: Định dạng ngày không đúng (yêu cầu: yyyy-MM-dd)!", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> validTimes = Arrays.asList("15:00", "16:00", "17:00", "18:00", "19:00", "20:00");
        if (!validTimes.contains(time)) {
            Toast.makeText(this, "Lỗi: Giờ đặt tiệc phải là: 15:00, 16:00, 17:00, 18:00, 19:00 hoặc 20:00!", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            int bookingId = dbHelper.addBooking(userId, restaurantId, tableCount, date, time, menuId, serviceIds, db);
            if (bookingId == -1) {
                throw new Exception("Lưu booking thất bại! Kiểm tra dữ liệu đầu vào hoặc cơ sở dữ liệu.");
            }
            Log.d("Payment", "Booking ID created: " + bookingId);

            int paymentId = dbHelper.addThanhToan(fullName, email, phone, tableCount, date, note, totalPrice,
                    paymentMethod, amountPaid, paymentStatus, bookingId, db);
            if (paymentId == -1) {
                throw new Exception("Lưu thanh toán thất bại!");
            }
            Log.d("Payment", "Payment ID created: " + paymentId);

            db.setTransactionSuccessful();
            Toast.makeText(this, "Thanh toán và đặt tiệc thành công! Booking ID: " + bookingId, Toast.LENGTH_LONG).show();

            Intent notificationIntent = new Intent(this, NotificationActivity.class);
            notificationIntent.putExtra("booking_id", bookingId);
            notificationIntent.putExtra("date", date);
            notificationIntent.putExtra("time", time);
            notificationIntent.putExtra("tableCount", tableCount);
            notificationIntent.putExtra("fullName", fullName);
            notificationIntent.putExtra("phone", phone);
            notificationIntent.putExtra("email", email);
            startActivity(notificationIntent);

            Intent mainIntent = new Intent(this, MainActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);
            finish();
        } catch (Exception e) {
            Log.e("PaymentError", "Lỗi khi xử lý thanh toán: ", e);
            Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if (db.inTransaction()) {
                db.endTransaction();
            }
            db.close();
        }
    }
}