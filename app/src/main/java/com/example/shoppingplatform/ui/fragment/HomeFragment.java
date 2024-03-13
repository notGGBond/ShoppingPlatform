package com.example.shoppingplatform.ui.fragment;

import android.content.Intent;
import android.database.sqlite.SQLiteBlobTooBigException;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.shoppingplatform.R;
import com.example.shoppingplatform.adapter.ShoppingAdapter;
import com.example.shoppingplatform.model.MerchandiseData;
import com.example.shoppingplatform.room.entity.AccountInformation;
import com.example.shoppingplatform.room.entity.Location;
import com.example.shoppingplatform.room.tool.DatabaseManager;
import com.example.shoppingplatform.ui.activity.MainActivity;
import com.example.shoppingplatform.ui.activity.Search;
import com.example.shoppingplatform.ui.tool.BottomDialog;
import com.example.shoppingplatform.ui.viewmodel.MyViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView cardRecyclerView;
    //private List<MerchandiseData> dataList=new ArrayList<>();

    private ShoppingAdapter shoppingAdapter;
    private SwipeRefreshLayout refresh;
    private BottomDialog bottomDialog;
    //private MyViewModel viewModel;
    private View view;
    private TextView click;

    private Handler handler= new Handler(Looper.getMainLooper());

    public HomeFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_home, container, false);
        bottomDialog=new BottomDialog(getActivity());


        // initThread();
        initRecyclerView();
        initRefresh();
        initClick();

        return view;
    }

    //textView的点击事件
    private void initClick(){
        click=view.findViewById(R.id.click_textview);
        click.setOnClickListener(v -> {
            Intent intent=new Intent(getActivity(), Search.class);
            startActivity(intent);
        });
    }

    //初始化RecyclerView
    private void initRecyclerView(){
        cardRecyclerView=view.findViewById(R.id.home_recyclerview);
        cardRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));

        new Thread(new Runnable() {
            boolean exit=false;
            List<MerchandiseData> dataList=new ArrayList<>();
            @Override
            public void run() {
                while (!exit){
                    try {
                        for(int i=0;i<4;i++){
                            MerchandiseData merchandiseData=DatabaseManager.getDatabaseManager(getActivity()).random();
                            dataList.add(merchandiseData);
                        }
                        exit=true;
                    }catch (SQLiteBlobTooBigException e){
                        exit=true;
                    }
                }
                handler.post(() -> {
                    if (dataList!=null){
                        shoppingAdapter=new ShoppingAdapter(getActivity(),dataList,1,bottomDialog);
                        cardRecyclerView.setAdapter(shoppingAdapter);
                    }
                });
            }
        }).start();


    }

    @Override
    public void onStop() {
        super.onStop();
        //结束更新购物车和代发货
        bottomDialog.insert();
        bottomDialog.cartInsert();
    }

    //下拉刷新
    private void initRefresh(){
        refresh=view.findViewById(R.id.Refresh);
        refresh.setColorSchemeResources(R.color.green);
        refresh.setOnRefreshListener(() -> {
           new Thread(new Runnable() {
               boolean exit=false;
               List<MerchandiseData> dataList=new ArrayList<>();
               @Override
               public void run() {
                   while (!exit){
                       try {
                           for(int i=0;i<4;i++){
                           MerchandiseData merchandiseData=DatabaseManager.getDatabaseManager(getActivity()).random();
                           dataList.add(merchandiseData);
                           }
                           exit=true;
                       }catch (SQLiteBlobTooBigException e){
                           exit=true;
                           refresh.setRefreshing(false);
                       }
                   }
                   handler.post(() -> {
                       if (dataList!=null){
                           shoppingAdapter=new ShoppingAdapter(getActivity(),dataList,1,bottomDialog);
                           cardRecyclerView.setAdapter(shoppingAdapter);
                           shoppingAdapter.notifyDataSetChanged();
                           refresh.setRefreshing(false);
                           }


                   });
               }
           }).start();
        });
    }
}
//线程初始化
//    private void initThread(){
//        new Thread(() -> {
//            List<MerchandiseData> dataList=DatabaseManager.getDatabaseManager(getActivity()).random();
//            handler.post(() -> {
//                initRecyclerView(dataList);
//            });
//        }).start();
//
//        new Thread(() -> {
//            List<MerchandiseData> dataList=DatabaseManager.getDatabaseManager(getActivity()).random();
//            handler.post(() -> initRefresh(dataList));
//        }).start();
//    }

