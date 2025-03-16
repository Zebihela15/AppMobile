package com.sinhvien.nhom11_app_dat_tiec;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EditBookingActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private EditText etTableCount, etFullName, etPhone, etEmail;
    private Button btnDecrease, btnIncrease, btnSelectDate, btnSaveChanges;
    private Spinner spinnerTime;
    private TextView tvOriginalDate; // Thêm TextView cho ngày gốc
    private int bookingId;
    private int paymentId;
    private String originalDate;
    private int originalMenuId;
    private List<Integer> originalServiceIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_booking);

        dbHelper = new DatabaseHelper(this);
        etTableCount = findViewById(R.id.etTableCount);
        etFullName = findViewById(R.id.etFullName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        btnDecrease = findViewById(R.id.btnDecrease);
        btnIncrease = findViewById(R.id.btnIncrease);
        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        spinnerTime = findViewById(R.id.spinnerTime);
        tvOriginalDate = findViewById(R.id.tvOriginalDate); // Khởi tạo TextView

        bookingId = getIntent().getIntExtra("booking_id", -1);
        paymentId = getIntent().getIntExtra("payment_id", -1);
        originalDate = getIntent().getStringExtra("original_date");
        Log.d("EditBooking", "Booking ID: " + bookingId + ", Payment ID: " + paymentId);

        List<String> timeOptions = Arrays.asList("15:00", "16:00", "17:00", "18:00", "19:00", "20:00");
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timeOptions);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTime.setAdapter(timeAdapter);

        // Hiển thị ngày gốc
        if (originalDate != null && !originalDate.isEmpty()) {
            tvOriginalDate.setText("Ngày gốc: " + originalDate);
        } else {
            tvOriginalDate.setText("Ngày gốc: Không có dữ liệu");
        }

        loadBookingDetails();

        btnDecrease.setOnClickListener(v -> {
            int count = Integer.parseInt(etTableCount.getText().toString());
            if (count > 5) {
                count--;
                etTableCount.setText(String.valueOf(count));
                Log.d("EditBooking", "Decreased to: " + count);
            }
        });

        btnIncrease.setOnClickListener(v -> {
            int count = Integer.parseInt(etTableCount.getText().toString());
            if (count < 50) {
                count++;
                etTableCount.setText(String.valueOf(count));
                Log.d("EditBooking", "Increased to: " + count);
            }
        });

        btnSelectDate.setOnClickListener(v -> showDatePicker());
        btnSaveChanges.setOnClickListener(v -> saveChanges());
    }

    private void loadBookingDetails() {
        String userIdStr = getSharedPreferences("UserPrefs", MODE_PRIVATE).getString("user_id", "-1");
        int userId;
        try {
            userId = Integer.parseInt(userIdStr);
        } catch (NumberFormatException e) {
            userId = -1;
        }

        if (userId == -1) {
            Toast.makeText(this, "Không tìm thấy user ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        List<DatabaseHelper.Booking> bookings = dbHelper.getUserBookings(userId);
        DatabaseHelper.Booking currentBooking = null;
        for (DatabaseHelper.Booking booking : bookings) {
            if (booking.getId() == bookingId) {
                currentBooking = booking;
                break;
            }
        }

        if (currentBooking != null) {
            etTableCount.setText(String.valueOf(currentBooking.getTableCount()));
            btnSelectDate.setText(currentBooking.getDate());
            spinnerTime.setSelection(((ArrayAdapter<String>) spinnerTime.getAdapter()).getPosition(currentBooking.getTime()));
            originalMenuId = currentBooking.getMenuId();
            originalServiceIds = dbHelper.getServiceIdsForBooking(bookingId);
            Log.d("EditBooking", "Loaded Table Count: " + currentBooking.getTableCount());
        } else {
            Toast.makeText(this, "Không tìm thấy booking với ID: " + bookingId, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        DatabaseHelper.ThanhToan thanhToan = dbHelper.getThanhToan(paymentId);
        if (thanhToan != null) {
            etFullName.setText(thanhToan.getHoTen());
            etPhone.setText(thanhToan.getSoDienThoai());
            etEmail.setText(thanhToan.getEmail());
        }
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                    btnSelectDate.setText(selectedDate);
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void saveChanges() {
        int tableCount = Integer.parseInt(etTableCount.getText().toString());
        String newDate = btnSelectDate.getText().toString();
        String newTime = spinnerTime.getSelectedItem().toString();
        String fullName = etFullName.getText().toString();
        String phone = etPhone.getText().toString();
        String email = etEmail.getText().toString();

        Log.d("EditBooking", "Saving - Table Count: " + tableCount);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date original = sdf.parse(originalDate);
            Date newSelected = sdf.parse(newDate);
            Calendar minChangeDate = Calendar.getInstance();
            minChangeDate.setTime(original);
            minChangeDate.add(Calendar.DAY_OF_MONTH, -5);

            if (newSelected.before(minChangeDate.getTime())) {
                Toast.makeText(this, "Chỉ có thể thay đổi ngày trước 5 ngày so với ngày gốc!", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (ParseException e) {
            Toast.makeText(this, "Lỗi định dạng ngày!", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean bookingUpdated = dbHelper.updateBooking(bookingId, tableCount, newDate, newTime, originalMenuId, originalServiceIds);
        if (bookingUpdated) {
            double menuPrice = originalMenuId == 1 ? 3550000 : 5000000;
            double serviceCost = 0;
            for (int serviceId : originalServiceIds) {
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
            double newTotalPrice = (tableCount * menuPrice) + serviceCost;

            DatabaseHelper.ThanhToan thanhToan = dbHelper.getThanhToan(paymentId);
            String paymentMethod = thanhToan != null ? thanhToan.getPhuongThucThanhToan() : "Đặt cọc 50%";
            double amountPaid = thanhToan != null ? thanhToan.getSoTienDaThanhToan() : newTotalPrice * 0.5;

            boolean paymentUpdated = dbHelper.updateThanhToan(paymentId, fullName, email, phone, tableCount, newDate, "",
                    newTotalPrice, paymentMethod, amountPaid, "Đã thanh toán");
            if (paymentUpdated) {
                Toast.makeText(this, "Cập nhật đơn hàng thành công!", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "Lỗi khi cập nhật thanh toán!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Lỗi khi cập nhật booking!", Toast.LENGTH_SHORT).show();
        }
    }
}