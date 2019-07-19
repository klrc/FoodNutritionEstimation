package com.example.camera;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static java.lang.Thread.sleep;

public class UserAttr extends AppCompatActivity {
    private String TAG = "UserAttribute";
    //public int pwdresetFlag = 0;
    private EditText mWeight;
    private EditText mHeight;
    private EditText mBirth;
    private Button mSureButton;
    private Button mCancelButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_attr);
        Log.i(TAG,"onCreate()");
        mWeight = (EditText)findViewById(R.id.user_weight);
        mHeight = (EditText)findViewById(R.id.user_height);
        mBirth = (EditText)findViewById(R.id.user_birth);
        mSureButton = (Button)findViewById(R.id.user_attribute_sure);
        mCancelButton=(Button)findViewById(R.id.user_attribute_cancel);
        mSureButton.setOnClickListener(m_userattr_listener);
        mCancelButton.setOnClickListener(m_userattr_listener);
    }
    View.OnClickListener m_userattr_listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.user_attribute_sure:
                   if(isattributevalid()){
                       String userWeight = mWeight.getText().toString().trim();
                       String userHeight = mHeight.getText().toString().trim();
                       String userbirth = mBirth.getText().toString().trim();
                       float w = Float.parseFloat(userWeight);
                       float h = Float.parseFloat(userHeight);
                       float bmi = calBMI(w,h);
                       Log.i(TAG,"BMI="+bmi);
                       if(bmi>=24){
                           Log.i(TAG,"overfat");
                           overfat(bmi);
                           break;
                       }
                       else {
                           Log.i(TAG,"normal");
                           calnutri(bmi);
                       }
                   }
                   else{
                       return;
                   }
                    Intent intent_UA_to_Login = new Intent(UserAttr.this, Login.class);
                    startActivity(intent_UA_to_Login);
                    finish();
                    break;
                case R.id.user_attribute_cancel:
                    Intent intent = new Intent(UserAttr.this, Login.class);
                    startActivity(intent);
                    finish();
                    break;
            }
            Log.i(TAG, "OnClickListener()");
        }
    };
    public boolean isattributevalid() {
        Log.i(TAG, "start");
        if(mWeight.getText().toString().trim().equals("")){
            Toast.makeText(this,"请输入体重！",Toast.LENGTH_SHORT).show();
            return false;
        }else if(mHeight.getText().toString().trim().equals("")){
            Toast.makeText(this,"请输入身高！",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(mBirth.getText().toString().trim().equals("")){
            Toast.makeText(this,"请输入生日！",Toast.LENGTH_SHORT).show();
            return false;
        }
        Log.i(TAG, "isattrValid()_end");
        return true;

    }
    public float calBMI(float weight,float height){
        float BMI = weight/height/height;
        return BMI;
    }
    public float calnutri(float BMI){
        Toast.makeText(this,"计算各种所需营养成分",Toast.LENGTH_SHORT).show();
        return 0;
    }
    public void overfat(float BMI){
        Log.i(TAG,"ENTER overfat1");
        final float bmi = BMI;
        AlertDialog.Builder dialog = new AlertDialog.Builder(UserAttr.this);
        dialog.setTitle("亲，您有点超重了呢");
        dialog.setMessage("是否已经咨询医生并自定义营养成分摄入量？否则我们将为您设定摄入量");
        dialog.setCancelable(false);
        dialog.setPositiveButton("是的，我要自定义",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(UserAttr.this,SetNutrition.class);
                        startActivity(intent);
                        finish();
                    }
                });
        dialog.setNegativeButton("没有，我听你们的",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        calnutri(bmi);
                        Intent intent = new Intent(UserAttr.this,Login.class);
                        startActivity(intent);
                        finish();
                    }
                });
        dialog.show();

    }
}
