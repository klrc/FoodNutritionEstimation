package com.example.camera;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
<<<<<<< HEAD
import android.util.Log;
=======
import android.os.Build;
import android.util.Log;
import android.widget.EditText;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
>>>>>>> refs/remotes/origin/master

public class UserDataManager {             //用户数据管理类
    //一些宏定义和声明
    private static final String TAG = "UserDataManager";
    private static final String DB_NAME = "user_data";
<<<<<<< HEAD
    private static final String TABLE_NAME = "users";
    public static final String ID = "_id";
    public static final String USER_NAME = "user_name";
    public static final String USER_PWD = "user_pwd";
=======
    private static final String TABLE_NAME = "users_info";
    public static final String ID = "_id";
    public static final String USER_NAME = "USER_NAME"; //啥情况嘛 数据库里列名是大写 这里是小写还能传进去？服气了  不影响。
    //public static final String USER_PWD = "user_pwd";
    public static final String USER_BIRTH = "Birth";
    public static final String USER_SEX = "Sex";
    public static final String USER_WEIGHT = "Weight";
    public static final String USER_HEIGHT = "Height";
    public static final String USER_BMI = "BMI";
    public static final String USER_KCAL = "Kcal";
    public static final String USER_PROTEIN = "Protein";
    public static final String USER_FATS = "Fats";
    public static final String USER_CHO = "CHO";
    //public static final String USER_NA = "Na";
>>>>>>> refs/remotes/origin/master
    //    public static final String SILENT = "silent";
//    public static final String VIBRATE = "vibrate";
    private static final int DB_VERSION = 2;
    private Context mContext = null;

    //创建用户book表
<<<<<<< HEAD
    private static final String DB_CREATE = "CREATE TABLE " + TABLE_NAME + " (" //TABLE NAME = "users"
            + "ID integer primary key,"
            + "USER_NAME  varchar(16),"
            + "USER_PWD varchar(16))";
=======
    private static final String DB_CREATE = "CREATE TABLE " + TABLE_NAME + " (" //TABLE NAME = "users_info"
            + "ID integer primary key autoincrement,"
            + "USER_NAME  varchar(100),"
            + "Sex varchar(10),"
            + "Birth varchar(20),"
            + "Weight varchar(20),"
            + "Height varchar(20),"
            + "BMI varchar(20),"
            + "Kcal varchar(20),"
            + "Protein varchar(20),"
            + "Fats varchar(20),"
            + "CHO varchar(20))";
//            + "Weight double,"
//            + "Height double,"
//            + "BMI double,"
//            + "Kcal double,"
//            + "Protein double,"
//            + "Fats double,"
//            + "CHO double)";
            //+ "Na double)";
>>>>>>> refs/remotes/origin/master

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
<<<<<<< HEAD
=======

>>>>>>> refs/remotes/origin/master
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
<<<<<<< HEAD
=======

    //log

>>>>>>> refs/remotes/origin/master
    //关闭数据库
    public void closeDataBase() throws SQLException {
        mDatabaseHelper.close();
    }
    //添加新用户，即注册
    public long insertUserData(UserData userData) {
        String userName=userData.getUserName();
<<<<<<< HEAD
        String userPwd=userData.getUserPwd();
        ContentValues values = new ContentValues();
        values.put(USER_NAME, userName);
        values.put(USER_PWD, userPwd);
        return mSQLiteDatabase.insert(TABLE_NAME, ID, values);
    }
    //更新用户信息，如修改密码
    public boolean updateUserData(UserData userData) {
        //int id = userData.getUserId();
        String userName = userData.getUserName();
        String userPwd = userData.getUserPwd();
        ContentValues values = new ContentValues();
        // values.put(USER_NAME, userName);
        values.put(USER_PWD, userPwd);
        String where = USER_NAME + "=" + "=\"" + userName + "\"";
        return mSQLiteDatabase.update(TABLE_NAME, values, where, null) > 0;
        //return mSQLiteDatabase.update(TABLE_NAME, values, ID + "=" + id, null) > 0;
    }
=======
        String Birth = userData.getBirth();
        String Sex = userData.getSex();
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
//    public  long calData(UserData userData){
//        EditText mWeight;
//        EditText mHeight;
//        EditText mBirth;
//        private float IBW =
//
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
>>>>>>> refs/remotes/origin/master
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
<<<<<<< HEAD
        Cursor mCursor=mSQLiteDatabase.query(TABLE_NAME, null, USER_NAME+"=?"+" and "+USER_PWD+"=?",
=======
        Cursor mCursor=mSQLiteDatabase.query(TABLE_NAME, null, USER_NAME+"=?",
>>>>>>> refs/remotes/origin/master
                new String[]{userName,pwd}, null, null, null);
        if(mCursor!=null){
            result=mCursor.getCount();
            mCursor.close();
            Log.i(TAG,"findUserByNameAndPwd , result="+result);
        }
        return result;
    }
<<<<<<< HEAD
=======
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
        Log.i(TAG,"findUserInfo2");
        Log.i(TAG, userName);
        Cursor cursor = mSQLiteDatabase.query("users_info",null,"USER_NAME="+userName,null,null,null,null);
        //Cursor cursor = mSQLiteDatabase.rawQuery("SELECT * FROM users_info",null);
        Log.i(TAG,"findUserInfo3");
        List<String> userinfo = new ArrayList<String>();
        if(cursor.moveToFirst()) {
            Log.i(TAG, "findUserInfo3.5");

            do {
                Log.i(TAG, "findUserInfo4");
                if (cursor.getCount()==0){Log.i(TAG,"NULL");}

                    Log.i(TAG, "findUserInfo6");
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
        //}
        cursor.close();
        return userinfo;
    }
    public String findUserInfo2(String userName){
        Log.i(TAG,"findUserInfo");
        int result=0;
        Log.i(TAG,"findUserInfo2");
        Cursor cursor = mSQLiteDatabase.query("users_info",null,null,null,null,null,null);
        Log.i(TAG,"findUserInfo3");
        String userinfo="a";
        if(cursor.moveToFirst()){
                String username = cursor.getString(cursor.getColumnIndex("USER_NAME"));
                Log.i(TAG, "findUserInfo3.5");
                if(username==userName) {
                    Log.i(TAG, "findUserInfo4");
                    String sex = cursor.getString(cursor.getColumnIndex("Sex"));
                    Log.i(TAG, "findUserInfo5");
                    String weight = cursor.getString(cursor.getColumnIndex("Weight"));
                    Log.i(TAG, "findUserInfo6");
                    String height = cursor.getString(cursor.getColumnIndex("Height"));
                    Log.i(TAG, "findUserInfo7");
                    String BMI = cursor.getString(cursor.getColumnIndex("BMI"));
                    String birth = cursor.getString(cursor.getColumnIndex("Birth"));
                    String kcal = cursor.getString(cursor.getColumnIndex("Kcal"));
                    Log.i(TAG, "findUserInfo10");
                    String CHO = cursor.getString(cursor.getColumnIndex("CHO"));
                    String protein = cursor.getString(cursor.getColumnIndex("Protein"));
                    Log.i(TAG, "findUserInfo12");
                    String fats = cursor.getString(cursor.getColumnIndex("Fats"));
                    Log.i(TAG, "findUserInfo13");

                Log.i("database",height);
                Log.i("database",BMI);
                Log.i("database",CHO);
                Log.i("database",protein);}
                else {

                Log.i(TAG, "findUserInfoFALSE");
                cursor.moveToNext();
//                    userinfo.add(height);
//                    userinfo.add(weight);
//                    userinfo.add(BMI);
//                    userinfo.add(birth);
//                    userinfo.add(kcal) ;
//                    userinfo.add(CHO);
//                    userinfo.add(protein);
//                    userinfo.add(fats);
            }
        }return userinfo;
    }
>>>>>>> refs/remotes/origin/master

}
