package com.example.camera;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class LoginList extends AppCompatActivity {
    private String TAG = "LoginList.class";
    //下拉框
    private static final String[] user_list = {"syh","sh","lkk"};
    private Spinner user_spinner;
    private ArrayAdapter<String> user_adapter;
    //其他控件
    private Button mRegisterButton;
    private Button mLoginButton;
    private Button mCancelButton;//注销
    private CheckBox mRememberCheck;//记住密码
    private SharedPreferences login_sp;
    private String userNameValue, passwordValue;
    private View loginView;//登陆界面容器
    private View longinSuccessView;
    private TextView loginSuccessShow;
    private TextView mChangepwdText;
    private TextView view;
    private UserDataManager mUserDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        mLoginButton = (Button)findViewById(R.id.login_btn_login);
        mRegisterButton = (Button)findViewById(R.id.login_btn_register);
        loginView = findViewById(R.id.login_view);
        longinSuccessView = findViewById(R.id.login_success_view);
        loginSuccessShow = (TextView) findViewById(R.id.login_success_show);
        login_sp = getSharedPreferences("userInfo", 0);
        String name = login_sp.getString("USER_NAME", "");
        //String pwd = login_sp.getString("PASSWORD", "");
        //boolean choseRemember = login_sp.getBoolean("mRememberCheck", false);
        ImageView image = (ImageView) findViewById(R.id.logo);
        image.setImageResource(R.drawable.logo);
//下拉框
        mRegisterButton.setOnClickListener(mListener);
        mLoginButton.setOnClickListener(mListener);
        view = (TextView) findViewById(R.id.spinnerText);
        user_spinner = (Spinner) findViewById(R.id.user_choose);
        //将可选内容与ArrayAdapter连接起来
        List<String> ul = mUserDataManager.showUserName("USER_NAME");
        String[] user_list2 = new String[ul.size()];
        ul.toArray(user_list2);
        user_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, user_list);

        //设置下拉列表的风格
        user_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //将adapter 添加到spinner中
        user_spinner.setAdapter(user_adapter);

        //添加事件Spinner事件监听
        user_spinner.setOnItemSelectedListener(new LoginList.SpinnerSelectedListener());

        //设置默认值
        user_spinner.setVisibility(View.VISIBLE);

        Log.i(TAG, "Login_onCreate()");

    }
    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            view.setText("您的用户是：");
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }
    View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.login_btn_register:
                    Intent intent_Login_to_Register = new Intent(LoginList.this, Register.class);
                    startActivity(intent_Login_to_Register);
                    finish();
                    break;
                case R.id.login_btn_login:
                    login();
                    break;
            }
            Log.i(TAG, "Login_OnClickListener()");
        }
    };

    public void login() {
        //判断你选的下拉框中的哪个用户


      //  Intent intent = new Intent(LoginList.this, User.class);
//        intent.putExtra("USER_NAME", userName);
//        intent.putExtra("PASSWORD", userPwd);
     //   startActivity(intent);
      //  finish();
     //   Toast.makeText(this, "登陆成功", Toast.LENGTH_SHORT).show();
     //   Log.i(TAG, "Login_login()");
    }
}
