package com.sinhvien.nhom11_app_dat_tiec;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {

    private RecyclerView rvOrderHistory;
    private TextView tvNoOrders;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        // Ánh xạ View
        rvOrderHistory = findViewById(R.id.rvOrderHistory);
        tvNoOrders = findViewById(R.id.tvNoOrders);
        dbHelper = new DatabaseHelper(this);

        // Lấy email từ SharedPreferences
        String email = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                .getString("user_email", "");

        // Lấy danh sách đơn đặt tiệc
        List<DatabaseHelper.Booking> bookingList = dbHelper.getUserBookings(email);

        if (bookingList.isEmpty()) {
            tvNoOrders.setVisibility(View.VISIBLE);
            rvOrderHistory.setVisibility(View.GONE);
        } else {
            tvNoOrders.setVisibility(View.GONE);
            rvOrderHistory.setVisibility(View.VISIBLE);

            // Thiết lập RecyclerView
            rvOrderHistory.setLayoutManager(new LinearLayoutManager(this));
            OrderHistoryAdapter adapter = new OrderHistoryAdapter(bookingList);
            rvOrderHistory.setAdapter(adapter);
        }
    }
}