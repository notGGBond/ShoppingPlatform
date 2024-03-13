package com.example.shoppingplatform.room.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class AccountInformation {

    @NonNull
    @PrimaryKey
    public String account;

    public String password;

    public String phone;

    public String name;

    public String gender;

    public String area;

    public String email;

    //标识最近账号
    public int recently;


    public String complete;


}
