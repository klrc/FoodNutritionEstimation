package com.example.camera;


/**
 * Created by kangkang59812 on 2017-11-02.
 */

public class UserData {
    private String userName;//用户名
    private String userPwd;//用户密码
    private int userId;//用户id
    public int pwdresetFlag=0;

    //获取账号
    public String getUserName(){
        return userName;
    }

    //获取密码
    public String getUserPwd(){
        return userPwd;
    }

    //获取用户id
    public int getUserId() {
        return userId;
    }

    //设置账号
    public void setUserName(String userName) {
        this.userName = userName;
    }

    //设置密码
    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    //设置用户id
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public UserData(String userName,String userPwd){
        super();//
        this.userName = userName;
        this.userPwd = userPwd;

    }
}
