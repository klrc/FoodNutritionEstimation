package com.example.camera;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
    private RadioButton mChild;
    private RadioButton mAdult;
    private RadioButton mElder;
    private UserDataManager mUserDataManager;
    private int identity=0;
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
        mChild = (RadioButton) findViewById(R.id.child);
        mAdult = (RadioButton) findViewById(R.id.adult);
        mElder = (RadioButton) findViewById(R.id.elder);
        mChild.setOnClickListener(mListener);
        mAdult.setOnClickListener(mListener);
        mElder.setOnClickListener(mListener);

        //mSex = (EditText)findViewById(R.id.user_sex);

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

    View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.child:
                    identity=1;
                    Log.i(TAG,"身份："+identity);
                    break;
                case R.id.adult:
                    identity=2;
                    Log.i(TAG,"身份："+identity);
                    break;
                case R.id.elder:
                    identity=3;
                    Log.i(TAG,"身份："+identity);
                    break;
            }
        }
    };
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

                    if(register_check()) {
                        if(identity==0){
                            Toast.makeText(Register.this,"请选择用户身份",Toast.LENGTH_SHORT).show();
                            break;
                        }
                        Log.i(TAG,"用户身份："+identity);
//                    if(mUserDataManager.is_num_enough(String.valueOf(identity))==false)
//                    {
//                        Toast.makeText(Register.this,"该身份用户数量已达上限，无法注册！",Toast.LENGTH_SHORT).show();
//                        break;
//                    }
                        final double userWeight = Double.parseDouble(mWeight.getText().toString().trim());
                        Log.i(TAG, "check finish");
                        final double userHeight = Double.parseDouble(mHeight.getText().toString().trim()) / 100f;
                        Log.i(TAG,"小数身高"+userHeight);
                        String sex = "";//mSex.getText().toString().trim();
                        final double BMI = userWeight / userHeight / userHeight;
                        Log.i(TAG,"BMI呀"+BMI);
                        final String userName = mUsername.getText().toString().trim();
                        String useryear = mUseryear.getText().toString().trim();
                        String usermonth = mUsermonth.getText().toString().trim();
                        String userday = mUserday.getText().toString().trim();
                        final String userheight = mHeight.getText().toString().trim();
                        String birthday = useryear + "-" + usermonth + "-" + userday;
                        String agestr = getAge(birthday);
                        String[] strage = agestr.split("-");
                        final int age = Integer.parseInt(strage[0]);
                        final int days = Integer.parseInt(strage[1]);
                        final float height = Float.parseFloat(userheight);
                        Calendar cal = Calendar.getInstance();
                        int yearNow = cal.get(Calendar.YEAR);
                        int monthNow = cal.get(Calendar.MONTH)+1;
                        int dayNow = cal.get(Calendar.DAY_OF_MONTH);
                        String comp_monthNow =String.valueOf(monthNow);
                        String comp_dayNow=String.valueOf(dayNow);
                        if(String.valueOf(monthNow).length()==1){
                            comp_monthNow = "0"+monthNow;
                        }
                        if(String.valueOf(dayNow).length()==1){
                            comp_dayNow="0"+dayNow;
                        }
                        final String Reg_time = yearNow+"-"+comp_monthNow+"-"+comp_dayNow;
                        int userage=days/365;
                        float calmonth =(days-userage*365)/30;
                        float real_age=0;
                        if(calmonth>=0&&calmonth<=4) real_age=userage;
                        if(calmonth>4&&calmonth<=8) real_age=userage+0.5f;
                        if(calmonth>8&&calmonth<=12) real_age=userage+1;

                        if(choosed_sex=="男"){
                            sex = "boy";
                        }
                        if(choosed_sex=="女"){
                            sex = "girl";
                        }


                        //float age = days/365;
                        Log.i(TAG, "性别" + sex);
                        Log.i(TAG, "age" + age);
                        Log.i(TAG, "days" + days);
                        Log.i(TAG, "STRTT" + agestr);
                       if(real_age<=2){
                           AlertDialog.Builder dialog = new AlertDialog.Builder(Register.this);
                           dialog.setTitle("该用户年纪过小");
                           dialog.setMessage("本APP将不提供建议营养成分摄入量");
                           dialog.setCancelable(false);
                           dialog.setNegativeButton("确认",
                                   new DialogInterface.OnClickListener() {
                                       @Override
                                       public void onClick(DialogInterface dialog, int which) {
                                           Log.i(TAG,"小年纪："+identity);
                                           UserData mUser = new UserData(userName, days, Integer.toString(age), choosed_sex, userWeight,
                                                   height,BMI, String.valueOf(identity),String.valueOf(-1),0, 0, 0, 0,
                                                   0, 0, 0, 0,
                                                   0, 0, 0, 0,
                                                   0, 0, 0, 0,
                                                   0, 0, 0, 0,
                                                   0, 0, 0, 0,
                                                   0, 0, 0, 0,
                                                   0, 0, 0, 0);
                                           mUserDataManager.openDataBase();
                                           long flag = mUserDataManager.insertUserData(mUser);
                                           if (flag == -1) {
                                               Toast.makeText(Register.this, "注册失败，请重试", Toast.LENGTH_SHORT).show();
                                           } else {

                                               long flag2 =  mUserDataManager.insert_userhw(userName,(float)userWeight,height,Reg_time);
                                               if (flag2==-1){
                                                   Log.i(TAG,"失败了~~~");
                                               }
                                               else
                                               {Log.i(TAG,"成功了~~~");}
                                               Toast.makeText(Register.this, "注册成功", Toast.LENGTH_SHORT).show();
                                           }
                                           Intent intent = new Intent(Register.this, Login.class);
                                           intent.putExtra("user_status",-1);
                                           intent.putExtra("user_sex",choosed_sex);
                                           intent.putExtra("user_days",days);
                                           startActivity(intent);
                                           finish();
                                       }
                                   });
                           dialog.show();
                       }

                        //new_status=0消瘦 1正常 2超胖 3肥胖
                        if(real_age>=2&&real_age<=18) {
                            final int new_status = mUserDataManager.new_BMIInfo(sex, days, BMI);
                            Log.i(TAG, "新的status" + new_status);
                            float ibw = mUserDataManager.IBWInfo(sex, age, days, height);
                            final double kcal =calkcal(ibw);
                            final double fats = calfats(age, kcal);
                            final double CHO = calCHO(age, kcal);
                            final double protein = calprotein(age, kcal);


                            if (new_status != -1) {


                                Log.i(TAG, "用户名："+userName +"用户日子："+ days +","+kcal+","+CHO+","+kcal+","+fats);
                                if (mUserDataManager.check_height(sex, days, height) == 1) {
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(Register.this);
                                    dialog.setTitle("该用户身高低于该年龄段10%");
                                    dialog.setMessage("本APP将不提供建议营养成分摄入量，建议就医");
                                    dialog.setCancelable(false);
                                    dialog.setNegativeButton("确认",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    UserData mUser = new UserData(userName, days, Integer.toString(age), choosed_sex, userWeight,
                                                            height, BMI,String.valueOf(identity),String.valueOf(new_status),0, 0, 0, 0,
                                                            0, 0, 0, 0,
                                                            0, 0, 0, 0,
                                                            0, 0, 0, 0,
                                                            0, 0, 0, 0,
                                                            0, 0, 0, 0,
                                                            0, 0, 0, 0,
                                                            0, 0, 0, 0);
                                                    mUserDataManager.openDataBase();
                                                    long flag = mUserDataManager.insertUserData(mUser);
                                                    if (flag == -1) {
                                                        Toast.makeText(Register.this, "注册失败，请重试", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        long flag2 = mUserDataManager.insert_userhw(userName, (float) userWeight, height, Reg_time);
                                                        if (flag2 == -1) {
                                                            Log.i(TAG, "失败了~~~");
                                                        } else {
                                                            Log.i(TAG, "成功了~~~");
                                                        }
                                                        Toast.makeText(Register.this, "注册成功", Toast.LENGTH_SHORT).show();
                                                    }
                                                    Intent intent = new Intent(Register.this, Login.class);
                                                    intent.putExtra("user_status",-2);
                                                    intent.putExtra("user_sex",choosed_sex);
                                                    intent.putExtra("user_days",days);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            });
//                            dialog.setPositiveButton("已咨询医生，自定义营养成分摄入值",
//                                    new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            askdr[0]=2;
//                                            Intent intent = new Intent(Register.this,SetNutrition.class);
//                                            startActivity(intent);
//                                            finish();
//                                        }
//                                    });
//                            dialog.setNegativeButton("不，不再设置营养成分阈值",
//                                    new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            UserData mUser = new UserData(userName,days,Integer.toString(age),sex,userWeight,
//                                                    userHeight,BMI,0,0,0,0);
//                                            mUserDataManager.openDataBase();
//                                            long flag = mUserDataManager.insertUserData(mUser);
//                                            if (flag == -1) {
//                                                Toast.makeText(Register.this, "注册失败，请重试", Toast.LENGTH_SHORT).show();
//                                            } else {
//                                                Toast.makeText(Register.this, "注册成功", Toast.LENGTH_SHORT).show();
//                                            }
//                                            Intent intent = new Intent(Register.this,Login.class);
//                                            startActivity(intent);
//                                            finish();
//                                        }
//                                    });
                                    dialog.show();
                                    //break;
                                }
                                if (mUserDataManager.check_height(sex, days, height) != 1) {
                                    //太瘦 不设置阈值
                                    if (new_status == 0) {
                                        AlertDialog.Builder dialog = new AlertDialog.Builder(Register.this);
                                        dialog.setTitle("亲，您有点太瘦了呢");
//                                dialog.setMessage("是否已经咨询医生并自定义营养成分摄入量？否则我们将为您设定摄入量");
                                        dialog.setMessage("本APP只为您提供正常人的建议营养成分摄入量，并建议您就医");
                                        dialog.setCancelable(false);
                                        dialog.setNegativeButton("确认",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        UserData mUser = new UserData(userName, days, Integer.toString(age), choosed_sex, userWeight,
                                                                Double.parseDouble(userheight), BMI, String.valueOf(identity),String.valueOf(new_status),kcal, protein, fats, CHO,
                                                                0, 0, 0, 0,
                                                                0, 0, 0, 0,
                                                                0, 0, 0, 0,
                                                                0, 0, 0, 0,
                                                                0, 0, 0, 0,
                                                                0, 0, 0, 0,
                                                                0, 0, 0, 0);
                                                        mUserDataManager.openDataBase();
                                                        long flag = mUserDataManager.insertUserData(mUser);
                                                        if (flag == -1) {
                                                            Toast.makeText(Register.this, "注册失败，请重试", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            long flag2 = mUserDataManager.insert_userhw(userName, (float) userWeight, height, Reg_time);
                                                            if (flag2 == -1) {
                                                                Log.i(TAG, "失败了~~~");
                                                            } else {
                                                                Log.i(TAG, "成功了~~~");
                                                            }
                                                            Toast.makeText(Register.this, "注册成功", Toast.LENGTH_SHORT).show();
                                                        }
                                                        Intent intent = new Intent(Register.this, Login.class);
                                                        intent.putExtra("user_status",new_status);
                                                        intent.putExtra("user_sex",choosed_sex);
                                                        intent.putExtra("user_days",days);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                });
//                                dialog.setPositiveButton("是的，我要自定义",
//                                        new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                askdr[0] = 2;
//                                                Intent intent = new Intent(Register.this, SetNutrition.class);
//                                                startActivity(intent);
//                                                finish();
//                                            }
//                                        });
//                                dialog.setNegativeButton("不，不再设置营养成分阈值",
//                                        new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                //calnutri(bmi);
//                                                UserData mUser = new UserData(userName, days, Integer.toString(age), sex, userWeight,
//                                                        userHeight, BMI, 0, 0, 0, 0);
//                                                mUserDataManager.openDataBase();
//                                                long flag = mUserDataManager.insertUserData(mUser);
//                                                if (flag == -1) {
//                                                    Toast.makeText(Register.this, "注册失败，请重试", Toast.LENGTH_SHORT).show();
//                                                } else {
//                                                    Toast.makeText(Register.this, "注册成功", Toast.LENGTH_SHORT).show();
//                                                }
//                                                Intent intent = new Intent(Register.this, Login.class);
//                                                startActivity(intent);
//                                                finish();
//                                            }
//                                        });
                                        dialog.show();
                                        //break;
                                    }
                                    //太胖 不设置阈值
                                    if (new_status == 2) {
                                        AlertDialog.Builder dialog = new AlertDialog.Builder(Register.this);
                                        dialog.setTitle("亲，您有点超重了呢");
                                        dialog.setMessage("本APP只为您提供正常人的建议营养成分摄入量，并建议您就医");
                                        dialog.setCancelable(false);
                                        dialog.setNegativeButton("确认",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        UserData mUser = new UserData(userName, days, Integer.toString(age), choosed_sex, userWeight,
                                                                Double.parseDouble(userheight), BMI, String.valueOf(identity),String.valueOf(new_status),kcal, protein, fats, CHO,
                                                                0, 0, 0, 0,
                                                                0, 0, 0, 0,
                                                                0, 0, 0, 0,
                                                                0, 0, 0, 0,
                                                                0, 0, 0, 0,
                                                                0, 0, 0, 0,
                                                                0, 0, 0, 0);
                                                        mUserDataManager.openDataBase();
                                                        long flag = mUserDataManager.insertUserData(mUser);
                                                        if (flag == -1) {
                                                            Toast.makeText(Register.this, "注册失败，请重试", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            long flag2 = mUserDataManager.insert_userhw(userName, (float) userWeight, height, Reg_time);
                                                            if (flag2 == -1) {
                                                                Log.i(TAG, "失败了~~~");
                                                            } else {
                                                                Log.i(TAG, "成功了~~~");
                                                            }
                                                            Toast.makeText(Register.this, "注册成功", Toast.LENGTH_SHORT).show();
                                                        }
                                                        Intent intent = new Intent(Register.this, Login.class);
                                                        intent.putExtra("user_status",new_status);
                                                        intent.putExtra("user_sex",choosed_sex);
                                                        intent.putExtra("user_days",days);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                });
//                                dialog.setMessage("是否已经咨询医生并自定义营养成分摄入量？");
//                                dialog.setCancelable(false);
//                                dialog.setPositiveButton("是的，我要自定义",
//                                        new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                Intent intent = new Intent(Register.this, SetNutrition.class);
//                                                startActivity(intent);
//                                                finish();
//                                            }
//                                        });
//                                dialog.setNegativeButton("没有，不再设置营养成分阈值",
//                                        new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                UserData mUser = new UserData(userName, days, Integer.toString(age), sex, userWeight,
//                                                        userHeight, BMI, 0, 0, 0, 0);
//                                                mUserDataManager.openDataBase();
//                                                long flag = mUserDataManager.insertUserData(mUser);
//                                                if (flag == -1) {
//                                                    Toast.makeText(Register.this, "注册失败，请重试", Toast.LENGTH_SHORT).show();
//                                                } else {
//                                                    Toast.makeText(Register.this, "注册成功", Toast.LENGTH_SHORT).show();
//                                                }
//                                                Intent intent = new Intent(Register.this, Login.class);
//                                                startActivity(intent);
//                                                return;//finish();
//                                            }
//                                        });
                                        dialog.show();
                                        //break;
                                    }
                                    if (new_status == 3) {
                                        AlertDialog.Builder dialog = new AlertDialog.Builder(Register.this);
                                        dialog.setTitle("亲，您的身体状态为肥胖");
                                        dialog.setMessage("本APP只为您提供正常人的建议营养成分摄入量，并建议您就医");
                                        dialog.setCancelable(false);
                                        dialog.setNegativeButton("确认",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
//                                                        UserData mUser = new UserData(userName, days, Integer.toString(age), choosed_sex, userWeight,
//                                                                height, BMI, 0, 0, 0, 0,
//                                                                0, 0, 0, 0,
//                                                                0, 0, 0, 0,
//                                                                0, 0, 0, 0,
//                                                                0, 0, 0, 0,
//                                                                0, 0, 0, 0,
//                                                                0, 0, 0, 0,
//                                                                0, 0, 0, 0);
                                                        UserData mUser = new UserData(userName, days, Integer.toString(age), choosed_sex, userWeight,
                                                                Double.parseDouble(userheight), BMI, String.valueOf(identity),String.valueOf(new_status),kcal, protein, fats, CHO,
                                                                0, 0, 0, 0,
                                                                0, 0, 0, 0,
                                                                0, 0, 0, 0,
                                                                0, 0, 0, 0,
                                                                0, 0, 0, 0,
                                                                0, 0, 0, 0,
                                                                0, 0, 0, 0);
                                                        mUserDataManager.openDataBase();
                                                        long flag = mUserDataManager.insertUserData(mUser);
                                                        if (flag == -1) {
                                                            Toast.makeText(Register.this, "注册失败，请重试", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            long flag2 = mUserDataManager.insert_userhw(userName, (float) userWeight, height, Reg_time);
                                                            if (flag2 == -1) {
                                                                Log.i(TAG, "失败了~~~");
                                                            } else {
                                                                Log.i(TAG, "成功了~~~");
                                                            }
                                                            Toast.makeText(Register.this, "注册成功", Toast.LENGTH_SHORT).show();
                                                        }
                                                        Intent intent = new Intent(Register.this, Login.class);
                                                        intent.putExtra("user_status",new_status);
                                                        intent.putExtra("user_sex",choosed_sex);
                                                        intent.putExtra("user_days",days);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                });
//                                dialog.setMessage("是否已经咨询医生并自定义营养成分摄入量？");
//                                dialog.setCancelable(false);
//                                dialog.setPositiveButton("是的，我要自定义",
//                                        new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                Intent intent = new Intent(Register.this, SetNutrition.class);
//                                                startActivity(intent);
//                                                finish();
//                                            }
//                                        });
//                                dialog.setNegativeButton("没有，不再设置营养成分阈值",
//                                        new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                UserData mUser = new UserData(userName, days, Integer.toString(age), sex, userWeight,
//                                                        userHeight, BMI, 0, 0, 0, 0);
//                                                mUserDataManager.openDataBase();
//                                                long flag = mUserDataManager.insertUserData(mUser);
//                                                if (flag == -1) {
//                                                    Toast.makeText(Register.this, "注册失败，请重试", Toast.LENGTH_SHORT).show();
//                                                } else {
//                                                    Toast.makeText(Register.this, "注册成功", Toast.LENGTH_SHORT).show();
//                                                }
//                                                Intent intent = new Intent(Register.this, Login.class);
//                                                startActivity(intent);
//                                                return;//finish();
//                                            }
//                                        });
                                        dialog.show();
                                        //break;
                                    }
                                    //如果正常bmi计算ibw 和营养成分
                                    //正常体重 用ibw计算各种营养成分值
                                    else if (new_status == 1) {
//                                        float ibw = 0;//mUserDataManager.IBWInfo(sex,age,days,height);
//                                        double kcal = 0;
//                                        double fats = 0;
//                                        double CHO = 0;
//                                        double protein = 0;
////                                        ibw = mUserDataManager.IBWInfo(sex, age, days, height);
////                                        kcal = calkcal(ibw);
////                                        fats = calfats(age, kcal);
////                                        CHO = calCHO(age, kcal);
////                                        protein = calprotein(age, kcal);
//                                        Log.i(TAG, "ibw" + ibw+","+kcal+","+CHO+","+kcal+","+fats);
                                        UserData mUser = new UserData(userName, days, Integer.toString(age), choosed_sex, userWeight,
                                                Double.parseDouble(userheight), BMI, String.valueOf(identity),String.valueOf(new_status),kcal, protein, fats, CHO,
                                                0, 0, 0, 0,
                                                0, 0, 0, 0,
                                                0, 0, 0, 0,
                                                0, 0, 0, 0,
                                                0, 0, 0, 0,
                                                0, 0, 0, 0,
                                                0, 0, 0, 0);
                                        mUserDataManager.openDataBase();
                                        long flag = mUserDataManager.insertUserData(mUser);
                                        if (flag == -1) {
                                            Toast.makeText(Register.this, "注册失败，请重试", Toast.LENGTH_SHORT).show();
                                        } else {
                                            long flag2 = mUserDataManager.insert_userhw(userName, (float) userWeight, height, Reg_time);
                                            if (flag2 == -1) {
                                                Log.i(TAG, "失败了~~~");
                                            } else {
                                                Log.i(TAG, "成功了~~~");
                                            }
                                            Toast.makeText(Register.this, "注册成功", Toast.LENGTH_SHORT).show();
                                        }
                                        Intent intent_Reg_to_Login = new Intent(Register.this, Login.class);
                                        intent_Reg_to_Login.putExtra("user_status",new_status);
                                        intent_Reg_to_Login.putExtra("user_sex",choosed_sex);
                                        intent_Reg_to_Login.putExtra("user_days",days);
                                        startActivity(intent_Reg_to_Login);
                                        finish();
                                        break;
                                    }

                                }
                            }
                        }
//////////////////////////////////////////////////////////
//                        if (age <= 18) {
//                            Log.i("bmi丫丫","?"+BMI);
//                            int status = mUserDataManager.BMIInfo(sex, age, days, BMI);
//                            float ibw = 0;//mUserDataManager.IBWInfo(sex,age,days,height);
//                            double kcal = 0;
//                            double fats = 0;
//                            double CHO = 0;
//                            double protein = 0;
//                            final int[] askdr = {0};
//                            Log.i(TAG, "状态" + status);
//                            //小于2岁 不给推荐
//                            if (status == -1) {
//                                AlertDialog.Builder dialog = new AlertDialog.Builder(Register.this);
//                                dialog.setTitle("该用户年纪过小");
//                                dialog.setMessage("本APP将不提供建议营养成分摄入量");
//                                dialog.setCancelable(false);
//                                dialog.setNegativeButton("确认",
//                                        new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                UserData mUser = new UserData(userName, days, Integer.toString(age), sex, userWeight,
//                                                        height,BMI, 0, 0, 0, 0,
//                                                        0, 0, 0, 0,
//                                                        0, 0, 0, 0,
//                                                        0, 0, 0, 0,
//                                                        0, 0, 0, 0,
//                                                        0, 0, 0, 0,
//                                                        0, 0, 0, 0,
//                                                        0, 0, 0, 0);
//                                                mUserDataManager.openDataBase();
//                                                long flag = mUserDataManager.insertUserData(mUser);
//                                                if (flag == -1) {
//                                                    Toast.makeText(Register.this, "注册失败，请重试", Toast.LENGTH_SHORT).show();
//                                                } else {
//
//                                                    long flag2 =  mUserDataManager.insert_userhw(userName,(float)userWeight,height,Reg_time);
//                                                    if (flag2==-1){
//                                                    Log.i(TAG,"失败了~~~");
//                                                    }
//                                                    else
//                                                    {Log.i(TAG,"成功了~~~");}
//                                                    Toast.makeText(Register.this, "注册成功", Toast.LENGTH_SHORT).show();
//                                                }
//                                                Intent intent = new Intent(Register.this, Login.class);
//                                                startActivity(intent);
//                                                finish();
//                                            }
//                                        });
//                                dialog.show();
//                                // break;
//                            }
//                            //status=1 个子小于年龄段p10不给推荐
//                            //不小于p10
//                            Log.i(TAG, "身高状况" + mUserDataManager.check_height(sex, days, height));
//                            if(status!=-1){
////                            if (mUserDataManager.check_height(sex, days, height) == 1) {
////                                AlertDialog.Builder dialog = new AlertDialog.Builder(Register.this);
////                                dialog.setTitle("该用户身高低于该年龄段10%，");
////                                dialog.setMessage("本APP将不提供建议营养成分摄入量");
////                                dialog.setCancelable(false);
////                                dialog.setNegativeButton("确认",
////                                        new DialogInterface.OnClickListener() {
////                                            @Override
////                                            public void onClick(DialogInterface dialog, int which) {
////                                                UserData mUser = new UserData(userName, days, Integer.toString(age), sex, userWeight,
////                                                        height, BMI, 0, 0, 0, 0,
////                                                        0, 0, 0, 0,
////                                                        0, 0, 0, 0,
////                                                        0, 0, 0, 0,
////                                                        0, 0, 0, 0,
////                                                        0, 0, 0, 0,
////                                                        0, 0, 0, 0,
////                                                        0, 0, 0, 0);
////                                                mUserDataManager.openDataBase();
////                                                long flag = mUserDataManager.insertUserData(mUser);
////                                                if (flag == -1) {
////                                                    Toast.makeText(Register.this, "注册失败，请重试", Toast.LENGTH_SHORT).show();
////                                                } else {
////                                                    long flag2 =  mUserDataManager.insert_userhw(userName,(float)userWeight,height,Reg_time);
////                                                    if (flag2==-1){
////                                                        Log.i(TAG,"失败了~~~");
////                                                    }
////                                                    else
////                                                    {Log.i(TAG,"成功了~~~");}
////                                                    Toast.makeText(Register.this, "注册成功", Toast.LENGTH_SHORT).show();
////                                                }
////                                                Intent intent = new Intent(Register.this, Login.class);
////                                                startActivity(intent);
////                                                finish();
////                                            }
////                                        });
//////                            dialog.setPositiveButton("已咨询医生，自定义营养成分摄入值",
//////                                    new DialogInterface.OnClickListener() {
//////                                        @Override
//////                                        public void onClick(DialogInterface dialog, int which) {
//////                                            askdr[0]=2;
//////                                            Intent intent = new Intent(Register.this,SetNutrition.class);
//////                                            startActivity(intent);
//////                                            finish();
//////                                        }
//////                                    });
//////                            dialog.setNegativeButton("不，不再设置营养成分阈值",
//////                                    new DialogInterface.OnClickListener() {
//////                                        @Override
//////                                        public void onClick(DialogInterface dialog, int which) {
//////                                            UserData mUser = new UserData(userName,days,Integer.toString(age),sex,userWeight,
//////                                                    userHeight,BMI,0,0,0,0);
//////                                            mUserDataManager.openDataBase();
//////                                            long flag = mUserDataManager.insertUserData(mUser);
//////                                            if (flag == -1) {
//////                                                Toast.makeText(Register.this, "注册失败，请重试", Toast.LENGTH_SHORT).show();
//////                                            } else {
//////                                                Toast.makeText(Register.this, "注册成功", Toast.LENGTH_SHORT).show();
//////                                            }
//////                                            Intent intent = new Intent(Register.this,Login.class);
//////                                            startActivity(intent);
//////                                            finish();
//////                                        }
//////                                    });
////                                dialog.show();
////                                //break;
////                            }
////                            if (mUserDataManager.check_height(sex, days, height) != 1) {
////                                //太瘦 不设置阈值
////                                if (status == 1) {
////                                    AlertDialog.Builder dialog = new AlertDialog.Builder(Register.this);
////                                    dialog.setTitle("亲，您有点太瘦了呢");
//////                                dialog.setMessage("是否已经咨询医生并自定义营养成分摄入量？否则我们将为您设定摄入量");
////                                    dialog.setMessage("本APP将不提供建议营养成分摄入量");
////                                    dialog.setCancelable(false);
////                                    dialog.setNegativeButton("确认",
////                                            new DialogInterface.OnClickListener() {
////                                                @Override
////                                                public void onClick(DialogInterface dialog, int which) {
////                                                    UserData mUser = new UserData(userName, days, Integer.toString(age), sex, userWeight,
////                                                            height, BMI, 0, 0, 0, 0,
////                                                            0, 0, 0, 0,
////                                                            0, 0, 0, 0,
////                                                            0, 0, 0, 0,
////                                                            0, 0, 0, 0,
////                                                            0, 0, 0, 0,
////                                                            0, 0, 0, 0,
////                                                            0, 0, 0, 0);
////                                                    mUserDataManager.openDataBase();
////                                                    long flag = mUserDataManager.insertUserData(mUser);
////                                                    if (flag == -1) {
////                                                        Toast.makeText(Register.this, "注册失败，请重试", Toast.LENGTH_SHORT).show();
////                                                    } else {
////                                                        long flag2 =  mUserDataManager.insert_userhw(userName,(float)userWeight,height,Reg_time);
////                                                        if (flag2==-1){
////                                                            Log.i(TAG,"失败了~~~");
////                                                        }
////                                                        else
////                                                        {Log.i(TAG,"成功了~~~");}
////                                                        Toast.makeText(Register.this, "注册成功", Toast.LENGTH_SHORT).show();
////                                                    }
////                                                    Intent intent = new Intent(Register.this, Login.class);
////                                                    startActivity(intent);
////                                                    finish();
////                                                }
////                                            });
//////                                dialog.setPositiveButton("是的，我要自定义",
//////                                        new DialogInterface.OnClickListener() {
//////                                            @Override
//////                                            public void onClick(DialogInterface dialog, int which) {
//////                                                askdr[0] = 2;
//////                                                Intent intent = new Intent(Register.this, SetNutrition.class);
//////                                                startActivity(intent);
//////                                                finish();
//////                                            }
//////                                        });
//////                                dialog.setNegativeButton("不，不再设置营养成分阈值",
//////                                        new DialogInterface.OnClickListener() {
//////                                            @Override
//////                                            public void onClick(DialogInterface dialog, int which) {
//////                                                //calnutri(bmi);
//////                                                UserData mUser = new UserData(userName, days, Integer.toString(age), sex, userWeight,
//////                                                        userHeight, BMI, 0, 0, 0, 0);
//////                                                mUserDataManager.openDataBase();
//////                                                long flag = mUserDataManager.insertUserData(mUser);
//////                                                if (flag == -1) {
//////                                                    Toast.makeText(Register.this, "注册失败，请重试", Toast.LENGTH_SHORT).show();
//////                                                } else {
//////                                                    Toast.makeText(Register.this, "注册成功", Toast.LENGTH_SHORT).show();
//////                                                }
//////                                                Intent intent = new Intent(Register.this, Login.class);
//////                                                startActivity(intent);
//////                                                finish();
//////                                            }
//////                                        });
////                                    dialog.show();
////                                    //break;
////                                }
////                                //太胖 不设置阈值
////                                if (status == 3) {
////                                    AlertDialog.Builder dialog = new AlertDialog.Builder(Register.this);
////                                    dialog.setTitle("亲，您有点超重了呢");
////                                    dialog.setMessage("本APP将不提供建议营养成分摄入量");
////                                    dialog.setCancelable(false);
////                                    dialog.setNegativeButton("确认",
////                                            new DialogInterface.OnClickListener() {
////                                                @Override
////                                                public void onClick(DialogInterface dialog, int which) {
////                                                    UserData mUser = new UserData(userName, days, Integer.toString(age), sex, userWeight,
////                                                            height, BMI, 0, 0, 0, 0,
////                                                            0, 0, 0, 0,
////                                                            0, 0, 0, 0,
////                                                            0, 0, 0, 0,
////                                                            0, 0, 0, 0,
////                                                            0, 0, 0, 0,
////                                                            0, 0, 0, 0,
////                                                            0, 0, 0, 0);
////                                                    mUserDataManager.openDataBase();
////                                                    long flag = mUserDataManager.insertUserData(mUser);
////                                                    if (flag == -1) {
////                                                        Toast.makeText(Register.this, "注册失败，请重试", Toast.LENGTH_SHORT).show();
////                                                    } else {
////                                                        long flag2 =  mUserDataManager.insert_userhw(userName,(float)userWeight,height,Reg_time);
////                                                        if (flag2==-1){
////                                                            Log.i(TAG,"失败了~~~");
////                                                        }
////                                                        else
////                                                        {Log.i(TAG,"成功了~~~");}
////                                                        Toast.makeText(Register.this, "注册成功", Toast.LENGTH_SHORT).show();
////                                                    }
////                                                    Intent intent = new Intent(Register.this, Login.class);
////                                                    startActivity(intent);
////                                                    finish();
////                                                }
////                                            });
//////                                dialog.setMessage("是否已经咨询医生并自定义营养成分摄入量？");
//////                                dialog.setCancelable(false);
//////                                dialog.setPositiveButton("是的，我要自定义",
//////                                        new DialogInterface.OnClickListener() {
//////                                            @Override
//////                                            public void onClick(DialogInterface dialog, int which) {
//////                                                Intent intent = new Intent(Register.this, SetNutrition.class);
//////                                                startActivity(intent);
//////                                                finish();
//////                                            }
//////                                        });
//////                                dialog.setNegativeButton("没有，不再设置营养成分阈值",
//////                                        new DialogInterface.OnClickListener() {
//////                                            @Override
//////                                            public void onClick(DialogInterface dialog, int which) {
//////                                                UserData mUser = new UserData(userName, days, Integer.toString(age), sex, userWeight,
//////                                                        userHeight, BMI, 0, 0, 0, 0);
//////                                                mUserDataManager.openDataBase();
//////                                                long flag = mUserDataManager.insertUserData(mUser);
//////                                                if (flag == -1) {
//////                                                    Toast.makeText(Register.this, "注册失败，请重试", Toast.LENGTH_SHORT).show();
//////                                                } else {
//////                                                    Toast.makeText(Register.this, "注册成功", Toast.LENGTH_SHORT).show();
//////                                                }
//////                                                Intent intent = new Intent(Register.this, Login.class);
//////                                                startActivity(intent);
//////                                                return;//finish();
//////                                            }
//////                                        });
////                                    dialog.show();
////                                    //break;
////                                }
////                                //如果正常bmi计算ibw 和营养成分
////                                //正常体重 用ibw计算各种营养成分值
////                                else if (status == 2) {
////                                    ibw = mUserDataManager.IBWInfo(sex, age, days, height);
////                                    kcal = calkcal(ibw);
////                                    fats = calfats(age, kcal);
////                                    CHO = calCHO(age, kcal);
////                                    protein = calprotein(age, kcal);
////                                    Log.i(TAG, "身高高" + userHeight + "+" + userheight);
////                                    UserData mUser = new UserData(userName, days, Integer.toString(age), sex, userWeight,
////                                            Double.parseDouble(userheight), BMI, kcal,protein, fats, CHO,
////                                            0, 0, 0, 0,
////                                            0, 0, 0, 0,
////                                            0, 0, 0, 0,
////                                            0, 0, 0, 0,
////                                            0, 0, 0, 0,
////                                            0, 0, 0, 0,
////                                            0, 0, 0, 0);
////                                    mUserDataManager.openDataBase();
////                                    long flag = mUserDataManager.insertUserData(mUser);
////                                    if (flag == -1) {
////                                        Toast.makeText(Register.this, "注册失败，请重试", Toast.LENGTH_SHORT).show();
////                                    } else {
////                                        long flag2 =  mUserDataManager.insert_userhw(userName,(float)userWeight,height,Reg_time);
////                                        if (flag2==-1){
////                                            Log.i(TAG,"失败了~~~");
////                                        }
////                                        else
////                                        {Log.i(TAG,"成功了~~~");}
////                                        Toast.makeText(Register.this, "注册成功", Toast.LENGTH_SHORT).show();
////                                    }
////                                    Intent intent_Reg_to_Login = new Intent(Register.this, Login.class);
////                                    startActivity(intent_Reg_to_Login);
////                                    finish();
////                                    break;
////                                }
////
////                            }
//                            }
//                        }
                   else if (age > 18) {
                                Intent intent_reg_to_adultattri = new Intent(Register.this, AdultAttri.class);
                                intent_reg_to_adultattri.putExtra("user_weight", userWeight);
                                intent_reg_to_adultattri.putExtra("user_bmi", BMI);
                                intent_reg_to_adultattri.putExtra("user_name", userName);
                                intent_reg_to_adultattri.putExtra("user_days", days);
                                intent_reg_to_adultattri.putExtra("user_age", age);
                                intent_reg_to_adultattri.putExtra("user_sex", sex);
                                intent_reg_to_adultattri.putExtra("user_height", userheight);
                                intent_reg_to_adultattri.putExtra("user_identity",identity);
                                Log.i(TAG, "JWO" + days);
                               // intent_reg_to_adultattri.putExtra("user_status",4);
                                startActivity(intent_reg_to_adultattri);
                                //finish();
                            }


                       // break;
                    }
                    break;

                case R.id.register_btn_cancel:
                    onBackPressed();
//                    Intent intent = new Intent(Register.this, Login.class);
//                    startActivity(intent);
//                    finish();
//                    break;
            }
            Log.i(TAG, "OnClickListener()");
        }
    };

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
        if(age<4&&age>1){
            CHO = kcal*0.5;
        }
        else if (age>4&&age<18){
            CHO = kcal*0.55;
        }
        Log.i(TAG,"calCHO"+CHO);
        return CHO;
    }
    public double calfats(int age,double kcal){
        double fats = 0;
        if(age<4&&age>1){
            fats = kcal*0.35;
        }
        else if (age>4&&age<18){
            fats = kcal*0.3;
        }
        Log.i(TAG,"calFATS"+fats);
        return fats;
    }
    public double calprotein(int age,double kcal){
        double protein = 0;
            protein = kcal*0.15;
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
                    //days = dayMinus;
                }
            } else if (monthMinus > 0) {
                age = 1;
                //days = monthMinus*30+dayMinus;
            }
        } else if (yearMinus > 0) {
            if (monthMinus < 0) {// 当前月>生日月
            } else if (monthMinus == 0) {// 同月份的，再根据日期计算年龄
                if (dayMinus < 0) {
                } else if (dayMinus >= 0) {
                    age = age + 1;
                    //days = yearMinus*365+monthMinus*30+dayMinus;
                }
            } else if (monthMinus > 0) {
                age = age + 1;
            }
        }
        days = yearMinus*365+monthMinus*30+dayMinus;
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
            Log.i(TAG_Account,mUsername.getText().toString().trim());
            //String userPwdCheck = mPwdCheck.getText().toString().trim();
            int IsExist=mUserDataManager.findUserByName(userName);

            if (IsExist > 0) {
                Toast.makeText(this, "用户名已存在，请重新输入", Toast.LENGTH_SHORT).show();
                return false;
            }
//                UserData mUser = new UserData(userName,days,Integer.toString(age),userSex,userWeight,
//                        userHeight,BMI,0,0,0,0);
//                        d1kcal,d1CHO,d1protein,d1fats,
//                        d2kcal,d2CHO,d2protein,d2fats,
//                        d3kcal,d3CHO,d3protein,d3fats,
//                        d4kcal,d4CHO,d4protein,d4fats,
//                        d5kcal,d5CHO,d5protein,d5fats,
//                        d6kcal,d6CHO,d6protein,d6fats,
//                        d7kcal,d7CHO,d7protein,d7fats);
//                mUserDataManager.openDataBase();
//                long flag = mUserDataManager.insertUserData(mUser);
//                if (flag == -1) {
//                    Toast.makeText(this, "注册失败，请重试", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
//                }
                return true;
        }
        Log.i(TAG, "end");
        return false;
    }
//    public void overfat(double bmi){
//        Log.i(TAG,"ENTER overfat1");
//            AlertDialog.Builder dialog = new AlertDialog.Builder(Register.this);
//            dialog.setTitle("亲，您有点超重了呢");
//            dialog.setMessage("是否已经咨询医生并自定义营养成分摄入量？否则我们将为您设定摄入量");
//            dialog.setCancelable(false);
//            dialog.setPositiveButton("是的，我要自定义",
//                    new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Intent intent = new Intent(Register.this,SetNutrition.class);
//                            startActivity(intent);
//                            finish();
//                        }
//                    });
//            dialog.setNegativeButton("没有，我听你们的",
//                    new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Intent intent = new Intent(Register.this,Login.class);
//                            startActivity(intent);
//                            return;//finish();
//                        }
//                    });
//            dialog.show();}
//    public void overyoung(){
//        AlertDialog.Builder dialog = new AlertDialog.Builder(Register.this);
//        dialog.setTitle("该用户年纪过小");
//        dialog.setMessage("本APP将不提供建议营养成分摄入量");
//        dialog.setCancelable(false);
//        dialog.setPositiveButton("已咨询医生，自定义营养成分摄入值",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = new Intent(Register.this,SetNutrition.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                });
//        dialog.setNegativeButton("不再设置营养成分阈值",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //calnutri(bmi);
//                        Intent intent = new Intent(Register.this,Login.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                });
//        dialog.show();
//
//    }
//    public void overthin(double bmi){
//            AlertDialog.Builder dialog = new AlertDialog.Builder(Register.this);
//            dialog.setTitle("亲，您有点太瘦了呢");
//            dialog.setMessage("是否已经咨询医生并自定义营养成分摄入量？否则我们将为您设定摄入量");
//            dialog.setCancelable(false);
//            dialog.setPositiveButton("是的，我要自定义",
//                    new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Intent intent = new Intent(Register.this,SetNutrition.class);
//                            startActivity(intent);
//                            finish();
//                        }
//                    });
//            dialog.setNegativeButton("没有，我听你们的",
//                    new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            //calnutri(bmi);
//                            Intent intent = new Intent(Register.this,Login.class);
//                            startActivity(intent);
//                            finish();
//                        }
//                    });
//            dialog.show();
//        }


/*
 * "1": 小于两岁，不显示营养推荐
 * "2": 身高小于10%，建议就医
 * "3": 超胖
 * "4": 消瘦
 * "5": 正常
 */
//private SQLiteDatabase mSQLiteDatabase = null;
//    public int check_health(String sex,double height,float age,float days,double BMI){
////        String string_age = String.valueOf(age);
////        String string_days = String.valueOf(days);
////        //小于两岁，不显示营养推荐
////        if(days < 731)
////            return 1;
////            //查BMI表
////        else {
////            //男孩
////            if (sex == "男") {
////                //身高小于10%
////                //Cursor height_boy = mSQLiteDatabase.query("boy_height",)
////                Cursor height_boy = mSQLiteDatabase.rawQuery("SELECT * FROM boy_height WHERE Age < ? ORDER BY Age DESC LIMIT 1", new String[]{string_age});
////                String boy_h = "";
////                if (height_boy.moveToFirst()) {
////                    do {
////                        if (height_boy.getCount() == 0) {
////                            Log.i(TAG, "NULL");
////                        }
////                        boy_h = height_boy.getString(height_boy.getColumnIndex("P10"));
////                    } while (height_boy.moveToNext());
////                }
////                float boyHeight = Float.parseFloat(boy_h);
////                if (height < boyHeight) {
////                    return 2;
////                } else {
////                    //2-5岁的孩子
////                    if (age >= 2 && age <= 5) {
////                        Cursor boy_2_5 = mSQLiteDatabase.query("boy_2_5", null, "Age=?", new String[]{string_days}, null, null, null, null);
////                        String boy_2_5_15 = "";
////                        String boy_2_5_85 = "";
////                        if (boy_2_5.moveToFirst()) {
////                            do {
////                                if (boy_2_5.getCount() == 0) {
////                                    Log.i(TAG, "NULL");
////                                }
////                                boy_2_5_15 = boy_2_5.getString(boy_2_5.getColumnIndex("P15"));
////                                boy_2_5_85 = boy_2_5.getString(boy_2_5.getColumnIndex("p85"));
////                            } while (boy_2_5.moveToNext());
////                        }
////                        float boy_15 = Float.parseFloat(boy_2_5_15);
////                        float boy_85 = Float.parseFloat(boy_2_5_85);
////                        //到表boy_2_5查询结果
////                        if (BMI >= boy_15 && BMI <= boy_85) {
////                            return 3;
////                        } else if (BMI < boy_15) {
////                            return 4;
////                        } else {
////                            return 5;
////                        }
////                    } else if (age >= 6 && age <= 18) {
////                        String month = String.valueOf(days / 30);
////                        //到表boy_5_19查询结果
////                        Cursor boy_6_18 = mSQLiteDatabase.query("boy_5_19", null, "Month=?", new String[]{month}, null, null, null);
////                        String boy_6_18_15 = "";
////                        String boy_6_18_85 = "";
////                        if (boy_6_18.moveToFirst()) {
////                            do {
////                                if (boy_6_18.getCount() == 0) {
////                                    Log.i(TAG, "NULL");
////                                }
////                                boy_6_18_15 = boy_6_18.getString(boy_6_18.getColumnIndex("P15"));
////                                boy_6_18_85 = boy_6_18.getString(boy_6_18.getColumnIndex("p85"));
////                            } while (boy_6_18.moveToNext());
////                        }
////                        float boy_15 = Float.parseFloat(boy_6_18_15);
////                        float boy_85 = Float.parseFloat(boy_6_18_85);
////                        if (BMI >= boy_15 && BMI <= boy_85) {
////                            return 3;
////                        } else if (BMI < boy_15) {
////                            return 4;
////                        } else {
////                            return 5;
////                        }
////                    }
////                }
////            }
////            //女孩
////            else {
////                //身高小于10%
////                Cursor height_girl = mSQLiteDatabase.rawQuery("SELECT * FROM girl_height WHERE Age < ? ORDER BY Age DESC LIMIT 1", new String[]{string_age});
////                String girl_h = "";
////                if (height_girl.moveToFirst()) {
////                    do {
////                        if (height_girl.getCount() == 0) {
////                            Log.i(TAG, "NULL");
////                        }
////                        girl_h = height_girl.getString(height_girl.getColumnIndex("P10"));
////                    } while (height_girl.moveToNext());
////                }
////                float girlHeight_10 = Float.parseFloat(girl_h);
////                if (height < girlHeight_10) {
////                    return 2;
////                } else {
////                    if (age >= 2 && age <= 5) {
////                        //到表girl_2_5查询结果
////                        Cursor girl_2_5 = mSQLiteDatabase.query("girl_2_5", null, "Age=?", new String[]{string_days}, null, null, null);
////                        String girl_2_5_15 = "";
////                        String girl_2_5_85 = "";
////                        if (girl_2_5.moveToFirst()) {
////                            do {
////                                if (girl_2_5.getCount() == 0) {
////                                    Log.i(TAG, "NULL");
////                                }
////                                girl_2_5_15 = girl_2_5.getString(girl_2_5.getColumnIndex("P15"));
////                                girl_2_5_85 = girl_2_5.getString(girl_2_5.getColumnIndex("p85"));
////                            } while (girl_2_5.moveToNext());
////                        }
////                        float girl_15 = Float.parseFloat(girl_2_5_15);
////                        float girl_85 = Float.parseFloat(girl_2_5_85);
////                        if (BMI >= girl_15 && BMI <= girl_85) {
////                            return 3;
////                        } else if (BMI < girl_15) {
////                            return 4;
////                        } else {
////                            return 5;
////                        }
////                    } else if (age >= 6 && age <= 18) {
////                        //到表girl_5_19查询结果
////                        String month = String.valueOf(days / 30);
////                        Cursor girl_6_18 = mSQLiteDatabase.query("girl_5_19", null, "Month=?", new String[]{month}, null, null, null);
////                        String girl_6_18_15 = "";
////                        String girl_6_18_85 = "";
////                        if (girl_6_18.moveToFirst()) {
////                            do {
////                                if (girl_6_18.getCount() == 0) {
////                                    Log.i(TAG, "NULL");
////                                }
////                                girl_6_18_15 = girl_6_18.getString(girl_6_18.getColumnIndex("P15"));
////                                girl_6_18_85 = girl_6_18.getString(girl_6_18.getColumnIndex("p85"));
////                            } while (girl_6_18.moveToNext());
////                        }
////                        float girl_15 = Float.parseFloat(girl_6_18_15);
////                        float girl_85 = Float.parseFloat(girl_6_18_85);
////                        if (BMI <= girl_85 && BMI >= girl_15) {
////                            return 3;
////                        } else if (BMI < girl_15) {
////                            return 4;
////                        } else {
////                            return 5;
////                        }
////                    }
////                }
////            }
////        }
////        return 0;
////    }


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




