package com.sinhvien.nhom11_app_dat_tiec;

public class Restaurant {
    private int image;
    private String title;
    private String description;

    public Restaurant(int image, String title, String description) {
        this.image = image;
        this.title = title;
        this.description = description;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}