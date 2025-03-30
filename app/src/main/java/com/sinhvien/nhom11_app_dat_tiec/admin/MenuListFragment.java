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
import com.sinhvien.nhom11_app_dat_tiec.MenuItem;
import com.sinhvien.nhom11_app_dat_tiec.R;

import java.util.ArrayList;
import java.util.List;

public class MenuListFragment extends Fragment {
    private RecyclerView recyclerView;
    private MenuAdapter adapter; // Sử dụng adapter của bạn, không phải từ androidx
    private List<MenuItem> menuItems;
    private DatabaseHelper dbHelper;

    public MenuListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_fragment_menu_list, container, false);

        recyclerView = view.findViewById(R.id.rv_services);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        menuItems = new ArrayList<>();
        adapter = new MenuAdapter(getActivity(), menuItems);
        recyclerView.setAdapter(adapter);

        dbHelper = new DatabaseHelper(getActivity());
        loadMenus();

        FloatingActionButton fabAdd = view.findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(v -> {
            AddMenuFragment addMenuFragment = new AddMenuFragment();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, addMenuFragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    private void loadMenus() {
        menuItems.clear();
        menuItems.addAll(dbHelper.getAllMenus());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadMenus();
    }
}