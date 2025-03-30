package com.sinhvien.nhom11_app_dat_tiec.admin;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.sinhvien.nhom11_app_dat_tiec.R;

public class MenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_menu);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new MenuListFragment())
                    .commit();
        }
    }
}
