package com.example.camera;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Setting extends AppCompatActivity {
    private Button chang_info;
    private Button submit_adv;
    private String TAG = "Setting";
    String loginuser;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home_item:
                    Intent intent_MainInterface_to_FoodDetection = new Intent(Setting.this,MainInterface.class);
                    intent_MainInterface_to_FoodDetection.putExtra("user",loginuser);
                    startActivity(intent_MainInterface_to_FoodDetection);
                    onBackPressed();
                 //   return true;
                    break;
                case R.id.nutri_table_item:
                    Intent intent_MainInterface_to_Nutrition = new Intent(Setting.this, NutritionIntake.class);
                    Log.i(TAG,"出去的用户是："+loginuser);
                    intent_MainInterface_to_Nutrition.putExtra("user",loginuser);
                    startActivity(intent_MainInterface_to_Nutrition);
                    finish();
                    Log.i(TAG,"GOTO NUTRI");
                    break;
                case R.id.food_set_item:
                    Intent intent_MainInterface_FoodMenu = new Intent(Setting.this,FoodMenu2.class);
                    intent_MainInterface_FoodMenu.putExtra("user",loginuser);
                    startActivity(intent_MainInterface_FoodMenu);
                    finish();
                    Log.i(TAG,"GOTO MENU");
                    break;
                case R.id.detect_item:
                     Intent intent_Detect = new Intent(Setting.this,FoodDetection.class);
                     intent_Detect.putExtra("user",loginuser);
                     startActivity(intent_Detect);
                     finish();
                    return true;
                case R.id.setting_item:
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().hide();
        chang_info = (Button)findViewById(R.id.change_info);
        submit_adv = (Button)findViewById(R.id.submit_adv);
        Intent intent =getIntent();
        loginuser=intent.getStringExtra("user");
        Log.i(TAG,"进来的用户是："+loginuser);

        BottomNavigationView navigation = findViewById(R.id.navigation_setting);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.setting_item);

        chang_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_setting_changinfo = new Intent(Setting.this,ChangInfo.class);
                intent_setting_changinfo.putExtra("user",loginuser);
                startActivity(intent_setting_changinfo);
                Log.i(TAG,"GO TO chang info");
              //  finish();
            }
        });
        submit_adv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_setting_submit =  new Intent(Setting.this,SubmitAdv.class);
                intent_setting_submit.putExtra("user",loginuser);
                startActivity(intent_setting_submit);
                Log.i(TAG,"GO TO submit");
                finish();
            }
        });
    }

}
