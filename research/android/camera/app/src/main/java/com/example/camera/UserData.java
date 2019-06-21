package com.example.camera;


/**
 * Created by kangkang59812 on 2017-11-02.
 */

public class UserData {
    private String userName;//用户名
    //private String userPwd;//用户密码
    private int userId;//用户id
    private String Birth;
    private double Weight;
    private double Height;
    private double BMI;
    private double Kcal;
    private double Protein;
    private double Fats;
    private double CHO;
    private double Na;
    private String Sex;
    public int pwdresetFlag=0;

    //获取账号
    public String getUserName(){
        return userName;
    }

    //获取密码
//    public String getUserPwd(){
//        return userPwd;
//    }

    //获取用户id
    public int getUserId() {
        return userId;
    }
    public String getBirth() { return Birth;}
    public String getSex() {return Sex;}
    public double getWeight() {return Weight;}
    public double getHeight() {return Height;}
    public double getBMI() {return BMI;}
    public double getKcal() { return  Kcal; }
    public double getProtein() { return Protein;}
    public double getFats() { return Fats;}
    public double getCHO()  { return CHO;}
    //public double getNa() {return  Na;}

    //设置账号
    public void setUserName(String userName) {
        this.userName = userName;
    }

    //设置密码
//    public void setUserPwd(String userPwd) {
//        this.userPwd = userPwd;
//    }

    //设置用户id
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public UserData(String userName,String Birth,String Sex,double Weight,double Height,double BMI,
                    double Kcal,double Protein,double Fats,double CHO){//,String userPwd){
        super();//
        this.userName = userName;
        this.Birth = Birth;
        this.Sex = Sex;
        this.Weight = Weight;
        this.Height = Height;
        this.BMI = BMI;
        this.Kcal = Kcal;
        this.Protein = Protein;
        this.Fats = Fats;
        this.CHO = CHO;
        //this.Na =Na;
    }
}
