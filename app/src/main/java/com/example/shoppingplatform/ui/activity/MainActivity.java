package com.example.shoppingplatform.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.shoppingplatform.R;
import com.example.shoppingplatform.room.entity.AccountInformation;
import com.example.shoppingplatform.room.entity.Location;
import com.example.shoppingplatform.room.tool.DatabaseManager;
import com.example.shoppingplatform.ui.fragment.CardFragment;
import com.example.shoppingplatform.ui.fragment.HomeFragment;
import com.example.shoppingplatform.ui.fragment.MessageFragment;
import com.example.shoppingplatform.ui.fragment.MyFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Fragment> fragments= new ArrayList();
    private Handler handler= new Handler(Looper.getMainLooper());
    Fragment homeFragment;
    Fragment cardFragment;
    Fragment messageFragment;
    Fragment myFragment;
    private BottomNavigationView bottomNavigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //判断数据库是否为null
        isDatabaseNull(this);


    }

    //判断数据库是否为null
    private void isDatabaseNull(Context context){
        new Thread(() -> {
            if (!DatabaseManager.getDatabaseManager(context).isNull()){
                DatabaseManager.getDatabaseManager(context).insertAll();
                Log.d("ww","www");
            }
            handler.post(() -> {
                initFragment();
            });
        }).start();

    }

    //初始化导航栏和fragment
    private void initFragment(){
        bottomNavigation=findViewById(R.id.bottom_navigation);
        bottomNavigation.setItemIconTintList(null);
        homeFragment=new HomeFragment();
        cardFragment=new CardFragment();
        messageFragment=new MessageFragment();
        myFragment=new MyFragment();
        fragments.add(homeFragment);
        fragments.add(cardFragment);
        fragments.add(messageFragment);
        fragments.add(myFragment);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_main,homeFragment).commit();

        initNavigationClick();
    }

    //导航栏点击事件
    private void initNavigationClick(){

        bottomNavigation.setOnItemSelectedListener(item -> {
            int menuId = item.getItemId();
            switch (menuId) {
                case R.id.home:switchFragment(fragments.get(0));
                    return true;
                case R.id.card:switchFragment(fragments.get(1));
                    return true;
                case R.id.message:switchFragment(fragments.get(2));
                    return true;
                case R.id.my:switchFragment(fragments.get(3));
                    return true;

            }

            return false;
        });
    }

    // 切换fragment
    private void switchFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragment_container_view_main, fragment);
        transaction.commit();
    }
}