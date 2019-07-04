//package com.example.camera;
//import android.Manifest;
//import android.annotation.TargetApi;
//import android.app.Activity;
//import android.content.ContentUris;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.os.Build;
//import android.provider.DocumentsContract;
//import android.provider.MediaStore;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.content.FileProvider;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//
//public class User extends AppCompatActivity {
//    public static final int TAKE_PHOTO = 1;
//    public static final int CHOOSE_PHOTO = 2;
//    private Button mReturnButton;
//    private Button mUserCancel;
//    private ImageView picture;
//    private Uri imageUri;
//    private String userName;
//    private String userPwd;
//    private String TAG = "User.Class";
//    private UserDataManager mUserDataManager;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.user_layout);
//        Intent intent = getIntent();
//        userName = intent.getStringExtra("USER_NAME");
//        userPwd = intent.getStringExtra("PASSWORD");
//        Log.i(TAG, "OnCreate()+"+userName);
//        Log.i(TAG, "OnCreate()+"+userPwd);
//        mReturnButton = (Button) findViewById(R.id.returnback);
//        mUserCancel = (Button) findViewById(R.id.user_cancel);
//        //相机
//
//        final Button takephoto = (Button)findViewById(R.id.take_photo);
//        picture = (ImageView) findViewById(R.id.picture);
//        mReturnButton.setOnClickListener(mListener);
//        mUserCancel.setOnClickListener(mListener);
//        if (mUserDataManager == null) {
//            mUserDataManager = new UserDataManager(this);
//            mUserDataManager.openDataBase();
//        }
//        takephoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                File outputImage = new File(getExternalCacheDir(),"output_image.jpg");
//                try{
//                    if (outputImage.exists()){
//                        outputImage.delete();
//                    }
//                    outputImage.createNewFile();
//                }catch (IOException e){
//                    e.printStackTrace();
//                }
//                if(Build.VERSION.SDK_INT >= 24){
//                    imageUri = FileProvider.getUriForFile(User.this,"com.example.kangkang59812.login.fileprovider",outputImage);
//                }
//                else
//                {
//                    imageUri = Uri.fromFile(outputImage);
//                }
//                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
//                startActivityForResult(intent,TAKE_PHOTO);
//            }
//        });
//        //相册
//        Button chooseFromAlbum = (Button)findViewById(R.id.choose_from_album);
//        chooseFromAlbum.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(ContextCompat.checkSelfPermission(User.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
//                    ActivityCompat.requestPermissions(User.this,new String[]{
//                            Manifest.permission.WRITE_EXTERNAL_STORAGE},1
//                    );
//                }
//                else{
//                    openAlbum();
//                }
//            }
//        });
//    }
//    private void openAlbum(){
//        Intent intent = new Intent("android.intent.action.GET_CONTENT");
//        intent.setType("image/*");
//        startActivityForResult(intent,CHOOSE_PHOTO);
//    }@Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults){
//        switch(requestCode){
//            case 1:
//                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
//                    openAlbum();
//                }else{
//                    Toast.makeText(this,"you denied the permission",Toast.LENGTH_SHORT).show();
//                }
//                break;
//            default:
//        }
//    }
//    @Override
//    protected void onActivityResult(int requestCode,int resultCode,Intent data){
//        switch (requestCode){
//            case TAKE_PHOTO:
//                if (resultCode == RESULT_OK){
//                    try{
//                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
//                        picture.setImageBitmap(bitmap);
//                    }catch (FileNotFoundException e){
//                        e.printStackTrace();
//                    }
//                }
//                break;
//            case CHOOSE_PHOTO:
//                if (resultCode == RESULT_OK){
//                    //判断手机版本号
//                    if (Build.VERSION.SDK_INT>=19){
//                        handleImageOnKitKat(data);
//                    } else{
//                        handleImageBeforeKitKat(data);
//                    }
//                }
//                break;
//            default:
//                break;
//        }
//    }
//    @TargetApi(19)
//    private void handleImageOnKitKat(Intent data){
//        String imagePath = null;
//        Uri uri = data.getData();
//        if (DocumentsContract.isDocumentUri(this,uri)) {
//            //如果是document类型的Uri，则通过document id处理
//            String docId = DocumentsContract.getDocumentId(uri);
//            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
//                String id = docId.split(":")[1];//解析出数字格式的id
//                String selection = MediaStore.Images.Media._ID + "=" + id;
//                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
//            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
//                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
//                imagePath = getImagePath(contentUri, null);
//            }
//        }
//        else if ("content".equalsIgnoreCase(uri.getScheme())) {
//            imagePath = getImagePath(uri, null);
//        }else if("file".equalsIgnoreCase(uri.getScheme())){
//            imagePath = uri.getPath();}
//        displayImage(imagePath);
//    }
//    private void handleImageBeforeKitKat(Intent data){
//        Uri uri =data.getData();
//        String imagePath = getImagePath(uri,null);
//        displayImage(imagePath);
//    }
//    private String getImagePath(Uri uri,String selection){
//        String path =null;
//        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
//        if(cursor!=null){
//            if(cursor.moveToFirst()){
//                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//            }
//            cursor.close();
//        }
//        return path;
//    }
//    private void displayImage(String imagePath){
//        if(imagePath !=null){
//            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//            picture.setImageBitmap(bitmap);}
//        else {
//            Toast.makeText(this,"failed to get image",Toast.LENGTH_SHORT).show();
//        }}
//
//    //lkk code
//    View.OnClickListener mListener=new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.returnback:
//                    back_to_login();
//                    break;
//                case R.id.user_cancel:
//                    user_cancel();
//                    break;
//            }
//        }
//    };
//
//    public void user_cancel() {
//        int result = mUserDataManager.findUserByNameAndPwd(userName, userPwd);
//        if (result == 1) {
//            Toast.makeText(this, "注销成功", Toast.LENGTH_SHORT).show();
//            mUserDataManager.deleteUserDataByName(userName);
//            back_to_login();
//        } else if (result == 0) {
//            Toast.makeText(this, "注销失败！请重试", Toast.LENGTH_SHORT).show();
//        }
//        Log.i(TAG, "user_cancel()");
//    }
//    public void back_to_login(){
//        Intent intent3=new Intent(User.this,Login.class);
//        startActivity(intent3);
//        finish();
//    }
//
//
//
//}
//
//
