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
import com.sinhvien.nhom11_app_dat_tiec.Service;
import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {
    private List<Service> services;
    private Context context;

    public ServiceAdapter(List<Service> services, Context context) {
        this.services = services;
        this.context = context;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.admin_item_services, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        Service service = services.get(position);
        holder.tvTitle.setText(service.getTitle());
        holder.tvId.setText("Mã: " + service.getId());
        holder.tvPrice.setText("Giá: " + service.getPrice() + " VND");

        holder.btnEdit.setOnClickListener(v -> {
            ((ServiceActivity) context).navigateToEditService(service);
        });

        holder.btnDelete.setOnClickListener(v -> {
            ((ServiceActivity) context).showDeleteConfirmation(service, position, this);
        });
    }

    public void removeItem(int position) {
        services.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, services.size());
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvId, tvPrice;
        Button btnEdit, btnDelete;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_service_name);
            tvId = itemView.findViewById(R.id.tv_service_id);
            tvPrice = itemView.findViewById(R.id.tv_service_price);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}