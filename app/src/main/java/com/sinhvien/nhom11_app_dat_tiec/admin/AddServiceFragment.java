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
import com.sinhvien.nhom11_app_dat_tiec.Service;

public class AddServiceFragment extends Fragment {
    private EditText etTitle, etPrice;
    private Button btnSave;
    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_fragment_add_service, container, false);

        dbHelper = new DatabaseHelper(getActivity());

        etTitle = view.findViewById(R.id.et_title);
        etPrice = view.findViewById(R.id.et_price);
        btnSave = view.findViewById(R.id.btn_save);

        btnSave.setOnClickListener(v -> saveService());

        return view;
    }

    private void saveService() {
        String title = etTitle.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();

        if (title.isEmpty()) {
            etTitle.setError("Vui lòng nhập tên dịch vụ");
            return;
        }

        if (priceStr.isEmpty()) {
            etPrice.setError("Vui lòng nhập giá dịch vụ");
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            etPrice.setError("Giá dịch vụ không hợp lệ");
            return;
        }

        Service service = new Service(title, price);
        long id = dbHelper.addService(service);

        if (id != -1) {
            Toast.makeText(getActivity(), "Thêm dịch vụ thành công", Toast.LENGTH_SHORT).show();
            getActivity().getSupportFragmentManager().popBackStack();
        } else {
            Toast.makeText(getActivity(), "Thêm dịch vụ thất bại", Toast.LENGTH_SHORT).show();
        }
    }
}
