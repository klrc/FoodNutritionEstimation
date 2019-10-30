package com.example.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
//import FoodMenu.*;

public class FoodInfo extends AppCompatActivity {
    private FoodMenu mFoodMenu;
    private String TAG = "FoodInfo";
    private TextView foodinfo;
    private UserDataManager mUserDataManager;
//    private RadioButton mMenuButton;
//    private RadioButton mNutriinfoButton;
    private fragment f1;
    private fragment f2;
    private List<Float> foodnutri = new ArrayList<>();
    private List<String> foodnutri2 = new ArrayList<>();
    private List<String> foodmat = new ArrayList<>();
    private TextView food_energy;
    private TextView food_CHO;
    private TextView food_protein;
    private TextView food_fats;
    private ImageView imgview;
    private TableLayout food_mat_table=null ;
    private TableLayout food_nutri_table=null;
    private TextView food_name;

    //底部三个按钮
//    private Button foot1;
//    private Button foot2;
    //private Button mNutriinfoButton;
    private Button mMenuButton;
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_info);
        getSupportActionBar().hide();
        if(mUserDataManager == null){
            mUserDataManager = new UserDataManager(this);
            mUserDataManager.openDataBase();
        }
       // food_CHO = findViewById(R.id.food_CHO);
      //  food_energy=findViewById(R.id.food_energy);
       // food_protein=findViewById(R.id.food_protein);
      //  food_fats = findViewById(R.id.food_fats);
        food_mat_table = findViewById(R.id.food_table);
        food_nutri_table = findViewById(R.id.food_nutri_table);
        food_mat_table.setStretchAllColumns(true);
        food_nutri_table.setStretchAllColumns(true);
        food_name = findViewById(R.id.foodinfo_foodname);
        imgview =findViewById(R.id.foodinfo_image);
        Intent intent = getIntent();
        String foodname = intent.getStringExtra("foodname");
        byte buff[] = intent.getByteArrayExtra("foodimage");
        bitmap = BitmapFactory.decodeByteArray(buff,0,buff.length);
        BitmapDrawable mBitmapDrawable = new BitmapDrawable(bitmap);
        imgview.setBackgroundDrawable(mBitmapDrawable);
        //food_table.setDividerDrawable(getResources().getDrawable(R.drawable.bonus_list_item_divider));//这个就是中间的虚线
        food_mat_table.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);//设置分割线为中间显示
        food_nutri_table.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        Log.i(TAG,foodname);
        foodnutri = mUserDataManager.findFoodInfo(foodname);
        Float energy = foodnutri.get(0);
        Float CHO = foodnutri.get(1);
        Float protein = foodnutri.get(2);
        Float fats = foodnutri.get(3);
        foodmat = mUserDataManager.findFoodMaterial(foodname);
        foodnutri2.add("热量"+"-"+stralign(energy.toString()));

        foodnutri2.add("碳水化合物"+"-"+stralign(CHO.toString()));
        foodnutri2.add("蛋白质"+"-"+stralign(protein.toString()));
        foodnutri2.add("脂肪"+"-"+stralign(fats.toString()));

//        food_CHO.setText(    "碳水化合物:"+"                                             "+stralign(CHO.toString())+"Kcal");
//        food_energy.setText( "热量:     "+"                                                    "+stralign(energy.toString())+"Kcal");
//        food_protein.setText("蛋白质:   "+"                                                  "+stralign(protein.toString())+"Kcal");
//        food_fats.setText(   "脂肪:     "+"                                                    "+stralign(fats.toString())+"Kcal");
        food_name.setText(foodname);//标题的菜品名
        buildTable(foodmat,food_mat_table,"g");
        buildTable(foodnutri2,food_nutri_table,"kcal");
//选中菜品的营养成分量,vol是get（4）；


       // mNutriinfoButton = findViewById(R.id.nutri_info);
    //    mMenuButton = findViewById(R.id.menu);
//        foodinfo = findViewById(R.id.foodinfo);
//        foodinfo.setText(foodname);
//        foot1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                initFragment1();
//                Log.i(TAG,"BUJU1");
//            }
//        });
//        foot2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                initFragment2();
//                Log.i(TAG,"BUJU2");
//            }
//        });
//        initFragment1();
//        mMenuButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(FoodInfo.this,FoodHtml.class);
//                startActivity(intent);
//                //finish();
//            }
//        });

    }
    public void buildTable(List<String> matlist,TableLayout tl,String suffix){
        int len = matlist.size();
        String str;
        String mat;
        String mat_weight;
        float sum=0;
        for(int i=0;i<len;i++){
            TableRow  tb = new TableRow(this);
            //
            //View layout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_food_info,null);//布局打气筒获取单行对象
            str = matlist.get(i);
            String matlist2[]=str.split("-");

            mat = matlist2[0];
            mat_weight=matlist2[1];
            TextView tv = new TextView(this);
            TextView tv2 = new TextView(this);
            if(suffix.equals("g")){
                sum+=Float.parseFloat(mat_weight);
            }
            Log.i(TAG,"该菜品总重量为:"+sum);
            tv.setText(mat);
            tv2.setText("                     "+mat_weight+suffix);
            tb.addView(tv,0);
            tb.addView(tv2,1);
            tl.addView(tb);//,new TableLayout.LayoutParams(FP, WC));

            //tb.getParent();
            //tl.removeView(tb);
            //tb.addView(mat_name,0);
            //tb.addView(mat_weight2,1);
//            mat_name.setText(mat);
//            mat_weight2.setText(mat_weight);
//            tl.addView(layout);
        }


    }
    public String stralign(String nutri){
        String[] list =nutri.split("[.]");
        Log.i(TAG,"数组长"+list.length);
        if(list.length!=0){
            nutri = list[0]+"."+list[1].substring(0,1);
            Log.i(TAG,"对齐后的nutri"+nutri);
        }

        return nutri;
    }
//    private void initFragment1(){
//        //开启事务，fragment的控制是由事务来实现的
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//
//        //第一种方式（add），初始化fragment并添加到事务中，如果为null就new一个
//        if(f1 == null){
//            f1 = new fragment();
//            transaction.add(R.id.foodmenu_frame, f1);
//        }
//        //隐藏所有fragment
//        hideFragment(transaction);
//        //显示需要显示的fragment
//        transaction.show(f1);
//
//        //第二种方式(replace)，初始化fragment
////        if(f1 == null){
////            f1 = new MyFragment("消息");
////        }
////        transaction.replace(R.id.main_frame_layout, f1);
//
//        //提交事务
//        transaction.commit();
//    }
//
//    //显示第二个fragment
//    private void initFragment2(){
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//
//        if(f2 == null){
//            f2 = new fragment();
//            transaction.add(R.id.foodmenu_frame,f2);
//        }
//        hideFragment(transaction);
//        transaction.show(f2);
//
////        if(f2 == null) {
////            f2 = new MyFragment("联系人");
////        }
////        transaction.replace(R.id.main_frame_layout, f2);
//
//        transaction.commit();
//    }
//    private void hideFragment(FragmentTransaction transaction){
//        if(f1 != null){
//            transaction.hide(f1);
//        }
//        if(f2 != null){
//            transaction.hide(f2);
//        }
//    }


}
