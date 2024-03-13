package com.example.shoppingplatform.model;

public class Information {

    private String name;
    private String phone;
    private String location;
   // public Account account=new Account("1","1");

    public Information(String name, String phone, String location){
        this.name=name;
        this.phone=phone;
        this.location=location;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getPhone() {
        return phone;
    }
}
