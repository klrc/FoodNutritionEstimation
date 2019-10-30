package com.example.camera;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
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

import static android.support.constraint.Constraints.TAG;

public class CustomSurfaceView extends SurfaceView implements SurfaceHolder.Callback{
    private Camera mCamera;
    private static int mCameraID = Camera.CameraInfo.CAMERA_FACING_BACK;

    private static SurfaceHolder holder;
    private ONTouchEvent mTouchEvent;
    private CustomSurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private boolean havePermission = false;
    private Button btnFocus;
    private Button btnTakePic;
    private Button btnRestar;
    private Button btnChange;
    private int useWidth;
    private int useHeight;
    private SensorManager mSensorManager;
    private Sensor mSensor;


    public CustomSurfaceView(Context context) {
        super(context);
    }

    public CustomSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.i(TAG, "new View ...");

        holder = getHolder();//后面会用到！
        holder.addCallback(this);
    }

    public CustomSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (MotionEvent.ACTION_DOWN == event.getAction()) {
            mTouchEvent.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    public void setCustomEvent(ONTouchEvent onTouchEvent) {
        mTouchEvent = onTouchEvent;
    }

    public interface ONTouchEvent {
        public void onTouchEvent(MotionEvent event);
    }
    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        Log.i(TAG, "surfaceCreated...");
        if (mCamera == null) {
            // mCamera.takePicture(null,null,);


            mCamera = Camera.open();//开启相机，可以放参数 0 或 1，分别代表前置、后置摄像头，默认为 0
            try {

                mCamera.cancelAutoFocus();
                Camera.Parameters params =  mCamera.getParameters();
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);//setZoom(40);
                mCamera.setParameters(params);
                mCamera.setPreviewDisplay(holder);//整个程序的核心，相机预览的内容放在 holder
                mCamera.setDisplayOrientation(90);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setRecordingHint(true);
            {
                //设置获取数据
                parameters.setPreviewFormat(ImageFormat.NV21);
                //通过setPreviewCallback方法监听预览的回调：
                mCamera.setPreviewCallback(new Camera.PreviewCallback() {
                    @Override
                    public void onPreviewFrame(byte[] bytes, Camera camera) {
                        //这里面的Bytes的数据就是NV21格式的数据,或者YUV_420_888的数据
                    }
                });
            }

        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        Log.i(TAG, "surfaceChanged...");
        mCamera.startPreview();//该方法只有相机开启后才能调用
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        Log.i(TAG, "surfaceChanged...");
        if (mCamera != null) {
            mCamera.release();//释放相机资源
            mCamera = null;
        }
    }
    /////////////////////////////////////////



}