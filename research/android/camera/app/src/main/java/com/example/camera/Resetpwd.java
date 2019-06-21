//package com.example.camera;
//
//import android.content.Intent;
//import android.media.effect.Effect;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.view.ViewDebug;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//public class Resetpwd extends AppCompatActivity {
//    private Button mSure;
//    private Button mCancel;
//    private EditText mAccount;
//    private EditText mPwd_old;
//    private EditText mPwd_new;
//    private EditText mPwd_check;
//    private UserDataManager mUserDataManager;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.resetpwd_layout);
//
//
//        mSure = (Button) findViewById(R.id.resetpwd_btn_sure);
//        mCancel = (Button) findViewById(R.id.resetpwd_btn_cancel);
//        mAccount = (EditText) findViewById(R.id.resetpwd_edit_name);
//        mPwd_old = (EditText) findViewById(R.id.resetpwd_edit_pwd_old);
//        mPwd_new = (EditText) findViewById(R.id.resetpwd_edit_pwd_new);
//        mPwd_check = (EditText) findViewById(R.id.resetpwd_edit_pwd_check);
//
//        mSure.setOnClickListener(mListener);
//        mCancel.setOnClickListener(mListener);
//
//        if (mUserDataManager == null) {
//            mUserDataManager = new UserDataManager(this);
//            mUserDataManager.openDataBase();
//        }
//    }
//
//    View.OnClickListener mListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.resetpwd_btn_sure:
//                    resetpwd_check();
//                    break;
//                case R.id.resetpwd_btn_cancel:
//                    Intent intent = new Intent(Resetpwd.this, Login.class);
//                    startActivity(intent);
//                    finish();
//                    break;
//                default:
//            }
//        }
//    };
//    public boolean isUserNameAndPwdValid() {
//        String username = mAccount.getText().toString().trim();
//        int count = mUserDataManager.findUserByName(username);
//
//        if (count < 0) {
//            Toast.makeText(this, "用户不存在，请重新输入！", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if (mAccount.getText().toString().trim().equals("")) {
//            Toast.makeText(this, "请输入账号！", Toast.LENGTH_SHORT).show();
//            return false;
//        } else if (mPwd_old.getText().toString().trim().equals("")) {
//            Toast.makeText(this, "请输入原密码！", Toast.LENGTH_SHORT).show();
//            return false;
//        } else if (mPwd_new.getText().toString().trim().equals("")) {
//            Toast.makeText(this, "请输入新密码！", Toast.LENGTH_SHORT).show();
//            return false;
//        } else if (mPwd_check.getText().toString().trim().equals("")) {
//            Toast.makeText(this, "请再次输入新密码！", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        return true;
//    }
//    public void resetpwd_check() {
//        if (isUserNameAndPwdValid()) {
//            String userName = mAccount.getText().toString().trim();
//            String userPwd_old = mPwd_old.getText().toString().trim();
//            String userPwd_new = mPwd_new.getText().toString().trim();
//            String userPwd_check = mPwd_check.getText().toString().trim();
//
//            int count = mUserDataManager.findUserByNameAndPwd(userName, userPwd_old);
//            if (count==0) {
//                Toast.makeText(this, "账号密码错误，请重新输入！", Toast.LENGTH_SHORT).show();
//                return ;
//            } else if (count == 1) {
//                if (userPwd_new.equals(userPwd_check) == false) {
//                    Toast.makeText(this, "两次密码输入不同，请重新输入！", Toast.LENGTH_SHORT).show();
//                    return;
//                } else {
//                    UserData mUser = new UserData(userName, userPwd_check);
//                    mUserDataManager.openDataBase();
//                    boolean flag = mUserDataManager.updateUserData(mUser);
//                    Log.i("Change PWD,flag+","111111");
//                    if (flag == false) {
//                        Toast.makeText(this, "修改密码失败，请重新尝试！", Toast.LENGTH_SHORT).show();
//                        return;
//                    } else {
//                        Toast.makeText(this, "修改密码成功！", Toast.LENGTH_SHORT).show();
//                        mUser.pwdresetFlag = 1;
//                        Intent intent = new Intent(Resetpwd.this, Login.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                }
//            }
//
//        }
//    }
//}
//
