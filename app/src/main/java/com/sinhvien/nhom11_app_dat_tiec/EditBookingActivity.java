package com.sinhvien.nhom11_app_dat_tiec;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
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
    private TextView tvOriginalDate;
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
        tvOriginalDate = findViewById(R.id.tvOriginalDate);

        bookingId = getIntent().getIntExtra("booking_id", -1);
        paymentId = getIntent().getIntExtra("payment_id", -1);
        originalDate = getIntent().getStringExtra("original_date");
        Log.d("EditBooking", "Booking ID: " + bookingId + ", Payment ID: " + paymentId);

        List<String> timeOptions = Arrays.asList("15:00", "16:00", "17:00", "18:00", "19:00", "20:00");
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timeOptions);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTime.setAdapter(timeAdapter);

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
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("user_id", null);

        if (userId == null) {
            Toast.makeText(this, "Không tìm thấy user ID. Vui lòng đăng nhập lại!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        DatabaseHelper.Booking currentBooking = dbHelper.getBookingById(bookingId);

        if (currentBooking != null) {
            // Kiểm tra ngày diễn ra để ngăn chỉnh sửa nếu đã qua
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            try {
                Date bookingDate = sdf.parse(currentBooking.getDate());
                Date currentDate = new Date();
                if (bookingDate != null && bookingDate.before(currentDate)) {
                    Toast.makeText(this, "Không thể chỉnh sửa booking đã diễn ra!", Toast.LENGTH_SHORT).show();
                    btnSaveChanges.setEnabled(false);
                    btnDecrease.setEnabled(false);
                    btnIncrease.setEnabled(false);
                    btnSelectDate.setEnabled(false);
                    spinnerTime.setEnabled(false);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

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

        Calendar minDate = Calendar.getInstance();
        minDate.add(Calendar.DAY_OF_MONTH, 14);
        datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());

        datePickerDialog.show();
    }

    private void saveChanges() {
        int tableCount = Integer.parseInt(etTableCount.getText().toString());
        String newDate = btnSelectDate.getText().toString();
        String newTime = spinnerTime.getSelectedItem().toString();
        String fullName = etFullName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

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

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date original = sdf.parse(originalDate);
            Date newSelected = sdf.parse(newDate);
            Calendar minChangeDate = Calendar.getInstance();
            minChangeDate.setTime(original);
            minChangeDate.add(Calendar.DAY_OF_MONTH, -5);

            Calendar minFutureDate = Calendar.getInstance();
            minFutureDate.add(Calendar.DAY_OF_MONTH, 14);

            if (newSelected.before(minChangeDate.getTime())) {
                Toast.makeText(this, "Chỉ có thể thay đổi ngày trước 5 ngày so với ngày gốc!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (newSelected.before(minFutureDate.getTime())) {
                Toast.makeText(this, "Ngày đặt tiệc phải sau ít nhất 14 ngày kể từ hôm nay!", Toast.LENGTH_SHORT).show();
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
                    case 1: serviceCost += 100000; break;
                    case 2: serviceCost += 150000; break;
                    case 3: serviceCost += 200000; break;
                }
            }
            double newTotalPrice = (tableCount * menuPrice) + serviceCost;

            DatabaseHelper.ThanhToan thanhToan = dbHelper.getThanhToan(paymentId);
            String paymentMethod = thanhToan != null ? thanhToan.getPhuongThucThanhToan() : "Đặt cọc 50%";
            double amountPaid = thanhToan != null ? thanhToan.getSoTienDaThanhToan() : newTotalPrice * 0.5;
            String paymentStatus = thanhToan != null ? thanhToan.getTrangThaiThanhToan() : "Đã thanh toán";

            boolean paymentUpdated = dbHelper.updateThanhToan(paymentId, fullName, email, phone, tableCount, newDate, "",
                    newTotalPrice, paymentMethod, amountPaid, paymentStatus);
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