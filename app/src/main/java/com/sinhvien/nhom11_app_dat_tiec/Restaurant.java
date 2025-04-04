package com.sinhvien.nhom11_app_dat_tiec;

import java.io.Serializable;

public class Restaurant implements Serializable {
    private int restaurantId;
    private String title;
    private String description;
    private String imageResource;

    // Constructor
    public Restaurant() {}

    public Restaurant(int restaurantId, String title, String description, String imageResource) {
        this.restaurantId = restaurantId;
        this.title = title;
        this.description = description;
        this.imageResource = imageResource;
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

    public String getImageResource() {
        return imageResource;
    }

    public void setImageResource(String imageResource) {
        this.imageResource = imageResource;
    }
}