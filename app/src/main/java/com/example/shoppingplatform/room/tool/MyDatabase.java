package com.example.shoppingplatform.room.tool;

import androidx.room.RoomDatabase;

import com.example.shoppingplatform.room.dao.AccountDao;
import com.example.shoppingplatform.room.dao.MerchandiseDao;
import com.example.shoppingplatform.room.dao.SubsidiaryDao;
import com.example.shoppingplatform.room.entity.AccountInformation;
import com.example.shoppingplatform.room.entity.AddCart;
import com.example.shoppingplatform.room.entity.Location;
import com.example.shoppingplatform.room.entity.Merchandise;


//数据库实例
//@TypeConverters(IntegerListTypeConverter.class)
@androidx.room.Database(version = 1,entities = {Merchandise.class, AccountInformation.class, AddCart.class, Location.class},exportSchema=false)
abstract public class MyDatabase extends RoomDatabase {

    abstract MerchandiseDao merchandiseDao();

    abstract AccountDao accountDao();

    abstract SubsidiaryDao subsidiaryDao();


}