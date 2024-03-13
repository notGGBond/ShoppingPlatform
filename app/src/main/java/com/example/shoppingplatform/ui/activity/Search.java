package com.example.shoppingplatform.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import com.example.shoppingplatform.databinding.ActivitySearchBinding;
import com.example.shoppingplatform.model.MerchandiseData;
import com.example.shoppingplatform.room.tool.DatabaseManager;
import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity {
    private ActivitySearchBinding activitySearchBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySearchBinding=ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(activitySearchBinding.getRoot());


        initThread();

        //back键
        activitySearchBinding.back.setOnClickListener(v -> {
            onBackPressed();
        });

        //搜索键
        activitySearchBinding.startSearchButton.setOnClickListener(v -> {
            String data=activitySearchBinding.searchEditTextTextPersonName.getText().toString();
            if (data.equals("")){
                Toast.makeText(this,"请输入想搜索的商品",Toast.LENGTH_LONG).show();
            }else {
                Intent intent=new Intent(this,ProductDisplay.class);
                intent.putExtra("Key",data);
                startActivity(intent);
            }

        });
    }

    //获取猜你想搜的数据
    private void initThread(){
        Handler handler= new Handler(Looper.getMainLooper());
        new Thread(() -> {
            List<MerchandiseData> dataList=new ArrayList<>();
            for(int i=0;i<4;i++){
                MerchandiseData merchandiseData= DatabaseManager.getDatabaseManager(Search.this).random();
                dataList.add(merchandiseData);
            }

            handler.post(() -> initGuess(dataList));
        }).start();
    }

    //设置数据
    private void initGuess(List<MerchandiseData> merchandiseDataList){

        if (merchandiseDataList!=null){
            activitySearchBinding.guess2.setText(merchandiseDataList.get(0).getMerchandise());
            activitySearchBinding.guess3.setText(merchandiseDataList.get(1).getMerchandise());
            activitySearchBinding.guess4.setText(merchandiseDataList.get(2).getMerchandise());
            activitySearchBinding.guess5.setText(merchandiseDataList.get(3).getMerchandise());

        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activitySearchBinding=null;
    }
}