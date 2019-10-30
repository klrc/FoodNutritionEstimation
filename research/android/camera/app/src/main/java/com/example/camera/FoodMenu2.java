package com.example.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FoodMenu2 extends AppCompatActivity
{
    private SearchView searchView;
    private ListView listView;
    private List<iconInformation> list;
    private List<iconInformation> findList;
    private List<String> nameList;
    private listViewAdapter adapter;
    private listViewAdapter findAdapter;
    private Bitmap bitmap;
    private UserDataManager mUserDataManager;
    private String TAG = "FoodMenu2";
    String loginuser;

    private Handler handler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 123:
                    adapter = new listViewAdapter(FoodMenu2.this, list);
                    listView.setAdapter(adapter);
                    for(int i = 0; i < list.size(); i++)
                    {
                        iconInformation information = list.get(i);
                        nameList.add(information.getName());
                    }
                    break;
            }
        }
    };
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home_item:
                    Intent intent = new Intent(FoodMenu2.this,MainInterface.class);
                    intent.putExtra("user",loginuser);
                    startActivity(intent);
                    //onBackPressed();
                    return true;
                case R.id.nutri_table_item:
                    Intent intent_MainInterface_to_Nutrition = new Intent(FoodMenu2.this, NutritionIntake.class);
                    intent_MainInterface_to_Nutrition.putExtra("user",loginuser);
                    startActivity(intent_MainInterface_to_Nutrition);
                    finish();
                    Log.i(TAG,"GOTO NUTRI");
                    break;
                case R.id.food_set_item:
                    return true;
                case R.id.detect_item:
                    // mTextMessage.setText(R.string.title_notifications);
                    Intent intent_MainInterface_to_FoodDetection = new Intent(FoodMenu2.this,FoodDetection.class);
                    intent_MainInterface_to_FoodDetection.putExtra("user",loginuser);
                    startActivity(intent_MainInterface_to_FoodDetection);
                    finish();
                    return true;
                case R.id.setting_item:
                    Intent intent_MainInterface_Setting = new Intent(FoodMenu2.this,Setting.class);
                    intent_MainInterface_Setting.putExtra("user",loginuser);
                    startActivity(intent_MainInterface_Setting);
                    finish();
                    return true;
            }
            return false;
        }
    };


    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_food_menu);
        getSupportActionBar().hide();
        searchView = (SearchView) findViewById(R.id.menu_search);
        listView = (ListView) findViewById(R.id.food_menu);
        findList = new ArrayList<iconInformation>();
        nameList = new ArrayList<String>();
        searchView.setIconified(true); //false是一进界面就在输入框内，也弹出虚拟键盘，true是没有反应
        searchView.setSubmitButtonEnabled(true);
        Intent intent = getIntent();
        loginuser = intent.getStringExtra("user");
        BottomNavigationView navigation = findViewById(R.id.navigation_food_menu);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.food_set_item);




        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            mUserDataManager.openDataBase();
        }

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                iconInformation food = findList.get(position);
//                String foodname = food.getName();
//                Log.i(TAG,"foodname"+foodname);
//                Intent intent_FoodName_FoodInfo = new Intent(FoodMenu2.this,FoodInfo.class);
//                intent_FoodName_FoodInfo.putExtra("foodname",foodname);
//                startActivity(intent_FoodName_FoodInfo);
//            }
//        });
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                iconInformation food = list.get(position);
//                String foodname = food.getName();
//                Log.i(TAG,"foodname"+foodname);
//                Intent intent_FoodName_FoodInfo = new Intent(FoodMenu2.this,FoodInfo.class);
//                intent_FoodName_FoodInfo.putExtra("foodname",foodname);
//                startActivity(intent_FoodName_FoodInfo);
//            }
//        });
        /**
         * 默认情况下是没提交搜索的按钮，所以用户必须在键盘上按下"enter"键来提交搜索.你可以同过setSubmitButtonEnabled(
         * true)来添加一个提交按钮（"submit" button)
         * 设置true后，右边会出现一个箭头按钮。如果用户没有输入，就不会触发提交（submit）事件
         */
        //searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {

            //输入完成后，提交时触发的方法，一般情况是点击输入法中的搜索按钮才会触发，表示现在正式提交了
            public boolean onQueryTextSubmit(String query)
            {
                Log.i(TAG,"查找内荣啊++"+query);
//                if(query.equals(""))
//                {
////                    Toast.makeText(FoodMenu2.this, "请输入查找内容！", Toast.LENGTH_SHORT).show();
////                    listView.setAdapter(adapter);
//                }
//                else
//                {
//                    findList.clear();
//                    for(int i = 0; i < list.size(); i++)
//                    {
//                        iconInformation information = list.get(i);
//                        if(information.getName().equals(query))
//                        {
//                            findList.add(information);
//                            //break;
//                        }
//                    }
////                    if(findList.size() == 0)
////                    {
////                        Toast.makeText(FoodMenu2.this, "查找的商品不在列表中", Toast.LENGTH_SHORT).show();
////                    }
////                    else
////                    {
////                        Toast.makeText(FoodMenu2.this, "查找成功", Toast.LENGTH_SHORT).show();
////                        findAdapter = new listViewAdapter(FoodMenu2.this, findList);
////                        listView.setAdapter(findAdapter);
////                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////                            @Override
////                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                                iconInformation food = findList.get(position);
////                                String foodname = food.getName();
////                                Log.i(TAG,"foodname"+foodname);
////                                Intent intent_FoodName_FoodInfo = new Intent(FoodMenu2.this,FoodInfo.class);
////                                intent_FoodName_FoodInfo.putExtra("foodname",foodname);
////                                startActivity(intent_FoodName_FoodInfo);
////                            }
////                        });
////                    }
//                }
                return true;
            }

            //在输入时触发的方法，当字符真正显示到searchView中才触发，像是拼音，在输入法组词的时候不会触发
            public boolean onQueryTextChange(String newText)
            {
                if(TextUtils.isEmpty(newText))
                {
                    listView.setAdapter(adapter);

                }
                else
                {
                    findList= new ArrayList<iconInformation>(list);
                    findList.clear();
                    Log.i(TAG,"list2的size"+list.size());
                    for(int i = 0; i < list.size(); i++)
                    {
                        iconInformation information = list.get(i);
                        if(information.getName().contains(newText)|| newText.equals(""))
                        {
                            findList.add(information);
                        }
                    }
                    if(list.size()==0){
                        Toast.makeText(FoodMenu2.this,"未搜索到该菜品",Toast.LENGTH_SHORT).show();
                    }
//                    findAdapter = new listViewAdapter(FoodMenu2.this, findList);
                    findAdapter = new listViewAdapter(FoodMenu2.this, findList);
                    findAdapter.notifyDataSetChanged();
                    listView.setAdapter(findAdapter);
//                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            iconInformation food = findList.get(position);
//                            String foodname = food.getName();
//                            Log.i(TAG,"foodname"+foodname);
//                            Intent intent_FoodName_FoodInfo = new Intent(FoodMenu2.this,FoodInfo.class);
//                            intent_FoodName_FoodInfo.putExtra("foodname",foodname);
//                            startActivity(intent_FoodName_FoodInfo);
//                        }
//                    });
                }

                return true;
            }
        });
//        if(searchView.getQuery().equals("")){
//            Log.i(TAG,"没用搜索");
//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    iconInformation food = list.get(position);
//                    String foodname = food.getName();
//                    Log.i(TAG,"foodname"+foodname);
//                    Intent intent_FoodName_FoodInfo = new Intent(FoodMenu2.this,FoodInfo.class);
//                    intent_FoodName_FoodInfo.putExtra("foodname",foodname);
//                    startActivity(intent_FoodName_FoodInfo);
//                }
//            });
//        }
//        else{
//            Log.i(TAG,"搜索");
//            Log.i(TAG,"提交内容2："+searchView.getQuery());
//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    iconInformation food = findList.get(position);
//                    String foodname = food.getName();
//                    Log.i(TAG,"foodname"+foodname);
//                    Intent intent_FoodName_FoodInfo = new Intent(FoodMenu2.this,FoodInfo.class);
//                    intent_FoodName_FoodInfo.putExtra("foodname",foodname);
//                    startActivity(intent_FoodName_FoodInfo);
//                }
//            });
//        }
        new Thread(new Runnable()
        {
            public void run()
            {
                list = new ArrayList<iconInformation>();
                List<String> fl = mUserDataManager.showFoodName("Food_Name");
                String[] foodlist = new String[fl.size()];
                //String fn;
                fl.toArray(foodlist);
//                for (int i=0;i<foodlist.length;i++){
//                    iconInformation iconInfo1 = new iconInformation();
//                    bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw);
//                    iconInfo1.setName(foodlist[i]);
//                    iconInfo1.setIcon(bitmap);
//                    list.add(iconInfo1);
//                }
                int i =0;
                    iconInformation iconInfo1 = new iconInformation();
                    bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.beef2);
                    iconInfo1.setName(foodlist[i]);
                    i++;
                    iconInfo1.setIcon(bitmap);
                    list.add(iconInfo1);
//                iconInformation iconInfo2 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.beef2);
//                iconInfo2.setName(foodlist[i]);
                i++;
//                iconInfo2.setIcon(bitmap);
//                list.add(iconInfo2);

//                iconInformation iconInfo3 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.beef2);
//                iconInfo3.setName(foodlist[i]);
                i++;
//                iconInfo3.setIcon(bitmap);
//                list.add(iconInfo3);

                iconInformation iconInfo4 = new iconInformation();
                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.currybeef);
                iconInfo4.setName(foodlist[i]);
                i++;
                iconInfo4.setIcon(bitmap);
                list.add(iconInfo4);

                iconInformation iconInfo5 = new iconInformation();
                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.beefpotato3);
                iconInfo5.setName(foodlist[i]);
                i++;
                iconInfo5.setIcon(bitmap);
                list.add(iconInfo5);

//                iconInformation iconInfo6 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.beef2);
//                iconInfo6.setName(foodlist[i]);
                i++;
//                iconInfo6.setIcon(bitmap);
//                list.add(iconInfo6);

                iconInformation iconInfo7 = new iconInformation();
                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.qctds2);
                iconInfo7.setName(foodlist[i]);
                i++;
                iconInfo7.setIcon(bitmap);
                list.add(iconInfo7);

//                iconInformation iconInfo8 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.qctds2);
//                iconInfo8.setName(foodlist[i]);
                i++;
//                iconInfo8.setIcon(bitmap);
//                list.add(iconInfo8);

                iconInformation iconInfo9 = new iconInformation();
                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.hspg3);
                iconInfo9.setName(foodlist[i]);
                i++;
                iconInfo9.setIcon(bitmap);
                list.add(iconInfo9);

                iconInformation iconInfo10 = new iconInformation();
                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.hsdp8);
                iconInfo10.setName(foodlist[i]);
                i++;
                iconInfo10.setIcon(bitmap);
                list.add(iconInfo10);

                iconInformation iconInfo11 = new iconInformation();
                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.tcpg6);
                iconInfo11.setName(foodlist[i]);
                i++;
                iconInfo11.setIcon(bitmap);
                list.add(iconInfo11);

                iconInformation iconInfo12 = new iconInformation();
                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.beefpotato3);
                iconInfo12.setName(foodlist[i]);
                i++;
                iconInfo12.setIcon(bitmap);
                list.add(iconInfo12);

                iconInformation iconInfo13 = new iconInformation();
                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.chiken8);
                iconInfo13.setName(foodlist[i]);
                i++;
                iconInfo13.setIcon(bitmap);
                list.add(iconInfo13);

                iconInformation iconInfo14 = new iconInformation();
                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.hsyk6);
                iconInfo14.setName(foodlist[i]);
                i++;
                iconInfo14.setIcon(bitmap);
                list.add(iconInfo14);

                iconInformation iconInfo15 = new iconInformation();
                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.hsjt1);
                iconInfo15.setName(foodlist[i]);
                i++;
                iconInfo15.setIcon(bitmap);
                list.add(iconInfo15);

                iconInformation iconInfo16 = new iconInformation();
                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.dtj1);
                iconInfo16.setName(foodlist[i]);
                i++;
                iconInfo16.setIcon(bitmap);
                list.add(iconInfo16);

                iconInformation iconInfo17 = new iconInformation();
                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.qchx8);
                iconInfo17.setName(foodlist[i]);
                i++;
                iconInfo17.setIcon(bitmap);
                list.add(iconInfo17);

                iconInformation iconInfo18 = new iconInformation();
                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.mdcsg7);
                iconInfo18.setName(foodlist[i]);
                i++;
                iconInfo18.setIcon(bitmap);
                list.add(iconInfo18);

                iconInformation iconInfo19 = new iconInformation();
                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.mdcrs2);
                iconInfo19.setName(foodlist[i]);
                i++;
                iconInfo19.setIcon(bitmap);
                list.add(iconInfo19);

                iconInformation iconInfo20 = new iconInformation();
                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.cdj1);
                iconInfo20.setName(foodlist[i]);
                i++;
                iconInfo20.setIcon(bitmap);
                list.add(iconInfo20);

                iconInformation iconInfo21 = new iconInformation();
                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.bdcrs1);
                iconInfo21.setName(foodlist[i]);
                i++;
                iconInfo21.setIcon(bitmap);
                list.add(iconInfo21);



                iconInformation iconInfo22 = new iconInformation();
                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.khs5);
                iconInfo22.setName(foodlist[i]);
                i++;
                iconInfo22.setIcon(bitmap);
                list.add(iconInfo22);

//                iconInformation iconInfo23 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.food);
//                iconInfo23.setName(foodlist[i]);
                i++;
//                iconInfo23.setIcon(bitmap);
//                list.add(iconInfo23);
//
//                iconInformation iconInfo24 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.food);
//                iconInfo24.setName(foodlist[i]);
                i++;
//                iconInfo24.setIcon(bitmap);
//                list.add(iconInfo24);

                iconInformation iconInfo25 = new iconInformation();
                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.gbrice8);
                iconInfo25.setName(foodlist[i]);
                i++;
                iconInfo25.setIcon(bitmap);
                list.add(iconInfo25);

//                iconInformation iconInfo26= new iconInformation();
//                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.food);
//                iconInfo26.setName(foodlist[i]);
                i++;
//                iconInfo26.setIcon(bitmap);
//                list.add(iconInfo26);

                iconInformation iconInfo27 = new iconInformation();
                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.noodle2);
                iconInfo27.setName(foodlist[i]);
                i++;
                iconInfo27.setIcon(bitmap);
                list.add(iconInfo27);

                iconInformation iconInfo28 = new iconInformation();
                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.hsdy1);
                iconInfo28.setName(foodlist[i]);
                i++;
                iconInfo28.setIcon(bitmap);
                list.add(iconInfo28);

                iconInformation iconInfo29 = new iconInformation();
                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.hsjy2);
                iconInfo29.setName(foodlist[i]);
                i++;
                iconInfo29.setIcon(bitmap);
                list.add(iconInfo29);

                iconInformation iconInfo30 = new iconInformation();
                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.qzly);
                iconInfo30.setName(foodlist[i]);
                i++;
                iconInfo30.setIcon(bitmap);
                list.add(iconInfo30);

                iconInformation iconInfo31 = new iconInformation();
                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.bzhx7);
                iconInfo31.setName(foodlist[i]);
                i++;
                iconInfo31.setIcon(bitmap);
                list.add(iconInfo31);

                iconInformation iconInfo32 = new iconInformation();
                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.hszt);
                iconInfo32.setName(foodlist[i]);
                i++;
                iconInfo32.setIcon(bitmap);
                list.add(iconInfo32);

                iconInformation iconInfo33 = new iconInformation();
                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.qzxhy);
                iconInfo33.setName(foodlist[i]);
                i++;
                iconInfo33.setIcon(bitmap);
                list.add(iconInfo33);

//                iconInformation iconInfo330 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.xcxhyt);
//                iconInfo330.setName(foodlist[i]);
//                i++;
//                iconInfo330.setIcon(bitmap);
//                list.add(iconInfo330);

                iconInformation iconInfo34 = new iconInformation();
                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.xcxhyt);
                iconInfo34.setName(foodlist[i]);
                i++;
                iconInfo34.setIcon(bitmap);
                list.add(iconInfo34);

                iconInformation iconInfo35 = new iconInformation();
                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.sswy);
                iconInfo35.setName(foodlist[i]);
                i++;
                iconInfo35.setIcon(bitmap);
                list.add(iconInfo35);

                iconInformation iconInfo36 = new iconInformation();
                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.xjswy);
                iconInfo36.setName(foodlist[i]);
                i++;
                iconInfo36.setIcon(bitmap);
                list.add(iconInfo36);

                iconInformation iconInfo37 = new iconInformation();
                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.cyhg);
                iconInfo37.setName(foodlist[i]);
                i++;
                iconInfo37.setIcon(bitmap);
                list.add(iconInfo37);

                iconInformation iconInfo38 = new iconInformation();
                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.zyn);
                iconInfo38.setName(foodlist[i]);
                i++;
                iconInfo38.setIcon(bitmap);
                list.add(iconInfo38);

                iconInformation iconInfo39 = new iconInformation();
                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.cyyn);
                iconInfo39.setName(foodlist[i]);
                i++;
                iconInfo39.setIcon(bitmap);
                list.add(iconInfo39);

                iconInformation iconInfo40 = new iconInformation();
                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.szbq);
                iconInfo40.setName(foodlist[i]);
                i++;
                iconInfo40.setIcon(bitmap);
                list.add(iconInfo40);



                iconInformation iconInfo41 = new iconInformation();
                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.szwd);
                iconInfo41.setName(foodlist[i]);
                i++;
                iconInfo41.setIcon(bitmap);
                list.add(iconInfo41);

                iconInformation iconInfo42 = new iconInformation();
                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.wdymxr);
                iconInfo42.setName(foodlist[i]);
                i++;
                iconInfo42.setIcon(bitmap);
                list.add(iconInfo42);

                iconInformation iconInfo43 = new iconInformation();
                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.szym);
                iconInfo43.setName(foodlist[i]);
                i++;
                iconInfo43.setIcon(bitmap);
                list.add(iconInfo43);

                iconInformation iconInfo44 = new iconInformation();
                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.cniangao);
                iconInfo44.setName(foodlist[i]);
                i++;
                iconInfo44.setIcon(bitmap);
                list.add(iconInfo44);

                iconInformation iconInfo45 = new iconInformation();
                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.zng);
                iconInfo45.setName(foodlist[i]);
                i++;
                iconInfo45.setIcon(bitmap);
                list.add(iconInfo45);

                iconInformation iconInfo46 = new iconInformation();
                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.cng);
                iconInfo46.setName(foodlist[i]);
                i++;
                iconInfo46.setIcon(bitmap);
                list.add(iconInfo46);

                iconInformation iconInfo47 = new iconInformation();
                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.tkos);
                iconInfo47.setName(foodlist[i]);
                i++;
                iconInfo47.setIcon(bitmap);
                list.add(iconInfo47);

                iconInformation iconInfo48 = new iconInformation();
                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.symerp);
                iconInfo48.setName(foodlist[i]);
                i++;
                iconInfo48.setIcon(bitmap);
                list.add(iconInfo48);

                iconInformation iconInfo49 = new iconInformation();
                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.rmcfs);
                iconInfo49.setName(foodlist[i]);
                i++;
                iconInfo49.setIcon(bitmap);
                list.add(iconInfo49);

                iconInformation iconInfo50 = new iconInformation();
                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.hbd);
                iconInfo50.setName(foodlist[i]);
                i++;
                iconInfo50.setIcon(bitmap);
                list.add(iconInfo50);

                iconInformation iconInfo51 = new iconInformation();
                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.jcdfg);
                iconInfo51.setName(foodlist[i]);
                i++;
                iconInfo51.setIcon(bitmap);
                list.add(iconInfo51);

                iconInformation iconInfo52 = new iconInformation();
                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.jcdf);
                iconInfo52.setName(foodlist[i]);
                i++;
                iconInfo52.setIcon(bitmap);
                list.add(iconInfo52);

                iconInformation iconInfo53 = new iconInformation();
                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.ymjb);
                iconInfo53.setName(foodlist[i]);
                i++;
                iconInfo53.setIcon(bitmap);
                list.add(iconInfo53);

                iconInformation iconInfo54 = new iconInformation();
                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.xpt);
                iconInfo54.setName(foodlist[i]);
                i++;
                iconInfo54.setIcon(bitmap);
                list.add(iconInfo54);
//                iconInformation iconInfo55 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.cyszx3);
//                               iconInfo55.setName(foodlist[i]);
//                i++;
//                iconInfo55.setIcon(bitmap);
//                list.add(iconInfo55);



//                iconInformation iconInfo53 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(FoodMenu2.this.getResources(), R.raw.beef2);
//                iconInfo53.setName(foodlist[i]);
//                i++;
//                iconInfo53.setIcon(bitmap);
//                list.add(iconInfo53);












//                iconInfo1.setName("Beer");
//                iconInfo1.setIcon(bitmap);
//                list.add(iconInfo1);
//                iconInformation iconInfo2 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.bread);
//                iconInfo2.setName("Bread");
//                iconInfo2.setIcon(bitmap);
//                list.add(iconInfo2);
//                iconInformation iconInfo3 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.breakfast);
//                iconInfo3.setName("Breakfast");
//                iconInfo3.setIcon(bitmap);
//                list.add(iconInfo3);
//                iconInformation iconInfo4 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.burger);
//                iconInfo4.setName("Burger");
//                iconInfo4.setIcon(bitmap);
//                list.add(iconInfo4);
//                iconInformation iconInfo5 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.cake);
//                iconInfo5.setName("Cake");
//                iconInfo5.setIcon(bitmap);
//                list.add(iconInfo5);
//                iconInformation iconInfo6 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.carrot);
//                iconInfo6.setName("Carrot");
//                iconInfo6.setIcon(bitmap);
//                list.add(iconInfo6);
//                iconInformation iconInfo7 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.check);
//                iconInfo7.setName("Check");
//                iconInfo7.setIcon(bitmap);
//                list.add(iconInfo7);
//                iconInformation iconInfo8 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.chicken);
//                iconInfo8.setName("Chicken");
//                iconInfo8.setIcon(bitmap);
//                list.add(iconInfo8);
//                iconInformation iconInfo9 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.closed);
//                iconInfo9.setName("Closed");
//                iconInfo9.setIcon(bitmap);
//                list.add(iconInfo9);
//                iconInformation iconInfo10 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.cocktails);
//                iconInfo10.setName("Cocktails");
//                iconInfo10.setIcon(bitmap);
//                list.add(iconInfo10);
//                iconInformation iconInfo11 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.coffeecup);
//                iconInfo11.setName("Coffee-Cup");
//                iconInfo11.setIcon(bitmap);
//                list.add(iconInfo11);
//                iconInformation iconInfo12 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.croissant);
//                iconInfo12.setName("Croissant");
//                iconInfo12.setIcon(bitmap);
//                list.add(iconInfo12);
//                iconInformation iconInfo13 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.cutlery1);
//                iconInfo13.setName("Cutlery1");
//                iconInfo13.setIcon(bitmap);
//                list.add(iconInfo13);
//                iconInformation iconInfo14 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.cutlery);
//                iconInfo14.setName("Cutlery");
//                iconInfo14.setIcon(bitmap);
//                list.add(iconInfo14);
//                iconInformation iconInfo15 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.favorite);
//                iconInfo15.setName("Favorite");
//                iconInfo15.setIcon(bitmap);
//                list.add(iconInfo15);
//                iconInformation iconInfo16 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.fish);
//                iconInfo16.setName("Fish");
//                iconInfo16.setIcon(bitmap);
//                list.add(iconInfo16);
//                iconInformation iconInfo17 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.fork);
//                iconInfo17.setName("Fork");
//                iconInfo17.setIcon(bitmap);
//                list.add(iconInfo17);
//                iconInformation iconInfo18 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.fruits);
//                iconInfo18.setName("Fruits");
//                iconInfo18.setIcon(bitmap);
//                list.add(iconInfo18);
//                iconInformation iconInfo19 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.icecream);
//                iconInfo19.setName("Ice-Cream");
//                iconInfo19.setIcon(bitmap);
//                list.add(iconInfo19);
//                iconInformation iconInfo20 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.invoice);
//                iconInfo20.setName("Invoice");
//                iconInfo20.setIcon(bitmap);
//                list.add(iconInfo20);
//                iconInformation iconInfo21 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.juice);
//                iconInfo21.setName("Juice");
//                iconInfo21.setIcon(bitmap);
//                list.add(iconInfo21);
//                iconInformation iconInfo22 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.kettle1);
//                iconInfo22.setName("Kettle1");
//                iconInfo22.setIcon(bitmap);
//                list.add(iconInfo22);
//                iconInformation iconInfo23 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.kettle);
//                iconInfo23.setName("Kettle");
//                iconInfo23.setIcon(bitmap);
//                list.add(iconInfo23);
//                iconInformation iconInfo24 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.lobster);
//                iconInfo24.setName("Lobster");
//                iconInfo24.setIcon(bitmap);
//                list.add(iconInfo24);
//                iconInformation iconInfo25 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.menu);
//                iconInfo25.setName("Menu");
//                iconInfo25.setIcon(bitmap);
//                list.add(iconInfo25);
//                iconInformation iconInfo26 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.mushrooms);
//                iconInfo26.setName("Mushrooms");
//                iconInfo26.setIcon(bitmap);
//                list.add(iconInfo26);
//                iconInformation iconInfo27 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.napkins);
//                iconInfo27.setName("Napkins");
//                iconInfo27.setIcon(bitmap);
//                list.add(iconInfo27);
//                iconInformation iconInfo28 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.open);
//                iconInfo28.setName("Open");
//                iconInfo28.setIcon(bitmap);
//                list.add(iconInfo28);
//                iconInformation iconInfo29 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.pan);
//                iconInfo29.setName("Pan");
//                iconInfo29.setIcon(bitmap);
//                list.add(iconInfo29);
//                iconInformation iconInfo30 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.paymentmethod);
//                iconInfo30.setName("Payment-Method");
//                iconInfo30.setIcon(bitmap);
//                list.add(iconInfo30);
//                iconInformation iconInfo31 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.pieceofcake);
//                iconInfo31.setName("Piece-Of-Cake");
//                iconInfo31.setIcon(bitmap);
//                list.add(iconInfo31);
//                iconInformation iconInfo32 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.pizza);
//                iconInfo32.setName("Pizza");
//                iconInfo32.setIcon(bitmap);
//                list.add(iconInfo32);
//                iconInformation iconInfo33 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.reserved);
//                iconInfo33.setName("Reserved");
//                iconInfo33.setIcon(bitmap);
//                list.add(iconInfo33);
//                iconInformation iconInfo34 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.restaurant);
//                iconInfo34.setName("Restaurant");
//                iconInfo34.setIcon(bitmap);
//                list.add(iconInfo34);
//                iconInformation iconInfo35 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.saltandpepper);
//                iconInfo35.setName("Salt-And-Pepper");
//                iconInfo35.setIcon(bitmap);
//                list.add(iconInfo35);
//                iconInformation iconInfo36 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.sausage);
//                iconInfo36.setName("Sausage");
//                iconInfo36.setIcon(bitmap);
//                list.add(iconInfo36);
//                iconInformation iconInfo37 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.skewer);
//                iconInfo37.setName("Skewer");
//                iconInfo37.setIcon(bitmap);
//                list.add(iconInfo37);
//                iconInformation iconInfo38 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.soup);
//                iconInfo38.setName("Soup");
//                iconInfo38.setIcon(bitmap);
//                list.add(iconInfo38);
//                iconInformation iconInfo39 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.spoon);
//                iconInfo39.setName("Spoon");
//                iconInfo39.setIcon(bitmap);
//                list.add(iconInfo39);
//                iconInformation iconInfo40 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.steak);
//                iconInfo40.setName("Steak");
//                iconInfo40.setIcon(bitmap);
//                list.add(iconInfo40);
//                iconInformation iconInfo41 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.sushi);
//                iconInfo41.setName("Sushi");
//                iconInfo41.setIcon(bitmap);
//                list.add(iconInfo41);
//                iconInformation iconInfo42 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.sushi1);
//                iconInfo42.setName("Sushi1");
//                iconInfo42.setIcon(bitmap);
//                list.add(iconInfo42);
//                iconInformation iconInfo43 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.sushi);
//                iconInfo43.setName("Sushi");
//                iconInfo43.setIcon(bitmap);
//                list.add(iconInfo43);
//                iconInformation iconInfo44 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.table);
//                iconInfo44.setName("Table");
//                iconInfo44.setIcon(bitmap);
//                list.add(iconInfo44);
//                iconInformation iconInfo45 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.teacup);
//                iconInfo45.setName("Tea-Cup");
//                iconInfo45.setIcon(bitmap);
//                list.add(iconInfo45);
//                iconInformation iconInfo46 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.terrace);
//                iconInfo46.setName("Terrace");
//                iconInfo46.setIcon(bitmap);
//                list.add(iconInfo46);
//                iconInformation iconInfo47 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.tray1);
//                iconInfo47.setName("Tray1");
//                iconInfo47.setIcon(bitmap);
//                list.add(iconInfo47);
//                iconInformation iconInfo48 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.tray);
//                iconInfo48.setName("Tray");
//                iconInfo48.setIcon(bitmap);
//                list.add(iconInfo48);
//                iconInformation iconInfo49 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.vegetables);
//                iconInfo49.setName("Vegetables");
//                iconInfo49.setIcon(bitmap);
//                list.add(iconInfo49);
//                iconInformation iconInfo50 = new iconInformation();
//                bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.wine);
//                iconInfo50.setName("Wine");
//                iconInfo50.setIcon(bitmap);
//                list.add(iconInfo50);

                Message message = new Message();
                message.what = 123;
                message.obj = list;
                handler.sendMessage(message);
            }
        }).start();
        //Log.i(TAG,"wtf");
      //  onBackPressed();

//        if(findList!=null){
//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            iconInformation food = findList.get(position);
//                            String foodname = food.getName();
//                            Log.i(TAG,"foodname"+foodname);
//                            Intent intent_FoodName_FoodInfo = new Intent(FoodMenu2.this,FoodInfo.class);
//                            intent_FoodName_FoodInfo.putExtra("foodname",foodname);
//                            startActivity(intent_FoodName_FoodInfo);
//                        }
//                    });
//        }

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    iconInformation food = list.get(position);
                    String foodname = food.getName();
                    Bitmap img = food.getIcon();
                    byte buff[] =  new byte[200*150];
                    buff = Bitmap2Bytes(img);

                    Log.i(TAG,"foodname"+foodname);
                    Intent intent_FoodName_FoodInfo = new Intent(FoodMenu2.this,FoodInfo.class);
                    intent_FoodName_FoodInfo.putExtra("foodname",foodname);
                    intent_FoodName_FoodInfo.putExtra("foodimage",buff);
                    startActivity(intent_FoodName_FoodInfo);

                   // list = new ArrayList<iconInformation>(list2);
                }
            });

    }
//    @Override
//    public void onBackPressed(){
//        list = new ArrayList<>(list2);
//        Log.i(TAG,"用back");
//        super.onBackPressed();
//        return;
//    }
private byte[] Bitmap2Bytes(Bitmap bm){
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
    return baos.toByteArray();
}




}


