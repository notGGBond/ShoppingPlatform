package com.example.shoppingplatform.model;

import android.graphics.Bitmap;

public class MerchandiseData {
    private Bitmap image;
    private String merchandise;
    private int id;
    private  double price;
    public MerchandiseData(int id, String name, double price, Bitmap image){
        this.id=id;
        this.merchandise=name;
        this.image=image;
        this.price=price;
    }

    public Bitmap getImage() {
        return image;
    }

    public double getPrice() {
        return price;
    }

    public String getMerchandise() {
        return merchandise;
    }

    public int getId() {
        return id;
    }
}
