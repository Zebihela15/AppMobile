package com.sinhvien.nhom11_app_dat_tiec.admin;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.sinhvien.nhom11_app_dat_tiec.R;
import com.sinhvien.nhom11_app_dat_tiec.Restaurant;

public class RestaurantActivity extends AppCompatActivity
        implements RestaurantAdapter.OnRestaurantClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_restaurant);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new RestaurantListFragment())
                    .commit();
        }
    }

    @Override
    public void onEditClick(Restaurant restaurant) {
        EditRestaurantFragment fragment = EditRestaurantFragment.newInstance(restaurant);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack("edit_restaurant")
                .commit();
    }

    @Override
    public void onDeleteClick(Restaurant restaurant) {

    }

    public void onAddRestaurant() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new AddRestaurantFragment())
                .addToBackStack("add_restaurant")
                .commit();
    }
}