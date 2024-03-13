package com.example.shoppingplatform.ui.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppingplatform.R;
import com.example.shoppingplatform.adapter.LocationAdapter;
import com.example.shoppingplatform.model.Information;
import com.example.shoppingplatform.room.entity.AccountInformation;
import com.example.shoppingplatform.room.entity.AddCart;
import com.example.shoppingplatform.room.entity.Location;
import com.example.shoppingplatform.room.tool.DatabaseManager;
import com.example.shoppingplatform.ui.viewmodel.MyViewModel;

import java.util.ArrayList;
import java.util.List;


public class LocationActivity extends AppCompatActivity {

   // private MyViewModel viewModel;
    private RecyclerView recyclerView;
    private Handler handler= new Handler(Looper.getMainLooper());


    private Button add;
    private TextView back;
    private Button bBack;
    private Button confirm;
    private EditText eName;
    private EditText ePhone;
    private EditText eDz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        add=findViewById(R.id.button_location_purchase1);
        bBack=findViewById(R.id.location_back_button);
        recyclerView=findViewById(R.id.location_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(LocationActivity.this));

        //刷新界面
        refreshRecyclerView();

        //退出按钮
        bBack.setOnClickListener(v -> onBackPressed());

        //添加地址按钮
       add.setOnClickListener(v -> new Thread(() -> {
           Long aLong=DatabaseManager.getDatabaseManager(LocationActivity.this).isAccountNull();
           if (aLong!=0){
               handler.post(() -> showDialog());
           }
       }).start());
    }




    //刷新recyclerview的数据
    private void refreshRecyclerView(){
      new Thread(() -> {
          List<Location> locationList;
          String account=DatabaseManager.getDatabaseManager(this).getRecentlyId(2);
          if (account!=null){
              locationList=DatabaseManager.getDatabaseManager(this).allLocation(account);

              List<Location> finalLocationList = locationList;
              handler.post(() -> {
                      if (finalLocationList !=null) {
                          LocationAdapter locationAdapter = new LocationAdapter(LocationActivity.this, finalLocationList);
                          locationAdapter.locationClick = location -> {
                              clickLocation(location);
                          };
                          recyclerView.setAdapter(locationAdapter);
                      }

                  });

              }else {
                  //数据为null则提示
                 // flag=false;
                  handler.post(() -> Toast.makeText(LocationActivity.this,"暂无添加地址",Toast.LENGTH_SHORT).show());
              }


      }).start();
    }

    //处理点击事件更新购买地址
    private void clickLocation(Location location){
        new Thread(() -> {
            Location locationNew=new Location();
            locationNew.name=location.name;
            locationNew.location=location.location;
            locationNew.phone=location.phone;
            String account=DatabaseManager.getDatabaseManager(LocationActivity.this).isAccount("Location");
            if (account!=null){
                DatabaseManager.getDatabaseManager(LocationActivity.this).updateLocation(locationNew.phone,locationNew.name,locationNew.location,account);
                handler.post(() -> Toast.makeText(LocationActivity.this,"选择成功",Toast.LENGTH_SHORT).show());
            }else {
                    AccountInformation accountInformation=new AccountInformation();
                    accountInformation.account="Location";
                    DatabaseManager.getDatabaseManager(LocationActivity.this).addAccount(accountInformation);
                    locationNew.account="Location";
                    DatabaseManager.getDatabaseManager(LocationActivity.this).addLocation(locationNew);
                    handler.post(() -> Toast.makeText(LocationActivity.this,"选择成功",Toast.LENGTH_SHORT).show());
            }
        }).start();
    }




    //对话框
    private void showDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_layout, null);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();

        back=view.findViewById(R.id.back_dialog);
        confirm=view.findViewById(R.id.confirm_dialog);
        eName=view.findViewById(R.id.editText_dialog_name);
        ePhone=view.findViewById(R.id.editText_dialog_phone);
        eDz=view.findViewById(R.id.editText_dialog_dz);

        back.setOnClickListener(v -> alertDialog.dismiss()
        );

        confirm.setOnClickListener(v -> {
            String name=eName.getText().toString();
            String phone=ePhone.getText().toString();
            String dz=eDz.getText().toString();
            // 处理弹窗内容，例如返回给Fragment
            handleDialogContent(name,phone,dz);
            alertDialog.dismiss();

        });


        alertDialog.show();
    }

    //回收对话框数据插入并且刷新
    private void handleDialogContent(String name,String phone,String dz) {
        // 在这里处理弹窗返回的内容
      new Thread(new Runnable() {
         // boolean refresh=true;
          @Override
          public void run() {
              String account=DatabaseManager.getDatabaseManager(LocationActivity.this).getRecentlyId(2);
              if (account!=null){
                  Location location=new Location();
                  location.account=account;
                  location.name=name;
                  location.phone=phone;
                  location.location=dz;
                  DatabaseManager.getDatabaseManager(LocationActivity.this).addLocation(location);
                 // refresh=false;
                  handler.post(new Runnable() {
                      @Override
                      public void run() {
                          //if (!refresh){
                              refreshRecyclerView();
                         // }
                      }
                  });
              }


          }
      }).start();

    }


}
