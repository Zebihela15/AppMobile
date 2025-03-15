package com.sinhvien.nhom11_app_dat_tiec;

import com.sinhvien.nhom11_app_dat_tiec.Restaurant;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
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
    private int tableCount = 5;
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
        rbMenu1=findViewById(R.id.rbMenu1);
        rbMenu2=findViewById(R.id.rbMenu2);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnBook = findViewById(R.id.btnBook);
        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnIncrease = findViewById(R.id.btnIncrease);
        btnDecrease = findViewById(R.id.btnDecrease);

        // Đặt giá trị mặc định cho số bàn
        etTableCount.setText(String.valueOf(tableCount));
    }

    private void setupSpinners() {

        List<DatabaseHelper.Restaurant> restaurants=dbHelper.getAllRestaurants();
        List<String> restaurantNames = new ArrayList<>();
        for (DatabaseHelper.Restaurant restaurant : restaurants) {
            restaurantNames.add(restaurant.getTitle());
        }

        // Thiết lập Spinner nhà hàng
        ArrayAdapter<String> restaurantAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, restaurantNames);
        restaurantAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRestaurant.setAdapter(restaurantAdapter);

        // Thiết lập Spinner giờ
        List<String> times = new ArrayList<>();
        for (int i = 15; i <= 20; i++) {
            times.add(i + ":00");
        }
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, times);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTime.setAdapter(timeAdapter);
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
            int menuId = rgMenu.getCheckedRadioButtonId() == R.id.rbMenu1 ? 1 : 2;

            // Danh sách dịch vụ được chọn
            List<Integer> serviceIds = new ArrayList<>();
            if (cbService1.isChecked()) serviceIds.add(1);
            if (cbService2.isChecked()) serviceIds.add(2);
            if (cbService3.isChecked()) serviceIds.add(3);

            // Thêm booking vào database
            boolean isSuccess = dbHelper.addBooking(1, restaurantId, tableCount, selectedDate, time, menuId, serviceIds, dbHelper.getWritableDatabase());
            if (isSuccess) {
                Toast.makeText(this, "Đặt tiệc thành công!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Đặt tiệc thất bại. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTotalPrice() {
        serviceCost = 0;
        if (cbService1.isChecked()) serviceCost += 100000;
        if (cbService2.isChecked()) serviceCost += 150000;
        if (cbService3.isChecked()) serviceCost += 200000;

        double total = (tableCount * menuPrice) + serviceCost;

        // Định dạng số tiền có dấu phân cách hàng nghìn
        DecimalFormat formatter = new DecimalFormat("#,###");
        String formattedTotal = formatter.format(total) + " VNĐ";

        tvTotalPrice.setText("Tổng cộng: " + formattedTotal);
    }
}