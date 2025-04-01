package com.sinhvien.nhom11_app_dat_tiec.admin;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sinhvien.nhom11_app_dat_tiec.DatabaseHelper;
import com.sinhvien.nhom11_app_dat_tiec.R;
import com.sinhvien.nhom11_app_dat_tiec.Service;

public class ServiceActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_service);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ServiceListFragment())
                    .commit();
        }
    }

    public void navigateToAddService() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new AddServiceFragment())
                .addToBackStack("add_service")
                .commit();
    }

    public void navigateToEditService(Service service) {
        EditServiceFragment editFragment = EditServiceFragment.newInstance(service);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, editFragment)
                .addToBackStack("edit_service")
                .commit();
    }

    public void showDeleteConfirmation(Service service, int position, ServiceAdapter adapter) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        // Kiểm tra dịch vụ có đang được sử dụng không
        if (dbHelper.isServiceInUse(service.getId())) {
            new AlertDialog.Builder(this)
                    .setTitle("Không thể xóa")
                    .setMessage("Dịch vụ \"" + service.getTitle() + "\" đang được sử dụng trong đơn đặt bàn. Không thể xóa!")
                    .setPositiveButton("OK", null)
                    .show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Xóa dịch vụ")
                .setMessage("Bạn có chắc chắn muốn xóa dịch vụ \"" + service.getTitle() + "\"?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    int result = dbHelper.deleteService(service.getId());
                    if (result > 0) {
                        adapter.removeItem(position);
                        Toast.makeText(this, "Đã xóa dịch vụ thành công", Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();
                    } else if (result == -1) {
                        Toast.makeText(this, "Dịch vụ đang được sử dụng, không thể xóa", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Xóa dịch vụ thất bại", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}