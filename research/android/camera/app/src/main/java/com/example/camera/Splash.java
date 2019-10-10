package com.example.camera;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import java.io.IOException;

import static java.lang.Thread.sleep;

public class Splash extends AppCompatActivity {
    private static String TAG = "splash";
    private static String DB_PATH = "data/data/com.example.camera/databases/";
    private static final String DB_NAME = "user_data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);
        UserDataManager.DataBaseManagementHelper helper = new UserDataManager.DataBaseManagementHelper(this);
        try {
            helper.createDataBase();
            helper.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SQLiteDatabase database = SQLiteDatabase.openDatabase(DB_PATH+DB_NAME,null,SQLiteDatabase.OPEN_READWRITE);

        Cursor cursor2 = database.rawQuery("select name from sqlite_master where type='table' order by name", null);
        while(cursor2.moveToNext()){
            //遍历出表名
            String name = cursor2.getString(0);
            Log.i(TAG, "have+"+name);
        }
        database.close();


//        Cursor cursor = database.query("user_hw", null, null, null, null, null, null, null);
//        while (cursor.moveToNext()){
//            String username = cursor.getString(cursor.getColumnIndex("User_name"));
////            Log.i("hwhwhwhw",username);
//        }

        Thread myThread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(500);
                    Intent it = new Intent(getApplicationContext(), Login.class);
                    startActivity(it);
                    Log.i(TAG,"splash over");
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start(); Log.i(TAG,"mythread start");
    }
}
