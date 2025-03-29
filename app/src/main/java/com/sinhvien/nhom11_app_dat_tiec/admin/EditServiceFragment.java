package com.sinhvien.nhom11_app_dat_tiec.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sinhvien.nhom11_app_dat_tiec.DatabaseHelper;
import com.sinhvien.nhom11_app_dat_tiec.R;
import com.sinhvien.nhom11_app_dat_tiec.Service;

public class EditServiceFragment extends Fragment {
    private static final String ARG_SERVICE = "service";

    private EditText etTitle, etPrice;
    private Button btnUpdate;
    private DatabaseHelper dbHelper;
    private Service service;

    public static EditServiceFragment newInstance(Service service) {
        EditServiceFragment fragment = new EditServiceFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_SERVICE, service);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            service = (Service) getArguments().getSerializable(ARG_SERVICE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_fragment_edit_service, container, false);

        dbHelper = new DatabaseHelper(getActivity());

        etTitle = view.findViewById(R.id.et_title);
        etPrice = view.findViewById(R.id.et_price);
        btnUpdate = view.findViewById(R.id.btn_update);

        // Set current values
        if (service != null) {
            etTitle.setText(service.getTitle());
            etPrice.setText(String.valueOf(service.getPrice()));
        }

        btnUpdate.setOnClickListener(v -> updateService());

        return view;
    }

    private void updateService() {
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

        service.setTitle(title);
        service.setPrice(price);
        int rowsAffected = dbHelper.updateService(service);

        if (rowsAffected > 0) {
            Toast.makeText(getActivity(), "Cập nhật dịch vụ thành công", Toast.LENGTH_SHORT).show();
            getActivity().getSupportFragmentManager().popBackStack();
        } else {
            Toast.makeText(getActivity(), "Cập nhật dịch vụ thất bại", Toast.LENGTH_SHORT).show();
        }
    }
}
