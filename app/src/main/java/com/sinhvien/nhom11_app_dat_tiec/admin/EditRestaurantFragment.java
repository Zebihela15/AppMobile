package com.sinhvien.nhom11_app_dat_tiec.admin;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.sinhvien.nhom11_app_dat_tiec.DatabaseHelper;
import com.sinhvien.nhom11_app_dat_tiec.R;
import com.sinhvien.nhom11_app_dat_tiec.Restaurant;


public class EditRestaurantFragment extends Fragment {
    private static final String ARG_RESTAURANT = "restaurant";

    private TextInputEditText etRestaurantName, etDescription;
    private ImageView ivRestaurantImage;
    private Button btnSaveChanges, btnDelete, btnChangeImage;
    private DatabaseHelper dbHelper;
    private Restaurant restaurant;
    private String selectedImageName; // Thay đổi từ int sang String

    public static EditRestaurantFragment newInstance(Restaurant restaurant) {
        EditRestaurantFragment fragment = new EditRestaurantFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_RESTAURANT, restaurant);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_fragment_edit_restaurant, container, false);

        dbHelper = new DatabaseHelper(requireContext());
        if (getArguments() != null) {
            restaurant = (Restaurant) getArguments().getSerializable(ARG_RESTAURANT);
            selectedImageName = restaurant.getImageResource();
        }

        // Ánh xạ view
        ivRestaurantImage = view.findViewById(R.id.iv_restaurant_image);
        btnChangeImage = view.findViewById(R.id.btn_change_image);
        etRestaurantName = view.findViewById(R.id.et_restaurant_name);
        etDescription = view.findViewById(R.id.et_description);
        btnSaveChanges = view.findViewById(R.id.btn_save_changes);
        btnDelete = view.findViewById(R.id.btn_delete);

        // Điền dữ liệu hiện tại
        if (restaurant != null) {
            int resId = getResources().getIdentifier(restaurant.getImageResource(), "drawable", requireContext().getPackageName());
            ivRestaurantImage.setImageResource(resId);
            etRestaurantName.setText(restaurant.getTitle());
            etDescription.setText(restaurant.getDescription());
        }

        // Sự kiện
        btnChangeImage.setOnClickListener(v -> showImageSelectionDialog());
        btnSaveChanges.setOnClickListener(v -> updateRestaurant());
        btnDelete.setOnClickListener(v -> showDeleteConfirmationDialog());

        return view;
    }

    private void showImageSelectionDialog() {
        String[] imageNames = {"d11", "d12", "d13"};
        String[] displayNames = {"Ảnh 1", "Ảnh 2", "Ảnh 3"};

        new AlertDialog.Builder(requireContext())
                .setTitle("Chọn ảnh nhà hàng")
                .setItems(displayNames, (dialog, which) -> {
                    selectedImageName = imageNames[which];
                    int resId = getResources().getIdentifier(selectedImageName, "drawable", requireContext().getPackageName());
                    ivRestaurantImage.setImageResource(resId);
                })
                .show();
    }

    private void updateRestaurant() {
        String title = etRestaurantName.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (title.isEmpty()) {
            etRestaurantName.setError("Vui lòng nhập tên nhà hàng");
            return;
        }

        if (restaurant != null) {
            restaurant.setTitle(title);
            restaurant.setDescription(description);
            restaurant.setImageResource(selectedImageName);

            dbHelper.updateRestaurant(restaurant);
            Toast.makeText(getContext(), "Cập nhật nhà hàng thành công", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().popBackStack();
        }
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa nhà hàng này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    if (restaurant != null) {
                        dbHelper.deleteRestaurant(restaurant.getRestaurantId());
                        Toast.makeText(getContext(), "Đã xóa nhà hàng", Toast.LENGTH_SHORT).show();
                        requireActivity().getSupportFragmentManager().popBackStack();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}