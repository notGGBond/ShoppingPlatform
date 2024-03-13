package com.example.shoppingplatform.room.tool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.room.Room;


import com.example.shoppingplatform.model.MerchandiseData;
import com.example.shoppingplatform.room.entity.AccountInformation;
import com.example.shoppingplatform.room.entity.AddCart;
import com.example.shoppingplatform.room.entity.Location;
import com.example.shoppingplatform.room.entity.Merchandise;

import java.io.ByteArrayOutputStream;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private static DatabaseManager databaseManager;
    private  static Context contextLocal;

    private MyDatabase database=Room.databaseBuilder(contextLocal.getApplicationContext(),MyDatabase.class,"MyDatabase").build();
    private DatabaseManager(){


    }

    //单例获取DatabaseManager
    public static synchronized DatabaseManager getDatabaseManager(Context context){

        if (databaseManager==null){
            contextLocal=context;
            databaseManager=new DatabaseManager();
        }
        return databaseManager;
    }




    //MerchandiseDao接口的封装
    //判断数据库是否为null
    public boolean isNull(){
        String flag=database.merchandiseDao().isNull("香蕉");
//        if (flag==null)return false;
//        else return true;
        return flag != null;
    }

    //插入所有数据
    public void insertAll(){
        double[] price={19.99,12.99,19.99,9.99,12.99,39.99,29.99,9.99,15.99,9.99,
                18.99,19.99,49.99,19.99,15.99,19.99,79.99,19.99,99.99,19.99
                ,1599.99,19.99,9.99,69.99,9.99,59.99,9.99,39.99,49.99,19.99,
                29.99,59.99,79.99,999.99,49.99,199.99,79.99,799.99,39.99,599.99,
                49.99,299.99,69.99,29.99,199.99,259.99,399.99,199.99,69.99,399.99,
                199.99,59.99,99.99,99.99,69.99,2999.99,199.99,59.99,99.99,99.99,
                69.99,99.99,599.99,109.99,99.99,199.99,399.99,69.99,9.99,59.99,
                99.99,99.99,69.99,29.99,199.99,599.99,199.99,299.99,69.99,699.99,
                599.99,199.99,199.99,189.99,189.99,259.99,29.99,599.99,29.99,99.99,
                99.99,69.99,299.99,37.99,389.99,99.99,129.99,12.99,59.99,489.99};

        String[] name={"木瓜","菠萝","草莓","苹果","桔子","蓝莓","葡萄","梨","石榴","青苹果",
                "香蕉","柠檬","哈密瓜","水蜜桃","西瓜","牛油果","车厘子","柿子","热水壶","西兰花",
                "洗衣机","灯笼椒","青菜","高脚凳","马铃薯","牛仔裤","洋葱","短袖","纯棉短袖","花菜",
                "灰色短袖","佛珠手串","红色连衣裙","珍珠项链","蓝猫短袖","时尚项链","西装内衬","翡翠耳坠","白色短袖","水晶珠项链",
                "老式短袖","咖啡机","黑色时尚项链","黑色潮流项链","高亮珍珠","帆布鞋","微波炉","复古怀表","灰色裙子","煤气灶",
                "运动鞋","手串","熨斗","龙形项链","拉菲草帽子","冰箱","女版运动鞋","多彩石头","潮流牛仔裤","吹风机",
                "国风毛笔","牛仔牛仔裤","多功能微波炉","米奇色风衣","蓝色车内吊坠","马丁靴","吊扇","红色长袖","剪刀","牛仔短裤",
                "天然水晶吊坠","蓝牙耳机","民族饰品","灰色潮流短袖","头戴式耳机","印式项链","旗袍","绿水晶项链","儿童牛仔背带裤","蓝水晶椭圆项链",
                "蓝水晶菱形项链","监控","蓝色琉璃项链","英伦复古皮鞋","行李箱","珍珠戒指","红色鸭舌帽","面包机","纯棉拖鞋","星空珠",
                "菩提子佛珠手串","黑色长裙","扫地机","洞洞鞋","红玛瑙项链","复古孔雀石耳坠","高跟鞋","小熊汽车挂件","唐老鸭短袖","VR眼镜"};


        for(int i=1;i<=100;i++){

            String s=Integer.toString(i);
            String path="d"+s;
            byte[] image=getBytesFromDrawable(contextLocal,path);

            Merchandise merchandise=new Merchandise();
            merchandise.id=i;
            merchandise.name=name[i-1];
            merchandise.price=price[i-1];
            merchandise.image=image;

            database.merchandiseDao().insert(merchandise);

        }
    }

    //随机查询10个数据
    public MerchandiseData random(){

        Merchandise merchandise=database.merchandiseDao().random();
        Bitmap bitmap=BitmapFactory.decodeByteArray(merchandise.image,0,merchandise.image.length);
        MerchandiseData merchandiseData=new MerchandiseData(merchandise.id,merchandise.name,merchandise.price,bitmap);
        return merchandiseData;
    }

    //升序查询
    public List<MerchandiseData> incremental(String search){
        List<Merchandise> merchandiseList;
        List<MerchandiseData> merchandiseDataList =new ArrayList<>();
        merchandiseList=database.merchandiseDao().incremental(search);

        for (Merchandise merchandise:merchandiseList){
            Bitmap bitmap=BitmapFactory.decodeByteArray(merchandise.image,0,merchandise.image.length);
            MerchandiseData merchandiseData=new MerchandiseData(merchandise.id,merchandise.name,merchandise.price,bitmap);
            merchandiseDataList.add(merchandiseData);
        }
        return merchandiseDataList;
    }

    //降序查询
    public List<MerchandiseData> decrement(String search){
        List<Merchandise> merchandiseList;
        List<MerchandiseData> merchandiseDataList = new ArrayList<>();
        merchandiseList=database.merchandiseDao().decrement(search);
        for (Merchandise merchandise:merchandiseList){
            Bitmap bitmap=BitmapFactory.decodeByteArray(merchandise.image,0,merchandise.image.length);
            MerchandiseData merchandiseData=new MerchandiseData(merchandise.id,merchandise.name,merchandise.price,bitmap);
            merchandiseDataList.add(merchandiseData);
        }
        return merchandiseDataList;

    }

    //是否包含该值
    public List<MerchandiseData> isName(String search){
        List<Merchandise> merchandiseList;
        List<MerchandiseData> merchandiseDataList = new ArrayList<>();
        merchandiseList=database.merchandiseDao().searchName(search);
        for (Merchandise merchandise:merchandiseList){
            Bitmap bitmap=BitmapFactory.decodeByteArray(merchandise.image,0,merchandise.image.length);
            MerchandiseData merchandiseData=new MerchandiseData(merchandise.id,merchandise.name,merchandise.price,bitmap);
            merchandiseDataList.add(merchandiseData);
        }
        return merchandiseDataList;

    }

    //返回byte数组
    private byte[] getBytesFromDrawable(Context context, String drawableName) {
        // 获取资源ID
        int resourceId = context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName());
        // 获取Drawable对象
        Drawable drawable = ContextCompat.getDrawable(context, resourceId);
        if (drawable == null) {
            return null; // 返回空数组如果找不到对应的资源
        }
        // 计算Bitmap的尺寸
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream); // 100为最佳质量
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }


    //AccountDao接口的封装
    //插入账号
    public void addAccount(AccountInformation accountInformation){
        database.accountDao().addAccount(accountInformation);
    }

    //判断是否是空表
    public Long isAccountNull(){
        return database.accountDao().getCount();
    }

    //返回最近的账号
    public AccountInformation getRecently(int recently){
        return database.accountDao().queryRecently(recently);
    }

    //返回最近的账号id
    public String getRecentlyId(int recently){
        return database.accountDao().queryRecentlyId(recently);
    }

    //更新最近账号
    public void updateRecently(String account,int recently){
        database.accountDao().updateRecently(account,recently);
    }

    //判断是否有该账号
    public String isAccount(String account){
        String result=database.accountDao().isAccount(account);;
        return result;
    }

    //更新账号
    public void updateAccount(AccountInformation accountInformation){
        database.accountDao().updateAccount(accountInformation);
    }

    //查询账号
    public AccountInformation queryAccount(String account, String password){
        return database.accountDao().queryAccount(account,password);
    }

    //代发货
    //插入
    public void addShopping(String account,String complete){
        database.accountDao().addShopping(account,complete);
    }

    //待发货名单
    public String waitShopping(String account){
        return database.accountDao().shipment(account);
    }

    //SubsidiaryDao接口的封装
    //购物车添加
    public void addCart(AddCart addCart){
        database.subsidiaryDao().addCart(addCart);
    }

    //返回商品
    public  AddCart identifyQuery(int identify){
        return database.subsidiaryDao().identifyQuery(identify);
    }

    //更新商品数量
    public  void updateNumber(int identify,int number){
        database.subsidiaryDao().updateNumber(identify,number);
    }

    //查询账号相关的商品
    public List<AddCart> allCart(String account){
        return database.subsidiaryDao().allCart(account);
    }

    //删除购物车商品
    public void deleteCart(int identify){
        database.subsidiaryDao().deleteCart(identify);
    }

    //查询账号相关的地址
    public List<Location> allLocation(String account){
        return database.subsidiaryDao().aLLLocation(account);
    }

    //插入地址
    public void addLocation(Location location){
        database.subsidiaryDao().addLocation(location);
    }


    public List<AccountInformation> all(){
        return database.accountDao().ALL();
    }

    //更新地址
    public void updateLocation(String phone,String name,String location,String account){
        database.subsidiaryDao().updateLocation(phone,name,location,account);
    }

    //根据id返回商品
    public MerchandiseData shoppingId(int id){
        Merchandise merchandise=database.merchandiseDao().idQuery(id);
        Bitmap bitmap=BitmapFactory.decodeByteArray(merchandise.image,0,merchandise.image.length);
        MerchandiseData merchandiseData=new MerchandiseData(merchandise.id,merchandise.name,merchandise.price,bitmap);
        return merchandiseData;
    }
}
