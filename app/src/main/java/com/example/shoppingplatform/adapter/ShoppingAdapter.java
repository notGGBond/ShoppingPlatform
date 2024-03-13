package com.example.shoppingplatform.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingplatform.R;
import com.example.shoppingplatform.model.MerchandiseData;
import com.example.shoppingplatform.room.entity.Location;
import com.example.shoppingplatform.ui.activity.LocationActivity;
import com.example.shoppingplatform.ui.tool.BottomDialog;
import com.example.shoppingplatform.ui.viewmodel.MyViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class ShoppingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<MerchandiseData> merchandiseDataList;
    private BottomDialog bottomDialog;
    private int flag;
    //private MyViewModel viewModel;

    public ShoppingAdapter(Context context, List<MerchandiseData> merchandiseDataList, int flag,BottomDialog bottomDialog){
        this.context=context;
        this.bottomDialog=bottomDialog;
        //this.viewModel=viewModel;
        this.merchandiseDataList=merchandiseDataList;
        this.flag=flag;
    }


    class CardViewHolder extends RecyclerView.ViewHolder{
        TextView cName;
        TextView cPrice;
        ImageView cImage;
        Button cCard;
        Button cPurchase;
        public CardViewHolder(@NonNull View view) {
            super(view);
            cName=view.findViewById(R.id.card_textView);
            cPrice=view.findViewById(R.id.price_textView);
            cImage=view.findViewById(R.id.card);
            cCard=view.findViewById(R.id.add_button);
            cPurchase=view.findViewById(R.id.shopping_button);


        }
    }

    class FlowingHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView price;
        ImageView image;

        Button card;
        Button purchase;
        public FlowingHolder(@NonNull View view) {
            super(view);
            name=view.findViewById(R.id.flowing_textView);
            price=view.findViewById(R.id.flowing_price_textView);
            image=view.findViewById(R.id.flowing);
            card=view.findViewById(R.id.add_button1);
            purchase=view.findViewById(R.id.shopping_button1);

            //购物车点击
            card.setOnClickListener(v -> {

            });

            //购买点击
            purchase.setOnClickListener(v -> {

            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (flag==1){
            View view= LayoutInflater.from(context).inflate(R.layout.card_layout,parent,false);
            return new CardViewHolder(view);
        }else {
            View view=LayoutInflater.from(context).inflate(R.layout.flowing_layout,parent,false);
            return new FlowingHolder(view);
        }

    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MerchandiseData merchandiseData=merchandiseDataList.get(position);

        if (holder instanceof CardViewHolder){
            //设置内容
            ((CardViewHolder) holder).cName.setText(merchandiseData.getMerchandise());
            ((CardViewHolder) holder).cPrice.setText(merchandiseData.getPrice()+"");
            ((CardViewHolder) holder).cImage.setImageBitmap(merchandiseData.getImage());

            //购物车点击
            ((CardViewHolder) holder).cCard.setOnClickListener(v -> {
                bottomDialog.numberCart(merchandiseData.getId());
            });

            //购买点击
            ((CardViewHolder) holder).cPurchase.setOnClickListener(v -> {
               bottomDialog.purchaseButton(merchandiseData);
            });

        } else if (holder instanceof FlowingHolder) {
            ((FlowingHolder) holder).name.setText(merchandiseData.getMerchandise());
            ((FlowingHolder) holder).price.setText(merchandiseData.getPrice()+"");
            ((FlowingHolder) holder).image.setImageBitmap(merchandiseData.getImage());

            //购买点击
            ((FlowingHolder)holder).purchase.setOnClickListener(v -> {
                bottomDialog.purchaseButton(merchandiseData);
            });

            //购物车点击
            ((FlowingHolder)holder).card.setOnClickListener(v -> bottomDialog.numberCart(merchandiseData.getId()));

        }else {
            throw new IllegalArgumentException();
        }
    }





    @Override
    public int getItemCount() {
        return merchandiseDataList.size();
    }

    //购物车
    public interface CartItem{
        void cartClick();
    }



}
