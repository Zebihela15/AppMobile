package com.sinhvien.nhom11_app_dat_tiec.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sinhvien.nhom11_app_dat_tiec.DatabaseHelper;
import com.sinhvien.nhom11_app_dat_tiec.R;
import com.sinhvien.nhom11_app_dat_tiec.Restaurant;
import java.util.List;

public class RestaurantListFragment extends Fragment {

    private RecyclerView rvRestaurants;
    private RestaurantAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<Restaurant> restaurantList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_fragment_restaurant_list, container, false);

        dbHelper = new DatabaseHelper(requireContext());
        rvRestaurants = view.findViewById(R.id.rv_restaurants);
        FloatingActionButton fabAdd = view.findViewById(R.id.fab_add);

        // Khởi tạo Adapter với Activity làm listener
        if (getActivity() instanceof RestaurantAdapter.OnRestaurantClickListener) {
            adapter = new RestaurantAdapter(
                    dbHelper.getAllRestaurants(),
                    (RestaurantAdapter.OnRestaurantClickListener) getActivity()
            );
        }

        rvRestaurants.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRestaurants.setAdapter(adapter);

        fabAdd.setOnClickListener(v -> {
            if (getActivity() instanceof RestaurantActivity) {
                ((RestaurantActivity) getActivity()).onAddRestaurant();
            }
        });

        return view;
    }
}