package com.sinhvien.nhom11_app_dat_tiec;

import java.io.Serializable;

public class Restaurant implements Serializable {
    private int restaurantId;
    private String title;
    private String description;
    private String imagePath;

    // Constructor
    public Restaurant() {}

    public Restaurant(int restaurantId, String title, String description, String imagePath) {
        this.restaurantId = restaurantId;
        this.title = title;
        this.description = description;
        this.imagePath = imagePath;
    }

    // Getter và Setter
    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}