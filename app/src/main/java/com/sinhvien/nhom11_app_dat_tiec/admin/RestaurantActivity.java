package com.sinhvien.nhom11_app_dat_tiec.admin;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.sinhvien.nhom11_app_dat_tiec.DatabaseHelper;
import com.sinhvien.nhom11_app_dat_tiec.R;
import com.sinhvien.nhom11_app_dat_tiec.Restaurant;

public class RestaurantActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_restaurant);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new RestaurantListFragment())
                    .commit();
        }
    }

    public void navigateToAddRestaurant() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new AddRestaurantFragment())
                .addToBackStack("add_restaurant")
                .commit();
    }

    public void navigateToEditRestaurant(Restaurant restaurant) {
        EditRestaurantFragment editFragment = EditRestaurantFragment.newInstance(restaurant);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, editFragment)
                .addToBackStack("edit_restaurant")
                .commit();
    }

    public void showDeleteConfirmation(Restaurant restaurant, int position, RestaurantAdapter adapter) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        // Kiểm tra nhà hàng có đang được sử dụng không
        if (dbHelper.isRestaurantInUse(restaurant.getRestaurantId())) {
            new AlertDialog.Builder(this)
                    .setTitle("Không thể xóa")
                    .setMessage("Nhà hàng \"" + restaurant.getTitle() + "\" đang có đơn đặt bàn. Không thể xóa!")
                    .setPositiveButton("OK", null)
                    .show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Xóa nhà hàng")
                .setMessage("Bạn có chắc chắn muốn xóa nhà hàng \"" + restaurant.getTitle() + "\"?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    int result = dbHelper.deleteRestaurant(restaurant.getRestaurantId());
                    if (result > 0) {
                        adapter.removeItem(position);
                        Toast.makeText(this, "Đã xóa nhà hàng thành công", Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();
                    } else if (result == -1) {
                        Toast.makeText(this, "Nhà hàng đang được sử dụng, không thể xóa", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Xóa nhà hàng thất bại", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}