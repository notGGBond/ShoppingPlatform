package com.example.shoppingplatform.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.shoppingplatform.room.entity.AddCart;
import com.example.shoppingplatform.room.entity.Location;

import java.util.List;

@Dao
public interface SubsidiaryDao {

    //购物车
    //插入购物车商品
    @Insert
    Long addCart(AddCart addCart);

    //根据标识返回对应商品
    @Query("SELECT * FROM AddCart WHERE identify=:identify")
    AddCart identifyQuery(int identify);

    //更新数量
    @Query("UPDATE AddCart SET number=:number WHERE identify=:identify ")
    void updateNumber(int identify,int number);

    //查询账号关联的商品
    @Query("SELECT * FROM AddCart WHERE account=:account ")
    List<AddCart> allCart(String account);

    //删除
    @Query("DELETE FROM AddCart WHERE identify=:identify")
    void deleteCart(int identify);

    //地址类
    //查询账号关联的地址
    @Query("SELECT * FROM Location WHERE account=:account")
    List<Location> aLLLocation(String account);

    //插入地址
    @Insert
    Long addLocation(Location location);

    //更新地址
    @Query("UPDATE Location SET phone=:phone,name=:name,location=:location WHERE account=:account")
    void updateLocation(String phone,String name,String location,String account);



}
