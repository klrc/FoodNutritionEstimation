package com.example.camera;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ResultShow extends AppCompatActivity {
    private Button result_sure;
    private Button result_other;
    private Button result_set_vol;
    private String result_food_name;
    private String TAG="ResultShow:";
    private TextView textView_result_name;
    private TextView textView_result_vol_weight;
    private TextView text_result_mat1_value;
    private TextView text_result_mat2_value;
    private TextView text_result_mat3_value;
    private TextView getText_result_sum_value;
    private String CHO_val="100";
    private String protein_val = "100";
    private String fats_val = "100";
    private String energy_val = "100";
    private static String DB_PATH = "data/data/com.example.camera/databases/";
    private static final String DB_NAME = "user_data";
    private float result_vol=100;
    private float result_weight=100;
    private UserDataManager mUserDataManager;
    private String daystr_import;
    private SQLiteDatabase mSQLiteDatabase = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READWRITE);
    private String loginuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_show);
        final Intent intent = getIntent();
        getSupportActionBar().hide();
        if(mUserDataManager == null){
            mUserDataManager = new UserDataManager(this);
            mUserDataManager.openDataBase();
        }
        //result_other= findViewById(R.id.result_others);
        result_set_vol=findViewById(R.id.result_set_vol);
        result_sure=findViewById(R.id.result_sure);
        text_result_mat1_value=findViewById(R.id.result_mat1_value);
        text_result_mat2_value=findViewById(R.id.result_mat2_value);
        text_result_mat3_value=findViewById(R.id.result_mat3_value);
        getText_result_sum_value=findViewById(R.id.result_energy_value);
        //textView_result_vol_weight = findViewById(R.id.result_vol_weight);
        result_sure.setOnClickListener(mListener);
        result_set_vol.setOnClickListener(mListener);
        textView_result_name=findViewById(R.id.result_name);
        result_food_name = intent.getStringExtra("result_food_name");
        loginuser = intent.getStringExtra("user");
        textView_result_name.setText("识别结果: "+result_food_name);
      //  textView_result_vol_weight.setText("食物体积： "+result_vol+"\n大概"+result_weight+"g");
        text_result_mat1_value.setText(CHO_val+"kcal");
        text_result_mat2_value.setText(protein_val+"kcal");
        text_result_mat3_value.setText(fats_val+"kcal");
        getText_result_sum_value.setText(energy_val+"kcal");

        Calendar cal = Calendar.getInstance();
        int weekNow = cal.get(Calendar.DAY_OF_WEEK);
        daystr_import ="D"+weekNow;





    }
    View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.result_sure:

                    ContentValues values = new ContentValues();
                    List<String> original_nutri = new ArrayList<String>();
                    Log.i(TAG,"loginuser:"+loginuser);
                    original_nutri = mUserDataManager.today_nutri_import(daystr_import,loginuser);
                    String n0 = String.valueOf(Float.valueOf(energy_val)+Float.valueOf(original_nutri.get(0)));
                    String n1 = String.valueOf(Float.valueOf(CHO_val)+Float.valueOf(original_nutri.get(1)));
                    String n2 = String.valueOf(Float.valueOf(protein_val)+Float.valueOf(original_nutri.get(2)));
                    String n3 = String.valueOf(Float.valueOf(fats_val)+Float.valueOf(original_nutri.get(3)));
                    values.put(daystr_import+"Kcal",n0);
                    values.put(daystr_import+"CHO",n1);
                    values.put(daystr_import+"Protein",n2);
                    values.put(daystr_import+"Fats",n3);
                    mSQLiteDatabase.update("users_info",values,"USER_NAME=?",new String[]{loginuser});
                    Intent intent = new Intent(ResultShow.this,FoodDetection.class);
                    intent.putExtra("user",loginuser);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.result_set_vol:
                    final EditText et =new EditText(ResultShow.this);
                    new AlertDialog.Builder(ResultShow.this).setTitle("请输入菜品重量(g)")
                            .setView(et)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //按下确定键后的事件
                                    Intent intent =new Intent(ResultShow.this,FoodDetection.class);
                                    intent.putExtra("user_set_weight",et.getText().toString().trim());
                                    Log.i(TAG,"用户填的重量："+et.getText().toString().trim());
                                }
                            }).setNegativeButton("取消",null).show();
                    break;

            }
        }
    };

}
