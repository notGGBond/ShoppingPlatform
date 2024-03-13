package com.example.shoppingplatform.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.shoppingplatform.room.entity.Merchandise;

import java.util.List;

@Dao
public interface MerchandiseDao {

    //插入数据
    @Insert
     Long insert(Merchandise merchandise);

    //判断数据库是否为null
    @Query("SELECT name FROM Merchandise WHERE name=:s")
    String isNull(String s);

    //查询是否包含该值
    @Query("SELECT * FROM Merchandise WHERE name LIKE  :search")
    List<Merchandise> searchName(String search);

    //随机查询前面n个数据
    @Query("SELECT * FROM Merchandise ORDER BY RANDOM() LIMIT 1")
    Merchandise random();

    //升序查询
    @Query("SELECT * FROM Merchandise WHERE name LIKE :search ORDER BY price ")
    List<Merchandise> incremental(String search);

    //降序查询
    @Query("SELECT * FROM Merchandise WHERE name LIKE :search ORDER BY price DESC ")
    List<Merchandise> decrement(String search);

    //id查询商品
    @Query("SELECT * FROM Merchandise WHERE id =:id")
    Merchandise idQuery(int id);


}
