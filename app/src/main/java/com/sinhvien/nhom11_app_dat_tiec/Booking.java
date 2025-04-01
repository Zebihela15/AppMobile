package com.sinhvien.nhom11_app_dat_tiec;

public class Booking {
    private int id;
    private String userId;
    private int restaurantId;
    private int tableCount;
    private String date;
    private String time;
    private int menuId;
    private double totalPrice;

    public Booking(int id, String userId, int restaurantId, int tableCount, String date, String time, int menuId, double totalPrice) {
        this.id = id;
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.tableCount = tableCount;
        this.date = date;
        this.time = time;
        this.menuId = menuId;
        this.totalPrice = totalPrice;
    }

    public int getId() { return id; }
    public String getUserId() { return userId; }
    public int getRestaurantId() { return restaurantId; }
    public int getTableCount() { return tableCount; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public int getMenuId() { return menuId; }
    public double getTotalPrice() { return totalPrice; }
}
