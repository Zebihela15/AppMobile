package com.sinhvien.nhom11_app_dat_tiec.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.sinhvien.nhom11_app_dat_tiec.DatabaseHelper;
import com.sinhvien.nhom11_app_dat_tiec.MenuItem;
import com.sinhvien.nhom11_app_dat_tiec.R;

public class AddMenuFragment extends Fragment {
    private TextInputEditText etTitle, etPrice, etDescription;
    private MaterialButton btnSave;
    private DatabaseHelper dbHelper;

    public AddMenuFragment() {
        // Required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_fragment_add_menu, container, false);

        etTitle = view.findViewById(R.id.et_title);
        etPrice = view.findViewById(R.id.et_price);
        etDescription = view.findViewById(R.id.et_description);
        btnSave = view.findViewById(R.id.btn_save);
        dbHelper = new DatabaseHelper(getActivity());

        btnSave.setOnClickListener(v -> saveMenu());

        return view;
    }

    private void saveMenu() {
        String title = etTitle.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (title.isEmpty()) {
            etTitle.setError("Vui lòng nhập tên menu");
            return;
        }
        if (dbHelper.isMenuNameExists(title)) {
            etTitle.setError("Tên Menu đã tồn tại");
            return;}

        if (description.isEmpty()) {
            etDescription.setError("Vui lòng nhập mô tả menu");
            etDescription.requestFocus();
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

        MenuItem menuItem = new MenuItem(title, price, description);
        long id = dbHelper.addMenu(menuItem);

        if (id != -1) {
            Toast.makeText(getActivity(), "Thêm menu thành công", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().popBackStack();
        } else {
            Toast.makeText(getActivity(), "Thêm menu thất bại", Toast.LENGTH_SHORT).show();
        }
    }
}