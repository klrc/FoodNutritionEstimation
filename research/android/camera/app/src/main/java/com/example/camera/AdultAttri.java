package com.example.camera;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.Calendar;

public class AdultAttri extends AppCompatActivity {
    private RadioButton mLightButton;
    private RadioButton mMidButton;
    private RadioButton mHeavyButton;
    private Button mSureButton;
    private Button mCancelButton;
    private String TAG = "AdultAttri";
    private int status = 0;
    private int weight_status = 0;
    private double nutri = 0;


    private double CHO=0;
    private double fats=0;
    private double protein = 0;
    private UserDataManager mUserDataManager;
    Calendar cal = Calendar.getInstance();
    int yearNow = cal.get(Calendar.YEAR);
    int monthNow = cal.get(Calendar.MONTH)+1;
    int dayNow = cal.get(Calendar.DAY_OF_MONTH);
    final String Reg_time = yearNow+"-"+monthNow+"-"+dayNow;
    double userweight = 0;
    double bmi = 0;
    String userName = "";
    String userheight = "";
    int days =0;
    String sex ="";
    int identity=-2;
    int age = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adult_attri);
        getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //虚拟键盘也透明
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        mLightButton = (RadioButton)findViewById(R.id.light_power);
        mMidButton = (RadioButton)findViewById(R.id.mid_power);
        mHeavyButton = (RadioButton)findViewById(R.id.heavy_power);
        mSureButton = (Button)findViewById(R.id.adult_attri_sure);
        mCancelButton = (Button)findViewById(R.id.adult_attri_cancel);
        mLightButton.setOnClickListener(mListener);
        mMidButton.setOnClickListener(mListener);
        mHeavyButton.setOnClickListener(mListener);
        mSureButton.setOnClickListener(mListener);
        mCancelButton.setOnClickListener(mListener);

        Intent intent = getIntent();
        userweight = intent.getDoubleExtra("user_weight",0.00);
        bmi = intent.getDoubleExtra("user_bmi",0.00);
        userName = intent.getStringExtra("user_name");
        userheight = intent.getStringExtra("user_height");
        days = intent.getIntExtra("user_days",0);
        sex = intent.getStringExtra("user_sex");
        age = intent.getIntExtra("user_age",0);
        identity = intent.getIntExtra("user_identity",-3);


        Log.i(TAG,"gao"+userheight);




        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            mUserDataManager.openDataBase();
        }


        //status1轻体力 2中体力 3 重体力
        //weight_status
        weight_status=judgebmi(bmi);
        Log.i(TAG,"weight status"+weight_status+"+"+status);

//        if(status==1){
//             if(weight_status==1)
//                 nutri = 35*userweight;
//             if(weight_status==2)
//                 nutri = 30*userweight;
//             if(weight_status==3)
//                 nutri = 25*userweight;
//        }
//        else if(status==2){
//            if(weight_status==1)
//                nutri = 40*userweight;
//            if(weight_status==2)
//                nutri = 35*userweight;
//            if(weight_status==3)
//                nutri = 30*userweight;
//        }
//        else if(status==3){
//            if(weight_status==1)
//                nutri = 50*userweight;
//            if(weight_status==2)
//                nutri = 40*userweight;
//            if(weight_status==3)
//                nutri = 35*userweight;
//        }
//        Log.i(TAG,"NUTRI"+nutri);
//        CHO = nutri*0.55;
//        protein = nutri*0.15;
//        fats = nutri*0.3;
//        mUserDataManager.openDataBase();
//        Log.i(TAG,String.valueOf(userweight)+"+"+String.valueOf(userheight)+"+"+String.valueOf(nutri)+
//                "+"+String.valueOf(CHO)+"+"+protein+fats);
//        UserData mUser = new UserData(userName,days,Integer.toString(age),sex,userweight,userheight,bmi,nutri,protein,fats,CHO);

    }
    View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.light_power:
                    status = 1;
                    Log.i(TAG,"SSS1");
                    break;
                case R.id.mid_power:
                    status = 2;
                    Log.i(TAG,"SSS2");
                    break;
                case R.id.heavy_power:
                    status = 3;
                    Log.i(TAG,"SSS3");
                    break;
                case R.id.adult_attri_sure:
                    if(status==0){
                        Toast.makeText(AdultAttri.this, "请选择活动水平！", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if(status==1){
                        if(weight_status==4)
                            nutri = 35*userweight;
                        if(weight_status==5)
                            nutri = 30*userweight;
                        if(weight_status==6||weight_status==7)
                            nutri = 25*userweight;
                    }
                    else if(status==2){
                        if(weight_status==4)
                            nutri = 40*userweight;
                        if(weight_status==5)
                            nutri = 35*userweight;
                        if(weight_status==6||weight_status==7)
                            nutri = 30*userweight;
                    }
                    else if(status==3){
                        if(weight_status==4)
                            nutri = 50*userweight;
                        if(weight_status==5)
                            nutri = 40*userweight;
                        if(weight_status==6||weight_status==7)
                            nutri = 35*userweight;
                    }
                    Log.i(TAG,"NUTRI"+nutri);
                    CHO = nutri*0.55;
                    protein = nutri*0.15;
                    fats = nutri*0.3;
                    mUserDataManager.openDataBase();
                    Log.i(TAG,String.valueOf(userweight)+"+"+String.valueOf(userheight)+"+"+String.valueOf(nutri)+
                            "+"+String.valueOf(CHO)+"+"+protein+fats);
                    UserData mUser = new UserData(userName,days,Integer.toString(age),sex,userweight,Double.parseDouble(userheight),bmi,String.valueOf(identity),String.valueOf(weight_status),nutri,protein,fats,CHO
                    ,0,0,0,0,
                            0,0,0,0,
                            0,0,0,0,
                            0,0,0,0,
                            0,0,0,0,
                            0,0,0,0,
                            0,0,0,0);
                    mUserDataManager.openDataBase();
                    long flag = mUserDataManager.insertUserData(mUser);
                    if (flag == -1) {
                        Toast.makeText(AdultAttri.this, "注册失败，请重试", Toast.LENGTH_SHORT).show();
                    } else {
                        long flag2 =  mUserDataManager.insert_userhw(userName,(float)userweight,Float.parseFloat(userheight),Reg_time);
                        if (flag2==-1){
                            Log.i(TAG,"失败了~~~");
                        }
                        else
                        {Log.i(TAG,"成功了~~~");}
                        Toast.makeText(AdultAttri.this, "注册成功", Toast.LENGTH_SHORT).show();
                    }
                    Intent intent_adultattri_to_login = new Intent(AdultAttri.this,Login.class);
                    intent_adultattri_to_login.putExtra("user_status",weight_status);
                    intent_adultattri_to_login.putExtra("user_days",days);
                    startActivity(intent_adultattri_to_login);
                    finish();
                    break;
                case R.id.adult_attri_cancel:
                    onBackPressed();
//                    Intent intent_adultattri_to_register = new Intent(AdultAttri.this,Register.class);
//                    startActivity(intent_adultattri_to_register);
//                   // finish();
                    break;
            }

        }
    };
    //1过低2正常3超重4肥胖
    public int judgebmi(double bmi){
        int weight_status2=0;
        if(bmi<=18.5)
            weight_status2 = 4;
        if(bmi>18.5&&bmi<24)
            weight_status2 = 5;
        if(bmi>=24&&bmi<28)
            weight_status2 = 6;
        if(bmi>=28)
            weight_status2 = 7;
        return  weight_status2;
    }


}
