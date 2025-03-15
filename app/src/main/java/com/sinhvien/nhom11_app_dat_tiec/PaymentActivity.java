package com.sinhvien.nhom11_app_dat_tiec;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class PaymentActivity extends AppCompatActivity {

    private TextView tvFullName, tvEmail, tvPhone, tvTableCount, tvDate, tvTotalPrice, tvTime, tvMenu, tvServices, tvSubTotalPrice;
    private RadioGroup rgPaymentMethod;
    private Button btnConfirmPayment;
    private DatabaseHelper dbHelper;
    private double totalPrice;
    private long bookingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        dbHelper = new DatabaseHelper(this);
        initializeViews();
        getBookingDetails();
        setupPayment();
    }

    private void initializeViews() {
        tvFullName = findViewById(R.id.tvFullName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvTableCount = findViewById(R.id.tvTableCount);
        tvDate = findViewById(R.id.tvDate);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        rgPaymentMethod = findViewById(R.id.rgPaymentMethod);
        btnConfirmPayment = findViewById(R.id.btnConfirmPayment);
        tvTime = findViewById(R.id.tvTime);
        tvMenu = findViewById(R.id.tvMenu);
        tvServices = findViewById(R.id.tvServices);
        tvSubTotalPrice = findViewById(R.id.tvSubTotalPrice);
    }

    private void getBookingDetails() {
        Intent intent = getIntent();
        if (intent == null || intent.getExtras() == null) {
            Toast.makeText(this, "Lỗi: Không nhận được dữ liệu đặt tiệc!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        bookingId = intent.getLongExtra("booking_id", -1);
        if (bookingId == -1) {
            Log.e("PaymentActivity", "Lỗi: Không nhận được booking_id!");
            Toast.makeText(this, "Lỗi dữ liệu đặt tiệc!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String fullName = intent.getStringExtra("full_name");
        String phone = intent.getStringExtra("phone");
        String email = intent.getStringExtra("email");
        String date = intent.getStringExtra("date");
        String time = intent.getStringExtra("time");
        String menu = intent.getStringExtra("menu");
        int tableCount = intent.getIntExtra("table_count", 0);
        totalPrice = (double) intent.getLongExtra("total_price", 0L); // Sửa lỗi ClassCastException
        ArrayList<String> services = intent.getStringArrayListExtra("services");

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        tvFullName.setText(fullName != null ? "Họ tên: " + fullName : "Họ tên: Chưa có");
        tvPhone.setText(phone != null ? "Số điện thoại: " + phone : "Số điện thoại: Chưa có");
        tvEmail.setText(email != null ? "Email: " + email : "Email: Chưa có");
        tvDate.setText(date != null ? "Ngày: " + date : "Ngày: Chưa có");
        tvTime.setText(time != null ? "Giờ: " + time : "Giờ: Chưa có");
        tvMenu.setText(menu != null ? "Menu: " + menu : "Menu: Chưa có");
        tvTableCount.setText(tableCount > 0 ? "Số bàn: " + tableCount : "Số bàn: Chưa có");
        tvServices.setText(services != null && !services.isEmpty() ? "Dịch vụ: " + String.join(", ", services) : "Dịch vụ: Không có");
        tvSubTotalPrice.setText("Tổng phụ: " + currencyFormat.format(totalPrice));
        tvTotalPrice.setText("Tổng cộng: " + currencyFormat.format(totalPrice));

        Log.d("PaymentActivity", "Booking ID: " + bookingId + ", Tổng tiền: " + totalPrice);
    }

    private void setupPayment() {
        btnConfirmPayment.setOnClickListener(v -> processPayment());
    }

    private void processPayment() {
        int selectedPaymentId = rgPaymentMethod.getCheckedRadioButtonId();
        if (selectedPaymentId == -1) {
            showToast("Vui lòng chọn phương thức thanh toán");
            return;
        }

        RadioButton selectedPayment = findViewById(selectedPaymentId);
        String paymentMethod = selectedPayment.getText().toString();
        double amountPaid = paymentMethod.equals("Đặt cọc 50%") ? totalPrice * 0.5 : totalPrice;
        String paymentStatus = paymentMethod.equals("Đặt cọc 50%") ? "Đã đặt cọc" : "Đã thanh toán toàn bộ";

        String fullName = tvFullName.getText().toString().replace("Họ tên: ", "");
        String email = tvEmail.getText().toString().replace("Email: ", "");
        String phone = tvPhone.getText().toString().replace("Số điện thoại: ", "");
        int tableCount = Integer.parseInt(tvTableCount.getText().toString().replace("Số bàn: ", ""));
        String date = tvDate.getText().toString().replace("Ngày: ", "");
        String note = "";

        long result = dbHelper.addThanhToan(
                fullName, email, phone, tableCount, date, note, totalPrice, paymentMethod, amountPaid, paymentStatus, bookingId
        );
        Log.d("PaymentActivity", "ThanhToan insert result: " + result);

        if (result != -1) {
            showToast("Thanh toán thành công!");
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            showToast("Thanh toán thất bại, vui lòng thử lại!");
            Log.e("PaymentActivity", "Failed to insert ThanhToan with bookingId: " + bookingId);
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}