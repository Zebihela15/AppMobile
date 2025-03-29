package com.sinhvien.nhom11_app_dat_tiec.admin;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.sinhvien.nhom11_app_dat_tiec.R;

public class ServiceActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_service);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ServiceListFragment())
                    .commit();
        }
    }
}
