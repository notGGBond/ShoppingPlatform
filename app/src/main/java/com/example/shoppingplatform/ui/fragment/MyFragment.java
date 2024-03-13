package com.example.shoppingplatform.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppingplatform.R;
import com.example.shoppingplatform.databinding.FragmentMyBinding;
import com.example.shoppingplatform.model.Account;
import com.example.shoppingplatform.room.entity.AccountInformation;
import com.example.shoppingplatform.room.tool.DatabaseManager;
import com.example.shoppingplatform.ui.activity.DispatchActivity;
import com.example.shoppingplatform.ui.activity.LocationActivity;

import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentMyBinding binding;

    private AccountInformation myAccount=new AccountInformation();
    private Handler handler= new Handler(Looper.getMainLooper());


    public MyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyFragment newInstance(String param1, String param2) {
        MyFragment fragment = new MyFragment();
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

  //     myAccount.account="";
        //ViewBinding初始化
        binding = FragmentMyBinding.inflate(inflater, container, false);

        //初始化账号
        initAccount();



        //地址文本的点击事件
        binding.myLocation.setOnClickListener(v -> {
            Intent intent=new Intent(getActivity(), LocationActivity.class);
            startActivity(intent);
        });

        //注册按钮的点击事件
        binding.buttonRegister.setOnClickListener(v -> {
            showDialog(1);
        });

        //登录按钮的点击事件
        binding.buttonLogin.setOnClickListener(v -> {
            showDialog(2);
        });

        //待收货
        binding.textViewWaitShopping.setOnClickListener(v -> {
            Intent intent=new Intent(getActivity(), DispatchActivity.class);
            startActivity(intent);
        });


        return binding.getRoot();
    }


    //初始化账号
    private void initAccount(){
        new Thread(() -> {
           AccountInformation accountInformation=DatabaseManager.getDatabaseManager(getActivity()).getRecently(2);
            handler.post(() -> {
                if (accountInformation!=null){
                    myAccount=accountInformation;
                    binding.textViewMyAccount.setText(myAccount.account);
                    binding.editTextMyArea.setText(myAccount.area);
                    binding.editTextMyEmail.setText(myAccount.email);
                    binding.editTextMyGender.setText(myAccount.gender);
                    binding.editTextMyName.setText(myAccount.name);
                    binding.editTextMyPhone.setText(myAccount.phone);
                }
            });
        }).start();
    }


    @Override
    public void onStop() {
        super.onStop();
        Log.d("stop","stop");

        //结束获取内容并且更新
        myAccount.name=binding.editTextMyName.getText().toString();
        myAccount.gender=binding.editTextMyGender.getText().toString();
        myAccount.area=binding.editTextMyArea.getText().toString();
        myAccount.email=binding.editTextMyEmail.getText().toString();
        myAccount.phone=binding.editTextMyPhone.getText().toString();
            new Thread(() -> {
                DatabaseManager.getDatabaseManager(getActivity()).updateAccount(myAccount);
            }).start();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding=null;
    }

    //账号密码输入框实现
    private void showDialog(int flag) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.login_layout, null);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();

        //标志位
        int mark=flag;

        //初始化
        TextView back=view.findViewById(R.id.back_login_textView);
        TextView error=view.findViewById(R.id.textView_error);
        EditText account=view.findViewById(R.id.editText_account_login);
        EditText password=view.findViewById(R.id.editText_password_login);
        Button sure=view.findViewById(R.id.button_login);

        //退出
        back.setOnClickListener(v -> alertDialog.dismiss());

        //确定点击事件
        sure.setOnClickListener(v -> {
            String sAccount=account.getText().toString();
            String sPassword=password.getText().toString();
            if (sAccount.length()!=6&& !sPassword.equals("")){
                error.setVisibility(View.VISIBLE);
            }else {
                error.setVisibility(View.INVISIBLE);
                handleDialogContent(sAccount,sPassword,mark);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }


    private void handleDialogContent(String account,String password,int mark) {
        // 在这里处理弹窗返回的内容
        //注册的逻辑
        if (mark==1){
            new Thread(() -> {
                String result=DatabaseManager.getDatabaseManager(getActivity()).isAccount(account);

                if (!Objects.equals(result, account)){
                    myAccount.account=account;
                    myAccount.password=password;
                    myAccount.recently=2;
                    String result1= DatabaseManager.getDatabaseManager(getActivity()).getRecentlyId(2);
                    if (result!=null){
                        DatabaseManager.getDatabaseManager(getActivity()).updateRecently(result1,1);
                    }
                    DatabaseManager.getDatabaseManager(getActivity()).addAccount(myAccount);
                }
                handler.post(() -> {
                    if (Objects.equals(result, account)){
                        Toast.makeText(getActivity(),"该账号已存在",Toast.LENGTH_LONG).show();
                    }else {
//                        myAccount.account=account;
//                        myAccount.password=password;
                        binding.textViewMyAccount.setText(account);
                        binding.editTextMyArea.setText("");
                        binding.editTextMyEmail.setText("");
                        binding.editTextMyGender.setText("");
                        binding.editTextMyName.setText("");
                        binding.editTextMyPhone.setText("");


                    }
                });
            }).start();
            //登录的逻辑
        }else {
            new Thread(() -> {
                AccountInformation accountInformation=DatabaseManager.getDatabaseManager(getActivity()).queryAccount(account,password);
              if (accountInformation!=null){
                  String result= DatabaseManager.getDatabaseManager(getActivity()).getRecentlyId(2);

                  if (result!=null){
                      DatabaseManager.getDatabaseManager(getActivity()).updateRecently(result,1);
                  }
              }
               handler.post(() -> {
                   if (accountInformation!=null){
                       myAccount=accountInformation;
                       myAccount.recently=2;
                       binding.textViewMyAccount.setText(myAccount.account);
                       binding.editTextMyArea.setText(myAccount.area);
                       binding.editTextMyEmail.setText(myAccount.email);
                       binding.editTextMyGender.setText(myAccount.gender);
                       binding.editTextMyName.setText(myAccount.name);
                       binding.editTextMyPhone.setText(myAccount.phone);
                   }else {
                       Toast.makeText(getActivity(),"登录失败",Toast.LENGTH_LONG).show();
                   }
               });
            }).start();
        }


    }

}