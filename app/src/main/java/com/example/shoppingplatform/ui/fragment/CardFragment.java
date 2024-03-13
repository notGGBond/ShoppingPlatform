package com.example.shoppingplatform.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppingplatform.R;
import com.example.shoppingplatform.adapter.CartAdapter;
import com.example.shoppingplatform.model.CartInformation;
import com.example.shoppingplatform.model.MerchandiseData;
import com.example.shoppingplatform.room.entity.AddCart;
import com.example.shoppingplatform.room.tool.DatabaseManager;
import com.example.shoppingplatform.ui.tool.Convert;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private RecyclerView cartRecyclerview;
    private View view;
    private TextView totalPrice;
    private List<AddCart> cartList;
    private Handler handler=new Handler(Looper.getMainLooper());
    private Button payment;
    private ArrayList<Integer> arrayList=new ArrayList<>();
    private Convert convert=new Convert();
    private boolean[] dispatch;
    private int price=0;

    private List<CartInformation> cartInformationList1=new ArrayList<>();

    public CardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CardFragment newInstance(String param1, String param2) {
        CardFragment fragment = new CardFragment();
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
        view=inflater.inflate(R.layout.fragment_card, container, false);
        totalPrice=view.findViewById(R.id.textView_card_price);
        payment=view.findViewById(R.id.button_card_payment);

        initRecyclerView();
        dispatchShopping();
        return view;
    }

    //购买键
    private void dispatchShopping(){
        payment.setOnClickListener(v -> {
            new Thread(() -> {
                if (dispatch!=null){
                    for (int i=0;i<dispatch.length;i++){
                        if (dispatch[i]){
                            for (int j=0;j<cartList.get(i).number;j++){
                                arrayList.add(cartList.get(i).identify);
                            }

                            DatabaseManager.getDatabaseManager(getActivity()).deleteCart(cartList.get(i).identify);
                        }
                    }
                    String account= DatabaseManager.getDatabaseManager(getActivity()).getRecentlyId(2);
                    if (account!=null&&!account.isEmpty()){
                       String shopping= DatabaseManager.getDatabaseManager(getActivity()).waitShopping(account);
                        ArrayList<Integer> integerArrayList=new ArrayList<>();
                        integerArrayList=convert.convertStringToArrayList(shopping);
                        integerArrayList.addAll(arrayList);
                        String result=convert.convertArrayListToString(integerArrayList);
                        DatabaseManager.getDatabaseManager(getActivity()).addShopping(account,result);
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),"购买成功",Toast.LENGTH_SHORT).show();
                            totalPrice.setText("$"+0);
                            initRecyclerView();
                        }
                    });

                }

            }).start();
        });
    }

    //初始化recyclerview
    private void initRecyclerView(){
        cartRecyclerview=view.findViewById(R.id.cart_shopping);
        cartRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

        new Thread(() -> {
            String account= DatabaseManager.getDatabaseManager(getActivity()).getRecentlyId(2);
            if (account!=null&&!account.isEmpty()){
                 List<CartInformation> cartInformationList=new ArrayList<>();

                cartList=DatabaseManager.getDatabaseManager(getActivity()).allCart(account);
                if (cartList!=null){
                    for (AddCart cart:cartList){
                        MerchandiseData merchandiseData=DatabaseManager.getDatabaseManager(getActivity()).shoppingId( cart.identify);
                        cartInformationList.add(new CartInformation(merchandiseData,cart.number));
                    }
                    handler.post(() -> {
                        if (cartInformationList!=null){
                            cartInformationList1=cartInformationList;
                            dispatch=new boolean[cartInformationList.size()];
                            CartAdapter cartAdapter=new CartAdapter(getActivity(),cartInformationList);

                            cartAdapter.checkBoxClick= (value, price) -> {
                                dispatch=value;
                                totalPrice.setText("$"+price);
                            };


                            cartRecyclerview.setAdapter(cartAdapter);
                        }
                    });
                }else {
                    handler.post(() -> Toast.makeText(getActivity(),"暂未添加商品到购物车",Toast.LENGTH_SHORT).show());
                }
            }else{
                handler.post(() -> Toast.makeText(getActivity(),"暂未添加账号",Toast.LENGTH_SHORT).show());
            }
        }).start();
    }






}