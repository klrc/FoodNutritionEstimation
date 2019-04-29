package com.example.camera;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends AppCompatActivity {
    private String TAG="Register";
    private String TAG_Account="Account_Register";
    private String TAG_Pwd="Pwd_Register";

    private EditText mAccount;
    private EditText mPwd;
    private EditText mPwdCheck;
    private Button mSureButton;
    private Button mCancelButton;
    private UserDataManager mUserDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        Log.i(TAG, "onCteate()");
        mAccount = (EditText) findViewById(R.id.register_edit_name);
        mPwd = (EditText) findViewById(R.id.register_edit_pwd_old);
        mPwdCheck = (EditText) findViewById(R.id.register_edit_pwd_old);

        mSureButton = (Button) findViewById(R.id.register_btn_sure);
        mCancelButton = (Button) findViewById(R.id.register_btn_cancel);

        mSureButton.setOnClickListener(m_register_listener);
        mCancelButton.setOnClickListener(m_register_listener);

        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            mUserDataManager.openDataBase();
        }

    }

    View.OnClickListener m_register_listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.register_btn_sure:
                    register_check();
                    break;
                case R.id.register_btn_cancel:
                    Intent intent = new Intent(Register.this, Login.class);
                    startActivity(intent);
                    finish();
                    break;
            }
            Log.i(TAG, "OnClickListener()");
        }
    };

    public boolean isUserNameAndPwdValid() {
        Log.i(TAG, "isUserNameAndPwdValid()_start");
        if (mAccount.getText().toString().trim().equals("")) {
            Toast.makeText(this, "账号为空，请重新输入",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (mPwd.getText().toString().trim().equals("")) {
            Toast.makeText(this,"密码为空，请重新输入",
                    Toast.LENGTH_SHORT).show();
            return false;
        }else if(mPwdCheck.getText().toString().trim().equals("")) {
            Toast.makeText(this, "请再次输入密码确认",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        Log.i(TAG, "isUserNameAndPwdValid()_end");
        return true;
    }
    public void register_check() {
        Log.i(TAG, "start");
        if (isUserNameAndPwdValid()) {
            String userName = mAccount.getText().toString().trim();
            String userPwd = mPwd.getText().toString().trim();
            Log.i(TAG_Account,mAccount.getText().toString().trim());
            Log.i(TAG_Pwd,mPwd.getText().toString().trim());

            String userPwdCheck = mPwdCheck.getText().toString().trim();
            int IsExit=mUserDataManager.findUserByName(userName);

            if (IsExit > 0) {
                Toast.makeText(this, "账号已存在，请重新输入", Toast.LENGTH_SHORT).show();
                return;
            }
            if (userPwd.equals(userPwdCheck) == false) {
                Toast.makeText(this, "两次输入密码不同，请重新输入", Toast.LENGTH_SHORT).show();
                return;
            } else {
                UserData mUser = new UserData(userName, userPwd);
                mUserDataManager.openDataBase();
                long flag = mUserDataManager.insertUserData(mUser);
                if (flag == -1) {
                    Toast.makeText(this, "注册失败，请重试", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                    Intent intent_Register_to_Login = new Intent(Register.this, Login.class);
                    startActivity(intent_Register_to_Login);
                    finish();
                }
            }
        }Log.i(TAG, "end");
    }
}



