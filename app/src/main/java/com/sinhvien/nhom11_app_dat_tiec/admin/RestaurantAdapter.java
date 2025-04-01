package com.sinhvien.nhom11_app_dat_tiec.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.sinhvien.nhom11_app_dat_tiec.R;
import com.sinhvien.nhom11_app_dat_tiec.Restaurant;
import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {
    private List<Restaurant> restaurants;
    private Context context;

    public RestaurantAdapter(List<Restaurant> restaurants, Context context) {
        this.restaurants = restaurants;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.admin_item_restaurant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Restaurant restaurant = restaurants.get(position);
        holder.tvRestaurantName.setText(restaurant.getTitle());
        holder.tvRestaurantId.setText("Mã: " + restaurant.getRestaurantId());
        holder.tvDescription.setText(restaurant.getDescription());

        holder.btnEdit.setOnClickListener(v -> {
            ((RestaurantActivity) context).navigateToEditRestaurant(restaurant);
        });

        holder.btnDelete.setOnClickListener(v -> {
            ((RestaurantActivity) context).showDeleteConfirmation(restaurant, position, this);
        });
    }

    public void removeItem(int position) {
        restaurants.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, restaurants.size());
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRestaurantName, tvRestaurantId, tvDescription;
        Button btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRestaurantName = itemView.findViewById(R.id.tv_restaurant_name);
            tvRestaurantId = itemView.findViewById(R.id.tv_restaurant_id);
            tvDescription = itemView.findViewById(R.id.tv_description);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
    public void updateList(List<Restaurant> newList) {
        this.restaurants = newList;
        notifyDataSetChanged();
    }
}