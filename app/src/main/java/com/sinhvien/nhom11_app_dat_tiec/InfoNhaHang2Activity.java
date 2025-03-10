package com.sinhvien.nhom11_app_dat_tiec;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



public class InfoNhaHang2Activity extends AppCompatActivity {

    private Dialog fullIntroDialog;
    private NestedScrollView nestedScrollView;
    private TextView introSection;
    private RecyclerView menuSection, gallerySection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_nhahang2);

        // Khởi tạo Dialog
        fullIntroDialog = new Dialog(this);
        fullIntroDialog.setContentView(R.layout.dialog_intro);
        fullIntroDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        // Khởi tạo các view
        nestedScrollView = findViewById(R.id.scrollView);
        introSection = findViewById(R.id.introSection);
        gallerySection = findViewById(R.id.gallerySection);
        menuSection = findViewById(R.id.menuSection);

        // Khởi tạo RecyclerView
        int[] imageGallery = {
                R.drawable.d21,
                R.drawable.d22,
                R.drawable.d23,
                R.drawable.d24,
                R.drawable.d25
        };
        int[] imageMenu = {
                R.drawable.menu1,
                R.drawable.menu2,
        };

        gallerySection.setLayoutManager(new GridLayoutManager(this, 3)); // 3 cột
        gallerySection.setAdapter(new ImageAdapter(this, imageGallery));

        menuSection.setLayoutManager(new GridLayoutManager(this, 3));
        menuSection.setAdapter(new ImageAdapter(this, imageMenu));
    }

    public void toggleIntro(View view) {
        fullIntroDialog.show();
    }

    public void closeDialog(View view) {
        fullIntroDialog.dismiss();
    }
}



