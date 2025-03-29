package com.sinhvien.nhom11_app_dat_tiec.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sinhvien.nhom11_app_dat_tiec.R;
import com.sinhvien.nhom11_app_dat_tiec.Restaurant;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {
    private List<Restaurant> restaurants;
    private final OnRestaurantClickListener listener;


    public interface OnRestaurantClickListener {
        void onEditClick(Restaurant restaurant);
        void onDeleteClick(Restaurant restaurant);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvRestaurantName, tvRestaurantId, tvDescription;
        public Button btnEdit, btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            tvRestaurantName = itemView.findViewById(R.id.tv_restaurant_name);
            tvRestaurantId = itemView.findViewById(R.id.tv_restaurant_id);
            tvDescription = itemView.findViewById(R.id.tv_description);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }

    public RestaurantAdapter(List<Restaurant> restaurants, OnRestaurantClickListener listener) {
        this.restaurants = restaurants;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.admin_item_restaurant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Restaurant restaurant = restaurants.get(position);

        holder.tvRestaurantName.setText(restaurant.getTitle());
        holder.tvRestaurantId.setText("Mã: " + restaurant.getRestaurantId());
        holder.tvDescription.setText(restaurant.getDescription());

        // Xử lý sự kiện với kiểm tra null
        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(restaurant);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(restaurant);
            }
        });
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }
}