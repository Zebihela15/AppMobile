package com.sinhvien.nhom11_app_dat_tiec;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class BookingActivity extends AppCompatActivity {

    private EditText etTableCount;
    private Button btnSelectDate;
    private Spinner spinnerTime;
    private Button btnBook;

    private int tableCount = 5;
    private String selectedDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        etTableCount = findViewById(R.id.etTableCount);
        btnSelectDate = findViewById(R.id.btnSelectDate);
        spinnerTime = findViewById(R.id.spinnerTime);
        btnBook = findViewById(R.id.btnBook);

        EditText etFullName = findViewById(R.id.etFullName);
        EditText etPhone = findViewById(R.id.etPhone);
        EditText etEmail = findViewById(R.id.etEmail);
        ImageView ivRestaurant = findViewById(R.id.ivRestaurant);
        TextView tvRestaurantName = findViewById(R.id.tvRestaurantName);


        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = etFullName.getText().toString();
                String phone = etPhone.getText().toString();
                String email = etEmail.getText().toString();
                String time = spinnerTime.getSelectedItem().toString();

                if (selectedDate.isEmpty()) {
                    Toast.makeText(BookingActivity.this, "Vui lòng chọn ngày", Toast.LENGTH_SHORT).show();
                } else if (fullName.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                    Toast.makeText(BookingActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    String message = "Đặt tiệc thành công!\nSố bàn: " + tableCount + "\nNgày: " + selectedDate + "\nGiờ: " + time +
                            "\nHọ tên: " + fullName + "\nSĐT: " + phone + "\nEmail: " + email;
                    Toast.makeText(BookingActivity.this, message, Toast.LENGTH_LONG).show();
                }
            }
        });

        etTableCount.setText(String.valueOf(tableCount));


        findViewById(R.id.btnIncrease).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tableCount++;
                etTableCount.setText(String.valueOf(tableCount));
            }
        });

        findViewById(R.id.btnDecrease).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tableCount > 5) {
                    tableCount--;
                    etTableCount.setText(String.valueOf(tableCount));
                }
            }
        });


        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });


        String[] times = {"7:00", "7:30", "8:00", "8:30", "9:00", "9:30", "10:00"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, times);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTime.setAdapter(adapter);

        // Xử lý đặt tiệc
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = spinnerTime.getSelectedItem().toString();
                if (selectedDate.isEmpty()) {
                    Toast.makeText(BookingActivity.this, "Vui lòng chọn ngày", Toast.LENGTH_SHORT).show();
                } else {
                    String message = "Đặt tiệc thành công!\nSố bàn: " + tableCount + "\nNgày: " + selectedDate + "\nGiờ: " + time;
                    Toast.makeText(BookingActivity.this, message, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                btnSelectDate.setText(selectedDate);
            }
        }, year, month, day);

        // Đặt ngày tối thiểu là 1 tuần sau
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

        datePickerDialog.show();
    }
}
