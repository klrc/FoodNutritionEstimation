package com.example.camera;

import android.graphics.Bitmap;
import android.util.Log;

public class ResultShowInfo {
    private int imageId;
    private String name;
    private Bitmap icon;


//    public Bitmap getIcon()
//    {
//        return icon;
//    }

    //    public void setIcon(Bitmap icon)
//    {
//        this.icon = icon;
//    }
    public ResultShowInfo(String name,int imageId,String foods_vol,String foods_weight){
        this.name = name+"\n食物体积： "+foods_vol+"ml\n大概"+foods_weight+"g";
        this.imageId = imageId;
    }

    public String getName()
    {
        return name;
    }
    public int clicked_user(){
        this.imageId = R.drawable.food;
        Log.i("点击事件","修改头像");
        return imageId;
    }
    public void set_food_info(){

    }


    public Bitmap getIcon()
    {
        return icon;
    }
    public int getImageId(){
        return imageId;
    }
}
