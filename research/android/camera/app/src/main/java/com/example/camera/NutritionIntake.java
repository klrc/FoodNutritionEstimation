package com.example.camera;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.app.Activity;
import android.view.View.OnClickListener;
import com.example.camera.view.HistoryChartView;
import com.example.camera.view.HistoryChartView.OnViewLayoutListener;
import com.example.camera.view.HistoryModeView;

import java.util.Calendar;
import java.util.List;

public class NutritionIntake extends Activity implements OnViewLayoutListener {

    private final static int DAY_MODE = 0;
    private final static int WEEK_MODE = 1;
    private final static int MONTH_MODE = 2;
    private final static int YEAR_MODE = 3;
    private String TAG = "NutritionIntake";
    private UserDataManager mUserDataManager;

    private HistoryModeView hmvDay;
    private HistoryModeView hmvWeek;
    private HistoryModeView hmvMonth;
    private HistoryModeView hmvYear;
    private HistoryModeView[] hmvArray;

    private HistoryChartView mHistoryChartView;

    private int mode = DAY_MODE;

    private int checkColor;
    private int unCheckColor;

    // ==================================================================================
    // Test Data//与柱状图的bug无关
    private String mStrDayCHOData = "220,230,230,230";
    private String mStrDayKcalData = "2760,2760,2760,2760,2760,2760";
    private String mStrDayProteinData = "800,700,900,300";
    private String mStrDayFatsData = "800,90,90,300";

    private String mStrWeekCHOData="";// = "260,250,230,240,280,300,220";
    private String mStrWeekKcalData="";// = "600,600,600,600,600,600,600";
    private String mStrWeekProteinData="";
    private String mStrWeekFatsData="";// = "600,700,800,800,900,950,1000";
    private String mStrWeekThresholdData="";

    String username;
    String loginuser;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home_item:
//                    Intent intent_MainInterface_to_FoodDetection = new Intent(FoodDetection.this,MainInterface.class);
//                    startActivity(intent_MainInterface_to_FoodDetection);
                    onBackPressed();
                    return true;
                case R.id.nutri_table_item:
//                    Intent intent_MainInterface_to_Nutrition = new Intent(NutritionIntake.this, NutritionIntake.class);
//                    Log.i(TAG,"出去的用户是："+loginuser);
//                    intent_MainInterface_to_Nutrition.putExtra("user",loginuser);
//                    startActivity(intent_MainInterface_to_Nutrition);
//                    finish();
//                    Log.i(TAG,"GOTO NUTRI");
//                    break;
                    return true;
                case R.id.food_set_item:
                    Intent intent_MainInterface_FoodMenu = new Intent(NutritionIntake.this,FoodMenu2.class);
                    intent_MainInterface_FoodMenu.putExtra("user",loginuser);
                    startActivity(intent_MainInterface_FoodMenu);
                    finish();
                    Log.i(TAG,"GOTO MENU");
                    break;
                case R.id.detect_item:
                    Intent intent_Detect = new Intent(NutritionIntake.this,FoodDetection.class);
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
//    private String mStrMonthRoomData = "22,23,23,23,23,23,23,22,23,23,23,23,23,23,22,23,23,23,23,23,23,22,23,23,23,23,23,23,24,26";
//    private String mStrMonthSettingData = "27,26,26,25,24,25,26,27,26,26,26,27,25,27,26,26,26,26,26,26,26,24,26,26,26,26,23,22,24,25";
//    private String mStrMonthPowerTimeData = "90,80,80,90,88,92,93,80,80,90,23,100,99,97,90,80,90,80,70,80,90,91,100,90,90,70,70,80,90,80";

//    private String mStrYearRoomData = "22,23,23,23,23,23,23,23,23,23,23,23";
//    private String mStrYearSettingData = "26,26,26,26,26,26,26,23,23,23,23,23";
//    private String mStrYearPowerTimeData = "80,90,91,93,100,100,50,91,93,100,100,50";

    // ===================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_intake);
        Intent intent = getIntent();
        username =  intent.getStringExtra("user");
        loginuser = intent.getStringExtra("user");
       // BottomNavigationView navigation = findViewById(R.id.navigation_table);
        //navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            mUserDataManager.openDataBase();
        }
        Log.i(TAG,"KAISHI");
        initData();
        initView();
    }

    public void initView() {
        mHistoryChartView = (HistoryChartView) findViewById(R.id.mutiHistoryChartView);

        hmvDay = (HistoryModeView) findViewById(R.id.hmv_day);
        hmvWeek = (HistoryModeView) findViewById(R.id.hmv_week);
        //hmvMonth = (HistoryModeView) findViewById(R.id.hmv_month);
        //hmvYear = (HistoryModeView) findViewById(R.id.hmv_year);
        hmvArray = new HistoryModeView[]{hmvDay, hmvWeek};
        hmvDay.setOnClickListener(onClickListener);
        hmvWeek.setOnClickListener(onClickListener);
//        hmvMonth.setOnClickListener(onClickListener);
//        hmvYear.setOnClickListener(onClickListener);
        mHistoryChartView.setOnViewLayoutListener(this);
    }

    public void initData() {
        checkColor = this.getResources().getColor(R.color.saswell_yellow);
        unCheckColor = this.getResources().getColor(R.color.saswell_light_grey);
    }

    /**
     * 点击事件实例
     */
    private View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.hmv_day:
                    mode = DAY_MODE;
                    break;

                case R.id.hmv_week:
                    mode = WEEK_MODE;
                    break;

//                case R.id.hmv_month:
//                    mode = MONTH_MODE;
//                    break;
//
//                case R.id.hmv_year:
//                    mode = YEAR_MODE;
//                    break;

                default:
                    break;
            }
            Log.i(TAG,"UP2");
            updateView();
        }
    };

    /**
     * 更新界面
     */
    protected void updateView() {
        for (int i = 0; i < hmvArray.length; i++) {
            if (i == mode) {
                hmvArray[i].setHistoryViewSelected(true);
                hmvArray[i].setTextColor(checkColor);
            } else {
                hmvArray[i].setHistoryViewSelected(false);
                hmvArray[i].setTextColor(unCheckColor);
            }
        }
        mHistoryChartView.setData(getAllHistoryViewData(), mode);
    }

    /**
     * 获取要绘制的历史数据全状态
     *
     * @return 全状态数据
     */
    private String getAllHistoryViewData() {
        String allHistoryData = "";
        Calendar mcalendar = Calendar.getInstance();
        int week = mcalendar.get(Calendar.DAY_OF_WEEK);

        mStrWeekCHOData="";// = "260,250,230,240,280,300,220";
        mStrWeekKcalData="";// = "600,600,600,600,600,600,600";
        mStrWeekProteinData="";
        mStrWeekFatsData="";
        Log.i(TAG,username);
        Log.i(TAG,"week"+Integer.toString(week));

//CHO//////////////////////////////////////////////////////////////////////
        //week是获取当前周几，比如周三，取周三之前的数据，后面几天全部补0
        List<String> CHOinfo = mUserDataManager.WeekCHOInfo(username);
        String[] CHOinfo2 = new String[CHOinfo.size()];
        CHOinfo.toArray(CHOinfo2);
        for (int i = 0;i<week;i++){
            mStrWeekCHOData+=CHOinfo2[i] + ',';//"500"+",";//

        }
        Log.i(TAG,"cho"+mStrWeekCHOData);
        for (int j =week;j<7;j++){
            mStrWeekCHOData+= "0"+",";

        }
        Log.i(TAG,"cho补齐"+mStrWeekCHOData);
        mStrWeekCHOData = mStrWeekCHOData.substring(0,mStrWeekCHOData.length() - 1);
        Log.i(TAG,"CHO"+mStrWeekCHOData);

//KCAL//////////////////////////////////////////////////////////////////////
        List<String> kcalinfo = mUserDataManager.WeekKcalInfo(username);
        String[] kcalinfo2 = new String[kcalinfo.size()];
        kcalinfo.toArray(kcalinfo2);
        for (int i = 1;i<=week;i++){
            mStrWeekKcalData+=kcalinfo2[i] + ',';//"400"+",";//
        }
        for (int j =week+1;j<=7;j++){
            mStrWeekKcalData+= "0"+",";
        }
        mStrWeekKcalData = mStrWeekKcalData.substring(0,mStrWeekKcalData.length()-1);
        Log.i(TAG,"KCAL"+mStrWeekKcalData);
//PROTEIN/////////////////////////////////////////////////////////////////////
        List<String> proteininfo = mUserDataManager.WeekProteinInfo(username);
        String[] proteininfo2 = new String[proteininfo.size()];
        proteininfo.toArray(proteininfo2);
        for (int i = 1;i<=week;i++){
            mStrWeekProteinData+=proteininfo2[i]+',';//"300"+",";//
        }
        for (int j =week+1;j<=7;j++){
            mStrWeekProteinData+= "0" +",";
        }
        mStrWeekProteinData = mStrWeekProteinData.substring(0,mStrWeekProteinData.length()-1);
        Log.i(TAG,"PRO"+mStrWeekProteinData);
//FATS//////////////////////////////////////////////////////////
        List<String> fatsinfo = mUserDataManager.WeekFatsInfo(username);
        String[] fatsinfo2 = new String[fatsinfo.size()];
        fatsinfo.toArray(fatsinfo2);
        for (int i = 1;i<=week;i++){
            mStrWeekFatsData+=fatsinfo2[i]+',';//"200"+",";
        }
        for (int j =week+1;j<=7;j++){
            mStrWeekFatsData+= "0" +",";
        }
        mStrWeekFatsData = mStrWeekFatsData.substring(0,mStrWeekFatsData.length()-1);
        Log.i(TAG,"FATS"+mStrWeekFatsData);
//DAYnutri//////////////////////////////////////////////////////////
        List<String> daynutriinfo = mUserDataManager.DayNutriInfo(username,week);
        String[] daynutriinfo2 = new String[daynutriinfo.size()];
        daynutriinfo.toArray(daynutriinfo2);
        List<String> userthreshold = mUserDataManager.findUserInfo(username);
        String[] userthreshold2 = new String[userthreshold.size()];
        userthreshold.toArray(userthreshold2);
        //mStrWeekThresholdData = userthreshold2[4]+","+userthreshold2[5]+","+userthreshold2[6]+","+userthreshold2[7]+","+"1000" + "," + "1500"+","+"2000";
        mStrWeekThresholdData = "3000,3000,3000,3000,3000,3000,3000";
        mStrDayProteinData = daynutriinfo2[1]+","+userthreshold2[5]+","+daynutriinfo2[2]+","+userthreshold2[6]+","
                +daynutriinfo2[3]+","+userthreshold2[7];//+","+daynutriinfo2[0]+","+daynutriinfo2[0];
        mStrDayKcalData = daynutriinfo2[0] + "," + daynutriinfo2[0] + "," + daynutriinfo2[0] + "," + daynutriinfo2[0] + "," + daynutriinfo2[0] + "," + daynutriinfo2[0] + "," + daynutriinfo2[0];
//不清楚这个代码咋弄的，day的柱状图竟然是按照proteindata的数据画的。这里没费功夫改，proteindata的数据赋值成本日的四种营养成分
//////////////////////////////////////////////////////////////////////////
        switch (mode) {
            case DAY_MODE:
                //allHistoryData = mStrDayKcalData + "-" + mStrDayCHOData
                  //      + "-"+ mStrDayProteinData + "-"+mStrDayFatsData;

                allHistoryData = mStrDayProteinData + "-" + mStrDayKcalData;
                break;
            case WEEK_MODE:
                allHistoryData = mStrWeekKcalData + "-" + mStrWeekCHOData  + "-"
                        + mStrWeekProteinData + "-" + mStrWeekFatsData + "-" + mStrWeekThresholdData;
                break;
//            case MONTH_MODE:
//                allHistoryData = mStrMonthRoomData + "-" + mStrMonthSettingData
//                        + "-" + mStrMonthPowerTimeData;
//                break;
//            case YEAR_MODE:
//                allHistoryData = mStrYearRoomData + "-" + mStrYearSettingData + "-"
//                        + mStrYearPowerTimeData;
//                break;
            default:
                break;
        }
        return allHistoryData;
    }

    @Override
    public void onLayoutSuccess() {
        //布局onlayout成功后，更新View数据
        Log.i(TAG,"UP1");
        updateView();
    }
}
