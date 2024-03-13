package com.example.shoppingplatform.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.shoppingplatform.R;
import com.example.shoppingplatform.databinding.ActivityConstellationBinding;
import com.example.shoppingplatform.model.ConstellationData;
import com.example.shoppingplatform.network.ConstellationService;
import com.example.shoppingplatform.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConstellationActivity extends AppCompatActivity {

    private ActivityConstellationBinding binding;
    private  String key="922302cdc7c5e435f99c16e86963bd5d";
    private  String type = "today";
    private Handler handler=new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityConstellationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //点击查找获取数据
        binding.buttonConstellation.setOnClickListener(v -> {
            //测试权限和网络
            boolean b1=isNetworkAvailable();
            Log.d("b1",b1+"");
            boolean b2=checkInternetPermissionDeclared();
            Log.d("b2",b2+"");

            String constellation=binding.editTextConstellation.getText().toString();

            if (!constellation.isEmpty()){
                ConstellationService constellationService= RetrofitClient.getService();
               constellationService.getConstellationData(key,constellation,type).enqueue(new Callback<ConstellationData>() {
                    @Override
                    public void onResponse(Call<ConstellationData> call, Response<ConstellationData> response) {
                        if(response.isSuccessful()){
                            ConstellationData constellationData=response.body();
                            handler.post(() -> {
                                //设置数据
                                if (constellationData!=null){
                                    updateView(constellationData);
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<ConstellationData> call, Throwable t) {
                        t.printStackTrace();
                        handler.post(() -> Toast.makeText(ConstellationActivity.this,"查找失败",Toast.LENGTH_SHORT).show());
                    }
                });
            }

        });


    }

    //网络
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
        return false;
    }

    //权限
    public boolean checkInternetPermissionDeclared() {
        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] declaredPermissions = packageInfo.requestedPermissions;
            if (declaredPermissions != null) {
                for (String permission : declaredPermissions) {
                    if (Manifest.permission.INTERNET.equals(permission)) {
                        return true; // 找到了INTERNET权限
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false; // 没有找到INTERNET权限
    }

    //更新界面
    private void updateView(ConstellationData constellationData){

        switch (constellationData.getXName()){
            case "水瓶座":binding.imageConstellation.setImageResource(R.drawable.c1);break;
            case "双鱼座":binding.imageConstellation.setImageResource(R.drawable.c2);break;
            case "白羊座":binding.imageConstellation.setImageResource(R.drawable.c3);break;
            case "金牛座":binding.imageConstellation.setImageResource(R.drawable.c4);break;
            case "双子座":binding.imageConstellation.setImageResource(R.drawable.c5);break;
            case "巨蟹座":binding.imageConstellation.setImageResource(R.drawable.c6);break;
            case "狮子座":binding.imageConstellation.setImageResource(R.drawable.c7);break;
            case "处女座":binding.imageConstellation.setImageResource(R.drawable.c8);break;
            case "天秤座":binding.imageConstellation.setImageResource(R.drawable.c9);break;
            case "天蝎座":binding.imageConstellation.setImageResource(R.drawable.c10);break;
            case "射手座":binding.imageConstellation.setImageResource(R.drawable.c11);break;
            case "摩羯座":binding.imageConstellation.setImageResource(R.drawable.c12);break;
        }

        binding.textViewDate.setText(constellationData.getDatetime());
        binding.textViewConstellation.setText(constellationData.getXName());
        binding.textViewConstellation1.setText(constellationData.getAll());
        binding.textViewConstellation2.setText(constellationData.getHealth());
        binding.textViewConstellation3.setText(constellationData.getLove());
        binding.textViewConstellation4.setText(constellationData.getMoney());
        binding.textViewConstellation5.setText(constellationData.getWork());
        binding.textViewConstellation6.setText(constellationData.getXNumber()+"");
        binding.textViewConstellation7.setText(constellationData.getXColor());
        binding.textViewConstellation8.setText(constellationData.getFriend());
        binding.textViewConstellation9.setText(constellationData.getSummary());
    }
}