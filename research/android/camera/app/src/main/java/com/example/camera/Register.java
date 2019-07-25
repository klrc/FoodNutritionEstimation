package com.example.camera;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

public class Register extends AppCompatActivity {
    private String TAG="Register";
    private String TAG_Account="Account_Register";
    private String TAG_Pwd="Pwd_Register";

    private EditText mUsername;
    private EditText mWeight;
    private EditText mHeight;
    private EditText mUseryear;
    private EditText mUsermonth;
    private EditText mUserday;
    private EditText mSex;
    private Button mSureButton;
    private Button mCancelButton;
    private UserDataManager mUserDataManager;
    //下拉框
    private Spinner sex_spinner;
    public String choosed_sex;
    private static final String[] sex_list = {"男","女"};
    private ArrayAdapter<String>sex_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        Log.i(TAG, "onCteate()");
        mUsername = (EditText) findViewById(R.id.register_edit_name);
        mWeight = (EditText)findViewById(R.id.user_weight);
        mHeight = (EditText)findViewById(R.id.user_height);
        mUseryear = (EditText)findViewById(R.id.user_year);
        mUsermonth = (EditText)findViewById(R.id.user_month);
        mUserday = (EditText)findViewById(R.id.user_day);
        mSex = (EditText)findViewById(R.id.user_sex);

        mSureButton = (Button) findViewById(R.id.register_btn_sure);
        mCancelButton = (Button) findViewById(R.id.register_btn_cancel);

        mSureButton.setOnClickListener(m_register_listener);
        mCancelButton.setOnClickListener(m_register_listener);

        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            mUserDataManager.openDataBase();
        }
        //下拉框
        sex_spinner = (Spinner)findViewById(R.id.sex_choose);
        sex_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,sex_list);
        sex_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sex_spinner.setOnItemSelectedListener(new SpinnerSexSelectedListener());

        sex_spinner.setAdapter(sex_adapter);
        //sex_spinner.setOnItemClickListener(new SpinnerSexSelectedListener());
        sex_spinner.setVisibility(View.VISIBLE);
        Log.i(TAG,"Register_onCreate()");
    }
    class SpinnerSexSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?>arg0,View arg1,int arg2,long arg3){
            choosed_sex = sex_list[arg2];


        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
    View.OnClickListener m_register_listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.register_btn_sure:
                    if(register_check()){

                    Log.i(TAG,"check finish1");
                    double userWeight = Double.parseDouble(mWeight.getText().toString().trim());
                    Log.i(TAG,"check finish2");
                    double userHeight = Double.parseDouble(mHeight.getText().toString().trim());
//                    String useryear =mUseryear.getText().toString().trim();
//                    String usermonth =mUsermonth.getText().toString().trim();
//                    String userday =mUserday.getText().toString().trim();
                    String sex =mSex.getText().toString().trim();
                    double BMI = userWeight/userHeight/userHeight;


                    //float w = Float.parseFloat(userWeight);
                    //float h = Float.parseFloat(userHeight);
//                    double BMI = userWeight/userHeight/userHeight*10000;
//                    int age = Integer.parseInt(userbirth);
//                    double IBW = calIBW(h,sex);
//                    double kcal = calkcal(IBW);
//                    double fats = calfats(age,kcal);
//                    double CHO = calCHO(age,kcal);
//                    double protein = calprotein(age,kcal);
                    if(BMI>=40){
                        Log.i(TAG,"overfat");
                        overfat(BMI);
                        break;
                    }
                    else if(BMI<=10) {
                        Log.i(TAG,"overthin");
                        overthin(BMI);
                        break;
                    }
                    else{
                        Log.i(TAG,"normal");
                    }
                    Intent intent_Reg_to_Login = new Intent(Register.this, Login.class);
                    startActivity(intent_Reg_to_Login);
                    finish();
                    break;}
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
    public double calIBW(double height,String sex){
        double IBW = 0;

        if(sex == "男"){
            IBW = (height-80)*0.7;
        }
        else {
            IBW = (height-70)*0.6;
        }
        return IBW;}
    public double calkcal(double IBW) {
        double kcal = 0;
        if (IBW <= 10) {
            kcal = 90 * IBW;
        } else if (IBW <= 20) {
            kcal = 900 + 50 * (IBW - 10);
        } else if (IBW > 20) {
            kcal = 1000 + 500 + 20 * (IBW - 20);
        }
        return kcal;
    }
    public double calCHO(int age,double kcal){
        double CHO = 0;
        if(age<4){
            CHO = kcal*0.5;
            //fats = kcal*0.35;
            //protein = kcal*0.15;
        }
        else if (age<18){
            CHO = kcal*0.55;
            //fats = kcal*0.3;
            //protein = kcal*0.15;
        }
        return CHO;
    }
    public double calfats(int age,double kcal){
        double fats = 0;
        if(age<4){
           // CHO = kcal*0.5;
            fats = kcal*0.35;
           // protein = kcal*0.15;
        }
        else if (age<18){
           // CHO = kcal*0.55;
            fats = kcal*0.3;
           // protein = kcal*0.15;
        }
        return fats;
    }
    public double calprotein(int age,double kcal){
        double protein = 0;
        if(age<4){
            //CHO = kcal*0.5;
            //fats = kcal*0.35;
            protein = kcal*0.15;
        }
        else if (age<18){
            //CHO = kcal*0.55;
            //fats = kcal*0.3;
            protein = kcal*0.15;
        }
        return protein;
    }
    public String getAge(String birthTimeString) {
        // 先截取到字符串中的年、月、日
        Log.i(TAG,birthTimeString);
        String strs[] = birthTimeString.trim().split("-");
        int selectYear = Integer.parseInt(strs[0]);
        int selectMonth = Integer.parseInt(strs[1]);
        int selectDay = Integer.parseInt(strs[2]);
        // 得到当前时间的年、月、日
        Calendar cal = Calendar.getInstance();
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayNow = cal.get(Calendar.DATE);

        // 用当前年月日减去生日年月日
        int yearMinus = yearNow - selectYear;
        int monthMinus = monthNow - selectMonth;
        int dayMinus = dayNow - selectDay;
        int days =0;
        int age = yearMinus;// 先大致赋值
        Log.i(TAG,"大致"+age);
        if (yearMinus < 0) {// 选了未来的年份
            age = 0;
        } else if (yearMinus == 0) {// 同年的，要么为1，要么为0
            if (monthMinus < 0) {// 选了未来的月份
                age = 0;
            } else if (monthMinus == 0) {// 同月份的
                if (dayMinus < 0) {// 选了未来的日期
                    age = 0;
                } else if (dayMinus >= 0) {
                    age = 1;
                    days = dayMinus;
                }
            } else if (monthMinus > 0) {
                age = 1;
                days = monthMinus*30+dayMinus;
            }
        } else if (yearMinus > 0) {
            if (monthMinus < 0) {// 当前月>生日月
            } else if (monthMinus == 0) {// 同月份的，再根据日期计算年龄
                if (dayMinus < 0) {
                } else if (dayMinus >= 0) {
                    age = age + 1;
                    days = yearMinus*365+monthMinus*30+dayMinus;
                }
            } else if (monthMinus > 0) {
                age = age + 1;
            }
        }
        Log.i(TAG,"最终"+age);
        Log.i(TAG,"DAYS"+days);
        String str = Integer.toString(age)+"-"+Integer.toString(days);
        return str;
    }
    public boolean isAttriValid() {
        Log.i(TAG, "isAttriValid()_start");
        if (mUsername.getText().toString().trim().equals("")) {
            Log.i(TAG, "isAttriValid()_username");
            Toast.makeText(this, "用户名为空，请重新输入",
                    Toast.LENGTH_SHORT).show();
            Log.i(TAG, "isAttriValid()_username2");
            return false;
        }else if(mWeight.getText().toString().trim().equals("")){
            Log.i(TAG, "isAttriValid()_userweight1");
            Toast.makeText(this,"请输入体重！",Toast.LENGTH_SHORT).show();
            return false;
        }else if(mHeight.getText().toString().trim().equals("")){
            Toast.makeText(this,"请输入身高！",Toast.LENGTH_SHORT).show();
            return false;
        }
// else if(mSex.getText().toString().trim().equals("")){
//            Toast.makeText(this,"请输入性别！",Toast.LENGTH_SHORT).show();
//            return false;}
         else if(mUseryear.getText().toString().trim().equals("")){
            Toast.makeText(this,"请输入出生年！",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(mUsermonth.getText().toString().trim().equals("")){
            Toast.makeText(this,"请输入出生月！",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(mUserday.getText().toString().trim().equals("")){
            Toast.makeText(this,"请输入出生日！",Toast.LENGTH_SHORT).show();
            return false;
        }
        Log.i(TAG, "isAttriValid()_end");
        return true;
    }
    public boolean register_check() {
        Log.i(TAG, "check_start");
        if (isAttriValid()) {
            String userName = mUsername.getText().toString().trim(); //删除空格
            double userWeight = Double.parseDouble(mWeight.getText().toString().trim());
            double userHeight = Double.parseDouble(mHeight.getText().toString().trim());
            //String userBirth = mBirth.getText().toString().trim();
            String userSex = choosed_sex;
            double BMI = userWeight/userHeight/userHeight;
            String useryear =mUseryear.getText().toString().trim();
            String usermonth =mUsermonth.getText().toString().trim();
            String userday =mUserday.getText().toString().trim();
            String birthday = useryear+"-"+usermonth+"-"+userday;
            String agestr = getAge(birthday);
            String[] strage = agestr.split("-");
            int age = Integer.parseInt(strage[0]);
            int days = Integer.parseInt(strage[1]);
            double IBW = calIBW(userHeight,userSex);
            double kcal = calkcal(IBW);
            double fats = calfats(age,kcal);
            double CHO = calCHO(age,kcal);
            double protein = calprotein(age,kcal);


            Log.i(TAG_Account,mUsername.getText().toString().trim());
            //String userPwdCheck = mPwdCheck.getText().toString().trim();
            int IsExist=mUserDataManager.findUserByName(userName);

            if (IsExist > 0) {
                Toast.makeText(this, "用户名已存在，请重新输入", Toast.LENGTH_SHORT).show();
                return false;
            }
//            if (userPwd.equals(userPwdCheck) == false) {
//                Toast.makeText(this, "两次输入密码不同，请重新输入", Toast.LENGTH_SHORT).show();
//                return;
//            } else {
                UserData mUser = new UserData(userName,days,Integer.toString(age

                ),userSex,userWeight,
                        userHeight,BMI,kcal,protein,fats,CHO);
//                        d1kcal,d1CHO,d1protein,d1fats,
//                        d2kcal,d2CHO,d2protein,d2fats,
//                        d3kcal,d3CHO,d3protein,d3fats,
//                        d4kcal,d4CHO,d4protein,d4fats,
//                        d5kcal,d5CHO,d5protein,d5fats,
//                        d6kcal,d6CHO,d6protein,d6fats,
//                        d7kcal,d7CHO,d7protein,d7fats);
                mUserDataManager.openDataBase();
                long flag = mUserDataManager.insertUserData(mUser);
                if (flag == -1) {
                    Toast.makeText(this, "注册失败，请重试", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                    //Intent intent_Register_to_UA = new Intent(Register.this, UserAttr.class);
                    //startActivity(intent_Register_to_UA);
                    //isUnhealthy(BMI);

                    //finish();
                }
                return true;
        }
        Log.i(TAG, "end");
        return false;
    }
    public void overfat(double bmi){
        Log.i(TAG,"ENTER overfat1");
            AlertDialog.Builder dialog = new AlertDialog.Builder(Register.this);
            dialog.setTitle("亲，您有点超重了呢");
            dialog.setMessage("是否已经咨询医生并自定义营养成分摄入量？否则我们将为您设定摄入量");
            dialog.setCancelable(false);
            dialog.setPositiveButton("是的，我要自定义",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Register.this,SetNutrition.class);
                            startActivity(intent);
                            finish();
                        }
                    });
            dialog.setNegativeButton("没有，我听你们的",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Register.this,Login.class);
                            startActivity(intent);
                            return;//finish();
                        }
                    });
            dialog.show();}
    public void overthin(double bmi){
            AlertDialog.Builder dialog = new AlertDialog.Builder(Register.this);
            dialog.setTitle("亲，您有点太瘦了呢");
            dialog.setMessage("是否已经咨询医生并自定义营养成分摄入量？否则我们将为您设定摄入量");
            dialog.setCancelable(false);
            dialog.setPositiveButton("是的，我要自定义",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Register.this,SetNutrition.class);
                            startActivity(intent);
                            finish();
                        }
                    });
            dialog.setNegativeButton("没有，我听你们的",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //calnutri(bmi);
                            Intent intent = new Intent(Register.this,Login.class);
                            startActivity(intent);
                            finish();
                        }
                    });
            dialog.show();
        }
    public boolean check_height(double height,String sex,float age,float days){
        return true;

    }
    public boolean check_health(String sex,float age,double BMI){
            if(age>=2&&age<=5){
                if(sex=="男"){
                    //查表，看BMI属于哪个层次。判断超胖或者消瘦
                }
                else if(sex=="女"){
                    //同上
                }
            }
            else if(age>5&&age<=18){
                if(sex=="男"){
                    //
                }
                else if(sex=="女"){
                    //
                }
            }
        else {//就医
            return false;
              }
        return true;
        }

//    public boolean register_check2() {
//        Log.i(TAG, "check_start");
//        if (isAttriValid()) {
//            String userName = mUsername.getText().toString().trim(); //删除空格
//            double userWeight = Double.parseDouble(mWeight.getText().toString().trim());
//            double userHeight = Double.parseDouble(mHeight.getText().toString().trim());
//            String userBirth = mBirth.getText().toString().trim();
//            String userSex = choosed_sex;
//            String job = "a";
//
//            int age =0;
//            if(check_height(userHeight,userSex,age)){
//                double BMI = userWeight/userHeight/userHeight;
//                if(check_health(userSex,age,BMI)){
//                    //正常人按表查IBW
//                    double kcal = calkcal(IBW);
//                    double fats = calfats(age,kcal);
//                    double CHO = calCHO(age,kcal);
//                    double protein = calprotein(age,kcal);
//                    if(job == ""){
//
//                    }
//                }
//              }
//
//            Log.i(TAG_Account,mUsername.getText().toString().trim());
//            //String userPwdCheck = mPwdCheck.getText().toString().trim();
//            int IsExist=mUserDataManager.findUserByName(userName);
//
//            if (IsExist > 0) {
//                Toast.makeText(this, "用户名已存在，请重新输入", Toast.LENGTH_SHORT).show();
//                return false;
//            }
////            if (userPwd.equals(userPwdCheck) == false) {
////                Toast.makeText(this, "两次输入密码不同，请重新输入", Toast.LENGTH_SHORT).show();
////                return;
////            } else {
//            UserData mUser = new UserData(userName,userBirth,userSex,userWeight,
//                    userHeight,BMI,kcal,protein,fats,CHO);
//            mUserDataManager.openDataBase();
//            long flag = mUserDataManager.insertUserData(mUser);
//            if (flag == -1) {
//                Toast.makeText(this, "注册失败，请重试", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
//                //Intent intent_Register_to_UA = new Intent(Register.this, UserAttr.class);
//                //startActivity(intent_Register_to_UA);
//                //isUnhealthy(BMI);
//
//                //finish();
//            }
//            return true;
//        }
//        Log.i(TAG, "end");
//        return false;
//    }
//    public void isUnhealthy(){
//        double userWeight = Double.parseDouble(mWeight.getText().toString().trim());
//        double userHeight = Double.parseDouble(mHeight.getText().toString().trim());
//        String userBirth = mBirth.getText().toString().trim();
//        String sex =mSex.getText().toString().trim();
//        double BMI = userWeight/userHeight/userHeight*10000;
//        //float w = Float.parseFloat(userWeight);
//        //float h = Float.parseFloat(userHeight);
////                    double BMI = userWeight/userHeight/userHeight*10000;
////                    int age = Integer.parseInt(userbirth);
////                    double IBW = calIBW(h,sex);
////                    double kcal = calkcal(IBW);
////                    double fats = calfats(age,kcal);
////                    double CHO = calCHO(age,kcal);
////                    double protein = calprotein(age,kcal);
//        if(BMI>=40){
//            Log.i(TAG,"overfat");
//            overfat(BMI);
//            break;
//        }
//        else if(BMI<=10) {
//            Log.i(TAG,"normal");
//            overthin(BMI);
//            break;
//        }
//        else{
//            Log.i(TAG,"normal");
//        }
//        Intent intent_Reg_to_Login = new Intent(Register.this, Login.class);
//        startActivity(intent_Reg_to_Login);
//        finish();
//    }

    }




