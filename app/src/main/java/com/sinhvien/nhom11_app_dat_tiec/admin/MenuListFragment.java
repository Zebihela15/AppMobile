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
import com.sinhvien.nhom11_app_dat_tiec.MenuItem;
import com.sinhvien.nhom11_app_dat_tiec.R;

import java.util.ArrayList;
import java.util.List;

public class MenuListFragment extends Fragment {
    private RecyclerView recyclerView;
    private MenuAdapter adapter;
    private List<MenuItem> menuItems;
    private List<MenuItem> menuItemsFull; // Danh sách đầy đủ để filter
    private DatabaseHelper dbHelper;
    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_fragment_menu_list, container, false);

        dbHelper = new DatabaseHelper(requireActivity());
        recyclerView = view.findViewById(R.id.rv_services);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Load dữ liệu
        menuItems = dbHelper.getAllMenus();
        menuItemsFull = new ArrayList<>(menuItems);
        adapter = new MenuAdapter(requireActivity(), menuItems);
        recyclerView.setAdapter(adapter);

        // Xử lý tìm kiếm
        searchView = view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterMenus(newText);
                return true;
            }
        });

        FloatingActionButton fabAdd = view.findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(v -> {
            ((MenuActivity) requireActivity()).navigateToAddMenu();
        });

        return view;
    }

    private void filterMenus(String text) {
        List<MenuItem> filteredList;
        if (text.isEmpty()) {
            filteredList = new ArrayList<>(menuItemsFull);
        } else {
            filteredList = dbHelper.searchMenu(text);
        }

        menuItems.clear();
        menuItems.addAll(filteredList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    private void refreshData() {
        menuItems.clear();
        menuItems.addAll(dbHelper.getAllMenus());
        menuItemsFull.clear();
        menuItemsFull.addAll(menuItems);
        adapter.notifyDataSetChanged();

        // Reset search view
        searchView.setQuery("", false);
    }
}