package com.example.camera;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.renderscript.Matrix3f;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.SimpleTimeZone;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.renderer.PieChartRenderer;
import com.github.mikephil.charting.utils.ColorTemplate;

import static com.github.mikephil.charting.components.Legend.LegendForm.EMPTY;


public class MainInterface extends AppCompatActivity  {
    private UserDataManager mUserDataManager;
    private Button food_detection;
    private Button nutrition_intake;
    private Button food_list;
    private Button setting;
    private static String TAG = "MainInterface";
    private String loginuser;
    private TextView user_text;
    private TextView user_attri;
    private static String DB_PATH = "data/data/com.example.camera/databases/";
    private static final String DB_NAME = "user_data";
    private MyPieChart myPieChart;
    private PieChart mPieChart;
    private PieChart mPieChart2;
    private PieChart mPieChart3;
    private PieChart mPieChart4;



    //private UserDataManager mUserdataManager;
    Calendar cal = Calendar.getInstance();
    int yearNow = cal.get(Calendar.YEAR);
    int monthNow = cal.get(Calendar.MONTH)+4;
    int dayNow = cal.get(Calendar.DAY_OF_MONTH);
    int weekNow = cal.get(Calendar.DAY_OF_WEEK);
    String comp_monthNow = String.valueOf(monthNow);
    String comp_dayNow = String.valueOf(dayNow);
    String age;
    String weight;
    String height;
    String bmi;
    String identity;
    String status;

    private int flag = -1;
    //flag表示本月是否记录过身高体重，0未记录过，
    private int changed_month=-1;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home_item:
                  //  mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.nutri_table_item:
                    Intent intent_MainInterface_to_Nutrition = new Intent(MainInterface.this, NutritionIntake.class);
                    intent_MainInterface_to_Nutrition.putExtra("user",loginuser);
                    startActivity(intent_MainInterface_to_Nutrition);
                    finish();
                    Log.i(TAG,"GOTO NUTRI");
                    break;
                case R.id.food_set_item:
                    Intent intent_MainInterface_FoodMenu = new Intent(MainInterface.this,FoodMenu2.class);
                    intent_MainInterface_FoodMenu.putExtra("user",loginuser);
                    startActivity(intent_MainInterface_FoodMenu);
                    finish();
                    Log.i(TAG,"GOTO MENU");
                    break;
                case R.id.detect_item:
                   // mTextMessage.setText(R.string.title_notifications);
                    Intent intent_MainInterface_to_FoodDetection = new Intent(MainInterface.this,FoodDetection.class);
                    Log.i(TAG,"传到识别的用户："+loginuser);
                    intent_MainInterface_to_FoodDetection.putExtra("user",loginuser);
                    startActivity(intent_MainInterface_to_FoodDetection);
                    finish();
                    return true;
                case R.id.setting_item:
                    Intent intent_MainInterface_Setting = new Intent(MainInterface.this,Setting.class);
                    intent_MainInterface_Setting.putExtra("user",loginuser);
                    startActivity(intent_MainInterface_Setting);
                    finish();
                    return true;
            }
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_interface);
        final Intent intent = getIntent();
        getSupportActionBar().hide();
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        loginuser = intent.getStringExtra("user");
        Log.i(TAG,"主界面用户："+loginuser);
        if(mUserDataManager == null){
            mUserDataManager = new UserDataManager(this);
            mUserDataManager.openDataBase();
        }
        BottomNavigationView navigation = findViewById(R.id.navigation);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.home_item);
        user_text = (TextView)findViewById(R.id.user_title);
        user_attri = (TextView)findViewById(R.id.user_maininter_attri);
        //food_detection = (Button)findViewById(R.id.food_detection);
        //nutrition_intake = (Button)findViewById(R.id.nutrition_intake);
        //food_list = (Button)findViewById(R.id.food_list);
        // setting = (Button)findViewById(R.id.setting);
        SQLiteDatabase database = SQLiteDatabase.openDatabase(DB_PATH+DB_NAME,null,SQLiteDatabase.OPEN_READWRITE);
       //////////////////////下文是测试一下hw表中的数据  与主线无关
        Cursor cursor = database.query("user_hw", null, null, null, null, null, null, null);
        while (cursor.moveToNext()){
            String username = cursor.getString(cursor.getColumnIndex("User_name"));
            Log.i("hwhwhwhw",username);
        }
        //////////////
        Cursor cursor2 = database.query("users_info",null,"USER_NAME=?",new String[]{loginuser},null,null,null,null);
        while (cursor2.moveToNext()){
            age = cursor2.getString(cursor2.getColumnIndex("Birth"));
            weight = cursor2.getString(cursor2.getColumnIndex("Weight"));
            height = cursor2.getString(cursor2.getColumnIndex("Height"));
            bmi = cursor2.getString(cursor2.getColumnIndex("BMI"));
            identity = cursor2.getString(cursor2.getColumnIndex("Identity"));
           status = cursor2.getString(cursor2.getColumnIndex("Status"));
            Log.i(TAG,loginuser+"的信息：identity:"+identity+",status:"+status);
        }
        ///////////////////////显示代码
        user_text.setText(loginuser);
        String sta = "";
        switch (status){
            case "-1":
                sta = "幼儿";
                break;

            case "-2":
                sta = "过矮";
                break;
            case "0":
                sta = "消瘦";
                break;
            case "1":
                sta = "正常";
                break;
            case "2":
                sta = "超重";
                break;
            case "3":
                sta = "肥胖";
                break;
            case "4":
                sta = "消瘦";
                break;
            case "5":
                sta = "正常";
                break;
            case "6":
                sta = "超重";
                break;
            case "7":
                sta = "肥胖";
                break;

        }
        user_attri.setText("      100"+"    "+height+"     "+weight+"   "+sta);
        List<Float> list =mUserDataManager.cal_nutripercent("D"+weekNow,loginuser);
        List<Float> list_static = mUserDataManager.cal_static_nutri_per(loginuser);
        Log.i(TAG,"热卡百分比"+list.get(0));
        Log.i(TAG,"CHO百分比"+list.get(1));
        Log.i(TAG,"蛋白质百分比"+list.get(2));
        Log.i(TAG,"脂肪百分比"+list.get(3));

////////////////////////////////////////////////////////////////////////////////////////////每三个月记录用户身高体重
        final String reg_time=mUserDataManager.get_reg_usertime(loginuser);
        if(String.valueOf(monthNow).length()==1){
            comp_monthNow = "0"+monthNow;
        }
        if(String.valueOf(dayNow).length()==1){
            comp_dayNow = "0"+dayNow;
        }
        final String now_time = yearNow+"-"+comp_monthNow+"-"+comp_dayNow;
        //int reg_year = Integer.parseInt(reg_time.split("-")[0]);
        //int reg_month = Integer.parseInt(reg_time.split("-")[1]);
        // int dif =
        String current_time = mUserDataManager.get_current_usertime(loginuser);
        //int current_year = Integer.parseInt(current_time.split("-")[0]);
        //int current_month = Integer.parseInt(current_time.split("-")[1]);
        String current_info = mUserDataManager.get_current_userinfo(loginuser);
        final float weight = Float.parseFloat(current_info.split("-")[0]);
        final float height = Float.parseFloat(current_info.split("-")[1]);
        Log.i(TAG,"reg_time    "+reg_time);
        Log.i(TAG,"current_time   "+current_time);
        Log.i(TAG,"now_time    "+now_time);
        ////////////////每次登陆的时候，按照userinfo表中days的值

       //每三个月 判断是否要修改信息的逻辑
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd") ;//("yyyy"+"-"+"MM"+"-"+"dd");
        int between_days=0;
    try {
        Date date1= format.parse(now_time);
        Date date2 = format.parse(current_time);
        between_days = (int)((date1.getTime()-date2.getTime())/(1000*3600*24))-2;
    }catch (ParseException e) {
        e.printStackTrace();
    }
    Log.i(TAG,"相距多少天："+between_days);
        if(between_days==90){
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainInterface.this);
            dialog.setTitle("提示");
            dialog.setMessage("请您记录并修改您的体重和身高");
            dialog.setCancelable(false);
            final Intent intent_to_change = new Intent(MainInterface.this,ChangInfo.class);
            dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    intent_to_change.putExtra("changed_user",loginuser);
                    Log.i(TAG,"要修改的用户为："+loginuser);
                    startActivity(intent_to_change);
                }
            });
            dialog.setNegativeButton("不想修改", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mUserDataManager.insert_userhw(loginuser,weight,height,now_time);

                }
            });

            dialog.show();
        }
        if(current_time==now_time){
            Log.i(TAG,"当前月份已经修改过信息了");
        }



        mPieChart = (PieChart)findViewById(R.id.mPieChart);
        mPieChart2 = (PieChart)findViewById(R.id.mPieChart2);
        mPieChart3 = (PieChart)findViewById(R.id.mPieChart3);
        mPieChart4 = (PieChart)findViewById(R.id.mPieChart4);
        mPieChart.setUsePercentValues(true);
        mPieChart.getDescription().setEnabled(false);
        mPieChart.setExtraOffsets(5, 10, 5, 5);
        mPieChart.setDragDecelerationFrictionCoef(0.95f);
        mPieChart2.setUsePercentValues(true);
        mPieChart2.getDescription().setEnabled(false);
        mPieChart2.setExtraOffsets(5, 0, 5, 5);
        mPieChart2.setDragDecelerationFrictionCoef(0.95f);
        mPieChart3.setUsePercentValues(true);
        mPieChart3.getDescription().setEnabled(false);
        mPieChart3.setExtraOffsets(5, 0, 5, 5);
        mPieChart3.setDragDecelerationFrictionCoef(0.95f);
        mPieChart4.setUsePercentValues(true);
        mPieChart4.getDescription().setEnabled(false);
        mPieChart4.setExtraOffsets(5, 0, 5, 5);
        mPieChart4.setDragDecelerationFrictionCoef(0.95f);
        float n = list.get(0);
        Log.i(TAG,"百分比:"+n);
        int scale =2;
        int rou = 4;


        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setHoleColor(Color.WHITE);

        mPieChart.setTransparentCircleColor(Color.WHITE);
        mPieChart.setTransparentCircleAlpha(110);

        mPieChart.setHoleRadius(58f);
        mPieChart.setTransparentCircleRadius(61f);

        mPieChart.setDrawCenterText(true);

        mPieChart.setRotationAngle(0);
        // 触摸旋转
        mPieChart.setRotationEnabled(true);
        mPieChart.setHighlightPerTapEnabled(true);
        /////////
        mPieChart2.setDrawHoleEnabled(true);
        mPieChart2.setHoleColor(Color.WHITE);

        mPieChart2.setTransparentCircleColor(Color.WHITE);
        mPieChart2.setTransparentCircleAlpha(110);

        mPieChart2.setHoleRadius(40f);
        mPieChart2.setTransparentCircleRadius(50f);

        mPieChart2.setDrawCenterText(false);

        mPieChart2.setRotationAngle(0);
        // 触摸旋转
        mPieChart2.setRotationEnabled(true);
        mPieChart2.setHighlightPerTapEnabled(true);
        /////////
        mPieChart3.setDrawHoleEnabled(true);
        mPieChart3.setHoleColor(Color.WHITE);

        mPieChart3.setTransparentCircleColor(Color.WHITE);
        mPieChart3.setTransparentCircleAlpha(110);

        mPieChart3.setHoleRadius(40f);
        mPieChart3.setTransparentCircleRadius(50f);

        mPieChart3.setDrawCenterText(false);

        mPieChart3.setRotationAngle(0);
        // 触摸旋转
        mPieChart3.setRotationEnabled(true);
        mPieChart3.setHighlightPerTapEnabled(true);
        /////////
        mPieChart4.setDrawHoleEnabled(true);
        mPieChart4.setHoleColor(Color.WHITE);

        mPieChart4.setTransparentCircleColor(Color.WHITE);
        mPieChart4.setTransparentCircleAlpha(110);

        mPieChart4.setHoleRadius(40f);
        mPieChart4.setTransparentCircleRadius(50f);
       // mPieChart4.setTransparentCircleRadius(61f);

        mPieChart4.setDrawCenterText(false);

        mPieChart4.setRotationAngle(0);
        // 触摸旋转
        mPieChart4.setRotationEnabled(true);
        mPieChart4.setHighlightPerTapEnabled(true);
        /////////
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        ArrayList<PieEntry> entries2 = new ArrayList<PieEntry>();
        ArrayList<PieEntry> entries3 = new ArrayList<PieEntry>();
        ArrayList<PieEntry> entries4 = new ArrayList<PieEntry>();
        if (status.equals("-1")||status.equals("-2")){
            mPieChart.setCenterText("无");
            mPieChart.setCenterTextColor(Color.BLACK);
            mPieChart.setCenterTextSize(20);
            mPieChart.setHoleColor(Color.GRAY);
            mPieChart2.setDrawCenterText(true);
            mPieChart2.setCenterText("无");
            mPieChart2.setCenterTextColor(Color.BLACK);
            mPieChart2.setCenterTextSize(15);
            mPieChart2.setHoleColor(Color.GRAY);
            mPieChart3.setDrawCenterText(true);
            mPieChart3.setCenterText("无");
            mPieChart3.setCenterTextColor(Color.BLACK);
            mPieChart3.setCenterTextSize(15);
            mPieChart3.setHoleColor(Color.GRAY);
            mPieChart4.setDrawCenterText(true);
            mPieChart4.setCenterText("无");
            mPieChart4.setCenterTextColor(Color.BLACK);
            mPieChart4.setCenterTextSize(15);
            mPieChart4.setHoleColor(Color.GRAY);
            entries.add(new PieEntry(0, "碳水化合物"));
            entries.add(new PieEntry(0, "蛋白质"));
            entries.add(new PieEntry(0, "脂肪"));
            entries2.add(new PieEntry(0, "碳水化合物"));
            entries3.add(new PieEntry(0, "蛋白质"));
            entries4.add(new PieEntry(0, "脂肪"));
            //设置数据
            setData(mPieChart,entries);
            setData2(mPieChart2,entries2,"cho");
            setData2(mPieChart3,entries3,"protein");
            setData2(mPieChart4,entries4,"fats");
        }
        //变化监听
       // mPieChart.setOnChartValueSelectedListener(this);
        //模拟数据
        else{
            BigDecimal bd = new BigDecimal((double)n);
            bd = bd.setScale(scale,rou);
            //设置中间文件
            mPieChart.setCenterText(bd+"%");

        entries.add(new PieEntry(list_static.get(0), "碳水化合物"));
        entries.add(new PieEntry(list_static.get(1), "蛋白质"));
        entries.add(new PieEntry(list_static.get(2), "脂肪"));
        if(list.get(1)<=100){
            entries2.add(new PieEntry(list.get(1), ""));
            entries2.add(new PieEntry(100-list.get(1), ""));
        }
        else {
        entries2.add(new PieEntry(list.get(1), ""));
            mPieChart2.setDrawCenterText(true);
            mPieChart2.setCenterTextColor(Color.RED);
            mPieChart2.setCenterText("超");
        }
        if(list.get(2)<=100){
        entries3.add(new PieEntry(list.get(2), ""));
        entries3.add(new PieEntry(100-list.get(2), ""));}
        else {
            entries3.add(new PieEntry(list.get(2), ""));
            mPieChart3.setDrawCenterText(true);
            mPieChart3.setCenterTextColor(Color.RED);
            mPieChart3.setCenterText("超");

        }
        if(list.get(3)<=100){
        entries4.add(new PieEntry(list.get(3), ""));
        entries4.add(new PieEntry(100-list.get(3), ""));}
        else {
            entries4.add(new PieEntry(list.get(3), ""));
            mPieChart4.setDrawCenterText(true);
            mPieChart4.setCenterTextColor(Color.RED);
            mPieChart4.setCenterText("超");
        }
            //设置数据
            setData(mPieChart,entries);
            setData2(mPieChart2,entries2,"cho");
            setData2(mPieChart3,entries3,"protein");
            setData2(mPieChart4,entries4,"fats");
        }
       // entries.add(new PieEntry(10, "不及格"));

        //设置数据
        //setData(entries);
        mPieChart.animateY(1400,                     Easing.EasingOption.EaseInOutQuad);

        Legend l = mPieChart.getLegend();
        Legend l2 = mPieChart2.getLegend();
        Legend l3 = mPieChart3.getLegend();
        Legend l4 = mPieChart4.getLegend();

        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

//        l2.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
//      //  l2.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
//        l2.setOrientation(Legend.LegendOrientation.VERTICAL);
        l2.setDrawInside(false);
        l2.setXEntrySpace(7000f);
        l2.setYEntrySpace(0f);
        l2.setYOffset(0f);
        l3.setDrawInside(false);
        l3.setXEntrySpace(7000f);
        l3.setYEntrySpace(0f);
        l3.setYOffset(0f);

        l4.setDrawInside(false);
        l4.setXEntrySpace(7000f);
        l4.setYEntrySpace(0f);
        l4.setYOffset(0f);




        // 输入标签样式
        mPieChart.setEntryLabelColor(Color.BLACK);
        mPieChart.setEntryLabelTextSize(12f);
        //设置数据



//        if(current_month!=monthNow){
//            if(check_change(reg_year,reg_month,yearNow,monthNow)==1){
//                AlertDialog.Builder dialog = new AlertDialog.Builder(MainInterface.this);
//                dialog.setTitle("提示");
//                dialog.setMessage("请您记录并修改您的体重和身高");
//                dialog.setCancelable(true);
//                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = new Intent(MainInterface.this,ChangInfo.class);
//                        intent.putExtra("changed_user",loginuser);
//                        startActivity(intent);
//                    }
//                });
//                dialog.setNegativeButton("不想修改", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        mUserDataManager.insert_userhw(loginuser,weight,height,yearNow+"-"+monthNow);
//
//                    }
//                });
//                dialog.show();
//            }
//        }
//        if(current_month==monthNow)
//        {
//            Log.i(TAG,"当前月份已经修改过信息了");
//        }
//  =1要修改  ！= 1 不修改
/////////////////////////////////////////////////
//        food_detection.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent_MainInterface_to_FoodDetection = new Intent(MainInterface.this,FoodDetection.class);
//                startActivity(intent_MainInterface_to_FoodDetection);
//                //finish();
//
//            }
//        });
//        nutrition_intake.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent_MainInterface_to_Nutrition = new Intent(MainInterface.this, NutritionIntake.class);
//                intent_MainInterface_to_Nutrition.putExtra("user",loginuser);
//                startActivity(intent_MainInterface_to_Nutrition);
//                Log.i(TAG,"GOTO NUTRI");
//                //finish();
//            }
//        });
//
//        food_list.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent_MainInterface_FoodMenu = new Intent(MainInterface.this,FoodMenu2.class);
//                startActivity(intent_MainInterface_FoodMenu);
//                Log.i(TAG,"GOTO MENU");
//                //finish();
//
//
//            }
//        });

//        setting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent_MainInterface_Setting = new Intent(MainInterface.this,Setting.class);
//                intent_MainInterface_Setting.putExtra("user",loginuser);
//                startActivity(intent_MainInterface_Setting);
//                Log.i(TAG,"goto setting");
//                //finish();
//            }
//        });
       // onBackPressed();
       // List<String> infolist= mUserDataManager.findUserInfo("GOD2");
        //Log.i(TAG,"info"+infolist)

///////////////////////饼状图

//        myPieChart = findViewById(R.id.mypiechat);
//        myPieChart.setRadius(DensityUtils.dp2px(this,80));
//       // myPieChart.setOnItemClickListener(MainInterface.class);
//        List<MyPieChart.PieEntry> pieEntries = new ArrayList<>();
//        int i =1;
//        pieEntries.add(new MyPieChart.PieEntry(i, R.color.chart_orange, true));
//        pieEntries.add(new MyPieChart.PieEntry(i+5, R.color.chart_green, false));
//        pieEntries.add(new MyPieChart.PieEntry(i+2, R.color.chart_blue, false));
//        pieEntries.add(new MyPieChart.PieEntry(0, R.color.chart_purple, false));
//        myPieChart.setPieEntries(pieEntries);

    }
    private void setData(PieChart Chart,ArrayList<PieEntry> entries) {
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        //数据和颜色
        ArrayList<Integer> colors = new ArrayList<Integer>();
//        for (int c : ColorTemplate.LIBERTY_COLORS)
//            colors.add(c);
//        for (int c : ColorTemplate.PASTEL_COLORS)
//            colors.add(c);
//        for (int c : ColorTemplate.MATERIAL_COLORS)
//            colors.add(c);
//        for (int c : ColorTemplate.JOYFUL_COLORS)
//            colors.add(c);
        colors.add(Color.rgb(192,255,140));
        colors.add(Color.rgb(140,234,255));
        colors.add(Color.rgb(255,208,140));
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        Chart.setData(data);
        Chart.highlightValues(null);
        //刷新
        Chart.invalidate();
    }
    private void setData2(PieChart Chart,ArrayList<PieEntry> entries,String nutri) {
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        //数据和颜色
        ArrayList<Integer> colors = new ArrayList<Integer>();
//        for (int c : ColorTemplate.COLORFUL_COLORS)
 //           colors.add(c);
//        for (int c : ColorTemplate.JOYFUL_COLORS)
//        colors.add(c);
//        for (int c : ColorTemplate.COLORFUL_COLORS)
//            colors.add(c);
//        for (int c : ColorTemplate.LIBERTY_COLORS)
//            colors.add(c);
//        for (int c : ColorTemplate.PASTEL_COLORS)
//            colors.add(c);
        //colors.add(ColorTemplate.getHoloBlue());
        //colors.add(c);
        switch (nutri){
            case "cho":
                //colors.add(Color.BLACK)
                colors.add(Color.rgb(192,255,140));
                colors.add(Color.argb(50,0,0,0));
                dataSet.setColors(colors);
                break;
            case "protein":
                colors.add(Color.rgb(140,234,255));
                colors.add(Color.argb(50,0,0,0));
                break;
            case "fats":
                colors.add(Color.rgb(255,208,140));
                colors.add(Color.argb(50,0,0,0));
                break;
        }

        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(8f);
        data.setValueTextColor(Color.BLACK);
        Chart.setData(data);
        Chart.highlightValues(null);
        //刷新
        Chart.invalidate();
    }

//    @Override
//    public void onClick(int position) {
//        Toast.makeText(this, "点击了" + position, Toast.LENGTH_SHORT).show();
//
//    }


}
