package com.example.camera;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//public class NewPhoto extends AppCompatActivity {
public class NewPhoto extends AppCompatActivity implements SurfaceHolder.Callback , SensorEventListener {
        private static int mOrientation = 0;
        private static int mCameraID = Camera.CameraInfo.CAMERA_FACING_BACK;
        private CustomSurfaceView mSurfaceView;
        private SurfaceView mPreview;
        private SurfaceHolder mSurfaceHolder;
        private Camera mCamera;
        private boolean havePermission = false;
        private Button btnFocus;
        private Button btnTakePic;
        private Button btnRestar;
        private Button btnChange;
        private int cameraId=1;
        private int useWidth;
        private int useHeight;
        private SensorManager mSensorManager;
        private Sensor mSensor;
        private String TAG = "NewPhoto";
//    private Camera.PictureCallback mpictureCallback=new Camera.PictureCallback(){
//        @Override
//        public void onPictureTaken(byte[] data,Camera camera){
//            File tempfile=new File("/sdcard/emp.png");//新建一个文件对象tempfile，并保存在某路径中
//            try{ FileOutputStream fos =new FileOutputStream(tempfile);
//                fos.write(data);//将照片放入文件中
//                fos.close();//关闭文件
//                Intent intent=new Intent(NewPhoto.this,FoodMenu2.class);//新建信使对象
//                intent.putExtra("picpath",tempfile.getAbsolutePath());//打包文件给信使
//                startActivity(intent);//打开新的activity，即打开展示照片的布局界面
//                NewPhoto.this.finish();//关闭现有界面
//            }
//            catch (IOException e){e.printStackTrace();}
//        }
//    };

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_new_photo);
            //mSurfaceView = findViewById(R.id.mView2);
          //  mSurfaceView = findViewById(R.id.mView2);
            btnFocus = findViewById(R.id.focus);
            btnTakePic = findViewById(R.id.takepic);
            btnRestar = findViewById(R.id.restar);
            btnChange = findViewById(R.id.change);

//            mSurfaceHolder.addCallback(this);
//            mSurfaceView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.i(TAG,"手动聚焦");
//                    mCamera.autoFocus(null);
//                }
//            });





//            btnChange.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    switchCamera();
//                }
//            });

//            btnRestar.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(mCamera != null){
//                        mCamera.startPreview();
//                    }
//                }
//            });
//            btnFocus.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mCamera != null && mCameraID == Camera.CameraInfo.CAMERA_FACING_BACK){
//                        mCamera.autoFocus(new Camera.AutoFocusCallback() {
//                            @Override
//                            public void onAutoFocus(boolean success, Camera camera) {
//                                if(success){
//                                    Toast.makeText(NewPhoto.this,"对焦成功",Toast.LENGTH_SHORT).show();
//                                }else{
//
//                                }
//                            }
//                        });
//                    }
//                }
//            });
            btnTakePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mCamera!= null){
                        Log.i(TAG,"mCamera!= null");
                        mCamera.startPreview();
                        mCamera.takePicture(null, null, new Camera.PictureCallback() {
                            @Override
                            public void onPictureTaken(byte[] data, Camera camera) {
                                // 获取Jpeg图片，并保存在sd卡上
                                String path = Environment.getExternalStorageDirectory()
                                        .getPath()  +"/focus/";
                                File pathDir = new File(path);
                                if (!pathDir.exists()){
                                    pathDir.mkdir();
                                }
                                File pictureFile = new File(path+ "focusdemo.jpg");
                                if (pictureFile.exists()){
                                    pictureFile.delete();
                                }
                                try {
                                    FileOutputStream fos = new FileOutputStream(pictureFile);
                                    fos.write(data);
                                    fos.close();
                                } catch (Exception e) {

                                }
                            }

                        });
                        Log.i(TAG,"拍照结束");
                    }

                }
            });


//获取当前设备支持的传感器列表
            mSensorManager = (SensorManager) NewPhoto.this.getSystemService(Activity.SENSOR_SERVICE);
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);// 加速度

            // Android 6.0相机动态权限检查,省略了
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                havePermission = true;
                init();
            } else {
                havePermission = false;
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        }, 100);

            }
        }
    //定义“拍照”方法
//    public void capture(View view){
//        Camera.Parameters parameters=mCamera.getParameters();
//        parameters.setPictureFormat(ImageFormat.JPEG);//设置照片格式
//        parameters.setPreviewSize(800,400);
//        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
//        //摄像头聚焦
//        mCamera.autoFocus(new Camera.AutoFocusCallback(){
//            @Override
//            public void onAutoFocus(boolean success, Camera camera) {
//                if(success){mCamera.takePicture(null,null, mpictureCallback);}
//            }
//        });
//
//    }
    //activity生命周期在onResume是界面应是显示状态
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (mCamera==null){//如果此时摄像头值仍为空
//            mCamera=getCamera();//则通过getCamera()方法开启摄像头
//            if(mSurfaceHolder!=null){
//                setStartPreview(mCamera,mSurfaceHolder);//开启预览界面
//            }
//        }
//    }
    //onResume()中提到的开启摄像头的方法
    private Camera getCamera(){
        Camera camera;//声明局部变量camera
        try{
            camera=Camera.open(cameraId);}//根据cameraId的设置打开前置摄像头
        catch (Exception e){
            camera=null;
            e.printStackTrace(); }
        return camera;
    }
    //开启预览界面
    private void setStartPreview(Camera camera,SurfaceHolder holder){
        try{
            camera.setPreviewDisplay(holder);
            camera.setDisplayOrientation(90);//如果没有这行你看到的预览界面就会是水平的
            camera.startPreview();}
        catch (Exception e){
            e.printStackTrace(); }
    }


        @Override
        protected void onStop() {
            super.onStop();
            mSensorManager.unregisterListener(this,mSensor);
        }
//释放摄像头资源
        public void releaseCamera(){
            if (mCamera != null) {
                mSurfaceHolder.removeCallback(this);
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();
                mCamera.lock();
                mCamera.release();
                mCamera = null;

            }
        }
//切换摄像头
        public void switchCamera(){
            if(mCameraID ==  Camera.CameraInfo.CAMERA_FACING_BACK){
                mCameraID =  Camera.CameraInfo.CAMERA_FACING_FRONT;
            }else{
                mCameraID =  Camera.CameraInfo.CAMERA_FACING_BACK;
            }
            try {
                initCamera();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void init(){
          //  if(mSurfaceView == null){
                mSurfaceView = findViewById(R.id.mView2);
                mSurfaceView.setCustomEvent(new CustomSurfaceView.ONTouchEvent() {
                    @Override
                    public void onTouchEvent(MotionEvent event) {
                        handleFocus(event, mCamera);
                    }
                });
                mSurfaceHolder = mSurfaceView.getHolder();
                mSurfaceHolder.addCallback(this);
                WindowManager wm = (WindowManager) NewPhoto.this.getSystemService(Context.WINDOW_SERVICE);
                int width = wm.getDefaultDisplay().getWidth();
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mSurfaceView.getLayoutParams();
                layoutParams.width = width;
                layoutParams.height = width*4/3;
                useWidth = width;
                useHeight = width*4/3;
                mSurfaceView.setLayoutParams(layoutParams);
       //     }

        }

        private void initCamera() {
            if (mCamera != null){
                releaseCamera();
                Log.i(TAG,"===================releaseCamera=============");
            }
            mCamera = Camera.open(mCameraID);
            Log.i(TAG,"===================openCamera=============");
            if (mCamera != null){
                try {
                    Log.i(TAG,"SETBEFORE");
                    mCamera.setPreviewDisplay(mSurfaceHolder);
                    Log.i(TAG,"SETAFTER");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Camera.Parameters parameters = mCamera.getParameters();
                parameters.setRecordingHint(true);
                {
                    //设置获取数据
                    parameters.setPreviewFormat(ImageFormat.NV21);
                    //parameters.setPreviewFormat(ImageFormat.YUV_420_888);

                    //通过setPreviewCallback方法监听预览的回调：
                    mCamera.setPreviewCallback(new Camera.PreviewCallback() {
                        @Override
                        public void onPreviewFrame(byte[] bytes, Camera camera) {
                            //这里面的Bytes的数据就是NV21格式的数据,或者YUV_420_888的数据


                        }
                    });
                }

                if(mCameraID == Camera.CameraInfo.CAMERA_FACING_BACK){
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                }

                mCamera.setParameters(parameters);

                calculateCameraPreviewOrientation(this);
                Camera.Size tempSize = setPreviewSize(mCamera, useHeight,useWidth);
                {
                    //此处可以处理，获取到tempSize，如果tempSize和设置的SurfaceView的宽高冲突，重新设置SurfaceView的宽高
                }

                setPictureSize(mCamera,  useHeight,useWidth);
                mCamera.setDisplayOrientation(mOrientation);
                int degree = calculateCameraPreviewOrientation(NewPhoto.this);
                mCamera.setDisplayOrientation(degree);

                mCamera.startPreview();

            }
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {


            mCamera = Camera.open();//开启相机，可以放参数 0 或 1，分别代表前置、后置摄像头，默认为 0
            try {

                mCamera.cancelAutoFocus();
                //  mCamera.getParameters().setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
//                mCamera.autoFocus(new Camera.AutoFocusCallback() {
//                    @Override
//                    public void onAutoFocus(boolean success, Camera camera) {
//                        if(success){
//                            Toast.makeText(null,"对焦成功", Toast.LENGTH_SHORT).show();
//                        }else{
//
//                        }
//                    }
//                });
                Camera.Parameters params =  mCamera.getParameters();
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);//setZoom(40);
                mCamera.setParameters(params);
                mCamera.setPreviewDisplay(holder);//整个程序的核心，相机预览的内容放在 holder
                mCamera.setDisplayOrientation(90);
//                Log.i(TAG,"对焦："+mCamera.getParameters().getZoom());
//                Log.i(TAG,"最大对焦："+mCamera.getParameters().getMaxZoom());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setRecordingHint(true);
            {
                //设置获取数据
                parameters.setPreviewFormat(ImageFormat.NV21);
                //parameters.setPreviewFormat(ImageFormat.YUV_420_888);

                //通过setPreviewCallback方法监听预览的回调：
                mCamera.setPreviewCallback(new Camera.PreviewCallback() {
                    @Override
                    public void onPreviewFrame(byte[] bytes, Camera camera) {
                        //这里面的Bytes的数据就是NV21格式的数据,或者YUV_420_888的数据


                    }
                });
            }

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            //当SurfaceView变化时也需要做相应操作，这里未做相应操作
            if (havePermission){
                initCamera();
            }

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            mCamera.stopPreview();
        }

        private void setPictureSize(Camera camera ,int expectWidth,int expectHeight){
            Camera.Parameters parameters = camera.getParameters();
            Point point = new Point(expectWidth, expectHeight);
            Camera.Size size = findProperSize(point,parameters.getSupportedPreviewSizes());
            parameters.setPictureSize(size.width, size.height);
            camera.setParameters(parameters);
        }

        private Camera.Size setPreviewSize(Camera camera, int expectWidth, int expectHeight) {
            Camera.Parameters parameters = camera.getParameters();
            Point point = new Point(expectWidth, expectHeight);
            Camera.Size size = findProperSize(point,parameters.getSupportedPictureSizes());
            parameters.setPictureSize(size.width, size.height);
            camera.setParameters(parameters);
            return size;
        }

        /**
         * 找出最合适的尺寸，规则如下：
         * 1.将尺寸按比例分组，找出比例最接近屏幕比例的尺寸组
         * 2.在比例最接近的尺寸组中找出最接近屏幕尺寸且大于屏幕尺寸的尺寸
         * 3.如果没有找到，则忽略2中第二个条件再找一遍，应该是最合适的尺寸了
         */
        private static Camera.Size findProperSize(Point surfaceSize, List<Camera.Size> sizeList) {
            if (surfaceSize.x <= 0 || surfaceSize.y <= 0 || sizeList == null) {
                return null;
            }

            int surfaceWidth = surfaceSize.x;
            int surfaceHeight = surfaceSize.y;

            List<List<Camera.Size>> ratioListList = new ArrayList<>();
            for (Camera.Size size : sizeList) {
                addRatioList(ratioListList, size);
            }

            final float surfaceRatio = (float) surfaceWidth / surfaceHeight;
            List<Camera.Size> bestRatioList = null;
            float ratioDiff = Float.MAX_VALUE;
            for (List<Camera.Size> ratioList : ratioListList) {
                float ratio = (float) ratioList.get(0).width / ratioList.get(0).height;
                float newRatioDiff = Math.abs(ratio - surfaceRatio);
                if (newRatioDiff < ratioDiff) {
                    bestRatioList = ratioList;
                    ratioDiff = newRatioDiff;
                }
            }

            Camera.Size bestSize = null;
            int diff = Integer.MAX_VALUE;
            assert bestRatioList != null;
            for (Camera.Size size : bestRatioList) {
                int newDiff = Math.abs(size.width - surfaceWidth) + Math.abs(size.height - surfaceHeight);
                if (size.height >= surfaceHeight && newDiff < diff) {
                    bestSize = size;
                    diff = newDiff;
                }
            }

            if (bestSize != null) {
                return bestSize;
            }

            diff = Integer.MAX_VALUE;
            for (Camera.Size size : bestRatioList) {
                int newDiff = Math.abs(size.width - surfaceWidth) + Math.abs(size.height - surfaceHeight);
                if (newDiff < diff) {
                    bestSize = size;
                    diff = newDiff;
                }
            }

            return bestSize;
        }

        private static void addRatioList(List<List<Camera.Size>> ratioListList, Camera.Size size) {
            float ratio = (float) size.width / size.height;
            for (List<Camera.Size> ratioList : ratioListList) {
                float mine = (float) ratioList.get(0).width / ratioList.get(0).height;
                if (ratio == mine) {
                    ratioList.add(size);
                    return;
                }
            }

            List<Camera.Size> ratioList = new ArrayList<>();
            ratioList.add(size);
            ratioListList.add(ratioList);
        }
        /**
         * 排序
         * @param list
         */
        private static void sortList(List<Camera.Size> list) {
            Collections.sort(list, new Comparator<Camera.Size>() {
                @Override
                public int compare(Camera.Size pre, Camera.Size after) {
                    if (pre.width > after.width) {
                        return 1;
                    } else if (pre.width < after.width) {
                        return -1;
                    }
                    return 0;
                }
            });
        }

        /**
         * 设置预览角度，setDisplayOrientation本身只能改变预览的角度
         * previewFrameCallback以及拍摄出来的照片是不会发生改变的，拍摄出来的照片角度依旧不正常的
         * 拍摄的照片需要自行处理
         * 这里Nexus5X的相机简直没法吐槽，后置摄像头倒置了，切换摄像头之后就出现问题了。
         * @param activity
         */
        public static int calculateCameraPreviewOrientation(Activity activity) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(mCameraID, info);
            int rotation = activity.getWindowManager().getDefaultDisplay()
                    .getRotation();
            int degrees = 0;
            switch (rotation) {
                case Surface.ROTATION_0:
                    degrees = 0;
                    break;
                case Surface.ROTATION_90:
                    degrees = 90;
                    break;
                case Surface.ROTATION_180:
                    degrees = 180;
                    break;
                case Surface.ROTATION_270:
                    degrees = 270;
                    break;
            }

            int result;
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                result = (info.orientation + degrees) % 360;
                result = (360 - result) % 360;
            } else {
                result = (info.orientation - degrees + 360) % 360;
            }
            mOrientation = result;
            System.out.println("=========orienttaion============="+result);
            return result;
        }

        @Override
        protected void onResume() {
            super.onResume();
            mSensorManager.registerListener(this, mSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
            if (havePermission && mCamera != null)
                mCamera.startPreview();
        }

        @Override
        protected void onPause() {
            super.onPause();
            if (havePermission && mCamera != null)
                mCamera.stopPreview();
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            switch (requestCode) {
                // 相机权限
                case 100:
                    havePermission = true;
                    init();
                    break;
            }
        }

        /**
         * 转换对焦区域
         * 范围(-1000, -1000, 1000, 1000)
         */
        private Rect calculateTapArea(float x, float y, int width, int height, float coefficient) {
            float focusAreaSize = 200;
            int areaSize = (int) (focusAreaSize * coefficient);
            int surfaceWidth = width;
            int surfaceHeight = height;
            int centerX = (int) (x / surfaceHeight * 2000 - 1000);
            int centerY = (int) (y / surfaceWidth * 2000 - 1000);
            int left = clamp(centerX - (areaSize / 2), -1000, 1000);
            int top = clamp(centerY - (areaSize / 2), -1000, 1000);
            int right = clamp(left + areaSize, -1000, 1000);
            int bottom = clamp(top + areaSize, -1000, 1000);
            return new Rect(left, top, right, bottom);
        }

        //不大于最大值，不小于最小值
        private  int clamp(int x, int min, int max) {
            if (x > max) {
                return max;
            }
            if (x < min) {
                return min;
            }
            return x;
        }

        private  void handleFocus(MotionEvent event, Camera camera) {
            int viewWidth = useWidth;
            int viewHeight = useHeight;
            Rect focusRect = calculateTapArea(event.getX(), event.getY(),  viewWidth, viewHeight,1.0f);

            //一定要首先取消
            camera.cancelAutoFocus();
            Camera.Parameters params = camera.getParameters();
            if (params.getMaxNumFocusAreas() > 0) {
                List<Camera.Area> focusAreas = new ArrayList<>();
                focusAreas.add(new Camera.Area(focusRect, 800));
                params.setFocusAreas(focusAreas);
            } else {
                //focus areas not supported
            }
            //首先保存原来的对焦模式，然后设置为macro，对焦回调后设置为保存的对焦模式
            final String currentFocusMode = params.getFocusMode();
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_MACRO);
            camera.setParameters(params);

            camera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    //回调后 还原模式
                    Camera.Parameters params = camera.getParameters();
                    params.setFocusMode(currentFocusMode);
                    camera.setParameters(params);
                    if(success){
                        Toast.makeText(NewPhoto.this,"对焦区域对焦成功",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            //手机移动一段时间后静止，然后静止一段时间后进行对焦
            // 读取加速度传感器数值，values数组0,1,2分别对应x,y,z轴的加速度
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                int x = (int) event.values[0];
                int y = (int) event.values[1];
                int z = (int) event.values[2];

            }
        }



    @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }
