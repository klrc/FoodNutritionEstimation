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
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class FoodDataManager {
    private static final String TAG = "FoodDataManager";
    private static final String DB_NAME = "user_data";
    private static final String TABLE_NAME = "foods_info";
    public static final  String ID = "_id";
    public static final String FOOD_NAME = "Food_Name";
    public static final String FOOD_KCAL = "Food_Kcal";
    public static final String FOOD_FATS = "Food_Fats";
    public static final String FOOD_PROTEIN = "Food_Protein";
    public static final String FOOD_CHO = "Food_CHO";

    private static final int DB_VERSION = 2;
    private Context mContext = null;
    private  static final String DB_CREATE = "CREATE TABLE " + TABLE_NAME + "("
            + "FOOD_ID integer primary key autoincrement,"
            + "Food_Name varchar(100),"
            + "Food_Kcal varchar(100),"
            + "Food_CHO varchar(100),"
            + "Food_Fats varchar(100),"
            + "Food_Protein varchar(100))";
    private SQLiteDatabase mSQLiteDatabase = null;
    private DataBaseManagementHelper mDatabaseHelper = null;
    private static class DataBaseManagementHelper extends SQLiteOpenHelper{
        DataBaseManagementHelper(Context context){super(context,DB_NAME,null,DB_VERSION);
        this.myContext = context;}
        @Override
        public void onCreate(SQLiteDatabase db){
            Log.i(TAG,"db.getVersion()= "+db.getVersion());
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME +";");
            db.execSQL(DB_CREATE);
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            Log.i(TAG,"DataBaseManagermentHelper onUpgrade");
            onCreate(db);
        }
        private Context myContext;
        private String DB_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
       // private static String B_NAME = "user_data.db";
        private static String ASSETS_NAME = "user_data.db";
        private SQLiteDatabase myDataBase = null;
        public void createDataBase() throws IOException {
            boolean dbExist = checkDataBase();

            if (!dbExist) {
                try {
                    File dir = new File(DB_PATH);
                    if (!dir.exists()) {
                        dir.mkdir();
                    }
                    File dbf = new File(DB_PATH + DB_NAME);
                    if (dbf.exists()) {
                        dbf.delete();
                    }
                    SQLiteDatabase.openOrCreateDatabase(dbf, null);
                    copyDataBase();
                } catch (IOException e) {
                    throw new Error("数据库创建失败");
                }
            }
        }
        private void copyDataBase() throws IOException {
            InputStream myInput = null;
//        try {
            myInput = myContext.getAssets().open(ASSETS_NAME);
            String outFileName = DB_PATH + DB_NAME;
            OutputStream myOutput = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();
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


    }
    public FoodDataManager(Context context){
        mContext = context;
        Log.i(TAG,"FoodDataManager construction");
    }
    //打开数据库
    public void openDataBase() throws SQLException {
        mDatabaseHelper = new FoodDataManager.DataBaseManagementHelper(mContext);
        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
    }

    //log

    //关闭数据库
    public void closeDataBase() throws SQLException {
        mDatabaseHelper.close();
    }
    public long insertFoodData(FoodData foodData){
        String FoodName = foodData.getFoodName();
        double FoodKcal = foodData.getFoodKcal();
        double FoodProtein = foodData.getFoodProtein();
        double FoodFats = foodData.getFoodFats();
        double FoodCHO = foodData.getFoodCHO();
        ContentValues values = new ContentValues();
        values.put(FOOD_NAME,FoodName);
        values.put(FOOD_CHO,FoodCHO);
        values.put(FOOD_FATS,FoodFats);
        values.put(FOOD_PROTEIN,FoodProtein);
        values.put(FOOD_KCAL,FoodKcal);
        return mSQLiteDatabase.insert(TABLE_NAME,ID,values);
    }
    //返回一个用户名的列表
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
}
