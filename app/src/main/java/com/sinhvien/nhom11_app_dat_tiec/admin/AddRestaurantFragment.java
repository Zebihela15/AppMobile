package com.sinhvien.nhom11_app_dat_tiec.admin;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.sinhvien.nhom11_app_dat_tiec.DatabaseHelper;
import com.sinhvien.nhom11_app_dat_tiec.R;
import com.sinhvien.nhom11_app_dat_tiec.Restaurant;

public class AddRestaurantFragment extends Fragment {
    private EditText etTitle, etDescription;
    private ImageView ivImage;
    private Button btnSave;
    private DatabaseHelper dbHelper;
    private String selectedImagePath = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_fragment_add_restaurant, container, false);

        dbHelper = new DatabaseHelper(getContext());
        etTitle = view.findViewById(R.id.et_title);
        etDescription = view.findViewById(R.id.et_description);
        ivImage = view.findViewById(R.id.iv_image);
        btnSave = view.findViewById(R.id.btn_save);

        ivImage.setOnClickListener(v -> showImageSelectionDialog());
        btnSave.setOnClickListener(v -> saveRestaurant());

        return view;
    }

    private void showImageSelectionDialog() {
        // Danh sách tên và đường dẫn ảnh
        String[] imageNames = {"Nhà hàng 1", "Nhà hàng 2", "Nhà hàng 3"};
        String[] imagePaths = {
                "path_to_image_1.jpg",
                "path_to_image_2.jpg",
                "path_to_image_3.jpg"
        };
        int[] imagePreviews = {R.drawable.d11, R.drawable.d21, R.drawable.d31}; // Ảnh xem trước

        new AlertDialog.Builder(getContext())
                .setTitle("Chọn hình ảnh")
                .setItems(imageNames, (dialog, which) -> {
                    selectedImagePath = imagePaths[which]; // Lưu đường dẫn
                    ivImage.setImageResource(imagePreviews[which]); // Hiển thị ảnh xem trước
                })
                .show();
    }

    private void saveRestaurant() {
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (title.isEmpty()) {
            etTitle.setError("Vui lòng nhập tên nhà hàng");
            return;
        }

        if (selectedImagePath.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng chọn ảnh nhà hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        Restaurant restaurant = new Restaurant();
        restaurant.setTitle(title);
        restaurant.setDescription(description);
        restaurant.setImageResource(selectedImagePath);

        long result = dbHelper.addRestaurant(restaurant);
        if (result != -1) {
            Toast.makeText(getContext(), "Thêm nhà hàng thành công", Toast.LENGTH_SHORT).show();
            getActivity().getSupportFragmentManager().popBackStack();
        } else {
            Toast.makeText(getContext(), "Thêm nhà hàng thất bại", Toast.LENGTH_SHORT).show();
        }
    }
}