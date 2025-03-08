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

        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLocation = locations.get(position);
                if (selectedLocation.equals("Chọn địa điểm")) {}
                else if (selectedLocation.equals("Hồ Chí Minh")) {
                    Intent intentHCM = new Intent(getContext(), HoChiMinhActivity.class);
                    startActivity(intentHCM);
                }
                else if (selectedLocation.equals("Hà Nội")) {
                    Intent intentHN = new Intent(getContext(), HaNoiActivity.class);
                    startActivity(intentHN);
                } else if (selectedLocation.equals("Đà Nẵng")) {
                    Intent intentDN = new Intent(getContext(), DaNangActivity.class);
                    startActivity(intentDN);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}


        });

    }
}