package com.sinhvien.nhom11_app_dat_tiec;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private ListView lvOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        dbHelper = new DatabaseHelper(this);
        lvOrders = findViewById(R.id.lvOrders);

        // Lấy user_id từ SharedPreferences dưới dạng String
        String userId = getSharedPreferences("UserPrefs", MODE_PRIVATE).getString("user_id", null);

        if (userId != null) {
            List<DatabaseHelper.Order> orders = dbHelper.getOrderHistory(userId);
            if (orders.isEmpty()) {
                Toast.makeText(this, "Bạn chưa có lịch sử đơn hàng!", Toast.LENGTH_SHORT).show();
            } else {
                OrderAdapter adapter = new OrderAdapter(orders);
                lvOrders.setAdapter(adapter);
            }
        } else {
            Toast.makeText(this, "Không tìm thấy user ID. Vui lòng đăng nhập lại!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private class OrderAdapter extends ArrayAdapter<DatabaseHelper.Order> {
        public OrderAdapter(List<DatabaseHelper.Order> orders) {
            super(OrderHistoryActivity.this, 0, orders);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.order_item, parent, false);
            }

            DatabaseHelper.Order order = getItem(position);

            TextView tvRestaurant = convertView.findViewById(R.id.tvRestaurant);
            TextView tvDetails = convertView.findViewById(R.id.tvDetails);
            Button btnEdit = convertView.findViewById(R.id.btnEdit);

            tvRestaurant.setText(order.getRestaurantTitle());
            tvDetails.setText("Ngày: " + order.getDate() + " | Giờ: " + order.getTime() + " | Số bàn: " + order.getTableCount());
            btnEdit.setOnClickListener(v -> {
                Intent intent = new Intent(OrderHistoryActivity.this, EditBookingActivity.class);
                intent.putExtra("booking_id", order.getBookingId());
                intent.putExtra("payment_id", order.getPaymentId());
                intent.putExtra("original_date", order.getDate());
                startActivity(intent);
            });

            return convertView;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Cập nhật lại danh sách khi quay lại từ EditBookingActivity
        String userId = getSharedPreferences("UserPrefs", MODE_PRIVATE).getString("user_id", null);
        if (userId != null) {
            List<DatabaseHelper.Order> orders = dbHelper.getOrderHistory(userId);
            OrderAdapter adapter = new OrderAdapter(orders);
            lvOrders.setAdapter(adapter);
        }
    }
}