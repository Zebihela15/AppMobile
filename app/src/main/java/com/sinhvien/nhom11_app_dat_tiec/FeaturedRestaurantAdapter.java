package com.sinhvien.nhom11_app_dat_tiec;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FeaturedRestaurantAdapter extends RecyclerView.Adapter<FeaturedRestaurantAdapter.ViewHolder> {

    private Context context;
    private List<Restaurant> restaurantList;

    public FeaturedRestaurantAdapter(Context context, List<Restaurant> restaurantList) {
        this.context = context;
        this.restaurantList = restaurantList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_featured_restaurant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Restaurant restaurant = restaurantList.get(position);
        holder.featuredImage.setImageResource(restaurant.getImage());
        holder.featuredTitle.setText(restaurant.getTitle());

        // Thêm sự kiện click
        holder.itemView.setOnClickListener(v -> {
            if (position == 0) { // Diamond Place 1 là item đầu tiên (index 0)
                Intent intent = new Intent(context, InfoNhaHang1Activity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView featuredImage;
        TextView featuredTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            featuredImage = itemView.findViewById(R.id.featuredImage);
            featuredTitle = itemView.findViewById(R.id.featuredTitle);
        }
    }
}