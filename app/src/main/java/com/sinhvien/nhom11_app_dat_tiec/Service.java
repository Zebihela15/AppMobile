package com.sinhvien.nhom11_app_dat_tiec;

import java.io.Serializable;

public class Service implements Serializable {
    private long id;
    private String title;
    private double price;

    // Constructors
    public Service() {}

    public Service(String title, double price) {
        this.title = title;
        this.price = price;
    }

    // Getters and setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
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
}
