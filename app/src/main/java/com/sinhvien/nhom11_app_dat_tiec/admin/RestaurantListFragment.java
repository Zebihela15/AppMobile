package com.sinhvien.nhom11_app_dat_tiec.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sinhvien.nhom11_app_dat_tiec.DatabaseHelper;
import com.sinhvien.nhom11_app_dat_tiec.R;
import com.sinhvien.nhom11_app_dat_tiec.Restaurant;
import com.sinhvien.nhom11_app_dat_tiec.Service;

import java.util.List;

public class RestaurantListFragment extends Fragment {
    private RecyclerView rvRestaurants;
    private RestaurantAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<Restaurant> restaurantList;
    private SearchView searchView;
    private FloatingActionButton fabAdd;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_fragment_restaurant_list, container, false);

        dbHelper = new DatabaseHelper(requireContext());
        rvRestaurants = view.findViewById(R.id.rv_restaurants);
        fabAdd = view.findViewById(R.id.fab_add);
        searchView = view.findViewById(R.id.search_view);

        // Load dữ liệu
        restaurantList = dbHelper.getAllRestaurants();
        adapter = new RestaurantAdapter(restaurantList, getActivity());

        rvRestaurants.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRestaurants.setAdapter(adapter);

        fabAdd.setOnClickListener(v -> {
            ((RestaurantActivity) getActivity()).navigateToAddRestaurant();
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Restaurant> results = dbHelper.searchRestaurantsAdmin(newText);
                adapter.updateList(results);
                return true;
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh dữ liệu khi quay lại fragment
        restaurantList = dbHelper.getAllRestaurants();
        adapter.notifyDataSetChanged();
    }
    private void searchRestaurant(String keyword) {
        List<Restaurant> result;
        if (keyword.isEmpty()) {
            result = dbHelper.getAllRestaurants();
        } else {
            result = dbHelper.searchRestaurantsAdmin(keyword);
        }

        restaurantList.clear();
        restaurantList.addAll(result);
        adapter.notifyDataSetChanged();
    }
}