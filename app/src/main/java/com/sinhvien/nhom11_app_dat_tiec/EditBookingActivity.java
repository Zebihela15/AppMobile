package com.sinhvien.nhom11_app_dat_tiec;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class EditBookingActivity extends AppCompatActivity {

    private EditText etTableCount, etFullName, etPhone, etEmail;
    private Button btnSelectDate, btnSaveChanges;
    private Spinner spinnerTime;
    private RadioGroup rgMenu;
    private CheckBox cbService1, cbService2, cbService3;
    private TextView tvTotalPrice, tvRestaurantName;
    private ImageView ivRestaurant;
    private DatabaseHelper dbHelper;
    private int tableCount;
    private String selectedDate;
    private static final int MAX_TABLES = 20;
    private long bookingId;
    private DatabaseHelper.ThanhToan thanhToan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_booking);

        dbHelper = new DatabaseHelper(this);
        initializeViews();
        loadBookingData();
        setupClickListeners();
        setupTimeSpinner();
        updateTotalPrice();
    }

    private void initializeViews() {
        etTableCount = findViewById(R.id.etTableCount);
        btnSelectDate = findViewById(R.id.btnSelectDate);
        spinnerTime = findViewById(R.id.spinnerTime);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        etFullName = findViewById(R.id.etFullName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        rgMenu = findViewById(R.id.rgMenu);
        cbService1 = findViewById(R.id.cbService1);
        cbService2 = findViewById(R.id.cbService2);
        cbService3 = findViewById(R.id.cbService3);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        ivRestaurant = findViewById(R.id.ivRestaurant);
        tvRestaurantName = findViewById(R.id.tvRestaurantName);

        View.OnClickListener priceListener = v -> updateTotalPrice();
        cbService1.setOnClickListener(priceListener);
        cbService2.setOnClickListener(priceListener);
        cbService3.setOnClickListener(priceListener);
        rgMenu.setOnCheckedChangeListener((group, checkedId) -> updateTotalPrice());
    }

    private void loadBookingData() {
        Intent intent = getIntent();
        bookingId = intent.getLongExtra("booking_id", -1);
        Log.d("EditBookingActivity", "Booking ID received: " + bookingId);
        if (bookingId == -1) {
            showToast("Không tìm thấy đơn đặt tiệc - bookingId không hợp lệ");
            finish();
            return;
        }

        DatabaseHelper.Booking booking = dbHelper.getBookingById(bookingId);
        Log.d("EditBookingActivity", "Booking: " + (booking != null ? "Found - Date: " + booking.getBookingDate() : "null"));
        if (booking == null) {
            showToast("Không tìm thấy đơn đặt tiệc với bookingId: " + bookingId);
            finish();
            return;
        }

        thanhToan = dbHelper.getThanhToanByBookingId(bookingId);
        Log.d("EditBookingActivity", "ThanhToan: " + (thanhToan != null ? "Found - Name: " + thanhToan.getHoTen() : "null"));

        // Điền dữ liệu từ booking
        tableCount = booking.getTableCount();
        selectedDate = booking.getBookingDate();
        etTableCount.setText(String.valueOf(tableCount));
        btnSelectDate.setText(selectedDate);
        spinnerTime.setSelection(getTimePosition(booking.getBookingTime()));
        tvRestaurantName.setText(booking.getRestaurantName());
        ivRestaurant.setImageResource(intent.getIntExtra("restaurant_image", R.drawable.dtc1));

        if (booking.getMenuId() == 1) rgMenu.check(R.id.rbMenu1);
        else if (booking.getMenuId() == 2) rgMenu.check(R.id.rbMenu2);

        ArrayList<String> services = intent.getStringArrayListExtra("services");
        if (services != null) {
            cbService1.setChecked(services.contains("Dịch vụ 1"));
            cbService2.setChecked(services.contains("Dịch vụ 2"));
            cbService3.setChecked(services.contains("Dịch vụ 3"));
        }

        if (thanhToan != null) {
            etFullName.setText(thanhToan.getHoTen());
            etPhone.setText(thanhToan.getSoDienThoai());
            etEmail.setText(thanhToan.getEmail());
        } else {
            etFullName.setText("");
            etPhone.setText("");
            etEmail.setText("");
            Log.d("EditBookingActivity", "No payment record found, allowing edit without payment info");
        }
    }

    private int getTimePosition(String time) {
        String[] times = {"7:00", "7:30", "8:00", "8:30", "9:00", "9:30", "10:00"};
        for (int i = 0; i < times.length; i++) {
            if (times[i].equals(time)) return i;
        }
        return 0;
    }

    private void setupClickListeners() {
        findViewById(R.id.btnIncrease).setOnClickListener(v -> {
            if (tableCount < MAX_TABLES) {
                tableCount++;
                etTableCount.setText(String.valueOf(tableCount));
                updateTotalPrice();
            } else {
                showToast("Số bàn tối đa là " + MAX_TABLES);
            }
        });

        findViewById(R.id.btnDecrease).setOnClickListener(v -> {
            if (tableCount > 5) {
                tableCount--;
                etTableCount.setText(String.valueOf(tableCount));
                updateTotalPrice();
            } else {
                showToast("Số bàn tối thiểu là 5");
            }
        });

        btnSelectDate.setOnClickListener(v -> showDatePickerDialog());
        btnSaveChanges.setOnClickListener(v -> handleSaveChanges());
    }

    private void setupTimeSpinner() {
        String[] times = {"7:00", "7:30", "8:00", "8:30", "9:00", "9:30", "10:00"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, times);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTime.setAdapter(adapter);
    }

    private double calculateTotalPrice() { // Đổi sang double để đồng bộ với PaymentActivity
        double total = 0;
        int selectedMenuId = rgMenu.getCheckedRadioButtonId();
        if (selectedMenuId == R.id.rbMenu1) total += tableCount * 3550000.0;
        else if (selectedMenuId == R.id.rbMenu2) total += tableCount * 5000000.0;
        if (cbService1.isChecked()) total += 100000.0;
        if (cbService2.isChecked()) total += 150000.0;
        if (cbService3.isChecked()) total += 200000.0;
        return total;
    }

    private void updateTotalPrice() {
        double total = calculateTotalPrice();
        tvTotalPrice.setText(String.format("Tổng cộng: %,d VNĐ", (long) total)); // Ép về long để hiển thị không có thập phân
    }

    private void handleSaveChanges() {
        String fullName = etFullName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String time = spinnerTime.getSelectedItem().toString();
        int menuId = getSelectedMenuId();
        double totalPrice = calculateTotalPrice();

        if (!validateInput(fullName, phone, email)) return;

        // Cập nhật bảng bookings
        boolean bookingUpdated = dbHelper.updateBooking(
                bookingId, tableCount, selectedDate, time, menuId, totalPrice
        );

        // Xóa và cập nhật lại dịch vụ
        dbHelper.deleteBookingServices(bookingId);
        boolean servicesAdded = true;
        if (cbService1.isChecked()) servicesAdded &= dbHelper.addBookingService(bookingId, 1);
        if (cbService2.isChecked()) servicesAdded &= dbHelper.addBookingService(bookingId, 2);
        if (cbService3.isChecked()) servicesAdded &= dbHelper.addBookingService(bookingId, 3);

        // Cập nhật hoặc thêm mới bảng thanhtoan
        boolean thanhToanUpdated;
        if (thanhToan != null) {
            thanhToanUpdated = dbHelper.updateThanhToan(
                    thanhToan.getMaThanhToan(), fullName, email, phone, tableCount, selectedDate,
                    thanhToan.getGhiChu(), totalPrice, thanhToan.getPhuongThucThanhToan(),
                    thanhToan.getSoTienDaThanhToan(), thanhToan.getTrangThaiThanhToan()
            );
        } else {
            long result = dbHelper.addThanhToan(
                    fullName, email, phone, tableCount, selectedDate, "", totalPrice,
                    "Chưa thanh toán", 0, "Chưa thanh toán", bookingId
            );
            thanhToanUpdated = result != -1;
        }

        if (bookingUpdated && thanhToanUpdated && servicesAdded) {
            showToast("Cập nhật đơn đặt tiệc thành công!", Toast.LENGTH_LONG);
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("updated_total_price", totalPrice);
            startActivity(intent);
            finish();
        } else {
            showToast("Cập nhật thất bại, vui lòng thử lại");
            Log.d("EditBookingActivity", "Booking updated: " + bookingUpdated +
                    ", ThanhToan updated: " + thanhToanUpdated +
                    ", Services added: " + servicesAdded);
        }
    }

    private int getSelectedMenuId() {
        int selectedId = rgMenu.getCheckedRadioButtonId();
        if (selectedId == R.id.rbMenu1) return 1;
        if (selectedId == R.id.rbMenu2) return 2;
        return -1;
    }

    private boolean validateInput(String fullName, String phone, String email) {
        if (selectedDate.isEmpty()) {
            showToast("Vui lòng chọn ngày");
            return false;
        }
        if (fullName.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            showToast("Vui lòng điền đầy đủ thông tin");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Email không hợp lệ");
            return false;
        }
        if (!Patterns.PHONE.matcher(phone).matches() || phone.length() < 10) {
            showToast("Số điện thoại không hợp lệ");
            return false;
        }
        RadioButton rbMenu1 = findViewById(R.id.rbMenu1);
        RadioButton rbMenu2 = findViewById(R.id.rbMenu2);
        if (!rbMenu1.isChecked() && !rbMenu2.isChecked()) {
            showToast("Vui lòng chọn 1 trong 2 menu");
            return false;
        }
        return true;
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    btnSelectDate.setText(selectedDate);
                }, year, month, day);

        calendar.add(Calendar.DAY_OF_MONTH, 7);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showToast(String message, int duration) {
        Toast.makeText(this, message, duration).show();
    }
}