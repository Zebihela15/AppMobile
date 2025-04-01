package com.sinhvien.nhom11_app_dat_tiec.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.sinhvien.nhom11_app_dat_tiec.MenuItem;
import com.sinhvien.nhom11_app_dat_tiec.R;
import java.text.NumberFormat;
import java.util.List;
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

    public void removeItem(int position) {
        menuItems.remove(position);
        notifyItemRemoved(position);
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;
        private final TextView tvServiceName, tvServiceId, tvServicePrice, tvDescription;
        private final Button btnEdit, btnDelete;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            tvServiceName = itemView.findViewById(R.id.tv_service_name);
            tvServiceId = itemView.findViewById(R.id.tv_service_id);
            tvServicePrice = itemView.findViewById(R.id.tv_service_price);
            tvDescription = itemView.findViewById(R.id.tv_menu_description);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }

        public void bind(MenuItem item) {
            tvServiceName.setText(item.getTitle());
            tvServiceId.setText(String.format("Mã: %d", item.getId()));
            tvServicePrice.setText(String.format("Giá: %s", currencyFormat.format(item.getPrice())));
            tvDescription.setText(item.getDescription());

            btnEdit.setOnClickListener(v -> {
                ((MenuActivity) context).navigateToEditMenu(item);
            });

            btnDelete.setOnClickListener(v -> {
                ((MenuActivity) context).showDeleteConfirmation(item, getAdapterPosition(), MenuAdapter.this);
            });
        }
    }
}