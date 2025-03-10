package com.sinhvien.nhom11_app_dat_tiec;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class BookingActivity extends AppCompatActivity {

    private EditText etTableCount, etFullName, etPhone, etEmail;
    private Button btnSelectDate, btnBook;
    private Spinner spinnerTime;
    private RadioGroup rgMenu;
    private CheckBox cbService1, cbService2, cbService3;
    private TextView tvTotalPrice, tvRestaurantName;
    private ImageView ivRestaurant;
    private DatabaseHelper dbHelper;
    private int tableCount = 5;
    private String selectedDate = "";
    private static final int MAX_TABLES = 20;
    private int restaurantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        dbHelper = new DatabaseHelper(this);
        initializeViews();
        loadUserInfo();
        etTableCount.setText(String.valueOf(tableCount));
        setupClickListeners();
        setupTimeSpinner();
        updateTotalPrice();

        // Lấy restaurantId từ Intent (giả sử bạn truyền nó từ activity trước)
        restaurantId = getIntent().getIntExtra("restaurant_id", 1); // Mặc định là 1 nếu không có
        tvRestaurantName.setText(getIntent().getStringExtra("restaurant_name"));
        ivRestaurant.setImageResource(getIntent().getIntExtra("restaurant_image", R.drawable.dtc1));
    }

    private void initializeViews() {
        etTableCount = findViewById(R.id.etTableCount);
        btnSelectDate = findViewById(R.id.btnSelectDate);
        spinnerTime = findViewById(R.id.spinnerTime);
        btnBook = findViewById(R.id.btnBook);
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

    private void loadUserInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("user_email", null);
        if (userEmail != null) {
            DatabaseHelper.User user = dbHelper.getUserInfo(userEmail);
            if (user != null) {
                etFullName.setText(user.getName());
                etPhone.setText(user.getPhone());
                etEmail.setText(user.getEmail());
            }
        }
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
                showToast("Số bàn tối thiểu là 1");
            }
        });

        btnSelectDate.setOnClickListener(v -> showDatePickerDialog());
        btnBook.setOnClickListener(v -> handleBooking());
    }

    private void setupTimeSpinner() {
        String[] times = {"7:00", "7:30", "8:00", "8:30", "9:00", "9:30", "10:00"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, times);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTime.setAdapter(adapter);
    }

    private long calculateTotalPrice() {
        long total = 0;

        int selectedMenuId = rgMenu.getCheckedRadioButtonId();
        if (selectedMenuId == R.id.rbMenu1) {
            total += tableCount * 3550000; // Menu 1
        } else if (selectedMenuId == R.id.rbMenu2) {
            total += tableCount * 5000000; // Menu 2
        }

        if (cbService1.isChecked()) total += 100000;
        if (cbService2.isChecked()) total += 150000;
        if (cbService3.isChecked()) total += 200000;

        return total;
    }

    private void updateTotalPrice() {
        long total = calculateTotalPrice();
        if (tvTotalPrice != null) {
            tvTotalPrice.setText(String.format("Tổng cộng: %,d VNĐ", total));
        }
    }

    private void handleBooking() {
        String fullName = etFullName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String time = spinnerTime.getSelectedItem().toString();
        int menuId = getSelectedMenuId();
        double totalPrice = calculateTotalPrice();

        if (!validateInput(fullName, phone, email)) return;

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("user_email", null);
        if (userEmail == null) {
            showToast("Không tìm thấy thông tin người dùng");
            return;
        }

        DatabaseHelper.User user = dbHelper.getUserInfo(userEmail);
        if (user == null) {
            showToast("Không tìm thấy thông tin người dùng");
            return;
        }

        int userId = user.getId();

        // Thêm booking
        long bookingId = dbHelper.addBooking(
                userId, restaurantId, tableCount, selectedDate, time,
                menuId, totalPrice
        );

        if (bookingId != -1) {
            // Thêm các dịch vụ đã chọn
            if (cbService1.isChecked()) dbHelper.addBookingService(bookingId, 1); // Giả sử ID dịch vụ 1 là 1
            if (cbService2.isChecked()) dbHelper.addBookingService(bookingId, 2); // Giả sử ID dịch vụ 2 là 2
            if (cbService3.isChecked()) dbHelper.addBookingService(bookingId, 3); // Giả sử ID dịch vụ 3 là 3

            showBookingSuccessMessage(fullName, phone, email, time, selectedMenuIdToString(), getSelectedServices(), totalPrice);
            resetForm();
        } else {
            showToast("Đặt tiệc thất bại, vui lòng thử lại");
        }
    }

    private int getSelectedMenuId() {
        int selectedId = rgMenu.getCheckedRadioButtonId();
        if (selectedId == R.id.rbMenu1) return 1; // Giả sử ID của Menu 1 là 1
        if (selectedId == R.id.rbMenu2) return 2; // Giả sử ID của Menu 2 là 2
        return -1;
    }

    private String selectedMenuIdToString() {
        int selectedId = rgMenu.getCheckedRadioButtonId();
        if (selectedId == R.id.rbMenu1) return "Menu 1";
        if (selectedId == R.id.rbMenu2) return "Menu 2";
        return "";
    }

    private ArrayList<String> getSelectedServices() {
        ArrayList<String> services = new ArrayList<>();
        if (cbService1.isChecked()) services.add("Dịch vụ 1");
        if (cbService2.isChecked()) services.add("Dịch vụ 2");
        if (cbService3.isChecked()) services.add("Dịch vụ 3");
        return services;
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
        if (rgMenu.getCheckedRadioButtonId() == -1) {
            showToast("Vui lòng chọn menu");
            return false;
        }
        return true;
    }

    private void showBookingSuccessMessage(String fullName, String phone, String email,
                                           String time, String menu, ArrayList<String> services,
                                           double totalPrice) {
        String message = String.format(
                "Đặt tiệc thành công!\nSố bàn: %d\nNgày: %s\nGiờ: %s\n" +
                        "Họ tên: %s\nSĐT: %s\nEmail: %s\nMenu: %s\nDịch vụ: %s\nTổng: %,d VNĐ",
                tableCount, selectedDate, time, fullName, phone, email,
                menu, services.toString(), totalPrice
        );
        showToast(message, Toast.LENGTH_LONG);
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

    private void resetForm() {
        tableCount = 5;
        etTableCount.setText(String.valueOf(tableCount));
        selectedDate = "";
        btnSelectDate.setText("Chọn ngày");
        spinnerTime.setSelection(0);
        rgMenu.clearCheck();
        cbService1.setChecked(false);
        cbService2.setChecked(false);
        cbService3.setChecked(false);
        updateTotalPrice();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showToast(String message, int duration) {
        Toast.makeText(this, message, duration).show();
    }
}