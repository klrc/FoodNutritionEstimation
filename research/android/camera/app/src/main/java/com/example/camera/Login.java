package com.example.camera;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    private String TAG = "Login.Class";
    public int pwdresetFlag=0;
    private EditText mAccount;//账号
    private EditText mPwd;//密码
    private Button mRegisterButton;//注册
    private Button mLoginButton;//登陆
    private Button mCancelButton;//注销
    private CheckBox mRememberCheck;//记住密码

    private SharedPreferences login_sp;
    private String userNameValue,passwordValue;

    private View loginView;//登陆界面容器
    private View longinSuccessView;
    private TextView loginSuccessShow;
    private TextView mChangepwdText;
    private UserDataManager mUserDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        //找到对应控件
        mAccount = (EditText) findViewById(R.id.login_edit_account);
        mPwd = (EditText) findViewById(R.id.login_edit_pwd);
        mRememberCheck = (CheckBox) findViewById(R.id.login_remember);
        mChangepwdText = (TextView) findViewById(R.id.login_text_change_pwd);
        mLoginButton = (Button) findViewById(R.id.login_btn_login);
        mRegisterButton = (Button) findViewById(R.id.login_btn_register);
        //  mCancelButton=(Button)findViewById(R.id.login_btn_cancel);  //登录界面注销

        loginView = findViewById(R.id.login_view);
        longinSuccessView = findViewById(R.id.login_success_view);
        loginSuccessShow = (TextView) findViewById(R.id.login_success_show);

        login_sp = getSharedPreferences("userInfo", 0);
        String name = login_sp.getString("USER_NAME", "");
        String pwd = login_sp.getString("PASSWORD", "");

        boolean choseRemember = login_sp.getBoolean("mRememberCheck", false);

        if (choseRemember) {
            mAccount.setText(name);
            mPwd.setText(pwd);
            mRememberCheck.setChecked(true);
        }

        ImageView image = (ImageView) findViewById(R.id.logo);
        image.setImageResource(R.drawable.logo);

        mRegisterButton.setOnClickListener(mListener);
        mLoginButton.setOnClickListener(mListener);
        // mCancelButton.setOnClickListener(mListener);
        mChangepwdText.setOnClickListener(mListener);

        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            mUserDataManager.openDataBase();
        }

        Log.i(TAG, "Login_onCreate()");
    }

    OnClickListener mListener=new OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.login_btn_register:
                    Intent intent_Login_to_Register = new Intent(Login.this, Register.class);
                    startActivity(intent_Login_to_Register);
                    finish();
                    break;
                case R.id.login_btn_login:
                    login();
                    break;
                //case R.id.login_btn_cancel:
                //   cancel();
                //    break;
                case R.id.login_text_change_pwd:
                    Intent intent_Login_to_reset = new Intent(Login.this, Resetpwd.class);
                    startActivity(intent_Login_to_reset);
                    finish();
                    break;
            }
            Log.i(TAG, "Login_OnClickListener()");
        }
    };

    public boolean isUserNameAndPwdValid() {

        if (mAccount.getText().toString().trim().equals("")) {
            Toast.makeText(this, "用户不存在，请重新输入",Toast.LENGTH_SHORT).show();
            return false;
        } else if (mPwd.getText().toString().trim().equals("")) {
            Toast.makeText(this, "请输入密码",Toast.LENGTH_SHORT).show();
            return false;
        }
        Log.i(TAG, "Login_isUserNameAndPwdValid()");
        return true;
    }

    public void login() {

        if (isUserNameAndPwdValid()) {
            String userName = mAccount.getText().toString().trim();
            String userPwd = mPwd.getText().toString().trim();
            SharedPreferences.Editor editor = login_sp.edit();////

            int result=mUserDataManager.findUserByNameAndPwd(userName, userPwd);
            if (result == 1) {
                editor.putString("USER_NAME", userName);
                editor.putString("PASSWORD", userPwd);

                if (mRememberCheck.isChecked()) {
                    editor.putBoolean("mRememberCheck", true);
                } else {
                    editor.putBoolean("mRememberCheck", false);
                }
                editor.apply();

                Intent intent = new Intent(Login.this, User.class);
                intent.putExtra("USER_NAME",userName);
                intent.putExtra("PASSWORD", userPwd);
                startActivity(intent);
                finish();
                Toast.makeText(this, "登陆成功", Toast.LENGTH_SHORT).show();
            } else if (result == 0) {
                Toast.makeText(this, "请输入正确的账号和密码", Toast.LENGTH_SHORT).show();
            }

        }
        Log.i(TAG, "Login_login()");
    }

    public void cancel() {

        if (isUserNameAndPwdValid()) {
            String userName = mAccount.getText().toString().trim();
            String userPwd = mPwd.getText().toString().trim();

            int result = mUserDataManager.findUserByNameAndPwd(userName, userPwd);
            if (result == 1) {
                Toast.makeText(this, "注销成功", Toast.LENGTH_SHORT).show();
                mAccount.setText("");
                mPwd.setText("");
                mUserDataManager.deleteUserDataByName(userName);

            } else if (result == 0) {
                Toast.makeText(this, "注销失败！请输入正确的账号和密码", Toast.LENGTH_SHORT).show();
            }
        } Log.i(TAG, "Login_cancel()");
    }

    @Override
    protected void onResume() {

        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            mUserDataManager.openDataBase();
        }
        super.onResume();
        Log.i(TAG, "Login_onResume()");
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        Log.i(TAG, "Login_onDestory()");
    }

    @Override
    protected void onPause() {

        if (mUserDataManager != null) {
            mUserDataManager.closeDataBase();
            mUserDataManager=null;
        }
        super.onPause();
        Log.i(TAG, "Login_onPause()");
    }
}

