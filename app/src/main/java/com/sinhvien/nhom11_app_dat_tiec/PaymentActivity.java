package com.sinhvien.nhom11_app_dat_tiec;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class PaymentActivity extends AppCompatActivity {
    private TextView tvFullName, tvPhone, tvEmail, tvDate, tvTime, tvTableCount, tvTotalPrice;
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
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        rgPaymentMethod = findViewById(R.id.rgPaymentMethod);
        rbFullPayment = findViewById(R.id.rbFullPayment);
        rbDeposit = findViewById(R.id.rbDeposit);
        btnConfirmPayment = findViewById(R.id.btnConfirmPayment);
    }

    private void setupPaymentDetails() {
        Intent intent = getIntent();
        tvFullName.setText(intent.getStringExtra("full_name"));
        tvPhone.setText(intent.getStringExtra("phone"));
        tvEmail.setText(intent.getStringExtra("email"));
        tvDate.setText(intent.getStringExtra("date"));
        tvTime.setText(intent.getStringExtra("time"));
        tvTableCount.setText(String.valueOf(intent.getIntExtra("table_count", 0)));

        int tableCount = intent.getIntExtra("table_count", 0);
        int menuId = intent.getIntExtra("menu_id", 1);
        ArrayList<Integer> serviceIds = intent.getIntegerArrayListExtra("service_ids");

        double menuPrice = (menuId == 1) ? 3550000 : 5000000;
        double serviceCost = 0;
        if (serviceIds != null) {
            for (int serviceId : serviceIds) {
                switch (serviceId) {
                    case 1:
                        serviceCost += 100000;
                        break;
                    case 2:
                        serviceCost += 150000;
                        break;
                    case 3:
                        serviceCost += 200000;
                        break;
                }
            }
        }

        totalPrice = (tableCount * menuPrice) + serviceCost;
        DecimalFormat formatter = new DecimalFormat("#,###");
        tvTotalPrice.setText(formatter.format(totalPrice) + " VNĐ");
    }

    private void setupListeners() {
        btnConfirmPayment.setOnClickListener(this::processPayment);
    }

    private void processPayment(View v) {
        Intent intent = getIntent();
        int userId = intent.getIntExtra("user_id", -1);
        int restaurantId = intent.getIntExtra("restaurant_id", 1);
        int tableCount = intent.getIntExtra("table_count", 0);
        String date = tvDate.getText().toString();
        String time = tvTime.getText().toString();
        int menuId = intent.getIntExtra("menu_id", 1);
        ArrayList<Integer> serviceIds = intent.getIntegerArrayListExtra("service_ids");

        String fullName = tvFullName.getText().toString();
        String phone = tvPhone.getText().toString();
        String email = tvEmail.getText().toString();
        String paymentMethod = rbFullPayment.isChecked() ? "Thanh toán toàn bộ" : "Đặt cọc 50%";
        String paymentStatus = rbFullPayment.isChecked() ? "Đã thanh toán toàn bộ" : "Đã đặt cọc";
        double amountPaid = rbDeposit.isChecked() ? totalPrice * 0.5 : totalPrice;
        String note = "";

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            // Lưu booking
            long bookingId = dbHelper.addBooking(userId, restaurantId, tableCount, date, time, menuId, serviceIds, db);
            if (bookingId == -1) {
                throw new Exception("Lưu booking thất bại!");
            }

            // Lưu thanh toán với booking_id
            long paymentId = dbHelper.addThanhToan(fullName, email, phone, tableCount, date, note, totalPrice,
                    paymentMethod, amountPaid, paymentStatus, (int) bookingId, db);
            if (paymentId == -1) {
                throw new Exception("Lưu thanh toán thất bại!");
            }

            db.setTransactionSuccessful();
            Toast.makeText(this, "Thanh toán và đặt tiệc thành công! Booking ID: " + bookingId, Toast.LENGTH_SHORT).show();

            Intent mainIntent = new Intent(this, MainActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            db.endTransaction();
            db.close();
        }
    }
}