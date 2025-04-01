package com.sinhvien.nhom11_app_dat_tiec.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
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
    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_fragment_services_list, container, false);

        dbHelper = new DatabaseHelper(getActivity());
        recyclerView = view.findViewById(R.id.rv_services);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
         searchView = view.findViewById(R.id.search_view);

        // Set up FAB
        FloatingActionButton fabAdd = view.findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(v -> {
            ((ServiceActivity) getActivity()).navigateToAddService();
        });

        loadServices();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchServices(newText);
                return true;
            }
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
        loadServices();
    }
    private void searchServices(String keyword) {
        List<Service> result;
        if (keyword.isEmpty()) {
            result = dbHelper.getAllServices();
        } else {
            result = dbHelper.searchServices(keyword);
        }

        services.clear();
        services.addAll(result);
        adapter.notifyDataSetChanged();
    }
}