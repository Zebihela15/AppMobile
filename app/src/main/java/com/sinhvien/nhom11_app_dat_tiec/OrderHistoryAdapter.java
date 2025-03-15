package com.sinhvien.nhom11_app_dat_tiec;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {

    private List<DatabaseHelper.Booking> bookingList;

    public OrderHistoryAdapter(List<DatabaseHelper.Booking> bookingList) {
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_history, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        DatabaseHelper.Booking booking = bookingList.get(position);
        holder.tvRestaurantName.setText(booking.getRestaurantName());
        holder.tvBookingDate.setText(booking.getBookingDate());
        holder.tvTableCount.setText(String.valueOf(booking.getTableCount()));
        holder.tvTotalPrice.setText(String.format("%,.0f VNĐ", booking.getTotalPrice()));
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvRestaurantName, tvBookingDate, tvTableCount, tvTotalPrice;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRestaurantName = itemView.findViewById(R.id.tvRestaurantName);
            tvBookingDate = itemView.findViewById(R.id.tvBookingDate);
            tvTableCount = itemView.findViewById(R.id.tvTableCount);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
        }
    }
}