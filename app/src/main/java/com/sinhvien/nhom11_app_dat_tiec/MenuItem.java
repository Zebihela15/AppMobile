package com.sinhvien.nhom11_app_dat_tiec;

public class MenuItem {
    private int id;
    private String title;
    private double price;
    private String description;

    public MenuItem() {}


    // Constructor mới có description
    public MenuItem(String title, double price, String description) {
        this.title = title;
        this.price = price;
        this.description = description;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}