package com.sinhvien.nhom11_app_dat_tiec.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sinhvien.nhom11_app_dat_tiec.DatabaseHelper;
import com.sinhvien.nhom11_app_dat_tiec.R;
import com.sinhvien.nhom11_app_dat_tiec.Booking;

import java.util.ArrayList;
import java.util.List;

public class BookingListFragment extends Fragment {
    private RecyclerView recyclerView;
    private BookingAdapter adapter;
    private SearchView searchView;
    private List<Booking> originalBookings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_fragment_booking_list, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_bookings);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        searchView = view.findViewById(R.id.search_view);
        setupSearchView();

        loadBookings();

        view.findViewById(R.id.fab_add_booking).setOnClickListener(v -> {
            ((BookingActivity) requireActivity()).navigateToAddBooking();
        });

        return view;
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterBookings(newText);
                return true;
            }
        });
    }

    private void filterBookings(String query) {
        if (originalBookings == null) return;

        List<Booking> filteredList = new ArrayList<>();
        if (query.isEmpty()) {
            filteredList.addAll(originalBookings);
        } else {
            try {
                int searchId = Integer.parseInt(query);
                for (Booking booking : originalBookings) {
                    if (String.valueOf(booking.getId()).contains(query) ||
                            booking.getUserId().toLowerCase().contains(query.toLowerCase())) {
                        filteredList.add(booking);
                    }
                }
            } catch (NumberFormatException e) {
                // Nếu không phải số, tìm theo userId
                for (Booking booking : originalBookings) {
                    if (booking.getUserId().toLowerCase().contains(query.toLowerCase())) {
                        filteredList.add(booking);
                    }
                }
            }
        }
        adapter.updateData(filteredList);
    }

    private void loadBookings() {
        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
        originalBookings = dbHelper.getAllBookings();
        adapter = new BookingAdapter(originalBookings, (booking, position) -> {
            ((BookingActivity) requireActivity()).navigateToEditBooking(booking);
        }, (booking, position) -> {
            ((BookingActivity) requireActivity()).showDeleteConfirmation(booking, position, adapter);
        });
        recyclerView.setAdapter(adapter);
    }
}