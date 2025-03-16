package com.sinhvien.nhom11_app_dat_tiec;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BookingActivity extends AppCompatActivity {
    private Spinner spinnerRestaurant, spinnerTime;
    private EditText etTableCount, etFullName, etPhone, etEmail;
    private CheckBox cbService1, cbService2, cbService3;
    private RadioGroup rgMenu;
    private RadioButton rbMenu1, rbMenu2;
    private TextView tvTotalPrice;
    private Button btnBook, btnSelectDate, btnIncrease, btnDecrease;
    private int tableCount = 5; // Giá trị mặc định
    private double menuPrice = 0;
    private double serviceCost = 0;
    private String selectedDate = "";
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        dbHelper = new DatabaseHelper(this);
        initViews();
        setupSpinners();
        setupListeners();
        updateTotalPrice();

        // Lấy thông tin người dùng từ SharedPreferences
        fillUserInfoFromPrefs();
    }

    private void initViews() {
        spinnerRestaurant = findViewById(R.id.spinnerRestaurant);
        spinnerTime = findViewById(R.id.spinnerTime);
        etTableCount = findViewById(R.id.etTableCount);
        etFullName = findViewById(R.id.etFullName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        cbService1 = findViewById(R.id.cbService1);
        cbService2 = findViewById(R.id.cbService2);
        cbService3 = findViewById(R.id.cbService3);
        rgMenu = findViewById(R.id.rgMenu);
        rbMenu1 = findViewById(R.id.rbMenu1);
        rbMenu2 = findViewById(R.id.rbMenu2);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnBook = findViewById(R.id.btnBook);
        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnIncrease = findViewById(R.id.btnIncrease);
        btnDecrease = findViewById(R.id.btnDecrease);

        etTableCount.setText(String.valueOf(tableCount));

        if (rgMenu == null) {
            Toast.makeText(this, "Lỗi: RadioGroup không được tìm thấy!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupSpinners() {
        List<DatabaseHelper.Restaurant> restaurants = dbHelper.getAllRestaurants();
        List<String> restaurantNames = new ArrayList<>();
        for (DatabaseHelper.Restaurant restaurant : restaurants) {
            restaurantNames.add(restaurant.getTitle());
        }

        ArrayAdapter<String> restaurantAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, restaurantNames);
        restaurantAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRestaurant.setAdapter(restaurantAdapter);

        List<String> times = new ArrayList<>();
        for (int i = 15; i <= 20; i++) {
            times.add(i + ":00");
        }
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, times);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTime.setAdapter(timeAdapter);
    }

    private void fillUserInfoFromPrefs() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("user_email", null);

        if (userEmail != null && !userEmail.isEmpty()) {
            DatabaseHelper.User user = dbHelper.getUserInfo(userEmail);
            if (user != null) {
                etFullName.setText(user.getName());
                etPhone.setText(user.getPhone());
                etEmail.setText(user.getEmail());
            }
        }
        // Không hiển thị thông báo hoặc chuyển sang LoginActivity nếu không tìm thấy thông tin
    }

    private void setupListeners() {
        btnIncrease.setOnClickListener(v -> {
            tableCount++;
            etTableCount.setText(String.valueOf(tableCount));
            updateTotalPrice();
        });

        btnDecrease.setOnClickListener(v -> {
            if (tableCount > 1) {
                tableCount--;
                etTableCount.setText(String.valueOf(tableCount));
                updateTotalPrice();
            }
        });

        btnSelectDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            Calendar minDate = Calendar.getInstance();
            minDate.add(Calendar.DAY_OF_MONTH, 7);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                btnSelectDate.setText(selectedDate);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

            datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
            datePickerDialog.show();
        });

        cbService1.setOnCheckedChangeListener((buttonView, isChecked) -> updateTotalPrice());
        cbService2.setOnCheckedChangeListener((buttonView, isChecked) -> updateTotalPrice());
        cbService3.setOnCheckedChangeListener((buttonView, isChecked) -> updateTotalPrice());

        rbMenu1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                menuPrice = 3550000;
                updateTotalPrice();
            }
        });

        rbMenu2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                menuPrice = 5000000;
                updateTotalPrice();
            }
        });

        btnBook.setOnClickListener(v -> {
            String name = etFullName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            int restaurantId = spinnerRestaurant.getSelectedItemPosition() + 1;
            String time = spinnerTime.getSelectedItem().toString();

            // Cập nhật tableCount từ EditText trước khi truyền
            try {
                tableCount = Integer.parseInt(etTableCount.getText().toString().trim());
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Số bàn không hợp lệ!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || selectedDate.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Email không hợp lệ!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!android.util.Patterns.PHONE.matcher(phone).matches()) {
                Toast.makeText(this, "Số điện thoại không hợp lệ!", Toast.LENGTH_SHORT).show();
                return;
            }

            List<Integer> serviceIds = new ArrayList<>();
            if (cbService1.isChecked()) serviceIds.add(1);
            if (cbService2.isChecked()) serviceIds.add(2);
            if (cbService3.isChecked()) serviceIds.add(3);

            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            String userIdStr = sharedPreferences.getString("user_id", "-1");
            int userId;
            try {
                userId = Integer.parseInt(userIdStr);
            } catch (NumberFormatException e) {
                userId = -1;
            }

            int menuId = -1;
            if (rgMenu != null) {
                menuId = rgMenu.getCheckedRadioButtonId() == R.id.rbMenu1 ? 1 : 2;
            } else {
                Toast.makeText(this, "Lỗi: Không thể xác định menu!", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(BookingActivity.this, PaymentActivity.class);
            intent.putExtra("table_count", tableCount);
            intent.putExtra("menu_id", menuId);
            intent.putIntegerArrayListExtra("service_ids", (ArrayList<Integer>) serviceIds);
            intent.putExtra("date", selectedDate);
            intent.putExtra("time", time);
            intent.putExtra("full_name", name);
            intent.putExtra("phone", phone);
            intent.putExtra("email", email);
            intent.putExtra("restaurant_id", restaurantId);
            intent.putExtra("user_id", userId);
            startActivity(intent);
        });
    }

    private void updateTotalPrice() {
        serviceCost = 0;
        if (cbService1.isChecked()) serviceCost += 100000;
        if (cbService2.isChecked()) serviceCost += 150000;
        if (cbService3.isChecked()) serviceCost += 200000;

        double total = (tableCount * menuPrice) + serviceCost;
        DecimalFormat formatter = new DecimalFormat("#,###");
        String formattedTotal = formatter.format(total) + " VNĐ";
        tvTotalPrice.setText("Tổng cộng: " + formattedTotal);
    }

    private void showBookingSuccessMessage(double total) {
        String message = String.format("Đặt tiệc thành công! Tổng tiền: %.2f VNĐ", total);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}