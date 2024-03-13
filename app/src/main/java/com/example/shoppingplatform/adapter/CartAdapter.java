package com.example.shoppingplatform.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingplatform.R;
import com.example.shoppingplatform.model.CartInformation;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<CartInformation> cartInformationList;
    private boolean[] click;
    public CheckBoxClick checkBoxClick;

    public CartAdapter(Context context,List<CartInformation> cartInformationList ){
        this.context=context;
        this.cartInformationList=cartInformationList;
        click=new boolean[cartInformationList.size()];
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView price;
        TextView number;
        ImageView imageView;
        CheckBox checkBox;
        public CartViewHolder(@NonNull View view) {
            super(view);
            name=view.findViewById(R.id.textView_cartItem_name);
            price=view.findViewById(R.id.textView_cartItem_price);
            number=view.findViewById(R.id.textView_cartItem_number);
            imageView=view.findViewById(R.id.imageView_cart_item);
            checkBox=view.findViewById(R.id.checkBox);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.cart_item_layout,parent,false);
        return new CartViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CartInformation cartInformation=cartInformationList.get(position);
        ((CartViewHolder)holder).name.setText(cartInformation.getMerchandiseData().getMerchandise());
        ((CartViewHolder)holder).price.setText(cartInformation.getMerchandiseData().getPrice()+"");
        ((CartViewHolder)holder).imageView.setImageBitmap(cartInformation.getMerchandiseData().getImage());
        ((CartViewHolder)holder).number.setText("X"+cartInformation.getNumber());
        ((CartViewHolder)holder).checkBox.setChecked(false);

        //CheckBox点击事件
        ((CartViewHolder)holder).checkBox.setOnClickListener(v -> {
            if (((CartViewHolder)holder).checkBox.isChecked()){
                click[position]=true;
                int price=0;
                for (int i=0;i<click.length;i++){
                    if (click[i]){
                        price+=cartInformationList.get(i).getNumber()*(int)cartInformationList.get(i).getMerchandiseData().getPrice();
                    }
                }
                checkBoxClick.itemClick(click,price);
            }else{
                click[position]=false;
                int price=0;
                for (int i=0;i<click.length;i++){
                    if (click[i]){
                        price+=cartInformationList.get(i).getNumber()*(int)cartInformationList.get(i).getMerchandiseData().getPrice();
                    }
                }
                checkBoxClick.itemClick(click,price);
            }
        });

    }


    public interface CheckBoxClick{
        void itemClick(boolean[] value,int price);
    }

    @Override
    public int getItemCount() {
        return cartInformationList.size();
    }
}

