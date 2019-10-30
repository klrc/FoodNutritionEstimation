package com.example.camera;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
//import android.graphics.Camera;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.camera.CameraGrid;
import com.example.camera.utils.CameraParaUtil;

import android.hardware.Camera;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;



public class FoodDetection extends AppCompatActivity{
    public static final int TAKE_PHOTO2 = 1;
    public static final int CHOOSE_PHOTO = 2;
    private ImageView picture;
    private Uri imageUri;
    private Button take_photo;
    private Button choose_from_album;
    private Button manual_choose;
    private UserDataManager mUserDataManager;
    public String TAG = "fooddetection";
    public MySurfaceView mView;
    String loginuser;




    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.home_item:
                    Intent intent_MainInterface_to_FoodDetection = new Intent(FoodDetection.this,MainInterface.class);
                    intent_MainInterface_to_FoodDetection.putExtra("user",loginuser);
                    startActivity(intent_MainInterface_to_FoodDetection);
                   // onBackPressed();
                    break;
                case R.id.nutri_table_item:
                    Intent intent_MainInterface_to_Nutrition = new Intent(FoodDetection.this, NutritionIntake.class);
                    Log.i(TAG,"出去的用户是："+loginuser);
                    intent_MainInterface_to_Nutrition.putExtra("user",loginuser);
                    startActivity(intent_MainInterface_to_Nutrition);
                    finish();
                    Log.i(TAG,"GOTO NUTRI");
                    break;
                case R.id.food_set_item:
                    Intent intent_MainInterface_FoodMenu = new Intent(FoodDetection.this,FoodMenu2.class);
                    intent_MainInterface_FoodMenu.putExtra("user",loginuser);
                    startActivity(intent_MainInterface_FoodMenu);
                    finish();
                    Log.i(TAG,"GOTO MENU");
                    break;
                case R.id.detect_item:
                    // mTextMessage.setText(R.string.title_notifications);
                    // Intent intent_MainInterface_to_FoodDetection = new Intent(MainInterface.this,FoodDetection.class);
                    return true;

                case R.id.setting_item:
                    Intent intent_MainInterface_Setting = new Intent(FoodDetection.this,Setting.class);
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
        setContentView(R.layout.activity_food_detection);
        getSupportActionBar().hide();
        take_photo = (Button)findViewById(R.id.take_photo2);
      //  tp = (Button)findViewById(R.id.btn_take_photo);
        choose_from_album = (Button)findViewById(R.id.choose_from_album2);
        manual_choose = (Button)findViewById(R.id.manual_selection);
        picture = (ImageView) findViewById(R.id.picture2);
        Intent intent =getIntent();
        loginuser=intent.getStringExtra("user");
        Log.i(TAG,"loginuser："+loginuser);
        if(mUserDataManager == null){
            mUserDataManager = new UserDataManager(this);
            mUserDataManager.openDataBase();
        }
        ///////////////////////////////////////////////////////camera.open测试
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.CAMERA}, 0);
            }
        }

        ///////////////////////////////////////////////////////
        BottomNavigationView navigation = findViewById(R.id.navigation_food_detection);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.detect_item);
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                File outputImage = new File (getExternalCacheDir(),"output_image.jpg");
//                Log.i(TAG,"get image1");
//                try{
//                    if(outputImage.exists()){
//                        outputImage.delete();
//                    }
//                    Log.i(TAG,"get image2");
//                    outputImage.createNewFile();
//                    Log.i(TAG,"get image3");
//                }catch (IOException e){
//                    e.printStackTrace();
//                }
//                if(Build.VERSION.SDK_INT >= 24){
//                    imageUri = FileProvider.getUriForFile(FoodDetection.this,"com.example.kangkang59812.login.fileprovider",outputImage);
//                }
//                else{
//                    imageUri = Uri.fromFile(outputImage);
//                }
//
//                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                Log.i(TAG,"get image4");
//                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
//                startActivityForResult(intent,TAKE_PHOTO2);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[] {Manifest.permission.CAMERA}, 1);
//            }
//        }

                startTakePhoto();
            }
        });
        choose_from_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(FoodDetection.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(FoodDetection.this, new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1
                    );
                } else {
                    openAlbum();
                }
            }
        });
        manual_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // mCamera = Camera.open(mCameraID);
                Intent intent_FoodDetection_to_ManualChoose = new Intent(FoodDetection.this,ManualChoose.class);
                startActivity(intent_FoodDetection_to_ManualChoose);


            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults){
        switch(requestCode){
            case 1:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else{
                    Toast.makeText(this,"you denied the permission",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        switch (requestCode){
            case TAKE_PHOTO2:

                    Log.i(TAG,"跳往结果00");
                    Log.i(TAG,"对勾");
                    Intent intent = new Intent(FoodDetection.this,ResultOthers.class);
                    intent.putExtra("user",loginuser);
                    startActivity(intent);
                       // finish();
                        break;


            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK){
                    //判断手机版本号
                    if (Build.VERSION.SDK_INT>=19){
                        handleImageOnKitKat(data);
                    } else{
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

        public void openAlbum(){
            Intent intent = new Intent("android.intent.action.GET_CONTENT");
            intent.setType("image/*");
            startActivityForResult(intent,CHOOSE_PHOTO);
        }
    private void startTakePhoto() {
        Intent intent = new Intent(FoodDetection.this, SydCameraActivity.class);
        intent.putExtra(CameraParaUtil.picQuality, 100); //图片质量0~100
//        intent.putExtra(CameraParaUtil.pictureSize, 300); //图片大小 KB
        intent.putExtra(CameraParaUtil.picWidth, 1536);  //照片最小宽度配置，高度根据屏幕比例自动配置
        intent.putExtra(CameraParaUtil.previewWidth, 1280);  //相机预览界面最小宽度配置，高度根据屏幕比例自动配置
        Log.i(TAG,"fd——》syd："+loginuser);
        intent.putExtra("user",loginuser);
        startActivityForResult(intent, CameraParaUtil.REQUEST_CODE_FROM_CAMERA);
    }
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this,uri)) {
            //如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        }
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri, null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            imagePath = uri.getPath();}
        Log.i(TAG,"display picture");
        displayImage(imagePath);
        Log.i(TAG,"display picture2");
    }
    private void handleImageBeforeKitKat(Intent data){
        Uri uri =data.getData();
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath);
    }
    private String getImagePath(Uri uri,String selection){
        String path =null;
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    private void displayImage(String imagePath){
        if(imagePath !=null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
            Toast.makeText(this,"success to get image",Toast.LENGTH_SHORT).show();}
        else {
            Toast.makeText(this,"failed to get image",Toast.LENGTH_SHORT).show();
        }}

}
