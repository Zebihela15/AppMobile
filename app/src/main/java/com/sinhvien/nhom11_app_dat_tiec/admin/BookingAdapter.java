package com.sinhvien.nhom11_app_dat_tiec.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sinhvien.nhom11_app_dat_tiec.R;
import com.sinhvien.nhom11_app_dat_tiec.Booking;

import java.text.DecimalFormat;
import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {
    private List<Booking> bookings;
    private final OnBookingClickListener clickListener;
    private final OnBookingDeleteListener deleteListener;

    public interface OnBookingClickListener {
        void onBookingClick(Booking booking, int position);
    }

    public interface OnBookingDeleteListener {
        void onBookingDelete(Booking booking, int position);
    }

    public BookingAdapter(List<Booking> bookings, OnBookingClickListener clickListener, OnBookingDeleteListener deleteListener) {
        this.bookings = bookings;
        this.clickListener = clickListener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookings.get(position);

        holder.tvBookingId.setText("Mã đặt bàn: #" + booking.getId());
        holder.tvUserId.setText("User ID: " + booking.getUserId());
        holder.tvRestaurantId.setText("Nhà hàng ID: " + booking.getRestaurantId());
        holder.tvTableCount.setText("Số bàn: " + booking.getTableCount());
        holder.tvDateTime.setText(booking.getDate() + " - " + booking.getTime());
        holder.tvTotalPrice.setText("Tổng tiền: " + formatPrice(booking.getTotalPrice()) + " VND");

        // Xử lý sự kiện click
        holder.btnEdit.setOnClickListener(v -> clickListener.onBookingClick(booking, position));
        holder.btnDelete.setOnClickListener(v -> deleteListener.onBookingDelete(booking, position));

        // Vẫn giữ click trên toàn bộ item nếu cần
        holder.itemView.setOnClickListener(v -> clickListener.onBookingClick(booking, position));
    }

    private String formatPrice(double price) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(price);
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public void removeItem(int position) {
        bookings.remove(position);
        notifyItemRemoved(position);
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView tvBookingId, tvUserId, tvRestaurantId, tvTableCount, tvDateTime, tvTotalPrice;
        Button btnEdit, btnDelete;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookingId = itemView.findViewById(R.id.tv_booking_id);
            tvUserId = itemView.findViewById(R.id.tv_user_id);
            tvRestaurantId = itemView.findViewById(R.id.tv_restaurant_id);
            tvTableCount = itemView.findViewById(R.id.tv_table_count);
            tvDateTime = itemView.findViewById(R.id.tv_date_time);
            tvTotalPrice = itemView.findViewById(R.id.tv_total_price);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
    public void updateData(List<Booking> newBookings) {
        this.bookings = newBookings;
        notifyDataSetChanged();
    }
}