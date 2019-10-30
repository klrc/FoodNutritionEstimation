package com.example.camera;

public class FoodData {
    private String FoodName;
    private double FoodKcal;
    private double FoodProtein;
    private double FoodFats;
    private double FoodCHO;
    public String getFoodName(){return FoodName;}
    public double getFoodKcal(){return FoodKcal;}
    public double getFoodFats() {return FoodFats;}
    public double getFoodProtein(){return FoodProtein;}
    public double getFoodCHO(){return FoodCHO;}
    public FoodData(String FoodName,double FoodKcal,double FoodProtein,double FoodFats,double FoodCHO){
        super();
        this.FoodName = FoodName;
        this.FoodKcal = FoodKcal;
        this.FoodCHO = FoodCHO;
        this.FoodFats = FoodFats;
        this.FoodProtein = FoodProtein;
    }
    }

