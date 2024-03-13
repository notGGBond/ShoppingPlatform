package com.example.shoppingplatform.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.shoppingplatform.R;
import com.example.shoppingplatform.adapter.ShoppingAdapter;
import com.example.shoppingplatform.model.MerchandiseData;
import com.example.shoppingplatform.room.tool.DatabaseManager;
import com.example.shoppingplatform.ui.tool.BottomDialog;
import com.example.shoppingplatform.ui.viewmodel.MyViewModel;

import java.util.ArrayList;
import java.util.List;

public class ProductDisplay extends AppCompatActivity {

    private TextView price;
    private RecyclerView recyclerView;
    private ShoppingAdapter shoppingAdapter;
    private Button search;
    private BottomDialog bottomDialog;
    private Button back;
    private EditText content;

    //private MyViewModel viewModel=new ViewModelProvider(this).get(MyViewModel.class);

    private List<MerchandiseData> dataList=new ArrayList<>();
    private Handler handler= new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_display);
        bottomDialog=new BottomDialog(this);

        initClick();
        initRecyclerView();
    }

    //初始化价格升降序，搜索键，返回键
    private void initClick(){

        search=findViewById(R.id.start_search_button1);
        back=findViewById(R.id.back_button);
        price=findViewById(R.id.Price_switching_textView);
        content=findViewById(R.id.search_editTextTextPersonName1);
        recyclerView=findViewById(R.id.price_recyclerview);

        //价格升降序:1
        price.setOnClickListener(v -> showPopupMenu(v));
        //搜索键
        search.setOnClickListener(v -> {
            String data=content.getText().toString();
            setRecyclerViewContent("%"+data+"%",1);
        });
        //返回键
        back.setOnClickListener(v -> {
            onBackPressed();
        });
    }


    //价格升降序
    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_price, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            String data=content.getText().toString();
            switch (item.getItemId()) {
                case R.id.Incremental:
                    setRecyclerViewContent("%"+data+"%",2);
                    return true;
                case R.id.Decrement:
                    setRecyclerViewContent("%"+data+"%",3);
                    return true;
            }
            return false;
        });
        popupMenu.show();
    }

    //RecyclerView的设置
    private void initRecyclerView(){
        String data=getIntent().getStringExtra("Key");
        content.setText(data);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setRecyclerViewContent(data,1);
    }

    //获取RecyclerView内容
    private void setRecyclerViewContent(String data,int flag){

        switch (flag){
            case 1:{
                new Thread(() -> {
                    dataList=DatabaseManager.getDatabaseManager(ProductDisplay.this).isName(data);
                    handler.post(() -> {
                        if (dataList!=null){
                            shoppingAdapter=new ShoppingAdapter(ProductDisplay.this,dataList,2,bottomDialog);
                            recyclerView.setAdapter(shoppingAdapter);
                        }
                    });
                }).start();
                break;
            }
            case 2:{
                new Thread(() -> {
                    dataList=DatabaseManager.getDatabaseManager(ProductDisplay.this).incremental(data);
                    handler.post(() -> {
                        if (dataList!=null){
                            shoppingAdapter=new ShoppingAdapter(ProductDisplay.this,dataList,2,bottomDialog);
                            recyclerView.setAdapter(shoppingAdapter);
                        }
                    });
                }).start();
                break;
            }
            case 3:{
                new Thread(() -> {
                    dataList= DatabaseManager.getDatabaseManager(ProductDisplay.this).decrement(data);
                    handler.post(() -> {
                        if (dataList!=null){
                            shoppingAdapter=new ShoppingAdapter(ProductDisplay.this,dataList,2,bottomDialog);
                            recyclerView.setAdapter(shoppingAdapter);
                        }
                    });
                }).start();
                break;
            }
        }


    }


    @Override
    protected void onPause() {
        super.onPause();
        //结束插入购物车和代发货
        bottomDialog.insert();
        bottomDialog.cartInsert();
    }
}