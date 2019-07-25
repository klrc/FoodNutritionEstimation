package com.example.camera;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;
import android.widget.EditText;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class UserDataManager {             //用户数据管理类
    //一些宏定义和声明
    private static final String TAG = "UserDataManager";
    private static final String DB_NAME = "user_data";
    private static final String TABLE_NAME = "users_info";
    public static final String ID = "_id";
    public static final String USER_NAME = "USER_NAME"; //啥情况嘛 数据库里列名是大写 这里是小写还能传进去？服气了  不影响。
    //public static final String USER_PWD = "user_pwd";
    public static final String USER_DAYS = "Days";
    public static final String USER_BIRTH = "Birth";
    public static final String USER_SEX = "Sex";
    public static final String USER_WEIGHT = "Weight";
    public static final String USER_HEIGHT = "Height";
    public static final String USER_BMI = "BMI";
    public static final String USER_KCAL = "Kcal";
    public static final String USER_PROTEIN = "Protein";
    public static final String USER_FATS = "Fats";
    public static final String USER_CHO = "CHO";
    public static final String D1KCAL = "D1Kcal";
    public static final String UD1CHO = "D1CHO";
    public static final String D1PROTEIN = "D1Protein";
    public static final String D1FATS = "D1Fats";
    public static final String D2KCAL = "D2Kcal";
    public static final String UD2CHO = "D2CHO";
    public static final String D2PROTEIN = "D2Protein";
    public static final String D2FATS = "D2Fats";
    public static final String D3KCAL = "D3Kcal";
    public static final String UD3CHO = "D3CHO";
    public static final String D3PROTEIN = "D3Protein";
    public static final String D3FATS = "D3Fats";
    public static final String D4KCAL = "D4Kcal";
    public static final String UD4CHO = "D4CHO";
    public static final String D4PROTEIN = "D4Protein";
    public static final String D4FATS = "D4Fats";
    public static final String D5KCAL = "D5Kcal";
    public static final String UD5CHO = "D5CHO";
    public static final String D5PROTEIN = "D5Protein";
    public static final String D5FATS = "D5Fats";
    public static final String D6KCAL = "D6Kcal";
    public static final String UD6CHO = "D6CHO";
    public static final String D6PROTEIN = "D6Protein";
    public static final String D6FATS = "D6Fats";
    public static final String D7KCAL = "D7Kcal";
    public static final String UD7CHO = "D7CHO";
    public static final String D7PROTEIN = "D7Protein";
    public static final String D7FATS = "D7Fats";

    private static final int DB_VERSION = 2;
    private Context mContext = null;

    //创建用户info表
    private static final String DB_CREATE = "CREATE TABLE " + TABLE_NAME + " (" //TABLE NAME = "users_info"
            + "ID integer primary key autoincrement,"
            + "USER_NAME  varchar(100),"
            + "Days varchar(20),"
            + "Sex varchar(10),"
            + "Birth varchar(20),"
            + "Weight varchar(20),"
            + "Height varchar(20),"
            + "BMI varchar(20),"
            + "Kcal varchar(20),"
            + "Protein varchar(20),"
            + "Fats varchar(20),"
            + "CHO varchar(20),"
            + "D1Kcal varchar(20),"
            + "D1CHO varchar(20),"
            + "D1Protein varchar(20),"
            + "D1Fats varchar(20),"
            + "D2Kcal varchar(20),"
            + "D2CHO varchar(20),"
            + "D2Protein varchar(20),"
            + "D2Fats varchar(20),"
            + "D3Kcal varchar(20),"
            + "D3CHO varchar(20),"
            + "D3Protein varchar(20),"
            + "D3Fats varchar(20),"
            + "D4Kcal varchar(20),"
            + "D4CHO varchar(20),"
            + "D4Protein varchar(20),"
            + "D4Fats varchar(20),"
            + "D5Kcal varchar(20),"
            + "D5CHO varchar(20),"
            + "D5Protein varchar(20),"
            + "D5Fats varchar(20),"
            + "D6Kcal varchar(20),"
            + "D6CHO varchar(20),"
            + "D6Protein varchar(20),"
            + "D6Fats varchar(20),"
            + "D7Kcal varchar(20),"
            + "D7CHO varchar(20),"
            + "D7Protein varchar(20),"
            + "D7Fats varchar(20))";


    private SQLiteDatabase mSQLiteDatabase = null;
    private DataBaseManagementHelper mDatabaseHelper = null;

    //DataBaseManagementHelper继承自SQLiteOpenHelper
    private static class DataBaseManagementHelper extends SQLiteOpenHelper {//继承字抽象类qoh，需要重写 oncreate和 onupgrade两个方法

        DataBaseManagementHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i(TAG,"db.getVersion()="+db.getVersion());
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
            db.execSQL(DB_CREATE);
            Log.i(TAG, "db.execSQL(DB_CREATE)");
            Log.e(TAG, DB_CREATE);  //drop if 操作是为了删除原有的  创建新的，是一种常用的更新方法
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i(TAG, "DataBaseManagementHelper onUpgrade");
            onCreate(db);
        }
    }

    public UserDataManager(Context context) {
        mContext = context;
        Log.i(TAG, "UserDataManager construction!");
    }
    //打开数据库
    public void openDataBase() throws SQLException {
        mDatabaseHelper = new DataBaseManagementHelper(mContext);
        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
    }

    //log

    //关闭数据库
    public void closeDataBase() throws SQLException {
        mDatabaseHelper.close();
    }
    //添加新用户，即注册
    public long insertUserData(UserData userData) {
        String userName=userData.getUserName();
        String Birth = userData.getBirth();
        String Sex = userData.getSex();
        double Days = userData.getDays();
        double Weight = userData.getWeight();
        double Height = userData.getHeight();
        double BMI = userData.getBMI();
        double Kcal = userData.getKcal();
        double Protein = userData.getProtein();
        double Fats = userData.getFats();
        double CHO = userData.getCHO();



        //double Na = userData.getNa();

        ContentValues values = new ContentValues();
        values.put(USER_NAME, userName);
        values.put(USER_DAYS,Days);
        values.put(USER_BIRTH,Birth);
        values.put(USER_SEX,Sex);
        values.put(USER_WEIGHT,Weight);
        values.put(USER_HEIGHT,Height);
        values.put(USER_BMI,BMI);

        values.put(USER_KCAL,Kcal);
        values.put(USER_PROTEIN,Protein);
        values.put(USER_FATS,Fats);
        values.put(USER_CHO,CHO);
        //values.put(USER_NA,Na);
        //values.put(USER_PWD, userPwd);
        return mSQLiteDatabase.insert(TABLE_NAME, ID, values);
    }
//    public long insertweeknutri(UserData userData){//,Date startTime) {
//        String userName = userData.getUserName();
//        final Calendar mCalendar = Calendar.getInstance();
//        int week = mCalendar.get(Calendar.DAY_OF_WEEK);
        //Date endTime = mCalendar.getTime();
//        double D1Kcal = userData.getD1Kcal();
//        double D1CHO = userData.getD1CHO();
//        double D1Protein = userData.getD1Protein();
//        double D1Fats = userData.getD1Fats();
//        double D2Kcal = userData.getD2Kcal();
//        double D2CHO = userData.getD2CHO();
//        double D2Protein = userData.getD2Protein();
//        double D2Fats = userData.getD2Fats();
//        double D3Kcal = userData.getD3Kcal();
//        double D3CHO = userData.getD3CHO();
//        double D3Protein = userData.getD3Protein();
//        double D3Fats = userData.getD3Fats();
//        double D4Kcal = userData.getD4Kcal();
//        double D4CHO = userData.getD4CHO();
//        double D4Protein = userData.getD4Protein();
//        double D4Fats = userData.getD4Fats();
//        double D5Kcal = userData.getD5Kcal();
//        double D5CHO = userData.getD5CHO();
//        double D5Protein = userData.getD5Protein();
//        double D5Fats = userData.getD5Fats();
//        double D6Kcal = userData.getD6Kcal();
//        double D6CHO = userData.getD6CHO();
//        double D6Protein = userData.getD6Protein();
//        double D6Fats = userData.getD6Fats();
//        double D7Kcal = userData.getD7Kcal();
//        double D7CHO = userData.getD7CHO();
//        double D7Protein = userData.getD7Protein();
//        double D7Fats = userData.getD7Fats();
        ContentValues values = new ContentValues();
//        int days = 0;
//        long time1 = startTime.getTime();
//        long time2 = endTime.getTime();
//        long diff;
//        if (time1 < time2) {
//            diff = time2 - time1;
//        } else {
//            diff = time1 - time2;
//        }
//        days = (int) (diff / (24 * 60 * 60 * 1000));
//        switch (week){
//            case 1:
//                values.put(D1KCAL, D1Kcal);
//                values.put(UD1CHO, D1CHO);
//                values.put(D1PROTEIN, D1Protein);
//                values.put(D1FATS, D1Fats);
//                break;
//            case 2:
//                values.put(D2KCAL, D2Kcal);
//                values.put(UD2CHO, D2CHO);
//                values.put(D2PROTEIN, D2Protein);
//                values.put(D2FATS, D2Fats);
//                break;
//            case 3:
//                values.put(D3KCAL, D3Kcal);
//                values.put(UD3CHO, D3CHO);
//                values.put(D3PROTEIN, D3Protein);
//                values.put(D3FATS, D3Fats);
//                break;
//            case 4:
//                values.put(D4KCAL, D4Kcal);
//                values.put(UD4CHO, D4CHO);
//                values.put(D4PROTEIN, D4Protein);
//                values.put(D4FATS, D4Fats);
//                break;
//            case 5:
//                values.put(D5KCAL, D5Kcal);
//                values.put(UD5CHO, D5CHO);
//                values.put(D5PROTEIN, D5Protein);
//                values.put(D5FATS, D5Fats);
//                break;
//            case 6:
//                values.put(D6KCAL, D6Kcal);
//                values.put(UD6CHO, D6CHO);
//                values.put(D6PROTEIN, D6Protein);
//                values.put(D6FATS, D6Fats);
//
//                break;
//            case 7:
//                values.put(D7KCAL, D7Kcal);
//                values.put(UD7CHO, D7CHO);
//                values.put(D7PROTEIN, D7Protein);
//                values.put(D7FATS, D7Fats);
//                break;
//        }
//        return mSQLiteDatabase.insert(TABLE_NAME, ID, values);
 //   }
//            if (days == 1) {
//        values.put(D1KCAL, D1Kcal);
//        values.put(UD1CHO, D1CHO);
//        values.put(D1PROTEIN, D1Protein);
//        values.put(D1FATS, D1Fats);
//        return mSQLiteDatabase.insert(TABLE_NAME, ID, values);
//
//    }
//            if (days == 2) {
//        values.put(D2KCAL, D2Kcal);
//        values.put(UD2CHO, D2CHO);
//        values.put(D2PROTEIN, D2Protein);
//        values.put(D2FATS, D2Fats);
//        return mSQLiteDatabase.insert(TABLE_NAME, ID, values);
//    }
//            if (days == 3) {
//        values.put(D3KCAL, D3Kcal);
//        values.put(UD3CHO, D3CHO);
//        values.put(D3PROTEIN, D3Protein);
//        values.put(D3FATS, D3Fats);
//        return mSQLiteDatabase.insert(TABLE_NAME, ID, values);
//    }
//            if (days == 4) {
//        values.put(D4KCAL, D4Kcal);
//        values.put(UD4CHO, D4CHO);
//        values.put(D4PROTEIN, D4Protein);
//        values.put(D4FATS, D4Fats);
//        return mSQLiteDatabase.insert(TABLE_NAME, ID, values);
//    }
//            if (days == 5) {
//        values.put(D5KCAL, D5Kcal);
//        values.put(UD5CHO, D5CHO);
//        values.put(D5PROTEIN, D5Protein);
//        values.put(D5FATS, D5Fats);
//        return mSQLiteDatabase.insert(TABLE_NAME, ID, values);
//    }
//            if (days == 6) {
//        values.put(D6KCAL, D6Kcal);
//        values.put(UD6CHO, D6CHO);
//        values.put(D6PROTEIN, D6Protein);
//        values.put(D6FATS, D6Fats);
//        return mSQLiteDatabase.insert(TABLE_NAME, ID, values);
//    }
//            if (days == 7) {
//        values.put(D7KCAL, D7Kcal);
//        values.put(UD7CHO, D7CHO);
//        values.put(D7PROTEIN, D7Protein);
//        values.put(D7FATS, D7Fats);
//        return mSQLiteDatabase.insert(TABLE_NAME, ID, values);
//    }



//        double Weight = userData.getWeight();
//        double Height = userData.getHeight();
//        double BMI = userData.getBMI();
//        double Kcal = userData.getKcal();
//        double Protein = userData.getProtein();
//        double Fats = userData.getFats();
//        double CHO = userData.getCHO();
//        //double Na = userData.getNa();
//
//        ContentValues values = new ContentValues();
//        values.put(USER_NAME, userName);
//        values.put(USER_BIRTH,Birth);
//        values.put(USER_SEX,Sex);
//        values.put(USER_WEIGHT,Weight);
//        values.put(USER_HEIGHT,Height);
//        values.put(USER_BMI,BMI);
//        values.put(USER_KCAL,Kcal);
//        values.put(USER_PROTEIN,Protein);
//        values.put(USER_FATS,Fats);
//        values.put(USER_CHO,CHO);
//        //values.put(USER_NA,Na);
//        //values.put(USER_PWD, userPwd);
//        return mSQLiteDatabase.insert(TABLE_NAME, ID, values);
//    }


    //更新用户信息，如修改密码
//    public boolean updateUserData(UserData userData) {
//        int id = userData.getUserId();
//        String userName = userData.getUserName();
//        String userPwd = userData.getUserPwd();
//        ContentValues values = new ContentValues();
//        values.put(USER_NAME, userName);
//        values.put(USER_PWD, userPwd);
//        String where = USER_NAME + "=" + "=\"" + userName + "\"";
//        return mSQLiteDatabase.update(TABLE_NAME, values, where, null) > 0;
//        return mSQLiteDatabase.update(TABLE_NAME, values, ID + "=" + id, null) > 0;
//    }
    //
    public Cursor fetchUserData(int id) throws SQLException {
        Cursor mCursor = mSQLiteDatabase.query(false, TABLE_NAME, null, ID
                + "=" + id, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    //
    public Cursor fetchAllUserDatas() {
        return mSQLiteDatabase.query(TABLE_NAME, null, null, null, null, null,
                null);
    }
    //根据id删除用户
    public boolean deleteUserData(int id) {
        return mSQLiteDatabase.delete(TABLE_NAME, ID + "=" + id, null) > 0;
    }
    //根据用户名注销
    public boolean deleteUserDataByName(String name) {
        return mSQLiteDatabase.delete(TABLE_NAME, USER_NAME + "=\"" + name+"\"", null) > 0;
    }
    //删除所有用户
    public boolean deleteAllUserDatas() {
        return mSQLiteDatabase.delete(TABLE_NAME, null, null) > 0;
    }

    //
    public String getStringByColumnName(String columnName, int id) {
        Cursor mCursor = fetchUserData(id);
        int columnIndex = mCursor.getColumnIndex(columnName);
        String columnValue = mCursor.getString(columnIndex);
        mCursor.close();
        return columnValue;
    }
    //
    public boolean updateUserDataById(String columnName, int id,
                                      String columnValue) {
        ContentValues values = new ContentValues();
        values.put(columnName, columnValue);
        return mSQLiteDatabase.update(TABLE_NAME, values, ID + "=" + id, null) > 0;
    }
    //根据用户名找用户，可以判断注册时用户名是否已经存在
    public int findUserByName(String userName){
        Log.i(TAG,"findUserByName , userName="+userName);
        int result=0;
        Cursor mCursor=mSQLiteDatabase.query(TABLE_NAME, null, USER_NAME+"=?",new String[]{userName}, null, null, null);
        if(mCursor!=null){
            result=mCursor.getCount();
            mCursor.close();
            Log.i(TAG,"findUserByName , result="+result);
        }
        return result;
    }
    //根据用户名和密码找用户，用于登录
    public int findUserByNameAndPwd(String userName,String pwd){
        Log.i(TAG,"findUserByNameAndPwd");
        int result=0;
        Cursor mCursor=mSQLiteDatabase.query(TABLE_NAME, null, USER_NAME+"=?",
                new String[]{userName,pwd}, null, null, null);
        if(mCursor!=null){
            result=mCursor.getCount();
            mCursor.close();
            Log.i(TAG,"findUserByNameAndPwd , result="+result);
        }
        return result;
    }
    public void showList(String colunmName){
        mDatabaseHelper = new DataBaseManagementHelper(mContext);
        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
        Cursor cursor = mSQLiteDatabase.query("users_info",null,null,null,null,null,null);
        List<String> userlist = new ArrayList<String>();
        if(cursor.moveToFirst()){
            do{
                String username = cursor.getString(cursor.getColumnIndex("USER_NAME"));
                Log.d("userlist","username"+username);

            }while (cursor.moveToNext());
        }
        cursor.close();

    }
    //返回一个用户名的列表
    public List<String> showUserName(String colunmName){
        //mDatabaseHelper = new DataBaseManagementHelper(mContext);
        //mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
        Cursor cursor = mSQLiteDatabase.query("users_info",null,null,null,null,null,null);
        List<String> userlist = new ArrayList<String>();
        if(cursor.moveToFirst()){
            do{
                String username = cursor.getString(cursor.getColumnIndex(colunmName));
                userlist.add(username);

            }while (cursor.moveToNext());
        }
        cursor.close();
        return userlist;
    }
    //问题记录 现在是用户名是数字的可以找到 ， AA这个字符类型的找不到。
    public List<String> findUserInfo(String userName){
        int result=0;
        Log.i(TAG,"findUserInfo");
        Log.i(TAG, userName);
        //Cursor cursor = mSQLiteDatabase.query("users_info",null,USER_NAME+"="+userName,null,null,null,null);
        Cursor cursor = mSQLiteDatabase.query("users_info",null,"USER_NAME=?", new String[]{userName},null,null,null,null);
        Log.i(TAG,"findUserInfo3");
        List<String> userinfo = new ArrayList<String>();
        if(cursor.moveToFirst()) {
            do {
                if (cursor.getCount()==0){Log.i(TAG,"NULL");}
                    String weight = cursor.getString(cursor.getColumnIndex("Weight"));//cursor.getString(cursor.getColumnIndex("Weight"));
                    String height = cursor.getString(cursor.getColumnIndex("Height"));
                    String BMI = cursor.getString(cursor.getColumnIndex("BMI"));
                    String birth = cursor.getString(cursor.getColumnIndex("Birth"));
                    String kcal = cursor.getString(cursor.getColumnIndex("Kcal"));
                    String CHO = cursor.getString(cursor.getColumnIndex("CHO"));
                    String protein = cursor.getString(cursor.getColumnIndex("Protein"));
                    String fats = cursor.getString(cursor.getColumnIndex("Fats"));
                    //userinfo.add(sex);
                    userinfo.add(height);
                    userinfo.add(weight);
                    userinfo.add(BMI);
                    userinfo.add(birth);
                    userinfo.add(kcal);
                    userinfo.add(CHO);
                    userinfo.add(protein);
                    userinfo.add(fats);
            } while (cursor.moveToNext());}
        cursor.close();
        return userinfo;
    }
    public List<String> WeekKcalInfo(String userName){
        Log.i(TAG,"findWeekcalkInfo");
        Cursor cursor = mSQLiteDatabase.query("users_info",null,"USER_NAME=?", new String[]{userName},null,null,null,null);
        //Log.i(TAG,"findWeekInfo2");
        List<String> weekkcalinfo = new ArrayList<String>();
        if(cursor.moveToFirst()) {
            do {
                if (cursor.getCount()==0){Log.i(TAG,"NULL");}
                String d1Kcal = cursor.getString(cursor.getColumnIndex("D1Kcal"));//cursor.getString(cursor.getColumnIndex("Weight"));
                String d2Kcal = cursor.getString(cursor.getColumnIndex("D2Kcal"));
                String d3Kcal = cursor.getString(cursor.getColumnIndex("D3Kcal"));
                String d4Kcal = cursor.getString(cursor.getColumnIndex("D4Kcal"));
                String d5Kcal = cursor.getString(cursor.getColumnIndex("D5Kcal"));
                String d6Kcal = cursor.getString(cursor.getColumnIndex("D6Kcal"));
                String d7Kcal = cursor.getString(cursor.getColumnIndex("D7Kcal"));
                weekkcalinfo.add(d1Kcal);
                weekkcalinfo.add(d2Kcal);
                weekkcalinfo.add(d3Kcal);
                weekkcalinfo.add(d4Kcal);
                weekkcalinfo.add(d5Kcal);
                weekkcalinfo.add(d6Kcal);
                weekkcalinfo.add(d7Kcal);

            } while (cursor.moveToNext());}

        cursor.close();
        return weekkcalinfo;
    }
    public List<String> WeekCHOInfo(String userName){
        Log.i(TAG,"findWeekCHOInfo");
        Cursor cursor = mSQLiteDatabase.query("users_info",null,"USER_NAME=?", new String[]{userName},null,null,null,null);
        Log.i(TAG,userName);
        List<String> weekCHOinfo = new ArrayList<String>();
        if(cursor.moveToFirst()) {
            do {
                if (cursor.getCount()==0){Log.i(TAG,"NULL");}
                String d1CHO = cursor.getString(cursor.getColumnIndex("D1CHO"));
                String d2CHO = cursor.getString(cursor.getColumnIndex("D2CHO"));
                String d3CHO = cursor.getString(cursor.getColumnIndex("D3CHO"));
                String d4CHO = cursor.getString(cursor.getColumnIndex("D4CHO"));
                String d5CHO = cursor.getString(cursor.getColumnIndex("D5CHO"));
                String d6CHO = cursor.getString(cursor.getColumnIndex("D6CHO"));
                String d7CHO = cursor.getString(cursor.getColumnIndex("D7CHO"));
                weekCHOinfo.add(d1CHO);
                weekCHOinfo.add(d2CHO);
                weekCHOinfo.add(d3CHO);
                weekCHOinfo.add(d4CHO);
                weekCHOinfo.add(d5CHO);
                weekCHOinfo.add(d6CHO);
                weekCHOinfo.add(d7CHO);

            } while (cursor.moveToNext());}

        cursor.close();
       // Log.i(TAG,weekCHOinfo[1]);
        return weekCHOinfo;
    }
    public List<String> WeekProteinInfo(String userName){
        Log.i(TAG,"findWeekproteinInfo");
        Cursor cursor = mSQLiteDatabase.query("users_info",null,"USER_NAME=?", new String[]{userName},null,null,null,null);
        List<String> weekproteininfo = new ArrayList<String>();
        if(cursor.moveToFirst()) {
            do {
                if (cursor.getCount()==0){Log.i(TAG,"NULL");}
                String d1Protein = cursor.getString(cursor.getColumnIndex("D1Protein"));
                String d2Protein = cursor.getString(cursor.getColumnIndex("D2Protein"));
                String d3Protein = cursor.getString(cursor.getColumnIndex("D3Protein"));
                String d4Protein = cursor.getString(cursor.getColumnIndex("D4Protein"));
                String d5Protein = cursor.getString(cursor.getColumnIndex("D5Protein"));
                String d6Protein = cursor.getString(cursor.getColumnIndex("D6Protein"));
                String d7Protein = cursor.getString(cursor.getColumnIndex("D7Protein"));
                weekproteininfo.add(d1Protein);
                weekproteininfo.add(d2Protein);
                weekproteininfo.add(d3Protein);
                weekproteininfo.add(d4Protein);
                weekproteininfo.add(d5Protein);
                weekproteininfo.add(d6Protein);
                weekproteininfo.add(d7Protein);

            } while (cursor.moveToNext());}

        cursor.close();
        return weekproteininfo;
    }
    public List<String> WeekFatsInfo(String userName){
        Log.i(TAG,"findWeekfatsInfo");
        Cursor cursor = mSQLiteDatabase.query("users_info",null,"USER_NAME=?", new String[]{userName},null,null,null,null);
        List<String> weekfatsinfo = new ArrayList<String>();
        if(cursor.moveToFirst()) {
            do {
                if (cursor.getCount()==0){Log.i(TAG,"NULL");}
                String d1Fats = cursor.getString(cursor.getColumnIndex("D1Fats"));
                String d2Fats = cursor.getString(cursor.getColumnIndex("D2Fats"));
                String d3Fats = cursor.getString(cursor.getColumnIndex("D3Fats"));
                String d4Fats = cursor.getString(cursor.getColumnIndex("D4Fats"));
                String d5Fats = cursor.getString(cursor.getColumnIndex("D5Fats"));
                String d6Fats = cursor.getString(cursor.getColumnIndex("D6Fats"));
                String d7Fats = cursor.getString(cursor.getColumnIndex("D7Fats"));
                weekfatsinfo.add(d1Fats);
                weekfatsinfo.add(d2Fats);
                weekfatsinfo.add(d3Fats);
                weekfatsinfo.add(d4Fats);
                weekfatsinfo.add(d5Fats);
                weekfatsinfo.add(d6Fats);
                weekfatsinfo.add(d7Fats);

            } while (cursor.moveToNext());}

        cursor.close();
        return weekfatsinfo;
    }
    public void changinfo(String userName,String columnName,String changed_value) {
        int result = 0;
        // Log.i(TAG,"changeInfo");
        Log.i(TAG, userName);
        ContentValues values = new ContentValues();
        values.put(columnName, changed_value);
        mSQLiteDatabase.update(TABLE_NAME, values, "USER_NAME=?", new String[]{userName});
    }
}
