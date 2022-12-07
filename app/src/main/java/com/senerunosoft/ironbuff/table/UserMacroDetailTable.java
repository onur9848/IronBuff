package com.senerunosoft.ironbuff.table;

import java.util.Map;

public class UserMacroDetailTable {

    private float dailyProtein;
    private float dailyCarbonhydrat;
    private float dailyFat;
    private float dailyMaxCalorie;
    private float dailyCalorie;
    private float dailyGram;
    private float dailyBreakfastCalorie;
    private float dailyLunchCalorie;
    private float dailyDinnerCalorie;
    private float dailyExtraCalorie;

    public UserMacroDetailTable() {
    }
    public UserMacroDetailTable(Map params) {
        this.dailyProtein = Float.parseFloat(params.get("dailyProtein").toString());
        this.dailyCarbonhydrat = Float.parseFloat(params.get("dailyCarbonhydrat").toString());
        this.dailyFat = Float.parseFloat(params.get("dailyFat").toString());
        this.dailyMaxCalorie = Float.parseFloat(params.get("dailyMaxCalorie").toString());
        this.dailyCalorie = Float.parseFloat(params.get("dailyCalorie").toString());
        this.dailyGram = Float.parseFloat(params.get("dailyGram").toString());
        this.dailyBreakfastCalorie =Float.parseFloat(params.get("dailyBreakfastCalorie").toString()) ;
        this.dailyLunchCalorie=Float.parseFloat(params.get("dailyLunchCalorie").toString());
        this.dailyDinnerCalorie=Float.parseFloat(params.get("dailyDinnerCalorie").toString());
        this.dailyExtraCalorie=Float.parseFloat(params.get("dailyExtraCalorie").toString());


    }
    public float getDailyProtein() {
        return dailyProtein;
    }

    public void setDailyProtein(float dailyProtein) {
        this.dailyProtein = dailyProtein;
    }


    public float getDailyCarbonhydrat() {
        return dailyCarbonhydrat;
    }

    public void setDailyCarbonhydrat(float dailyCarbonhydrat) {
        this.dailyCarbonhydrat = dailyCarbonhydrat;
    }


    public float getDailyFat() {
        return dailyFat;
    }

    public void setDailyFat(float dailyFat) {
        this.dailyFat = dailyFat;
    }

    public float getDailyMaxCalorie() {
        return dailyMaxCalorie;
    }

    public void setDailyMaxCalorie(float dailyMaxCalorie) {
        this.dailyMaxCalorie = dailyMaxCalorie;
    }

    public float getDailyCalorie() {
        return dailyCalorie;
    }

    public void setDailyCalorie(float dailyCalorie) {
        this.dailyCalorie = dailyCalorie;
    }

    public float getDailyGram() {
        return dailyGram;
    }

    public void setDailyGram(float dailyGram) {
        this.dailyGram = dailyGram;
    }


    public float getDailyBreakfastCalorie() {
        return dailyBreakfastCalorie;
    }

    public void setDailyBreakfastCalorie(float dailyBreakfastCalorie) {
        this.dailyBreakfastCalorie = dailyBreakfastCalorie;
    }

    public float getDailyLunchCalorie() {
        return dailyLunchCalorie;
    }

    public void setDailyLunchCalorie(float dailyLunchCalorie) {
        this.dailyLunchCalorie = dailyLunchCalorie;
    }

    public float getDailyDinnerCalorie() {
        return dailyDinnerCalorie;
    }

    public void setDailyDinnerCalorie(float dailyDinnerCalorie) {
        this.dailyDinnerCalorie = dailyDinnerCalorie;
    }

    public float getDailyExtraCalorie() {
        return dailyExtraCalorie;
    }

    public void setDailyExtraCalorie(float dailyExtraCalorie) {
        this.dailyExtraCalorie = dailyExtraCalorie;
    }
}
