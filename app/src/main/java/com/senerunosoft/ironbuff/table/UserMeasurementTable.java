package com.senerunosoft.ironbuff.table;

import java.util.Date;
import java.util.Map;

public class UserMeasurementTable implements Comparable<UserMeasurementTable> {

    private String chest, leftArm, rightArm, waist, hips, leftThigh, rightThigh, leftCalf, rightCalf, weight;

    Date date;
    public UserMeasurementTable() {
    }

    public UserMeasurementTable(Date date) {
        this.date = date;
    }

    public UserMeasurementTable(Map param){
        this.chest = param.get("chest").toString();
        this.leftArm = param.get("leftArm").toString();
        this.rightArm = param.get("rightArm").toString();
        this.waist = param.get("waist").toString();
        this.hips = param.get("hips").toString();
        this.leftThigh = param.get("leftThigh").toString();
        this.rightThigh = param.get("rightThigh").toString();
        this.leftCalf = param.get("leftCalf").toString();
        this.rightCalf = param.get("rightCalf").toString();
        this.weight = param.get("weight").toString();

    }





    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getChest() {
        return chest;
    }

    public void setChest(String chest) {
        this.chest = chest;
    }

    public String getLeftArm() {
        return leftArm;
    }

    public void setLeftArm(String leftArm) {
        this.leftArm = leftArm;
    }

    public String getRightArm() {
        return rightArm;
    }

    public void setRightArm(String rightArm) {
        this.rightArm = rightArm;
    }

    public String getWaist() {
        return waist;
    }

    public void setWaist(String waist) {
        this.waist = waist;
    }

    public String getHips() {
        return hips;
    }

    public void setHips(String hips) {
        this.hips = hips;
    }

    public String getLeftThigh() {
        return leftThigh;
    }

    public void setLeftThigh(String leftThigh) {
        this.leftThigh = leftThigh;
    }

    public String getRightThigh() {
        return rightThigh;
    }

    public void setRightThigh(String rightThigh) {
        this.rightThigh = rightThigh;
    }

    public String getLeftCalf() {
        return leftCalf;
    }

    public void setLeftCalf(String leftCalf) {
        this.leftCalf = leftCalf;
    }

    public String getRightCalf() {
        return rightCalf;
    }
    public void setRightCalf(String rightCalf) {
        this.rightCalf = rightCalf;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    @Override
    public int compareTo(UserMeasurementTable table) {
        if (getDate() == null || table.getDate()==null){
            return 0;
        }
        return getDate().compareTo(table.getDate());
    }

}
