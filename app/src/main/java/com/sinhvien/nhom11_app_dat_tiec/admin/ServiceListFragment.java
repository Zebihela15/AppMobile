package com.sinhvien.nhom11_app_dat_tiec.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sinhvien.nhom11_app_dat_tiec.DatabaseHelper;
import com.sinhvien.nhom11_app_dat_tiec.R;
import com.sinhvien.nhom11_app_dat_tiec.Service;

import java.util.List;

public class ServiceListFragment extends Fragment {
    private RecyclerView recyclerView;
    private ServiceAdapter adapter;
    private List<Service> services;
    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_fragment_services_list, container, false);

        // Initialize database helper
        dbHelper = new DatabaseHelper(getActivity());

        // Set up RecyclerView
        recyclerView = view.findViewById(R.id.rv_services);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Load services
        loadServices();

        // Set up FAB
        FloatingActionButton fabAdd = view.findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(v -> {
            AddServiceFragment addServiceFragment = new AddServiceFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, addServiceFragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    private void loadServices() {
        services = dbHelper.getAllServices();
        adapter = new ServiceAdapter(services, getActivity());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadServices(); // Refresh the list when returning from other fragments
    }
}
