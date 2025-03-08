package com.sinhvien.nhom11_app_dat_tiec;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private List<Integer> imageList;

    // Constructor cho MainActivity
    public ImageAdapter(List<Integer> imageList) {
        this.imageList = imageList;
    }

    // Constructor cho InfoNhaHang1Activity
    public ImageAdapter(AppCompatActivity activity, int[] imageResources) {
        this.imageList = new ArrayList<>();
        for (int res : imageResources) {
            this.imageList.add(res);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int imageRes = imageList.get(position);
        holder.imageView.setImageResource(imageRes);
    }

    @Override
    public int getItemCount() {
        return (imageList != null) ? imageList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}