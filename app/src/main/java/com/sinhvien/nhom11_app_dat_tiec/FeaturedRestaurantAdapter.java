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
    private List<DatabaseHelper.Restaurant> restaurantList;

    public FeaturedRestaurantAdapter(Context context, List<DatabaseHelper.Restaurant> restaurantList) {
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
        DatabaseHelper.Restaurant restaurant = restaurantList.get(position);

        holder.featuredTitle.setText(restaurant.getTitle());
        holder.featuredDescription.setText(restaurant.getDescription());

        // Thêm sự kiện click dựa trên tiêu đề nhà hàng
        holder.itemView.setOnClickListener(v -> {
            Intent intent;
            String restaurantTitle = restaurant.getTitle();

            switch (restaurantTitle) {
                case "Diamond Place 1":
                    intent = new Intent(context, InfoNhaHang1Activity.class);
                    break;
                case "Diamond Place 2":
                    intent = new Intent(context, InfoNhaHang2Activity.class);
                    break;
                case "Diamond Place 3":
                    intent = new Intent(context, InfoNhaHang3Activity.class);
                    break;
                default:
                    return; // Không làm gì nếu không khớp
            }
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    // Cập nhật danh sách nhà hàng khi tìm kiếm
    public void updateList(List<DatabaseHelper.Restaurant> newList) {
        restaurantList = newList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView featuredImage;
        TextView featuredTitle;
        TextView featuredDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            featuredImage = itemView.findViewById(R.id.featuredImage);
            featuredTitle = itemView.findViewById(R.id.featuredTitle);
            featuredDescription = itemView.findViewById(R.id.featuredDescription);
        }
    }
}