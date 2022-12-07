package com.senerunosoft.ironbuff.table;

import java.util.List;

public class MealFoodList {

    List<FoodTable> BreakfastList,LunchList,DinnerList,ExtraList;

    public List<FoodTable> getBreakfastList() {
        return BreakfastList;
    }

    public void setBreakfastList(List<FoodTable> breakfastList) {
        BreakfastList = breakfastList;
    }

    public List<FoodTable> getLunchList() {
        return LunchList;
    }

    public void setLunchList(List<FoodTable> lunchList) {
        LunchList = lunchList;
    }

    public List<FoodTable> getDinnerList() {
        return DinnerList;
    }

    public void setDinnerList(List<FoodTable> dinnerList) {
        DinnerList = dinnerList;
    }

    public List<FoodTable> getExtraList() {
        return ExtraList;
    }

    public void setExtraList(List<FoodTable> extraList) {
        ExtraList = extraList;
    }
}
