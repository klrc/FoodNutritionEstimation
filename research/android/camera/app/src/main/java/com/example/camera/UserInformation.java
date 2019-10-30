package com.example.camera;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class UserInformation {
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
    public UserInformation(String name,int imageId,String identity){
        this.name = name+"\n用户身份："+identity;
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

//    public void setName(String name)
//    {
//        this.name = name;
//    }


    public int getImageId(){
        return imageId;
    }
}