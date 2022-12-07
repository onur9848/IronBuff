package com.senerunosoft.ironbuff.table;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;

public class FoodTable {

    private String foodName;
    private float foodCarbonhydrat;
    private float foodFat;
    private float foodProtein;
    private float foodCalorie;
    private float foodGram;

    public FoodTable() {
    }

    public FoodTable(String foodName, float foodCarbonhydrat, float foodFat, float foodProtein, float foodCalori, int foodGram) {
        this.foodName = foodName;
        this.foodCarbonhydrat = foodCarbonhydrat;
        this.foodFat = foodFat;
        this.foodProtein = foodProtein;
        this.foodCalorie = foodCalori;
        this.foodGram = foodGram;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public float getFoodCarbonhydrat() {
        return foodCarbonhydrat;
    }

    public void setFoodCarbonhydrat(float foodCarbonhydrat) {
        this.foodCarbonhydrat = foodCarbonhydrat;
    }

    public float getFoodFat() {
        return foodFat;
    }

    public void setFoodFat(float foodFat) {
        this.foodFat = foodFat;
    }

    public float getFoodProtein() {
        return foodProtein;
    }

    public void setFoodProtein(float foodProtein) {
        this.foodProtein = foodProtein;
    }

    public float getFoodCalorie() {
        return foodCalorie;
    }

    public void setFoodCalorie(float foodCalorie) {
        this.foodCalorie = foodCalorie;
    }

    public float getFoodGram() {
        return foodGram;
    }

    public void setFoodGram(float foodGram) {
        this.foodGram = foodGram;
    }
}
