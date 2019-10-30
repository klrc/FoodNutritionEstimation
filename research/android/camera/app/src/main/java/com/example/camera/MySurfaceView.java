package com.example.camera;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.Toast;

public class MySurfaceView extends SurfaceView implements
        SurfaceHolder.Callback {
    private static final String TAG = "Kintai";
    private static int mCameraID = Camera.CameraInfo.CAMERA_FACING_BACK;
    private Button btn;

    private static SurfaceHolder holder;
    private Camera mCamera;

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.i(TAG, "new View ...");

        holder = getHolder();//后面会用到！
        holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        Log.i(TAG, "surfaceCreated...");
        if (mCamera == null) {
           // mCamera.takePicture(null,null,);


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
    /////////////////////////////////////////////////////////////


}
