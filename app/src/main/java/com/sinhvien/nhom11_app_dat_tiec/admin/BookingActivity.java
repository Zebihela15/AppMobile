package com.sinhvien.nhom11_app_dat_tiec.admin;

import android.app.AlertDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.sinhvien.nhom11_app_dat_tiec.DatabaseHelper;
import com.sinhvien.nhom11_app_dat_tiec.R;
import com.sinhvien.nhom11_app_dat_tiec.Booking;

public class BookingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_booking);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new BookingListFragment())
                    .commit();
        }
    }

    public void navigateToAddBooking() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new AddBookingFragment())
                .addToBackStack("add_booking")
                .commit();
    }

    public void navigateToEditBooking(Booking booking) {
        EditBookingFragment editFragment = EditBookingFragment.newInstance(booking);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, editFragment)
                .addToBackStack("edit_booking")
                .commit();
    }

    public void showDeleteConfirmation(Booking booking, int position, BookingAdapter adapter) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa đặt bàn")
                .setMessage("Bạn có chắc chắn muốn xóa đặt bàn này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    DatabaseHelper dbHelper = new DatabaseHelper(this);
                    dbHelper.deleteBooking(booking.getId());
                    adapter.removeItem(position);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}