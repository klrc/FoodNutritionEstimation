package com.example.camera;
import android.content.Intent;
import android.content.SharedPreferences;
<<<<<<< HEAD
=======
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
>>>>>>> refs/remotes/origin/master
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

<<<<<<< HEAD
public class Login extends AppCompatActivity {
    private String TAG = "Login.Class";
    public int pwdresetFlag=0;
=======
import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {
    private String TAG = "Login.Class";
    public int pwdresetFlag = 0;
>>>>>>> refs/remotes/origin/master
    private EditText mAccount;//账号
    private EditText mPwd;//密码
    private Button mRegisterButton;//注册
    private Button mLoginButton;//登陆
    private Button mCancelButton;//注销
    private CheckBox mRememberCheck;//记住密码
    private SharedPreferences login_sp;
<<<<<<< HEAD
    private String userNameValue,passwordValue;
=======
    private String userNameValue, passwordValue;
>>>>>>> refs/remotes/origin/master
    private View loginView;//登陆界面容器
    private View longinSuccessView;
    private TextView loginSuccessShow;
    private TextView mChangepwdText;
    private UserDataManager mUserDataManager;
<<<<<<< HEAD
    //下拉框

//    private static final String[] user_list={"syh","sh","lkk"};
//    private TextView user_view;
//    private Spinner user_choose;
//    private ArrayAdapter<String> user_adapter;
    private static final String[] user_list={"syh","sh","lkk"};
    private TextView view ;
    private Spinner user_spinner;
    private ArrayAdapter<String> user_adapter;

=======

    //下拉框
    private TextView view;
    private Spinner user_spinner;
    public String choosed_user;
>>>>>>> refs/remotes/origin/master
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
<<<<<<< HEAD
        //找到对应控件
        mAccount = (EditText) findViewById(R.id.login_edit_account);
        mPwd = (EditText) findViewById(R.id.login_edit_pwd);
        mRememberCheck = (CheckBox) findViewById(R.id.login_remember);
        mChangepwdText = (TextView) findViewById(R.id.login_text_change_pwd);
=======
        ArrayAdapter<String> user_adapter;
>>>>>>> refs/remotes/origin/master
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
<<<<<<< HEAD
        mChangepwdText.setOnClickListener(mListener);
=======
//        mChangepwdText.setOnClickListener(mListener);
>>>>>>> refs/remotes/origin/master

        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            mUserDataManager.openDataBase();
        }
<<<<<<< HEAD
        //下拉框
//        user_view = (TextView)findViewById(R.id.user_text);
//        user_choose = (Spinner) findViewById(R.id.user_choose);

//        user_adapter = new ArrayAdapter<String>(this,R.layout.login_layout,user_list);
//        user_adapter.setDropDownViewResource(R.layout.login_layout);
//        user_choose.setAdapter(user_adapter);
//        user_choose.setOnItemSelectedListener(new SpinnerSelectedListener());
//        user_choose.setVisibility(View.VISIBLE);
        view = (TextView) findViewById(R.id.spinnerText);
        user_spinner = (Spinner) findViewById(R.id.user_choose);
        //将可选内容与ArrayAdapter连接起来
        user_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,user_list);
=======


        //下拉框
        view = (TextView) findViewById(R.id.spinnerText);
        user_spinner = (Spinner) findViewById(R.id.user_choose);
        /////////////////////////////////////////////
        List<String> ul = mUserDataManager.showUserName("USER_NAME");
        String[] user_list2 = new String[ul.size()];
        ul.toArray(user_list2);
        //////////////////////////////////////////////////
        Log.i(TAG,"111");
//        Log.i(TAG,user_list2[2]);
        //将可选内容与ArrayAdapter连接起来

        user_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ul);
>>>>>>> refs/remotes/origin/master

        //设置下拉列表的风格
        user_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //将adapter 添加到spinner中
        user_spinner.setAdapter(user_adapter);

        //添加事件Spinner事件监听
        user_spinner.setOnItemSelectedListener(new SpinnerSelectedListener());

        //设置默认值
        user_spinner.setVisibility(View.VISIBLE);


        Log.i(TAG, "Login_onCreate()");
<<<<<<< HEAD
}
=======
    }


    //触发点击事件
>>>>>>> refs/remotes/origin/master
    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
<<<<<<< HEAD
            view.setText("你的用户是："+user_list[arg2]);
=======
            List<String> ul = mUserDataManager.showUserName("USER_NAME");
            String[] user_list2 = new String[ul.size()];
            ul.toArray(user_list2);
            choosed_user = user_list2[arg2];
            Log.i(TAG,user_list2[arg2]);
            //Log.i(TAG,user_list2[1]);ul[arg2]
            //view.setText("你的用户是：");
            //Log.i(TAG,"user_list2");
>>>>>>> refs/remotes/origin/master
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }


<<<<<<< HEAD
    OnClickListener mListener=new OnClickListener() {
=======
    OnClickListener mListener = new OnClickListener() {
>>>>>>> refs/remotes/origin/master
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
<<<<<<< HEAD
                    break;
                //case R.id.login_btn_cancel:
                //   cancel();
                //    break;
                case R.id.login_text_change_pwd:
                    Intent intent_Login_to_reset = new Intent(Login.this, Resetpwd.class);
                    startActivity(intent_Login_to_reset);
                    finish();
                    break;
=======
                    Intent intent_Login_to_MainInterface = new Intent(Login.this, MainInterface.class);
                    startActivity(intent_Login_to_MainInterface);
                    break;
                case R.id.user_choose:

                //case R.id.login_btn_cancel:
                //   cancel();
                //    break;
//                case R.id.login_text_change_pwd:
//                    Intent intent_Login_to_reset = new Intent(Login.this, Resetpwd.class);
//                    startActivity(intent_Login_to_reset);
//                    finish();
//                    break;
>>>>>>> refs/remotes/origin/master
            }
            Log.i(TAG, "Login_OnClickListener()");
        }
    };

    public boolean isUserNameAndPwdValid() {

        if (mAccount.getText().toString().trim().equals("")) {
<<<<<<< HEAD
            Toast.makeText(this, "用户不存在，请重新输入",Toast.LENGTH_SHORT).show();
            return false;
        } else if (mPwd.getText().toString().trim().equals("")) {
            Toast.makeText(this, "请输入密码",Toast.LENGTH_SHORT).show();
=======
            Toast.makeText(this, "用户不存在，请重新输入", Toast.LENGTH_SHORT).show();
            return false;
        } else if (mPwd.getText().toString().trim().equals("")) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
>>>>>>> refs/remotes/origin/master
            return false;
        }
        Log.i(TAG, "Login_isUserNameAndPwdValid()");
        return true;
    }

    public void login() {
<<<<<<< HEAD

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
=======
        Log.i(TAG,choosed_user);
        List<String> userinfo = mUserDataManager.findUserInfo(choosed_user);
        String[] userinfo2 = new String[userinfo.size()];
        userinfo.toArray(userinfo2);
        //String userweight = mUserDataManager.findUserInfo2(choosed_user);

        //Log.i(TAG,userweight);
        Log.i(TAG,userinfo2[0]);
        Log.i(TAG,userinfo2[1]);
        Log.i(TAG,userinfo2[2]);
        Log.i(TAG,userinfo2[3]);
        Log.i(TAG,userinfo2[4]);
        Log.i(TAG,userinfo2[5]);
        Log.i(TAG,userinfo2[6]);
                //mDatabaseHelper = new UserDataManager.DataBaseManagementHelper(mContext);
        //mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();

//        if (true){    //isUserNameAndPwdValid()) {
//            String userName = mAccount.getText().toString().trim();
//            String userPwd = mPwd.getText().toString().trim();
//            SharedPreferences.Editor editor = login_sp.edit();
//
//            int result = mUserDataManager.findUserByNameAndPwd(userName, userPwd);
//            if (result == 1) {
//                editor.putString("USER_NAME", userName);
//                editor.putString("PASSWORD", userPwd);
//
//                if (mRememberCheck.isChecked()) {
//                    editor.putBoolean("mRememberCheck", true);
//                } else {
//                    editor.putBoolean("mRememberCheck", false);
//                }
//                editor.apply();
//
//                Intent intent = new Intent(Login.this, User.class);
//                intent.putExtra("USER_NAME", userName);
//                intent.putExtra("PASSWORD", userPwd);
//                startActivity(intent);
//                finish();
//                Toast.makeText(this, "登陆成功", Toast.LENGTH_SHORT).show();
//            } else if (result == 0) {
//                Toast.makeText(this, "请输入正确的账号和密码", Toast.LENGTH_SHORT).show();
//            }
//
//        }
>>>>>>> refs/remotes/origin/master
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

