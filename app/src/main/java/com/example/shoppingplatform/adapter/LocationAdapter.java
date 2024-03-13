package com.example.shoppingplatform.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.shoppingplatform.R;
import com.example.shoppingplatform.model.Information;
import com.example.shoppingplatform.room.entity.Location;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter {
  
    private List<Location> locationList;
    private Context context;
    public LocationClick locationClick;


    
    public LocationAdapter(Context context, List<Location> list){
        this.context=context;
        this.locationList=list;

    }
    
    class LocationViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView location;
        TextView phone;
        LinearLayout layout;
        
        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.location_layout_name);
            location=itemView.findViewById(R.id.location_layout_text);
            phone=itemView.findViewById(R.id.location_layout_phone);
            layout=itemView.findViewById(R.id.location_linearLayout);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.location_layout,parent,false);
        return new LocationViewHolder(view);
    }

   
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Location location=locationList.get(position);
        ((LocationViewHolder)holder).name.setText(location.name);
        ((LocationViewHolder)holder).phone.setText(location.phone);
        ((LocationViewHolder)holder).location.setText(location.location);

        //子项点击选择地址
        ((LocationViewHolder) holder).layout.setOnClickListener(v -> {
            locationClick.getLocation(location);
        });

    }

    public interface LocationClick{
        void getLocation(Location location);
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }
}
