package com.sinhvien.nhom11_app_dat_tiec;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.sinhvien.nhom11_app_dat_tiec.Restaurant;

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

        // Hiển thị thông tin nhà hàng
        holder.featuredTitle.setText(restaurant.getTitle());
        holder.featuredDescription.setText(restaurant.getDescription());

        // Hiển thị hình ảnh từ database
        if (restaurant.getImagePath() != null && !restaurant.getImagePath().isEmpty()) {
            String imageName = restaurant.getImagePath();

            // Loại bỏ đuôi file nếu có
            if (imageName.contains(".")) {
                imageName = imageName.substring(0, imageName.lastIndexOf('.'));
            }

            int imageResId = context.getResources().getIdentifier(
                    imageName,
                    "drawable",
                    context.getPackageName()
            );

            if (imageResId != 0) {
                holder.featuredImage.setImageResource(imageResId);
            } else {
                holder.featuredImage.setImageResource(R.drawable.dtc3); // Ảnh mặc định
                Log.e("ImageError", "Không tìm thấy ảnh: " + imageName);
            }
        } else {
            holder.featuredImage.setImageResource(R.drawable.dtc3); // Ảnh mặc định
        }

        // Xử lý sự kiện click
        holder.itemView.setOnClickListener(v -> {
            Intent intent;
            switch (restaurant.getTitle()) {
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
                    return;
            }
            intent.putExtra("restaurant", restaurant);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    public void updateList(List<Restaurant> newList) {
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