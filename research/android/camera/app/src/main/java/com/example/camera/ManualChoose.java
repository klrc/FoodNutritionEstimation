package com.example.camera;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class ManualChoose extends AppCompatActivity {
    private Button manual_sure;
    private Button manual_back;
    //下拉框
    private Spinner food_spinner;
    public String choosed_food;
    private static final  String[] food_list = {"素包","肉包","素水饺","肉水饺","汤圆","馄饨"};
    private ArrayAdapter<String> adapter;
    private String TAG = "ManualChoose";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_choose);
        getSupportActionBar().hide();
        Log.i(TAG,"start");
        manual_sure = findViewById(R.id.manual_sure);
        //下拉框
        food_spinner = (Spinner)findViewById(R.id.food_spinner);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,food_list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        food_spinner.setAdapter(adapter);
       // food_spinner.setOnItemClickListener(new Login.SpinnerSelectedListener);
        manual_sure.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Log.i(TAG,"SURE");
            Intent intent = new Intent(ManualChoose.this,FoodDetection.class);
            startActivity(intent);
            finish();}
        });
//        manual_back.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Log.i(TAG,"CANCEL");
//                Intent intent_ManualChoose_FoodDetection = new Intent(ManualChoose.this,FoodDetection.class);
//                startActivity(intent_ManualChoose_FoodDetection);
//                finish();}
//        });


        }
    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener{
        public void onItemSelected(AdapterView<?> arg0,View arg1,int arg2,long arg3){
            choosed_food = food_list[arg2];


        }
        public void onNothingSelected(AdapterView<?> arg0){}
    }


}
