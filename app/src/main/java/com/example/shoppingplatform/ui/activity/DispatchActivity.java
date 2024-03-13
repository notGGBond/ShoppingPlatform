package com.example.shoppingplatform.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.sqlite.SQLiteBlobTooBigException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.shoppingplatform.R;
import com.example.shoppingplatform.adapter.DispatchAdapter;
import com.example.shoppingplatform.model.MerchandiseData;
import com.example.shoppingplatform.room.tool.DatabaseManager;
import com.example.shoppingplatform.ui.tool.CallbackDispatch;
import com.example.shoppingplatform.ui.tool.Convert;

import java.util.ArrayList;
import java.util.List;

public class DispatchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private DispatchAdapter dispatchAdapter;
    private List<MerchandiseData> merchandiseDataList=new ArrayList<>();
    private Convert convert=new Convert();
    private CallbackDispatch callbackDispatch;
    private String account;
    private Button back;
    private Handler handler= new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatch);

        recyclerView=findViewById(R.id.recyclerview_dispatch);
        back=findViewById(R.id.button_dispatch_back);

        //关闭
        back.setOnClickListener(v -> onBackPressed());

        //初始化RecyclerView
        initRecyclerView(this);

    }

    //初始化
    private void initRecyclerView(Context context){
        Context rContext=context;

        recyclerView.setLayoutManager(new GridLayoutManager(rContext,1));

        new Thread(() -> {

           account=DatabaseManager.getDatabaseManager(rContext).getRecentlyId(2);
            if (!account.isEmpty()){
                String shopping=DatabaseManager.getDatabaseManager(rContext).waitShopping(account);
                if (!shopping.isEmpty()){
                    ArrayList<Integer> integerArrayList=convert.convertStringToArrayList(shopping);
                    getAdapterData(integerArrayList,rContext);
                }else{
                    handler.post(() -> Toast.makeText(rContext,"还未购买任何商品",Toast.LENGTH_LONG).show());
                }
            }else {
                handler.post(() -> Toast.makeText(rContext,"还未添加账号喔",Toast.LENGTH_LONG).show());
            }
        }).start();

    }

    //结束插入
    @Override
    public void onBackPressed() {
        ArrayList<Integer> arrayList=new ArrayList<>();
        String complete;
        if (merchandiseDataList!=null){
            for (MerchandiseData merchandiseData:merchandiseDataList){
                arrayList.add(merchandiseData.getId());
            }
            complete=convert.convertArrayListToString(arrayList);
            new Thread(() -> {
                boolean flag=false;
                while (!flag){
                    DatabaseManager.getDatabaseManager(DispatchActivity.this).addShopping(account,complete);
                    flag=true;
                }
                handler.post(() -> {
                    super.onBackPressed();
                });
            }).start();
        }else{
            super.onBackPressed();
        }
    }



    //获取并且设置recyclerview的数据
    private void getAdapterData(ArrayList<Integer> list,Context rContext){
        new Thread(() -> {
            boolean flag=false;
            while (!flag){
                try{
                    for (int i:list){
                        MerchandiseData merchandiseData= DatabaseManager.getDatabaseManager(rContext).shoppingId(i);
                        if (merchandiseData!=null){
                            merchandiseDataList.add(merchandiseData);
                        }
                    }
                    flag=true;
                }catch (SQLiteBlobTooBigException e){
                    flag=true;
                }

            }

            handler.post(() -> {
                if (merchandiseDataList!=null){
                    callbackDispatch= position -> {
                        if (merchandiseDataList!=null){
                            merchandiseDataList.remove(position);
                            dispatchAdapter=new DispatchAdapter(rContext,merchandiseDataList);
                            dispatchAdapter.callbackDispatch=callbackDispatch;
                            recyclerView.setAdapter(dispatchAdapter);
                        }
                    };
                    dispatchAdapter=new DispatchAdapter(rContext,merchandiseDataList);
                    dispatchAdapter.callbackDispatch=callbackDispatch;
                    recyclerView.setAdapter(dispatchAdapter);
                }
            });

        }).start();
    }






}