package com.sinhvien.nhom11_app_dat_tiec.admin;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.sinhvien.nhom11_app_dat_tiec.DatabaseHelper;
import com.sinhvien.nhom11_app_dat_tiec.R;
import com.sinhvien.nhom11_app_dat_tiec.Restaurant;

public class AddRestaurantFragment extends Fragment {
    private EditText etTitle, etDescription;
    private ImageView ivImage;
    private Button btnSave;
    private DatabaseHelper dbHelper;
    private int selectedImageResource = R.drawable.d11;

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
        // Implement image selection logic
        // For simplicity, we'll just use a hardcoded list
        String[] images = {"Nhà hàng 1", "Nhà hàng 2", "Nhà hàng 3"};
        int[] imageResources = {R.drawable.d11, R.drawable.d21, R.drawable.d31};

        new AlertDialog.Builder(getContext())
                .setTitle("Chọn hình ảnh")
                .setItems(images, (dialog, which) -> {
                    selectedImageResource = imageResources[which];
                    ivImage.setImageResource(selectedImageResource);
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

        Restaurant restaurant = new Restaurant();
        restaurant.setTitle(title);
        restaurant.setDescription(description);
        restaurant.setImageResource(selectedImageResource);

        dbHelper.addRestaurant(restaurant);
        Toast.makeText(getContext(), "Đã thêm nhà hàng", Toast.LENGTH_SHORT).show();
        getActivity().getSupportFragmentManager().popBackStack();
    }
}