package com.example.camera;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.camera.Login;

import java.util.Calendar;
import java.util.List;

public class ChangInfo extends Login {
    private Button change_sure;
    private Button change_back;
    private EditText changed_weight;
    private EditText changed_height;
    private EditText changed_kcal;
    private EditText changed_protein;
    private EditText changed_fats;
    private EditText changed_CHO;
    private UserDataManager mUserDataManger;
    private String TAG = "ChangeInfo";
    private static String DB_PATH = "data/data/com.example.camera/databases/";
    private static String DB_NAME = "user_data";
    private SQLiteDatabase mSQLiteDatabase = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READWRITE);
    Calendar cal = Calendar.getInstance();
    private String change_user;
    private int days=0;
    private float weight=0;
    private float height=0;
    private String sex="";
    private Cursor cursor;
    private String changed_username ="";



    //Login login =new Login();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chang_info);
        Intent intent = getIntent();
        final String loginuser = intent.getStringExtra("user");
       // String user_from_setting = intent2.getStringExtra("user_from_setting");
        if(mUserDataManger == null){
            mUserDataManger = new UserDataManager(this);
            mUserDataManger.openDataBase();
        }
        Log.i(TAG,"修改的用户名:"+loginuser);
        cursor= mSQLiteDatabase.query("users_info",null,"USER_NAME=?",new String[]{loginuser},null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                sex = cursor.getString(cursor.getColumnIndex("Sex")).trim();
                days = Integer.parseInt(cursor.getString(cursor.getColumnIndex("Days")));
            }while (cursor.moveToNext());
        }
        cursor.close();
        change_back = findViewById(R.id.changed_back);
        change_sure = findViewById(R.id.changed_sure);
        changed_weight = findViewById(R.id.changed_weight);
        changed_height = findViewById(R.id.changed_height);
        //changed_kcal = findViewById(R.id.changed_kcal);
        //changed_protein = findViewById(R.id.changed_protein);
        //changed_CHO = findViewById(R.id.changed_CHO);
        //changed_fats = findViewById(R.id.changed_fats);

        final int yearNow = cal.get(Calendar.YEAR);
        final int monthNow = cal.get(Calendar.MONTH)+1;
        final int dayNow = cal.get(Calendar.DAY_OF_MONTH);

        //Log.i(TAG,"user is "+changed_username);



        change_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.i(TAG,"user is "+getUserName());
                String ch_height = changed_height.getText().toString().trim();
                String ch_weight = changed_weight.getText().toString().trim();
                if(checknull(ch_height,ch_weight)==1){
                    Log.i(TAG,"insert sex"+sex);
                    mUserDataManger.insert_userhw(changed_username,Float.parseFloat(ch_weight),Float.parseFloat(ch_height),yearNow+"-"+monthNow+"-"+dayNow);
                    check_bodystatus(changed_username,ch_weight,ch_height,days,sex);
                    Toast.makeText(ChangInfo.this,"修改成功！",Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
                else if(checknull(ch_height,ch_weight)==0){
                    Toast.makeText(ChangInfo.this,"请填写完整！",Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(ChangInfo.this,Setting.class);
                intent.putExtra("user",loginuser);
                startActivity(intent);
                finish();

            }
        });
        change_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ChangInfo.this,Setting.class);
                intent.putExtra("user",loginuser);
                startActivity(intent);
                finish();
            }
        });
    }
    //user_status  -1 小于2岁， -2 太矮  ，0 太瘦 1正常 2超重 3肥胖 4成人消瘦 5 成人正常 6 成人超重 7 成人肥胖
    public void check_bodystatus(String user_name, final String changed_weight, final String changed_height, int user_days, String user_sex){
        float height = Float.parseFloat(changed_height)/100f;
        float changed_bmi = Float.parseFloat(changed_weight)/height/height;
        int userage=user_days/365;
        float calmonth =(user_days-userage*365)/30;
        ContentValues values = new ContentValues();
//        float real_age=0;
//        if(calmonth>=0&&calmonth<=4) real_age=userage;
//        if(calmonth>4&&calmonth<=8) real_age=userage+0.5f;
//        if(calmonth>8&&calmonth<=12) real_age=userage+1;
        int now_days=user_days+90;
        if(now_days<=730){
            Log.i(TAG,"修改后的用户依旧小于2岁");
        }
        if(now_days>730&&now_days<=6750){//2-18岁

            String esex="";
            if (user_sex.equals("男"))
            { Log.i(TAG,"检测为男");
                esex="boy";}
            if(user_sex.equals("女"))
            {esex="girl";}
            Log.i(TAG,"修改后的用户是2-18岁,性别为"+user_sex+"英文"+esex);
                int changed_status = mUserDataManger.new_BMIInfo(esex,now_days,changed_bmi);
                if(changed_status!=-1){
                    Log.i(TAG,"性别换英语"+esex);
                    if(mUserDataManger.check_height(esex,now_days,Float.parseFloat(changed_height))==1){
                        Log.i(TAG,"修改后用户身高依旧低于p10,不给建议");
                        values.put("Weight",changed_weight);
                        values.put("Height",changed_height);
                        mSQLiteDatabase.update("users_info",values,"USER_NAME=?",new String[]{user_name});
                    }
                    if(mUserDataManger.check_height(esex,now_days,Float.parseFloat(changed_height))!=1){
                        if(changed_status==0){
                            Log.i(TAG,"修改后用户的身体状态为消瘦，不给建议");
                            values.put("Weight",changed_weight);
                            values.put("Height",changed_height);
                            mSQLiteDatabase.update("users_info",values,"USER_NAME=?",new String[]{user_name});
                        }
                        if(changed_status==2){
                            Log.i(TAG,"修改后用户的身体状态为超胖，不给建议");
                            values.put("Weight",changed_weight);
                            values.put("Height",changed_height);
                            mSQLiteDatabase.update("users_info",values,"USER_NAME=?",new String[]{user_name});
                        }
                        if(changed_status==3){
                            Log.i(TAG,"修改后用户的身体状态为肥胖，不给建议");
                            values.put("Weight",changed_weight);
                            values.put("Height",changed_height);
                            mSQLiteDatabase.update("users_info",values,"USER_NAME=?",new String[]{user_name});
                        }
                        if(changed_status==1){
                            Log.i(TAG,"修改后用户的身体状态为正常，给建议");
                            values.put("Weight",changed_weight);
                            values.put("Height",changed_height);
                            mSQLiteDatabase.update("users_info",values,"USER_NAME=?",new String[]{user_name});
                            //这里要用ibw_info来修改用户的摄入值了
                        }
                    }

                }

             }
             else if(now_days>6750) {
            Log.i(TAG, "修改后的用户已成年");
            int weight_status = judgebmi(changed_bmi);
            if (weight_status == 4) {
                Log.i(TAG, "修改后的用户成年消瘦");
            }
            if (weight_status == 5) {
                Log.i(TAG, "修改后的用户成年正常");
            }
            if (weight_status == 6) {
                Log.i(TAG, "修改后的用户成年超胖");
            }
            if (weight_status == 7) {
                Log.i(TAG, "修改后的用户成年肥胖");
            }
//            AlertDialog.Builder dialog = new AlertDialog.Builder(ChangInfo.this);
//            dialog.setTitle("该用户已是成年人");
////                                dialog.setMessage("是否已经咨询医生并自定义营养成分摄入量？否则我们将为您设定摄入量");
//            dialog.setMessage("需要选择或重新选择用户的活动水平");
//            dialog.setCancelable(false);
//            dialog.setNegativeButton("确认",
//                    new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Intent intent = new Intent(ChangInfo.this, AdultAttri.class);
//                            intent.putExtra("changed_height",changed_height);
//                            intent.putExtra("changed_weight",changed_weight);
//                            startActivity(intent);
//                            finish();
//                        }
//
//                    });
        }
       // mUserDataManger.BMIInfo()

    }
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
    public int checknull(String ch_height,String ch_weight){
        //String username = "123";

        int status=-1;
        Log.i(TAG, "height = " + changed_height.getText().toString().trim());
        Log.i(TAG, "weight = " + changed_weight.getText().toString().trim());
        if(TextUtils.isEmpty(ch_height)||TextUtils.isEmpty(ch_weight)){
            Log.i(TAG,"修改为空");
            status=0;
        }
        else
            status=1;
//        if(ch_height.equals("")||ch_weight.equals(""))
//        {
//            Log.i(TAG,"修改为空");
//            status=0;}
//        if(ch_height.equals("")&&ch_weight.equals(""))
//        {
//            Log.i(TAG,"修改为空");
//            status=0;}
//        else
//        {status=1;}
//            if (ch_height.isEmpty()==false) {
//                Log.i(TAG, "height = " + changed_height.getText().toString());
//                status=1;
//            }
//            if (ch_weight.isEmpty()==false) {
//                Log.i(TAG, "weight=" + changed_weight.getText().toString());
//                status = 1;}

        Log.i(TAG,"状态"+status);
        return status;

    }
}
