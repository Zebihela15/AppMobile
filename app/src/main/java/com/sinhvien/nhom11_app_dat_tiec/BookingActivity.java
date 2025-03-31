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
import android.widget.LinearLayout;
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
    private LinearLayout servicesContainer;
    private RadioGroup rgMenu;
    private TextView tvTotalPrice;
    private Button btnBook, btnSelectDate, btnIncrease, btnDecrease;
    private int tableCount = 5;
    private double menuPrice = 0;
    private double serviceCost = 0;
    private String selectedDate = "";
    private DatabaseHelper dbHelper;
    private List<MenuItem> menuItems;
    private List<Service> services;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        dbHelper = new DatabaseHelper(this);
        initViews();
        setupSpinners();
        loadServicesFromDatabase();
        loadMenusFromDatabase();
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
        servicesContainer = findViewById(R.id.servicesContainer);
        rgMenu = findViewById(R.id.rgMenu);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnBook = findViewById(R.id.btnBook);
        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnIncrease = findViewById(R.id.btnIncrease);
        btnDecrease = findViewById(R.id.btnDecrease);

        etTableCount.setText(String.valueOf(tableCount));
    }

    private void setupSpinners() {
        // Spinner nhà hàng
        List<Restaurant> restaurants = dbHelper.getAllRestaurants();
        List<String> restaurantNames = new ArrayList<>();
        for (Restaurant restaurant : restaurants) {
            restaurantNames.add(restaurant.getTitle());
        }

        ArrayAdapter<String> restaurantAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, restaurantNames);
        restaurantAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRestaurant.setAdapter(restaurantAdapter);

        // Spinner giờ
        List<String> times = new ArrayList<>();
        for (int i = 15; i <= 20; i++) {
            times.add(i + ":00");
        }
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, times);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTime.setAdapter(timeAdapter);
    }

    private void loadServicesFromDatabase() {
        services = dbHelper.getAllServices();
        servicesContainer.removeAllViews();

        for (Service service : services) {
            LinearLayout serviceLayout = new LinearLayout(this);
            serviceLayout.setOrientation(LinearLayout.HORIZONTAL);
            serviceLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            serviceLayout.setPadding(0, 8, 0, 8);

            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(service.getTitle() + " - " + formatPrice(service.getPrice()));
            checkBox.setTag(service);
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> updateTotalPrice());

            serviceLayout.addView(checkBox);
            servicesContainer.addView(serviceLayout);
        }
    }


    private void loadMenusFromDatabase() {
        menuItems = dbHelper.getAllMenus();
        rgMenu.removeAllViews();

        for (MenuItem menuItem : menuItems) {
            RadioButton radioButton = new RadioButton(this);
            String menuText = menuItem.getTitle() + " - " + formatPrice(menuItem.getPrice()) + "\n" + menuItem.getDescription();
            radioButton.setText(menuText);
            radioButton.setTag(menuItem);
            radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    menuPrice = menuItem.getPrice();
                    updateTotalPrice();
                }
            });
            rgMenu.addView(radioButton);
        }


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
    }

    private void setupListeners() {
        // Nút tăng số bàn
        btnIncrease.setOnClickListener(v -> {
            tableCount++;
            etTableCount.setText(String.valueOf(tableCount));
            updateTotalPrice();
        });

        // Nút giảm số bàn
        btnDecrease.setOnClickListener(v -> {
            if (tableCount > 1) {
                tableCount--;
                etTableCount.setText(String.valueOf(tableCount));
                updateTotalPrice();
            }
        });

        // Chọn ngày
        btnSelectDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            Calendar minDate = Calendar.getInstance();
            minDate.add(Calendar.DAY_OF_MONTH, 7);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year, month, dayOfMonth) -> {
                        selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                        btnSelectDate.setText(selectedDate);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));

            datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
            datePickerDialog.show();
        });

        // Nút đặt tiệc
        btnBook.setOnClickListener(v -> {
            String name = etFullName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            int restaurantId = spinnerRestaurant.getSelectedItemPosition() + 1;
            String time = spinnerTime.getSelectedItem().toString();

            try {
                tableCount = Integer.parseInt(etTableCount.getText().toString().trim());
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Số bàn không hợp lệ!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra đã chọn menu chưa
            int selectedMenuId = -1;
            int selectedRadioButtonId = rgMenu.getCheckedRadioButtonId();
            if (selectedRadioButtonId == -1) {
                Toast.makeText(this, "Vui lòng chọn menu tiệc!", Toast.LENGTH_SHORT).show();
                return;
            } else {
                RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
                MenuItem selectedMenuItem = (MenuItem) selectedRadioButton.getTag();
                selectedMenuId = selectedMenuItem.getId();
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

            // Kiểm tra xem ngày giờ đã có người đặt chưa
            if (dbHelper.isTimeSlotBooked(selectedDate, time, restaurantId)) {
                Toast.makeText(this, "Thời gian này đã có người đặt. Vui lòng chọn thời gian khác!", Toast.LENGTH_LONG).show();
                return;
            }

            // Lấy danh sách dịch vụ đã chọn
            List<Long> serviceIds = new ArrayList<>();
            for (int i = 0; i < servicesContainer.getChildCount(); i++) {
                View child = servicesContainer.getChildAt(i);
                if (child instanceof LinearLayout) {
                    LinearLayout serviceLayout = (LinearLayout) child;
                    CheckBox checkBox = (CheckBox) serviceLayout.getChildAt(0);
                    if (checkBox.isChecked()) {
                        Service service = (Service) checkBox.getTag();
                        serviceIds.add(service.getId());
                    }
                }
            }

            // Lấy user ID từ SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            String userIdStr = sharedPreferences.getString("user_id", "-1");
            int userId;
            try {
                userId = Integer.parseInt(userIdStr);
            } catch (NumberFormatException e) {
                userId = -1;
            }

            // Chuyển sang PaymentActivity
            Intent intent = new Intent(BookingActivity.this, PaymentActivity.class);
            intent.putExtra("table_count", tableCount);
            intent.putExtra("menu_id", selectedMenuId);
            intent.putExtra("service_ids", convertToPrimitiveArray(serviceIds));
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

        private long[] convertToPrimitiveArray(List<Long> longList) {
        long[] array = new long[longList.size()];
        for (int i = 0; i < longList.size(); i++) {
            array[i] = longList.get(i);
        }
        return array;
    }

    private void updateTotalPrice() {
        serviceCost = 0;

        // Tính tổng chi phí dịch vụ
        for (int i = 0; i < servicesContainer.getChildCount(); i++) {
            View child = servicesContainer.getChildAt(i);
            if (child instanceof LinearLayout) {
                LinearLayout serviceLayout = (LinearLayout) child;
                CheckBox checkBox = (CheckBox) serviceLayout.getChildAt(0);
                if (checkBox.isChecked()) {
                    Service service = (Service) checkBox.getTag();
                    serviceCost += service.getPrice();
                }
            }
        }

        // Tính tổng chi phí
        double total = (tableCount * menuPrice) + serviceCost;
        tvTotalPrice.setText("Tổng cộng: " + formatPrice(total));
    }

    private String formatPrice(double price) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(price) + " VNĐ";
    }
}