package com.example.camera;
import android.arch.lifecycle.SingleGeneratedAdapterObserver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.provider.Contacts;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Login extends AppCompatActivity {
    private String TAG = "Login.Class";
    public int pwdresetFlag = 0;
    private EditText mAccount;//账号
    private EditText mPwd;//密码
    private Button mRegisterButton;//注册
 //   private Button mLoginButton;//登陆
    private Button mCancelButton;//注销
    private CheckBox mRememberCheck;//记住密码
    private SharedPreferences login_sp;
    private String userNameValue, passwordValue;
    private View loginView;//登陆界面容器
    private View longinSuccessView;
    private TextView loginSuccessShow;
    private TextView mChangepwdText;
    private UserDataManager mUserDataManager;
    private Bitmap bitmap;
    private String loginuser;


    //下拉框
    private TextView view;
    private Spinner user_spinner;
    public String choosed_user;
    ////
    private String DB_PATH = "data/data/com.example.camera/databases/";//android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+"/";
    private static String DB_NAME ="user_data";
    //recycle布局
    private List<UserInformation> userList= new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.login_layout);
        ArrayAdapter<String> user_adapter;
        ArrayAdapter<UserInformation> user_adapter2;
       // mLoginButton = (Button) findViewById(R.id.login_btn_login);
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
//        mLoginButton.setOnClickListener(mListener);
//        mChangepwdText.setOnClickListener(mListener);
        ////////////////////////////////////////////////////////////////////////////////////
//        UserDataManager.DataBaseManagementHelper helper =new UserDataManager.DataBaseManagementHelper(this);
//        try {
//            helper.createDataBase();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Log.i(TAG,"DATABASE"+DB_PATH+DB_NAME);
        //SQLiteDatabase database = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READWRITE);

       // Cursor cursor = database.query("users_info", null, null, null, null, null, null, null);
      //  while (cursor.moveToNext()) {
      //      String username = cursor.getString(cursor.getColumnIndex("USER_NAME"));
       //     Log.i(TAG,"1111111111111"+ username);
      //  }
////////////////////////////////////////////////////////////////////////////////////////////////////////////

        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            mUserDataManager.openDataBase();
        }


        //下拉框
//        view = (TextView) findViewById(R.id.spinnerText);
//        user_spinner = (Spinner) findViewById(R.id.user_choose);
        //user_spinner.setDropDownWidth(400);
        /////////////////////////////////////////////
        final List<String> ul = mUserDataManager.showUserName("USER_NAME");
        final List<String> user_id_list = mUserDataManager.showUserName("Identity");
        String[] user_list2 = new String[ul.size()];
        ul.toArray(user_list2);

        //////////////////////////////////////////////////

        ///recycle布局
        initUser(ul,user_id_list);
//        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.rec_user);
        final UserAdapter adapter = new UserAdapter(Login.this,R.layout.user_item,userList);
        final ListView listView = (ListView)findViewById(R.id.list_user);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener (new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                UserInformation user = userList.get(position);
                loginuser = user.getName().split("用")[0].trim();

                Intent i = new Intent(Login.this,MainInterface.class);
                i.putExtra("user",loginuser);
                startActivity(i);
                Log.i(TAG,"选择的用户是："+loginuser);

            }
        });
        //listView.setOnItemClickListener(listItemClickListener);
      //  LinearLayoutManager layoutManager = new LinearLayoutManager(this);
      //  recyclerView.setLayoutManager(layoutManager);
        //UserAdapter adapter = new UserAdapter(userList);
      //  recyclerView.setAdapter(adapter);
        //将可选内容与ArrayAdapter连接起来

     //   user_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ul);
        //user_adapter2 = new UserAdapter(Login.this,R.layout.user_item,userList);
        //设置下拉列表的风格
     //   user_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //user_adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将adapter 添加到spinner中
        //user_spinner.setAdapter(user_adapter);
        //user_spinner.setAdapter(user_adapter2);
        //添加事件Spinner事件监听
        //user_spinner.setOnItemSelectedListener(new SpinnerSelectedListener());

        //设置默认值
        //user_spinner.setVisibility(View.VISIBLE);




        Log.i(TAG, "Login_onCreate()");
    }


    //触发点击事件
    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            List<String> ul = mUserDataManager.showUserName("USER_NAME");
            String[] user_list2 = new String[ul.size()];
            ul.toArray(user_list2);
            choosed_user = user_list2[arg2];
            Log.i(TAG,"choosed"+choosed_user);

        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }
    private void initUser(List<String> userlist,List<String> user_id_list){
        Log.i(TAG,"asda"+userlist.toString());

        String id="null";
        for(int i=0;i<userlist.size();i++){
            Log.i(TAG,"用户都有："+userlist.get(i));
            if(user_id_list.get(i).equals("1")){
                id="儿童";
            }
            if(user_id_list.get(i).equals("2")){
                id="成人";
            }
            if(user_id_list.get(i).equals("3")){
                id="老人";
            }
            UserInformation u = new UserInformation(userlist.get(i),R.drawable.login_1_temp_icon,id);
            userList.add(u);
        }
    }




    OnClickListener mListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.login_btn_register:
                    Intent intent_Login_to_Register = new Intent(Login.this, Register.class);
                    startActivity(intent_Login_to_Register);
                    //finish();
                    break;
//                case R.id.login_btn_login:
//                    //String loginuser = login();
//                    mUserDataManager.closeDataBase();
//                    Intent intent_Login_to_MainInterface = new Intent(Login.this, MainInterface.class);
//                    intent_Login_to_MainInterface.putExtra("user",loginuser);
//                    //intent_Login_to_MainInterface.putExtra("roll",100);
//                    Log.i(TAG,"登录用户为"+loginuser);
//                    startActivity(intent_Login_to_MainInterface);
//                    break;
            //    case R.id.user_choose:

                //case R.id.login_btn_cancel:
                //   cancel();
                //    break;
//                case R.id.login_text_change_pwd:
//                    Intent intent_Login_to_reset = new Intent(Login.this, Resetpwd.class);
//                    startActivity(intent_Login_to_reset);
//                    finish();
//                    break;
            }
            Log.i(TAG, "Login_OnClickListener()");
        }
    };

    public boolean isUserNameAndPwdValid() {

        if (mAccount.getText().toString().trim().equals("")) {
            Toast.makeText(this, "用户不存在，请重新输入", Toast.LENGTH_SHORT).show();
            return false;
        } else if (mPwd.getText().toString().trim().equals("")) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        Log.i(TAG, "Login_isUserNameAndPwdValid()");
        return true;
    }

    public String login() {
        Log.i(TAG,choosed_user);
        List<String> userinfo = mUserDataManager.findUserInfo(choosed_user);
        String[] userinfo2 = new String[userinfo.size()];
        userinfo.toArray(userinfo2);
        //String userweight = mUserDataManager.findUserInfo2(choosed_user);

        //Log.i(TAG,userweight);
        Log.i(TAG,userinfo2[0]);
        Log.i(TAG,userinfo2[1]);
        Log.i(TAG,userinfo2[2]);
        Log.i(TAG,"age"+userinfo2[3]);
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
        Log.i(TAG, "Login_login()");
        return choosed_user;
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

