package com.example.shoppingplatform.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

//商品表
@Entity
public class Merchandise {

    @PrimaryKey
    public
    int id;

    public double price;

    public String name;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    public byte[] image;
}
