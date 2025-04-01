package com.sinhvien.nhom11_app_dat_tiec.admin;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sinhvien.nhom11_app_dat_tiec.DatabaseHelper;
import com.sinhvien.nhom11_app_dat_tiec.MenuItem;
import com.sinhvien.nhom11_app_dat_tiec.R;
import com.sinhvien.nhom11_app_dat_tiec.Restaurant;

public class MenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_menu);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new MenuListFragment())
                    .commit();
        }
    }
    public void navigateToAddMenu() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new AddMenuFragment())
                .addToBackStack("add_menu")
                .commit();
    }

    public void navigateToEditMenu(MenuItem menu) {
        EditMenuFragment editFragment = EditMenuFragment.newInstance(menu);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, editFragment)
                .addToBackStack("edit_menu")
                .commit();
    }

    public void showDeleteConfirmation(MenuItem menu, int position, MenuAdapter adapter) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        // Kiểm tra xem menu có đang được sử dụng trong booking nào không
        if (dbHelper.isMenuInUse(menu.getId())) {
            new AlertDialog.Builder(this)
                    .setTitle("Không thể xóa")
                    .setMessage("Menu này đang được sử dụng trong đơn đặt bàn. Không thể xóa!")
                    .setPositiveButton("OK", null)
                    .show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Xóa menu")
                .setMessage("Bạn có chắc chắn muốn xóa menu \"" + menu.getTitle() + "\"?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    int result = dbHelper.deleteMenu(menu.getId());
                    if (result > 0) {
                        adapter.removeItem(position);
                        Toast.makeText(this, "Đã xóa menu thành công", Toast.LENGTH_SHORT).show();

                        // Cập nhật dữ liệu nếu cần
                        adapter.notifyDataSetChanged();
                    } else if (result == -1) {
                        Toast.makeText(this, "Menu đang được sử dụng, không thể xóa", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Xóa menu thất bại", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
