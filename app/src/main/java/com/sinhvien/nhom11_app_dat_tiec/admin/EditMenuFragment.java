package com.sinhvien.nhom11_app_dat_tiec.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.sinhvien.nhom11_app_dat_tiec.DatabaseHelper;
import com.sinhvien.nhom11_app_dat_tiec.MenuItem;
import com.sinhvien.nhom11_app_dat_tiec.R;

public class EditMenuFragment extends Fragment {
    private TextInputEditText etTitle, etPrice, etDescription;
    private MaterialButton btnUpdate, btnDelete;
    private DatabaseHelper dbHelper;
    private MenuItem menuItem;

    public static EditMenuFragment newInstance(MenuItem item) {
        EditMenuFragment fragment = new EditMenuFragment();
        Bundle args = new Bundle();
        args.putInt("id", item.getId());
        args.putString("title", item.getTitle());
        args.putDouble("price", item.getPrice());
        args.putString("description", item.getDescription()); // Thêm dòng này
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            menuItem = new MenuItem();
            menuItem.setId(getArguments().getInt("id"));
            menuItem.setTitle(getArguments().getString("title"));
            menuItem.setPrice(getArguments().getDouble("price"));
            menuItem.setDescription(getArguments().getString("description")); // Thêm dòng này
        }
        dbHelper = new DatabaseHelper(requireActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_fragment_edit_menu, container, false);

        etTitle = view.findViewById(R.id.et_title);
        etPrice = view.findViewById(R.id.et_price);
        etDescription = view.findViewById(R.id.et_description); // Thêm dòng này
        btnUpdate = view.findViewById(R.id.btn_update);
        btnDelete = view.findViewById(R.id.btn_delete);

        // Hiển thị dữ liệu hiện tại
        if (menuItem != null) {
            etTitle.setText(menuItem.getTitle());
            etPrice.setText(String.valueOf(menuItem.getPrice()));
            etDescription.setText(menuItem.getDescription()); // Thêm dòng này
        }

        btnUpdate.setOnClickListener(v -> updateMenu());
        btnDelete.setOnClickListener(v -> showDeleteConfirmationDialog());

        return view;
    }

    private void updateMenu() {
        String title = etTitle.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String description = etDescription.getText().toString().trim(); // Thêm dòng này

        if (title.isEmpty()) {
            etTitle.setError("Vui lòng nhập tên menu");
            return;
        }

        if (priceStr.isEmpty()) {
            etPrice.setError("Vui lòng nhập giá");
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            etPrice.setError("Giá không hợp lệ");
            return;
        }

        menuItem.setTitle(title);
        menuItem.setPrice(price);
        menuItem.setDescription(description); // Thêm dòng này

        int rowsAffected = dbHelper.updateMenu(menuItem);
        if (rowsAffected > 0) {
            Toast.makeText(requireContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().popBackStack();
        } else {
            Toast.makeText(requireContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDeleteConfirmationDialog() {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa menu này?")
                .setPositiveButton("Xóa", (dialog, which) -> deleteMenu())
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteMenu() {
        dbHelper.deleteMenu(menuItem.getId());
        Toast.makeText(requireContext(), "Đã xóa menu thành công", Toast.LENGTH_SHORT).show();
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}