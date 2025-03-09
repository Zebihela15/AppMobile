package com.sinhvien.nhom11_app_dat_tiec;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private Spinner locationSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate layout cho Fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Thiết lập Spinner
        setupSpinner(view);

        return view;
    }

    private void setupSpinner(View view) {
        locationSpinner = view.findViewById(R.id.locationSpinner);

        List<String> locations = new ArrayList<>();
        locations.add("Chọn địa điểm");
        locations.add("Hà Nội");
        locations.add("Hồ Chí Minh");
        locations.add("Đà Nẵng");

        // Tạo Adapter cho Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, locations);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapter);



    }
}