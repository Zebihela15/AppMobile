package com.sinhvien.nhom11_app_dat_tiec.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sinhvien.nhom11_app_dat_tiec.DatabaseHelper;
import com.sinhvien.nhom11_app_dat_tiec.R;
import com.sinhvien.nhom11_app_dat_tiec.Booking;

public class EditBookingFragment extends Fragment {
    private EditText etUserId, etRestaurantId, etTableCount, etDate, etTime, etMenuId, etTotalPrice;
    private Button btnUpdate;
    private Booking booking;

    public static EditBookingFragment newInstance(Booking booking) {
        EditBookingFragment fragment = new EditBookingFragment();
        Bundle args = new Bundle();
        args.putInt("id", booking.getId());
        args.putString("userId", booking.getUserId());
        args.putInt("restaurantId", booking.getRestaurantId());
        args.putInt("tableCount", booking.getTableCount());
        args.putString("date", booking.getDate());
        args.putString("time", booking.getTime());
        args.putInt("menuId", booking.getMenuId());
        args.putDouble("totalPrice", booking.getTotalPrice());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            booking = new Booking(
                    getArguments().getInt("id"),
                    getArguments().getString("userId"),
                    getArguments().getInt("restaurantId"),
                    getArguments().getInt("tableCount"),
                    getArguments().getString("date"),
                    getArguments().getString("time"),
                    getArguments().getInt("menuId"),
                    getArguments().getDouble("totalPrice")
            );
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_fragment_edit_booking, container, false);

        etUserId = view.findViewById(R.id.et_user_id);
        etRestaurantId = view.findViewById(R.id.et_restaurant_id);
        etTableCount = view.findViewById(R.id.et_table_count);
        etDate = view.findViewById(R.id.et_date);
        etTime = view.findViewById(R.id.et_time);
        etMenuId = view.findViewById(R.id.et_menu_id);
        etTotalPrice = view.findViewById(R.id.et_total_price);
        btnUpdate = view.findViewById(R.id.btn_update);

        // Hiển thị thông tin hiện tại
        etUserId.setText(booking.getUserId());
        etRestaurantId.setText(String.valueOf(booking.getRestaurantId()));
        etTableCount.setText(String.valueOf(booking.getTableCount()));
        etDate.setText(booking.getDate());
        etTime.setText(booking.getTime());
        etMenuId.setText(String.valueOf(booking.getMenuId()));
        etTotalPrice.setText(String.valueOf(booking.getTotalPrice()));

        btnUpdate.setOnClickListener(v -> updateBooking());

        return view;
    }

    private void updateBooking() {
        String userId = etUserId.getText().toString().trim();
        int restaurantId = Integer.parseInt(etRestaurantId.getText().toString().trim());
        int tableCount = Integer.parseInt(etTableCount.getText().toString().trim());
        String date = etDate.getText().toString().trim();
        String time = etTime.getText().toString().trim();
        int menuId = Integer.parseInt(etMenuId.getText().toString().trim());
        double totalPrice = Double.parseDouble(etTotalPrice.getText().toString().trim());

        if (userId.isEmpty() || date.isEmpty() || time.isEmpty()) {
            Toast.makeText(getActivity(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        Booking updatedBooking = new Booking(
                booking.getId(),
                userId,
                restaurantId,
                tableCount,
                date,
                time,
                menuId,
                totalPrice
        );

        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
        dbHelper.updateBookingAdmin(updatedBooking);

        Toast.makeText(getActivity(), "Đã cập nhật đặt bàn thành công", Toast.LENGTH_SHORT).show();
        requireActivity().onBackPressed();
    }
}