package com.sinhvien.nhom11_app_dat_tiec.admin;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.sinhvien.nhom11_app_dat_tiec.DatabaseHelper;
import com.sinhvien.nhom11_app_dat_tiec.MenuItem;
import com.sinhvien.nhom11_app_dat_tiec.R;
import java.util.List;
import java.text.NumberFormat;
import java.util.Locale;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {
    private final Context context;
    private final List<MenuItem> menuItems;
    private final NumberFormat currencyFormat;

    public MenuAdapter(Context context, List<MenuItem> menuItems) {
        this.context = context;
        this.menuItems = menuItems;
        this.currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_item_menu, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuItem item = menuItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;
        private final TextView tvServiceName, tvServiceId, tvServicePrice,tvDescription;
        private final Button btnEdit, btnDelete;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            tvServiceName = itemView.findViewById(R.id.tv_service_name);
            tvServiceId = itemView.findViewById(R.id.tv_service_id);
            tvServicePrice = itemView.findViewById(R.id.tv_service_price);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            tvDescription = itemView.findViewById(R.id.tv_menu_description);
        }

        public void bind(MenuItem item) {
            tvServiceName.setText(item.getTitle());
            tvServiceId.setText(String.format("Mã: %d", item.getId()));
            tvServicePrice.setText(String.format("Giá: %s", currencyFormat.format(item.getPrice())));


            btnEdit.setOnClickListener(v -> {
                EditMenuFragment editMenuFragment = EditMenuFragment.newInstance(item);
                ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, editMenuFragment)
                        .addToBackStack(null)
                        .commit();
            });

            btnDelete.setOnClickListener(v -> {
                showDeleteConfirmationDialog(item);
            });
        }

        private void showDeleteConfirmationDialog(MenuItem item) {
            new AlertDialog.Builder(context)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa dịch vụ '" + item.getTitle() + "'?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            // Xóa từ database
                            DatabaseHelper dbHelper = new DatabaseHelper(context);
                            dbHelper.deleteMenu(item.getId());
                            menuItems.remove(position);
                            notifyItemRemoved(position);

                            Toast.makeText(context, "Đã xóa dịch vụ", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Hủy", null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }
}