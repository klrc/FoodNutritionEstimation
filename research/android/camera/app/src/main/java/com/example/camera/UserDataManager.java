package com.example.camera;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;

import static java.lang.Runtime.getRuntime;

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
    public static final String USER_IDENTITY = "Identity";
    public static final String USER_STATUS = "Status";
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
    /////////////////////////////////////////////////////////////
    private static String DB_PATH = "data/data/com.example.camera/databases/";

    /////////////////////////////////////////////////////////////
    //创建用户身高体重表
    private static final String DB_CREATE2 = "CREATE TABLE "+"user_hw" +" ("
            + "ID integer primary key autoincrement,"
            + "User_name  varchar(100),"
            + "Weight varchar(40),"
            + "Height varchar(40),"
            + "Reg_time varchar(40))";
//    private static final String DB_CREATE3 = "CREATE TABLE " + TABLE_NAME + " (" //TABLE NAME = "users_info"
//            + "ID integer primary key autoincrement,"
//            + "User_name  varchar(100),"
//            + "Weight varchar(20),"
//            + "Height varchar(20),"
//            + "Reg_time varchar(20))";
    //创建用户info表
    private static final String DB_CREATE = "CREATE TABLE " + TABLE_NAME + " (" //TABLE NAME = "users_info"
            + "ID integer primary key autoincrement,"
            + "USER_NAME  varchar(100),"
            + "Days varchar(20),"
            + "Sex varchar(10),"
            + "Birth varchar(20),"
            + "Weight varchar(20),"
            + "Height varchar(20),"
            + "BMI double(20,3),"
            + "Identity varchar(10),"
            + "Status varchar(10),"
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
    SQLiteDatabase mdatabase = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READWRITE);

    private DataBaseManagementHelper mDatabaseHelper = null;

    //DataBaseManagementHelper继承自SQLiteOpenHelper
    //private static class DataBaseManagementHelper extends SQLiteOpenHelper {//继承字抽象类qoh，需要重写 oncreate和 onupgrade两个方法
    public static class DataBaseManagementHelper extends SQLiteOpenHelper {
        DataBaseManagementHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            this.myContext = context;
        }
        private Context myContext;
        //The Android's default system path of your application database.
        private static String ASSETS_NAME = "user_data";
        private SQLiteDatabase myDataBase = null;
        @Override
        public void onCreate(SQLiteDatabase db) {
//            Log.i(TAG,"db.getVersion()="+db.getVersion());
//            db.execSQL("DROP TABLE IF EXISTS " + "user_hw" + ";");
//            db.execSQL(DB_CREATE3);
//            Log.i(TAG, "db.execSQL(DB_CREATE)");
//            Log.e(TAG, DB_CREATE3);  //drop if 操作是为了删除原有的  创建新的，是一种常用的更新方法
        }
        public void createDataBase() throws IOException {
            boolean dbExist = checkDataBase();

            if (!dbExist) {
                try {
                    File dir = new File(DB_PATH);
                    if (!dir.exists()) {
                        dir.mkdir();
                    }
                    File dbf = new File(DB_PATH + DB_NAME);
                    String dirPath = DB_PATH + DB_NAME;
                    if (!dbf.exists()) {
                        SQLiteDatabase.openOrCreateDatabase(dbf, null);
                        copyDataBase();

                    }
                   // Runtime runtime = getRuntime();
                    //String command = "chmod 777 " + dirPath;
                   // String command2 = "chmod 777 " + DB_PATH;
                   // String command3 = "chmod 777" + DB_PATH+"user_data-shm";
                   // String command4 = "chmod 777" + DB_PATH+"user_data-wal";
                    /////////////删除copy过来的wal等文件

                    /////////////////////////////////////
                 //   try {
                      //  Process process2 = runtime.exec(command2);
                      //  Process process = runtime.exec(command);
                      //  Process process3 = runtime.exec(command3);
                       // Process process4 = runtime.exec(command4);

                      //  process.waitFor();
                     //   int existValue = process.exitValue();
                    //    if(existValue != 0){
                     //       Log.i(TAG, "Change file permission failed.");
                     //   }
                  //  } catch (Exception e) {
                 //       Log.i(TAG, "Command execute failed.");
                  //  }

//                    SQLiteDatabase.openOrCreateDatabase(dbf, null);
//                    copyDataBase();
                } catch (IOException e) {
                    throw new Error("数据库创建失败");
                }
                File res_file =new File(DB_PATH+"user_data-shm");
                File res_file2 =new File(DB_PATH+"user_data-wal");
                Log.i(TAG,"开始del");
                res_file.delete();
                res_file2.delete();
                Log.i(TAG,"完成del");
            }
        }

        private void copyDataBase() throws IOException {
//            InputStream myInput = null;
////        try {
//            myInput = myContext.getClass().getClassLoader().getResourceAsStream("assets/" + DB_NAME);//getAssets().open(ASSETS_NAME);
//            String outFileName = DB_PATH + DB_NAME;
//            OutputStream myOutput = new FileOutputStream(outFileName);
//
//            byte[] buffer = new byte[1024];
//            int length;
//            while ((length = myInput.read(buffer)) > 0) {
//                myOutput.write(buffer, 0, length);
//            }
//            myOutput.flush();
//            myOutput.close();
//            myInput.close();
            InputStream is = myContext.getResources().openRawResource(
                    R.raw.user_data); //欲导入的数据库
            String outFileName = DB_PATH + DB_NAME;
            FileOutputStream fos = new FileOutputStream(outFileName);
            byte[] buffer = new byte[400000];
            int count = 0;
            while ((count = is.read(buffer)) > 0) {
                fos.write(buffer, 0, count);
            }
            fos.close();
            is.close();

            File dbf = new File(DB_PATH);
            File[] files=dbf.listFiles();
            Log.i(TAG,"文件长度"+files.length);
            Log.i(TAG,"成功建库");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        }

        private boolean checkDataBase() {
            SQLiteDatabase checkDB = null;
            String myPath = DB_PATH + DB_NAME;
            try {
                checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
            } catch (SQLiteException e) { //database does't exist yet.
            }
            if (checkDB != null) {
                checkDB.close();
            }
            return checkDB != null ? true : false;
        }

        @Override
        public synchronized void close() {
            if (myDataBase != null) {
                myDataBase.close();
            }
            super.close();
        }


//创建数据库


        // @Override
  //      public void onCreate(SQLiteDatabase db) {
//            Log.i(TAG,"db.getVersion()="+db.getVersion());
//            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
//            db.execSQL(DB_CREATE);
//            Log.i(TAG, "db.execSQL(DB_CREATE)");
//            Log.e(TAG, DB_CREATE);  //drop if 操作是为了删除原有的  创建新的，是一种常用的更新方法

 //      }

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
        mdatabase = mDatabaseHelper.getWritableDatabase();
    }

    //log

    //关闭数据库
    public void closeDataBase() throws SQLException {
        mDatabaseHelper.close();
    }
    public long insert_userhw(String user_name,float weight,float height,String reg_time){
        ContentValues values = new ContentValues();
        values.put("User_name", user_name);
        values.put("Weight",weight);
        values.put("Height",height);
        values.put("Reg_time",reg_time);
        return mSQLiteDatabase.insert("user_hw", ID, values);
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
        String Identity = userData.getIdentity();
        String Status = userData.getStatus();
        double Kcal = userData.getKcal();
        double Protein = userData.getProtein();
        double Fats = userData.getFats();
        double CHO = userData.getCHO();
        double D1Kcal=userData.getD1Kcal();
        double D1CHO = userData.getD1CHO();
        double D1Protein = userData.getD1Protein();
        double D1Fats = userData.getD1Fats();
        double D2Kcal=userData.getD2Kcal();
        double D2CHO = userData.getD2CHO();
        double D2Protein = userData.getD2Protein();
        double D2Fats = userData.getD2Fats();
        double D3Kcal=userData.getD3Kcal();
        double D3CHO = userData.getD3CHO();
        double D3Protein = userData.getD3Protein();
        double D3Fats = userData.getD3Fats();
        double D4Kcal=userData.getD4Kcal();
        double D4CHO = userData.getD4CHO();
        double D4Protein = userData.getD4Protein();
        double D4Fats = userData.getD4Fats();
        double D5Kcal=userData.getD5Kcal();
        double D5CHO = userData.getD5CHO();
        double D5Protein = userData.getD5Protein();
        double D5Fats = userData.getD5Fats();
        double D6Kcal=userData.getD6Kcal();
        double D6CHO = userData.getD6CHO();
        double D6Protein = userData.getD6Protein();
        double D6Fats = userData.getD6Fats();
        double D7Kcal=userData.getD7Kcal();
        double D7CHO = userData.getD7CHO();
        double D7Protein = userData.getD7Protein();
        double D7Fats = userData.getD7Fats();



        //double Na = userData.getNa();

        ContentValues values = new ContentValues();
        values.put(USER_NAME, userName);
        values.put(USER_DAYS,Days);
        values.put(USER_BIRTH,Birth);
        values.put(USER_SEX,Sex);
        values.put(USER_WEIGHT,Weight);
        values.put(USER_HEIGHT,Height);
        values.put(USER_BMI,BMI);
        values.put(USER_IDENTITY,Identity);
        values.put(USER_STATUS,Status);
        values.put(USER_KCAL,Kcal);
        values.put(USER_PROTEIN,Protein);
        values.put(USER_FATS,Fats);
        values.put(USER_CHO,CHO);
        values.put(D1KCAL,D1Kcal);
        values.put(UD1CHO,D1CHO);
        values.put(D1PROTEIN,D1Protein);
        values.put(D1FATS,D1Fats);
        values.put(D2KCAL,D2Kcal);
        values.put(UD2CHO,D2CHO);
        values.put(D2PROTEIN,D2Protein);
        values.put(D2FATS,D2Fats);
        values.put(D3KCAL,D3Kcal);
        values.put(UD3CHO,D3CHO);
        values.put(D3PROTEIN,D3Protein);
        values.put(D3FATS,D3Fats);
        values.put(D4KCAL,D4Kcal);
        values.put(UD4CHO,D4CHO);
        values.put(D4PROTEIN,D4Protein);
        values.put(D4FATS,D4Fats);
        values.put(D5KCAL,D5Kcal);
        values.put(UD5CHO,D5CHO);
        values.put(D5PROTEIN,D5Protein);
        values.put(D5FATS,D5Fats);
        values.put(D6KCAL,D6Kcal);
        values.put(UD6CHO,D6CHO);
        values.put(D6PROTEIN,D6Protein);
        values.put(D6FATS,D6Fats);
        values.put(D7KCAL,D7Kcal);
        values.put(UD7CHO,D7CHO);
        values.put(D7PROTEIN,D7Protein);
        values.put(D7FATS,D7Fats);

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
        //ContentValues values = new ContentValues();
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
        Cursor mCursor = mdatabase.query(TABLE_NAME, null, USER_NAME+"=?",new String[]{userName}, null, null, null);
        //Cursor mCursor=mSQLiteDatabase.query(TABLE_NAME, null, USER_NAME+"=?",new String[]{userName}, null, null, null);
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
    public boolean is_num_enough(String identity){
        Cursor cursor = mSQLiteDatabase.query("users_info",null,"Identity=?",new String[]{identity},null ,null,null,null);

        boolean sat=true;
        if(cursor!=null){
            int count = cursor.getCount();
            Log.i(TAG,"该身份："+identity+"用户人数："+count);
            if (count<2&&count>=0) sat=true;
            else if (count>=2) sat=false;
        }
        return sat;
    }

    public void showList(String colunmName){
        mDatabaseHelper = new DataBaseManagementHelper(mContext);
        mdatabase = mDatabaseHelper.getWritableDatabase();

        //mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
       // Cursor cursor = mdatabase.query("users_info",null,null,null,null,null,null);
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
        //SQLiteDatabase database = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READWRITE);
        Cursor cursor = mdatabase.query("users_info",null,null,null,null,null,null);
        //Cursor cursor = mSQLiteDatabase.query("users_info",null,null,null,null,null,null);
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
//    public List<String> showUserIdentity(String colunmName){
//        //mDatabaseHelper = new DataBaseManagementHelper(mContext);
//        //mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
//        //SQLiteDatabase database = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READWRITE);
//        Cursor cursor = mdatabase.query("users_info",null,null,null,null,null,null);
//        //Cursor cursor = mSQLiteDatabase.query("users_info",null,null,null,null,null,null);
//        List<String> user_id_list = new ArrayList<String>();
//        if(cursor.moveToFirst()){
//            do{
//                String username = cursor.getString(cursor.getColumnIndex(colunmName));
//                user_id_list.add(username);
//
//            }while (cursor.moveToNext());
//        }
//        cursor.close();
//        return user_id_list;
//    }

    public String get_reg_usertime(String user_name){
        Cursor cursor = mSQLiteDatabase.query("user_hw",null,"User_name=?",new String[]{user_name},null,null,null,"1");
        String reg_time="";
       // Log.i(TAG,"zhaodaole");
        if(cursor.moveToFirst()){
            do{
                if (cursor.getCount()==0){Log.i(TAG,"NULL");}
                    reg_time = cursor.getString(cursor.getColumnIndex("Reg_time"));
            }while (cursor.moveToNext());}
        cursor.close();

        return reg_time;
    }
    public String get_current_usertime(String user_name){
        Cursor cursor = mSQLiteDatabase.query("user_hw",null,"User_name=?",new String[]{user_name},null,null,null,null);
        String reg_time="";
        // Log.i(TAG,"zhaodaole");
        if(cursor.moveToFirst()){
            do{
                if (cursor.getCount()==0){Log.i(TAG,"NULL");}
                reg_time = cursor.getString(cursor.getColumnIndex("Reg_time"));
                Log.i(TAG,"每次修改的时间为"+reg_time);
            }while (cursor.moveToNext());}
        cursor.close();

        return reg_time;
    }
    public String get_current_userinfo(String user_name){
        Cursor cursor = mSQLiteDatabase.query("user_hw",null,"User_name=?",new String[]{user_name},null,null,null,null);
        //String weight_height="";
        String weight="";
        String height="";
        Log.i(TAG,"zhaodaole");
        if(cursor.moveToFirst()){
            do{
                if (cursor.getCount()==0){Log.i(TAG,"NULL");}
                weight = cursor.getString(cursor.getColumnIndex("Weight"));
                height = cursor.getString(cursor.getColumnIndex("Height"));
                Log.i(TAG,"每次修改的信息为:体重："+weight+",身高："+height);
            }while (cursor.moveToNext());}
        cursor.close();
        //weight_height=weight+"-"+height;
        return weight+"-"+height;
    }

    //问题记录 现在是用户名是数字的可以找到 ， AA这个字符类型的找不到。
    public List<Float> findFoodInfo(String foodname){
        Cursor cursor = mSQLiteDatabase.query("foods_info",null,"Food_Name=?",new String[]{foodname},null,null,null,null);
        List<Float> foodinfo = new ArrayList<Float>();
        Log.i(TAG,"foodinfostart");
        if(cursor.moveToFirst()){
            do{
                if (cursor.getCount()==0){Log.i(TAG,"NULL");}
            Float vol = cursor.getFloat(cursor.getColumnIndex("Vol"));
            Float Energy = cursor.getFloat(cursor.getColumnIndex("Energy"));
            Float CHO = cursor.getFloat(cursor.getColumnIndex("CHO"));
            Float Protein = cursor.getFloat(cursor.getColumnIndex("Protein"));
            Float Fats = cursor.getFloat(cursor.getColumnIndex("Fats"));
            Float Weight = cursor.getFloat(cursor.getColumnIndex("Weight"));
            foodinfo.add(Energy);
            foodinfo.add(CHO);
            foodinfo.add(Protein);
            foodinfo.add(Fats);
            foodinfo.add(vol);
            foodinfo.add(Weight);
                Log.i(TAG,"FOODNUTRI"+Energy+"+"+CHO+"+"+Protein+"+"+Fats);
        }while (cursor.moveToNext());}
        cursor.close();

        return foodinfo;

    }
    public List<String> findFoodMaterial(String foodname) {
        Cursor cursor = mSQLiteDatabase.query("foods_material",null,"Food_Name=?",new String[]{foodname},null,null,null);
        List<String> foodmaterial = new ArrayList<String>();
        String mat;
        String weight;
        String sum;
        if(cursor.moveToFirst()){
            do{
                if(cursor.getCount()==0){Log.i(TAG,"莫得");}
            mat = cursor.getString(cursor.getColumnIndex("Material"));
            weight = cursor.getString(cursor.getColumnIndex("Weight"));
            sum = mat+"-"+weight;
            foodmaterial.add(sum);
            Log.i(TAG,"原料有："+foodmaterial);
        }while (cursor.moveToNext());}
        cursor.close();
        return  foodmaterial;
    }
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
    }public List<String> DayNutriInfo(String userName,int week){
        Log.i(TAG,"findWeekfatsInfo");
        Cursor cursor = mSQLiteDatabase.query("users_info",null,"USER_NAME=?", new String[]{userName},null,null,null,null);
        List<String> daynutriinfo = new ArrayList<String>();
        if(cursor.moveToFirst()) {
            do {
                if (cursor.getCount()==0){Log.i(TAG,"NULL");}
                String weekKcal = cursor.getString(cursor.getColumnIndex("D"+Integer.toString(week)+"Kcal"));
                String weekCHO = cursor.getString(cursor.getColumnIndex("D"+Integer.toString(week)+"CHO"));
                String weekProtein = cursor.getString(cursor.getColumnIndex("D"+Integer.toString(week)+"Protein"));
                String weekFats = cursor.getString(cursor.getColumnIndex("D"+Integer.toString(week)+"Fats"));
                daynutriinfo.add(weekKcal);
                daynutriinfo.add(weekCHO);
                daynutriinfo.add(weekProtein);
                daynutriinfo.add(weekFats);
                daynutriinfo.add("1000");
            } while (cursor.moveToNext());}

        cursor.close();
        return daynutriinfo;
    }
    public int new_BMIInfo(String usersex,int days,double BMI){
        float p3 =0;
        float p50=0;
        float overweight=0;
        float fat=0;
        int status = -1;
        int userage=days/365;
        float usermonth =(days-userage*365)/30;
        float real_age=0;
        if(usermonth>=0&&usermonth<=4) real_age=userage;
        if(usermonth>4&&usermonth<=8) real_age=userage+0.5f;
        if(usermonth>8&&usermonth<=12) real_age=userage+1;
        Log.i(TAG,"计算好的年龄:"+real_age);
        //Log.i(TAG,"整数年："+userage);
        //Log.i(TAG,"整数月份："+usermonth);
        Cursor cursor = mSQLiteDatabase.query(usersex+"_bmi",null,"Age=?",new String[]{Float.toString(real_age)},null,null,null,null);
        if(cursor.moveToFirst()) {
            do {
                if (cursor.getCount()==0){Log.i(TAG,"NULL");}
                 p3= Float.parseFloat(cursor.getString(cursor.getColumnIndex("P3")));
                 //p50 = Float.parseFloat(cursor.getString(cursor.getColumnIndex("P50")));
                 overweight = Float.parseFloat(cursor.getString(cursor.getColumnIndex("Overweight")));
                 fat= Float.parseFloat(cursor.getString(cursor.getColumnIndex("Fat")));
                //bmiinfo = p15+"-"+p85;
                //return bmiinfo;
            } while (cursor.moveToNext());}
        cursor.close();
        Log.i(TAG,"各个指标"+p3+","+overweight+","+fat);
        if(BMI<=p3) status=0;
        if (BMI>p3&&BMI<=overweight) status=1;
        if(BMI>overweight&&BMI<=fat) status=2;
        if(BMI>fat) status=3;
        return status;
    }
    public int BMIInfo(String usersex,int age,int days,double BMI){
        Log.i(TAG,"BMIInfo");
        Cursor cursor;
        String p15="";
        String p85="";
        String bmiinfo="";
        int status = 0;
        if(days<730){status=-1;}
        else if(days>=730){
        if (usersex=="男"){
            Log.i(TAG,"男");
            if(age>=2&&age<=5){
                Log.i(TAG,"男用户年龄2~5"+Integer.toString(days));
                cursor = mSQLiteDatabase.query("boy_2_5",null,"Age=?", new String[]{Integer.toString(days)},null,null,null,null);
                if(cursor.moveToFirst()) {
                    do {
                        if (cursor.getCount()==0){Log.i(TAG,"NULL");}
                        p15 = cursor.getString(cursor.getColumnIndex("P15"));
                        p85 = cursor.getString(cursor.getColumnIndex("P85"));
                        //bmiinfo = p15+"-"+p85;
                        //return bmiinfo;
                    } while (cursor.moveToNext());}
                cursor.close();
                //Log.i(TAG,bmiinfo);
                if(BMI<Float.parseFloat(p15)){
                    status = 1;
                }
                else if(BMI>Float.parseFloat(p15)&&BMI<Float.parseFloat(p85)){
                    status = 2;
                }
                else if(BMI>Float.parseFloat(p85)){
                    status = 3;
                }
            }
            else if(age>5&&age<=19){
                Log.i(TAG,"男用户年龄5~19");
                Log.i(TAG,"用户月数"+Integer.toString(days/30));
                cursor = mSQLiteDatabase.query("boy_5_19",null,"Month=?", new String[]{Integer.toString(days/30)},null,null,null,null);
                if(cursor.moveToFirst()) {
                    do {
                        if (cursor.getCount()==0){Log.i(TAG,"NULL");}
                        p15 = cursor.getString(cursor.getColumnIndex("P15"));
                        p85 = cursor.getString(cursor.getColumnIndex("P85"));
                    } while (cursor.moveToNext());}
                cursor.close();
                if(BMI<Float.parseFloat(p15)){
                    status = 1;
                }
                else if(BMI>Float.parseFloat(p15)&&BMI<Float.parseFloat(p85)){
                    status = 2;
                }
                else if(BMI>Float.parseFloat(p85)){
                    status = 3;
                }
            }
            }

        else if (usersex=="女"){
            Log.i(TAG,"女");
            if(age>=2&&age<=5){
                Log.i(TAG,"女用户年龄2~5");
                cursor = mSQLiteDatabase.query("girl_2_5",null,"Age=?", new String[]{Integer.toString(days)},null,null,null,null);
                if(cursor.moveToFirst()) {
                    do {
                        if (cursor.getCount()==0){Log.i(TAG,"NULL");}
                        p15 = cursor.getString(cursor.getColumnIndex("P15"));
                        p85 = cursor.getString(cursor.getColumnIndex("P85"));
                        //return bmiinfo;
                    } while (cursor.moveToNext());}
                if(BMI<Float.parseFloat(p15)){
                    status = 1;
                }
                else if(BMI>Float.parseFloat(p15)&&BMI<Float.parseFloat(p85)){
                    status = 2;
                }
                else if(BMI>Float.parseFloat(p85)){
                    status = 3;
                }
                cursor.close();
            }
            if(age>5&&age<=19){
                Log.i(TAG,"女用户年龄5~19");
                cursor = mSQLiteDatabase.query("girl_5_19",null,"Month=?", new String[]{Integer.toString(days/30)},null,null,null,null);
                if(cursor.moveToFirst()) {
                    do {
                        if (cursor.getCount()==0){Log.i(TAG,"NULL");}
                        p15 = cursor.getString(cursor.getColumnIndex("P15"));
                        p85 = cursor.getString(cursor.getColumnIndex("P85"));

                       // return bmiinfo;
                    } while (cursor.moveToNext());}
                if(BMI<Float.parseFloat(p15)){
                    status = 1;
                }
                else if(BMI>Float.parseFloat(p15)&&BMI<Float.parseFloat(p85)){
                    status = 2;
                }
                else if(BMI>Float.parseFloat(p85)){
                    status = 3;
                }
                cursor.close();
            }
        }}
        Log.i(TAG,Integer.toString(status));
        return status;
    }
    //查ibw表，如果身高高于表内最高值，按成人算ibw。
    public List<String> today_nutri_import(String prefix,String username){
        List<String> nutri_str=new ArrayList<String>();
        String nutri_kcal;
        String nutri_cho;
        String nutri_protein;
        String nutri_fats;
        Cursor cursor = mSQLiteDatabase.query("users_info",null ,"USER_NAME=?",new String[]{username},null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                if (cursor.getCount()==0){Log.i(TAG,"NULL");}
                nutri_kcal=cursor.getString(cursor.getColumnIndex(prefix+"Kcal"));
                nutri_cho = cursor.getString(cursor.getColumnIndex(prefix+"CHO"));
                nutri_protein=cursor.getString(cursor.getColumnIndex(prefix+"Protein"));
                nutri_fats=cursor.getString(cursor.getColumnIndex(prefix+"Fats"));
            }while (cursor.moveToNext());
            cursor.close();
            nutri_str.add(nutri_kcal);
            nutri_str.add(nutri_cho);
            nutri_str.add(nutri_protein);
            nutri_str.add(nutri_fats);
        }
        return  nutri_str;
    }
    public float IBWInfo(String sex,int userage,int days,float height){
        Cursor cursor=mSQLiteDatabase.query("boy_ibw", null, "Age=?", new String[]{"0"}, null, null, null, null);
        int month = days/30;
        float ibw = 0;
        float agecol=0;
        float overtall=0;
        float adultweight = 0;
        String weight="";
        String strsex="";
//        if(sex=="男"){
//            strsex="boy";
//            if(height>183.9){
//                overtall = (height-100)*0.9f;
//            }
//        }
//
//        if(sex=="女"){
//            strsex="girl";
//            if(height>170.7){
//                overtall= (height-100)*0.9f-2.5f;
//            }
//        }

            if(month<24) {
                agecol = month/12f;
            Log.i(TAG, "agecol" + String.valueOf(agecol));
            if (agecol < 0.166)
                cursor = mSQLiteDatabase.query(sex+"_ibw", null, "Age>=? AND P_height>=?", new String[]{"0",Float.toString(height)}, null, null, null, "1");
            if (agecol >= 0.166 && agecol < 0.333)
                cursor = mSQLiteDatabase.query(sex+"_ibw", null, "Age>=? AND P_height>=?", new String[]{"0.166",Float.toString(height)}, null, null, null, "1");
            if (agecol >= 0.333 && agecol < 0.5)
                cursor = mSQLiteDatabase.query(sex+"_ibw", null, "Age>=? AND P_height>=?", new String[]{"0.333",Float.toString(height)}, null, null, null, "1");
            if (agecol >= 0.5 && agecol < 0.75)
                cursor = mSQLiteDatabase.query(sex+"_ibw", null, "Age>=? AND P_height>=?", new String[]{"0.5",Float.toString(height)}, null, null, null, "1");
            if (agecol >= 0.75 && agecol < 1)
                cursor = mSQLiteDatabase.query(sex+"_ibw", null, "Age>=? AND P_height>=?", new String[]{"0.75",Float.toString(height)}, null, null, null, "1");
            if (agecol >= 1 && agecol < 1.25)
                cursor = mSQLiteDatabase.query(sex+"_ibw", null, "Age>=? AND P_height>=?", new String[]{"1",Float.toString(height)}, null, null, null, "1");
            if (agecol >= 1.25 && agecol < 1.5)
                cursor = mSQLiteDatabase.query(sex+"_ibw", null, "Age>=? AND P_height>=?", new String[]{"1.25",Float.toString(height)}, null, null, null, "1");
            if (agecol >= 1.5 && agecol < 1.75)
                cursor = mSQLiteDatabase.query(sex+"_ibw", null, "Age>=? AND P_height>=?", new String[]{"1.5",Float.toString(height)}, null, null, null, "1");
            if (agecol >= 1.75 && agecol < 2)
                cursor = mSQLiteDatabase.query(sex+"_ibw", null, "Age>=? AND P_height>=?", new String[]{"1.75",Float.toString(height)}, null, null, null, "1");
        }
        if(month>=24&&month<=216){
            int age_low = month/12;
            float age_mid = age_low+1f/2f;
            int age_high = age_low+1;
            agecol = month/12f;
            Log.i(TAG,"agelow"+String.valueOf(age_low));
            Log.i(TAG,"agemid"+String.valueOf(age_mid));
            Log.i(TAG,"agehigh"+String.valueOf(age_high));
            if(agecol>=age_low&&agecol<age_mid){
                cursor = mSQLiteDatabase.query(sex+"_ibw",null,"Age>=? AND P_height>=?", new String[]{Float.toString(age_low),Float.toString(height)},null,null,null,"1");
                Log.i(TAG,"chossedcursor low"); }
            if(agecol>=age_mid&&agecol<age_high){
                cursor = mSQLiteDatabase.query(sex+"_ibw",null,"Age>=? AND P_height>=?", new String[]{Float.toString(age_mid),Float.toString(height)},null,null,null,"1");
                Log.i(TAG,"chossedcursor mid");}
            if(agecol==age_high){
                cursor = mSQLiteDatabase.query(sex+"_ibw",null,"Age>=? AND P_height>=?", new String[]{Float.toString(age_high),Float.toString(height)},null,null,null,"1");
                Log.i(TAG,"chossedcursor high");}

            Log.i(TAG,"agecol"+agecol);

        }
        if(month>216){
            if(sex=="boy")
                ibw = (height-100)*0.9f;
            if(sex=="girl")
                ibw = (height-100)*0.9f-2.5f;
        }
        if(cursor.moveToFirst()) {
            do {
                if (cursor.getCount()==0){Log.i(TAG,"NULL");}
                        weight = cursor.getString(cursor.getColumnIndex("P_weight"));
                } while (cursor.moveToNext());}
            cursor.close();
//        Log.i(TAG,"USER_HEIGHT"+height);
        Log.i(TAG,"USER_IBW"+weight);

        if(sex=="boy"){
            if(month<=216){
                if(height>183.9){
                    ibw = (height-100)*0.9f;}
                else if(height>0&&height<=183.9){
                    ibw = Float.parseFloat(weight);}
            }
        }

        if(sex=="girl"){
            if(month<=216){
                if(height>170.7)
                    ibw = (height-100)*0.9f-2.5f;
                else
                    ibw = Float.parseFloat(weight);
            }
        }
        Log.i(TAG,"USER_IBW IS "+ String.valueOf(ibw));
        return ibw;
    }
    public int check_height(String sex,int days,float height){
        Cursor cursor=mSQLiteDatabase.query("boy_ibw", null, "Age=?", new String[]{"0"}, null, null, null, null);
        int month = days/30;
        float ibw = 0;
        float agecol=0;
        int status = 0;
        String weight="";
//        String strsex="";
//        if(sex=="男")
//            strsex="boy";
//        if(sex=="女")
//            strsex="girl";
        if(month<24) {
            agecol = month/12f;
            Log.i(TAG, "agecol" + String.valueOf(agecol));
            if (agecol < 0.166)
                cursor = mSQLiteDatabase.query(sex+"_ibw", null, "Age>=?", new String[]{"0"}, null, null, null, "2");
            if (agecol >= 0.166 && agecol < 0.333)
                cursor = mSQLiteDatabase.query(sex+"_ibw", null, "Age>=?", new String[]{"0.166"}, null, null, null, "2");
            if (agecol >= 0.333 && agecol < 0.5)
                cursor = mSQLiteDatabase.query(sex+"_ibw", null, "Age>=?", new String[]{"0.333"}, null, null, null, "2");
            if (agecol >= 0.5 && agecol < 0.75)
                cursor = mSQLiteDatabase.query(sex+"_ibw", null, "Age>=?", new String[]{"0.5"}, null, null, null, "2");
            if (agecol >= 0.75 && agecol < 1)
                cursor = mSQLiteDatabase.query(sex+"_ibw", null, "Age>=?", new String[]{"0.75"}, null, null, null, "2");
            if (agecol >= 1 && agecol < 1.25)
                cursor = mSQLiteDatabase.query(sex+"_ibw", null, "Age>=?", new String[]{"1"}, null, null, null, "2");
            if (agecol >= 1.25 && agecol < 1.5)
                cursor = mSQLiteDatabase.query(sex+"_ibw", null, "Age>=?", new String[]{"1.25"}, null, null, null, "2");
            if (agecol >= 1.5 && agecol < 1.75)
                cursor = mSQLiteDatabase.query(sex+"_ibw", null, "Age>=?", new String[]{"1.5"}, null, null, null, "2");
            if (agecol >= 1.75 && agecol < 2)
                cursor = mSQLiteDatabase.query(sex+"_ibw", null, "Age>=?", new String[]{"1.75"}, null, null, null, "2");
        }
        if(month>=24){
            int age_low = month/12;
            float age_mid = age_low+1f/2f;
            int age_high = age_low+1;
            agecol = month/12f;

            Log.i(TAG,"agelow"+String.valueOf(age_low));
            Log.i(TAG,"agemid"+String.valueOf(age_mid));
            Log.i(TAG,"agehigh"+String.valueOf(age_high));
            if(agecol>=age_low&&agecol<age_mid){
                cursor = mSQLiteDatabase.query(sex+"_ibw",null,"Age=?", new String[]{Float.toString(age_low)},null,null,null,"2");
                Log.i(TAG,"chossedcursor low"); }
            if(agecol>=age_mid&&agecol<age_high){
                cursor = mSQLiteDatabase.query(sex+"_ibw",null,"Age>=?", new String[]{Float.toString(age_mid)},null,null,null,"2");
                Log.i(TAG,"chossedcursor mid");}
            if(agecol==age_high){
                cursor = mSQLiteDatabase.query(sex+"_ibw",null,"Age>=?", new String[]{Float.toString(age_high)},null,null,null,"2");
                Log.i(TAG,"chossedcursor high");}
                Log.i(TAG,"agecol"+agecol);

        }
        String p10_height="";
        if(cursor.moveToFirst()) {
            do {
                if (cursor.getCount()==0){Log.i(TAG,"NULL");}
                p10_height = cursor.getString(cursor.getColumnIndex("P_height"));
            } while (cursor.moveToNext());}
        cursor.close();
        //p10_height = cursor.getColumnIndex("P_height");
        Log.i(TAG,"最低身高"+p10_height);
        if(height<=Float.parseFloat(p10_height)){
            status=1;
        }
        Log.i(TAG,"身高状态"+status);
        return status;
    }
    public void changinfo(String userName,String columnName,String changed_value) {
        int result = 0;
        // Log.i(TAG,"changeInfo");
        Log.i(TAG, userName);
        ContentValues values = new ContentValues();
        values.put(columnName, changed_value);
        mSQLiteDatabase.update(TABLE_NAME, values, "USER_NAME=?", new String[]{userName});
    }
    public List<String> showFoodName(String colunmName){
        mDatabaseHelper = new DataBaseManagementHelper(mContext);
        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
        Cursor cursor = mSQLiteDatabase.query("foods_info",null,null,null,null,null,null);
        List<String> foodlist = new ArrayList<String>();
        if(cursor.moveToFirst()){
            do{
                // Log.i(TAG,"show1");
                String foodname = cursor.getString(cursor.getColumnIndex(colunmName));
                // Log.i(TAG,foodname);
                foodlist.add(foodname);

            }while (cursor.moveToNext());
        }
        cursor.close();
        return foodlist;
    }
    public List<Float> cal_static_nutri_per(String username){
        List<Float> list = new ArrayList<>();
        String kcal;
        String cho;
        String protein,fats;
        float percent;
        Cursor cursor = mSQLiteDatabase.query("users_info", null, "USER_NAME=?", new String[]{username}, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getCount() == 0) {
                    Log.i(TAG, "NULL");
                }
                kcal = cursor.getString(cursor.getColumnIndex( "Kcal"));
                cho = cursor.getString(cursor.getColumnIndex("CHO"));
                protein = cursor.getString(cursor.getColumnIndex("Protein"));
                fats = cursor.getString(cursor.getColumnIndex("Fats"));
            } while (cursor.moveToNext());
            percent = Float.parseFloat(cho)/Float.parseFloat(kcal)*100;
            list.add(percent);
            percent = Float.parseFloat(protein)/Float.parseFloat(kcal)*100;
            list.add(percent);
            percent = Float.parseFloat(fats)/Float.parseFloat(kcal)*100;
            list.add(percent);
            }
        return list;
    }
    public List<Float> cal_nutripercent(String prefix,String username) {
        List<Float> list = new ArrayList<>();
        Cursor cursor = mSQLiteDatabase.query("users_info", null, "USER_NAME=?", new String[]{username}, null, null, null, null);
        String nutri_kcal;
        String nutri_cho;
        String nutri_protein;
        String nutri_fats;
        String sum_kcal;
        String sum_cho;
        String sum_protein;
        String sum_fats;
        float percent;

        if (cursor.moveToFirst()) {
            do {
                if (cursor.getCount() == 0) {
                    Log.i(TAG, "NULL");
                }
                nutri_kcal = cursor.getString(cursor.getColumnIndex(prefix + "Kcal"));
                nutri_cho = cursor.getString(cursor.getColumnIndex(prefix + "CHO"));
                nutri_protein = cursor.getString(cursor.getColumnIndex(prefix + "Protein"));
                nutri_fats = cursor.getString(cursor.getColumnIndex(prefix + "Fats"));
                sum_kcal = cursor.getString(cursor.getColumnIndex("Kcal"));
                sum_cho = cursor.getString(cursor.getColumnIndex("CHO"));
                sum_protein=cursor.getString(cursor.getColumnIndex("Protein"));
                sum_fats = cursor.getString(cursor.getColumnIndex("Fats"));

            } while (cursor.moveToNext());
            percent = Float.parseFloat(nutri_kcal)/Float.parseFloat(sum_kcal)*100;
            list.add(percent);
            percent = Float.parseFloat(nutri_cho)/Float.parseFloat(sum_cho)*100;
            list.add(percent);
            percent = Float.parseFloat(nutri_protein)/Float.parseFloat(sum_protein)*100;
            list.add(percent);
            percent = Float.parseFloat(nutri_fats)/Float.parseFloat(sum_fats)*100;
            list.add(percent);
        }
        return list;
    }
}

