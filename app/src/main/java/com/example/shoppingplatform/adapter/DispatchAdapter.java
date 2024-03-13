package com.example.shoppingplatform.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingplatform.R;
import com.example.shoppingplatform.model.MerchandiseData;
import com.example.shoppingplatform.room.entity.Merchandise;
import com.example.shoppingplatform.ui.tool.CallbackDispatch;

import java.util.List;

public class DispatchAdapter extends RecyclerView.Adapter {

    private List<MerchandiseData> merchandiseList;
    private Context context;
    public CallbackDispatch callbackDispatch;


    public DispatchAdapter(Context context, List<MerchandiseData> merchandiseList){
        this.context=context;
        //this.callbackDispatch=callbackDispatch;
        this.merchandiseList=merchandiseList;
    }

    class DispatchViewHolder extends RecyclerView.ViewHolder{

        TextView delete;
        ImageView imageView;
        TextView name;
        TextView price;

        public DispatchViewHolder(@NonNull View view) {
            super(view);
            delete=view.findViewById(R.id.textView_dispatch_delete);
            imageView=view.findViewById(R.id.imageView_dispatch);
            name=view.findViewById(R.id.textView_dispatch_name);
            price=view.findViewById(R.id.textView_dispatch_price);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.dispatch_layout,parent,false);
        return new DispatchViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MerchandiseData merchandise=merchandiseList.get(position);
        ((DispatchViewHolder)holder).name.setText(merchandise.getMerchandise());
        ((DispatchViewHolder)holder).price.setText("X"+merchandise.getPrice());
        ((DispatchViewHolder)holder).imageView.setImageBitmap(merchandise.getImage());

        //删除订单
        ((DispatchViewHolder)holder).delete.setOnClickListener(v -> {
            callbackDispatch.delete(position);
        });

    }
    @Override
    public int getItemCount() {
        return merchandiseList.size();
    }
}
