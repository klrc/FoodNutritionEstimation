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

public class Register extends AppCompatActivity {
    private String TAG="Register";
    private String TAG_Account="Account_Register";
    private String TAG_Pwd="Pwd_Register";

    private EditText mUsername;
    private EditText mWeight;
    private EditText mHeight;
    private EditText mBirth;
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
        mBirth = (EditText)findViewById(R.id.user_birth);
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
                    String userBirth = mBirth.getText().toString().trim();
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
        //double BMI = weight/height/height*10000;
        double IBW = 0;
//        double kcal = 0;
//        double CHO;
//        double fats;
//        double protein;
//        int age = Integer.parseInt(birth);
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
         else if(mBirth.getText().toString().trim().equals("")){
            Toast.makeText(this,"请输入生日！",Toast.LENGTH_SHORT).show();
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
            String userBirth = mBirth.getText().toString().trim();
            String userSex = choosed_sex;
            double BMI = userWeight/userHeight/userHeight;
            int age = Integer.parseInt(userBirth);
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
                UserData mUser = new UserData(userName,userBirth,userSex,userWeight,
                        userHeight,BMI,kcal,protein,fats,CHO);
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
                            Log.i(TAG,"overfat negative");
                            Intent intent = new Intent(Register.this,Login.class);
                            Log.i(TAG,"overfat negative2");
                            startActivity(intent);
                            Log.i(TAG,"overfat negative3");
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




