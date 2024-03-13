package com.example.shoppingplatform.model;

public class CartInformation {
    private MerchandiseData merchandiseData;
    private int number;
    public CartInformation(MerchandiseData merchandiseData,int number){
        this.merchandiseData=merchandiseData;
        this.number=number;
    }

    public int getNumber() {
        return number;
    }

    public MerchandiseData getMerchandiseData() {
        return merchandiseData;
    }
}
