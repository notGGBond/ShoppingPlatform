package com.example.shoppingplatform.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;



@Entity(foreignKeys = @ForeignKey(entity = AccountInformation.class,
        parentColumns = "account",
        childColumns = "account",
        onDelete = ForeignKey.CASCADE))
public class AddCart {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int number;

    public int identify;

    @ColumnInfo(index = true)
    public String account;

}
