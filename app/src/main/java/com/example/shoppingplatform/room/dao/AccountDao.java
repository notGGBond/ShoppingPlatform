package com.example.shoppingplatform.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.shoppingplatform.room.entity.AccountInformation;

import java.util.List;

@Dao
public interface AccountDao {

    //判断是否空表
    @Query("SELECT COUNT(*) FROM AccountInformation ")
    Long getCount();

    //返回所有的账号
    @Query("SELECT *FROM AccountInformation")
    List<AccountInformation> ALL();

    //插入账号
    @Insert
    Long addAccount(AccountInformation accountInformation) ;

    //查询账号
    @Query("SELECT * FROM AccountInformation WHERE account=:account AND password=:password")
    AccountInformation queryAccount(String account,String password);

    //返回最近账号
    @Query("SELECT * FROM AccountInformation WHERE recently =:recently")
    AccountInformation queryRecently(int recently) ;

    //返回最近账号id
    @Query("SELECT account FROM AccountInformation WHERE recently=:recently")
    String queryRecentlyId(int recently);

    //根据账号更新最近登录
    @Query("UPDATE AccountInformation SET recently=:recently WHERE account=:account")
    void updateRecently(String account,int recently);

    //判断是否有该账号
    @Query("SELECT account FROM AccountInformation WHERE account = :account")
    String isAccount(String account);

    //更新账号
    @Update
    void updateAccount(AccountInformation accountInformation);

    //待发货
    //插入购买商品的id
    @Query("UPDATE AccountInformation SET complete=:complete WHERE account=:account")
    void addShopping(String account,String complete);

    //返回账号已经购买的商品id
    @Query("SELECT complete FROM Accountinformation WHERE account=:account")
    String shipment(String account);




}
