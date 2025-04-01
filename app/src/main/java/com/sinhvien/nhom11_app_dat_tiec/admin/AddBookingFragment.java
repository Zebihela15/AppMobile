package com.sinhvien.nhom11_app_dat_tiec.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.sinhvien.nhom11_app_dat_tiec.DatabaseHelper;
import com.sinhvien.nhom11_app_dat_tiec.R;
import com.sinhvien.nhom11_app_dat_tiec.Booking;

public class AddBookingFragment extends Fragment {
    private EditText etUserId, etRestaurantId, etTableCount, etDate, etTime, etMenuId, etTotalPrice;
    private Button btnSave;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_fragment_add_booking, container, false);

        etUserId = view.findViewById(R.id.et_user_id);
        etRestaurantId = view.findViewById(R.id.et_restaurant_id);
        etTableCount = view.findViewById(R.id.et_table_count);
        etDate = view.findViewById(R.id.et_date);
        etTime = view.findViewById(R.id.et_time);
        etMenuId = view.findViewById(R.id.et_menu_id);
        etTotalPrice = view.findViewById(R.id.et_total_price);
        btnSave = view.findViewById(R.id.btn_save);

        btnSave.setOnClickListener(v -> saveBooking());

        return view;
    }

    private void saveBooking() {
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

        Booking booking = new Booking(0, userId, restaurantId, tableCount, date, time, menuId, totalPrice);
        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
        dbHelper.addBookingAdmin(booking);

        Toast.makeText(getActivity(), "Đã thêm đặt bàn thành công", Toast.LENGTH_SHORT).show();
        requireActivity().onBackPressed();
    }
}