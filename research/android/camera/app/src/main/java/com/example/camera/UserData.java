package com.example.camera;


/**
 * Created by kangkang59812 on 2017-11-02.
 */

public class UserData {
    private String userName;//用户名
    private double days;
    //private String userPwd;//用户密码
    private int userId;//用户id
    private String Birth;
    private String Sex;
    public int pwdresetFlag=0;
    private double Weight;
    private double Height;
    private String  Identity;
    private String Status;
    private double BMI;
    private double Kcal;
    private double Protein;
    private double Fats;
    private double CHO;
    private double D1Kcal;
    private double D1CHO;
    private double D1Protein;
    private double D1Fats;
    private double D2Kcal;
    private double D2CHO;
    private double D2Protein;
    private double D2Fats;
    private double D3Kcal;
    private double D3CHO;
    private double D3Protein;
    private double D3Fats;
    private double D4Kcal;
    private double D4CHO;
    private double D4Protein;
    private double D4Fats;
    private double D5Kcal;
    private double D5CHO;
    private double D5Protein;
    private double D5Fats;
    private double D6Kcal;
    private double D6CHO;
    private double D6Protein;
    private double D6Fats;
    private double D7Kcal;
    private double D7CHO;
    private double D7Protein;
    private double D7Fats;


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
    public double getDays() {return days;}
    public String getSex() {return Sex;}
    public double getWeight() {return Weight;}
    public double getHeight() {return Height;}
    public double getBMI() {return BMI;}
    public String getIdentity(){return Identity;}
    public String getStatus(){return Status;}
    public double getKcal() { return  Kcal; }
    public double getProtein() { return Protein;}
    public double getFats() { return Fats;}
    public double getCHO()  { return CHO;}
    public double getD1Kcal() {return D1Kcal;}
    public double getD1CHO() {return D1CHO;}
    public double getD1Protein() {return D1Protein;}
    public double getD1Fats() {return D1Fats;}
    public double getD2Kcal() {return D2Kcal;}
    public double getD2CHO() {return D2CHO;}
    public double getD2Protein() {return D2Protein;}
    public double getD2Fats() {return D2Fats;}
    public double getD3Kcal() {return D3Kcal;}
    public double getD3CHO() {return D3CHO;}
    public double getD3Protein() {return D3Protein;}
    public double getD3Fats() {return D3Fats;}
    public double getD4Kcal() {return D4Kcal;}
    public double getD4CHO() {return D4CHO;}
    public double getD4Protein() {return D4Protein;}
    public double getD4Fats() {return D4Fats;}
    public double getD5Kcal() {return D5Kcal;}
    public double getD5CHO() {return D5CHO;}
    public double getD5Protein() {return D5Protein;}
    public double getD5Fats() {return D5Fats;}
    public double getD6Kcal() {return D6Kcal;}
    public double getD6CHO() {return D6CHO;}
    public double getD6Protein() {return D6Protein;}
    public double getD6Fats() {return D6Fats;}
    public double getD7Kcal() {return D7Kcal;}
    public double getD7CHO() {return D7CHO;}
    public double getD7Protein() {return D7Protein;}
    public double getD7Fats() {return D7Fats;}

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

    public UserData(String userName,double days,String Birth,String Sex,double Weight,double Height,double BMI,String Identity,String Status,
                    double Kcal,double Protein,double Fats,double CHO,
                    double D1Kcal,double D1CHO, double D1Protein,double D1Fats,
                    double D2Kcal,double D2CHO,double D2Protein,double D2Fats,
                    double D3Kcal,double D3CHO,double D3Protein,double D3Fats,
                    double D4Kcal,double D4CHO,double D4Protein,double D4Fats,
                    double D5Kcal,double D5CHO,double D5Protein,double D5Fats,
                    double D6Kcal,double D6CHO,double D6Protein,double D6Fats,
                    double D7Kcal,double D7CHO,double D7Protein,double D7Fats){
        super();//
        this.userName = userName;
        this.days = days;
        this.Birth = Birth;
        this.Sex = Sex;
        this.Weight = Weight;
        this.Height = Height;
        this.BMI = BMI;
        this.Identity = Identity;
        this.Status = Status;
        this.Kcal = Kcal;
        this.Protein = Protein;
        this.Fats = Fats;
        this.CHO = CHO;
        this.D1Kcal = D1Kcal;
        this.D1CHO = D1CHO;
        this.D1Protein = D1Protein;
        this.D1Fats = D1Fats;
        this.D2Kcal = D2Kcal;
        this.D2CHO = D2CHO;
        this.D2Protein = D2Protein;
        this.D2Fats = D2Fats;
        this.D3Kcal = D3Kcal;
        this.D3CHO = D3CHO;
        this.D3Protein = D3Protein;
        this.D3Fats = D3Fats;
        this.D4Kcal = D4Kcal;
        this.D4CHO = D4CHO;
        this.D4Protein = D4Protein;
        this.D4Fats = D4Fats;
        this.D5Kcal = D5Kcal;
        this.D5CHO = D5CHO;
        this.D5Protein = D5Protein;
        this.D5Fats = D5Fats;
        this.D6Kcal = D6Kcal;
        this.D6CHO = D6CHO;
        this.D6Protein = D6Protein;
        this.D6Fats = D6Fats;
        this.D7Kcal = D7Kcal;
        this.D7CHO = D7CHO;
        this.D7Protein = D7Protein;
        this.D7Fats = D7Fats;
        //this.Na =Na;
    }
}
