package com.example.shoppingplatform.ui.tool;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.shoppingplatform.R;
import com.example.shoppingplatform.model.MerchandiseData;
import com.example.shoppingplatform.room.entity.AddCart;
import com.example.shoppingplatform.room.entity.Location;
import com.example.shoppingplatform.room.tool.DatabaseManager;
import com.example.shoppingplatform.ui.activity.LocationActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class BottomDialog {

    private Handler handler= new Handler(Looper.getMainLooper());
    private ArrayList<Integer> arrayList=new ArrayList<>();
    private Context context;
    private TextView phonePurchase;
    private TextView namePurchase;
    private TextView dzPurchase;
    private int[]cart =new int[100];

    public BottomDialog(Context context){
        this.context=context;
    }


    //购物车数量
    public void numberCart(int id){
        cart[id-1]+=1;
        Toast.makeText(context,"添加成功",Toast.LENGTH_SHORT).show();
    }

    //插入购物车
    public void cartInsert(){
       new Thread(() -> {
           String account=DatabaseManager.getDatabaseManager(context).getRecentlyId(2);
           if (account!=null&&!account.isEmpty()){
               for (int i=0;i<cart.length;i++){
                   if (cart[i]!=0){
                       AddCart addCart= DatabaseManager.getDatabaseManager(context).identifyQuery(i+1);
                       if (addCart!=null){
                           DatabaseManager.getDatabaseManager(context).updateNumber(i+1,addCart.number+cart[i]);
                       }else {
                           AddCart newCart=new AddCart();
                           newCart.number=cart[i];
                           newCart.identify=i+1;
                           newCart.account=account;
                           DatabaseManager.getDatabaseManager(context).addCart(newCart);
                       }
                   }
               }
           }
       }).start();
    }

    //购买框
    public void purchaseButton(MerchandiseData merchandiseData){

        BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(context);
        View bottomSheetView= LayoutInflater.from(context).inflate(R.layout.payment_layout,null);
        bottomSheetDialog.setContentView(bottomSheetView);

        ImageView imagePurchase=bottomSheetView.findViewById(R.id.image_purchase);
        ImageView imageLocation=bottomSheetView.findViewById(R.id.image_purchase1);
        ImageView refresh=bottomSheetView.findViewById(R.id.imageView_purchase_refresh);
        TextView textPurchase=bottomSheetView.findViewById(R.id.textview_purchase);
        namePurchase=bottomSheetView.findViewById(R.id.textView_purchase_name);
         phonePurchase=bottomSheetView.findViewById(R.id.textView_purchase_phone);
         dzPurchase=bottomSheetView.findViewById(R.id.textView_purchase_dz);
        TextView priceCalculate=bottomSheetView.findViewById(R.id.calculate_price);
        TextView back=bottomSheetView.findViewById(R.id.textView_back);
        Button calculate=bottomSheetView.findViewById(R.id.calculate_button);

        //设置商品信息
        textPurchase.setText(merchandiseData.getMerchandise());
        imagePurchase.setImageBitmap(merchandiseData.getImage());
        priceCalculate.setText(merchandiseData.getPrice()+"");



        //选择地址
        imageLocation.setOnClickListener(v -> {
            Intent intent=new Intent(context, LocationActivity.class);
            context.startActivity(intent);
            //设置地址
            getLocation();
        });

        //刷新地址
        refresh.setOnClickListener(view -> getLocation());


        //购买点击按键
        calculate.setOnClickListener(v -> {
            if (namePurchase.getText()!=null&&dzPurchase.getText()!=""){
                //发送通知
                arrayList.add(merchandiseData.getId());
                notice(merchandiseData);
                Toast.makeText(context,"购买成功",Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            }else {
                Toast.makeText(context,"暂未选择地址",Toast.LENGTH_SHORT).show();
            }
        });

        //关闭弹窗
        back.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();

        });
        bottomSheetDialog.show();
    }

    //插入购买商品
    public void insert(){
        new Thread(() -> {
            String s=DatabaseManager.getDatabaseManager(context).getRecentlyId(2);
            if (s!=null){
                Convert convert=new Convert();
                ArrayList<Integer> arrayListConvert=new ArrayList<>();
                String shopping=DatabaseManager.getDatabaseManager(context).waitShopping(s);
                if (shopping!=null&& !shopping.equals("")){
                   arrayListConvert=convert.convertStringToArrayList(shopping);
                   for (int i:arrayListConvert){
                       arrayList.add(i);
                   }
                   String result=convert.convertArrayListToString(arrayList);
                   DatabaseManager.getDatabaseManager(context).addShopping(s,result);

                }else {
                    String result=convert.convertArrayListToString(arrayList);
                    DatabaseManager.getDatabaseManager(context).addShopping(s,result);

                }
            }
            arrayList.clear();
        }).start();
    }

    //设置地址
    private void getLocation(){
        new Thread(() -> {
            List<Location> list= DatabaseManager.getDatabaseManager(context).allLocation("Location");
            for(Location location:list){
                handler.post(() -> {
                    namePurchase.setText(location.name);
                    phonePurchase.setText(location.phone);
                    dzPurchase.setText(location.location);
                });
            }

        }).start();
    }

    //通知
    private void notice(MerchandiseData merchandiseData){
        NotificationManager notificationManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel=new NotificationChannel("Purchase_Successful","Purchase",NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(notificationChannel);
        Notification notification= new NotificationCompat.Builder(context,"Purchase_Successful")
                .setContentTitle("购买成功")
                .setContentText("您成功的下单了一个:"+merchandiseData.getMerchandise()+" 共花费您X"+merchandiseData.getPrice()+" 请耐心等待发货呦")
                .setSmallIcon(R.drawable.delivery)
                .build();
        notificationManager.notify(merchandiseData.getId(),notification);
    }
}
