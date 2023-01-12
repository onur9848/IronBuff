package com.senerunosoft.ironbuff.table;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class UserTrainingTable {

    private String docID;
    private Date date;
    private ArrayList<String> exerciseZone;
    private int exerciseCount;
    private int exerciseZoneCount;
    private ArrayList<String> exerciseName;
    private ArrayList<String> exerciseRepsAndSets;
    private ArrayList<String> exerciseDetail;
    private ArrayList<String> exerciseImg1;
    private ArrayList<String> exerciseImg2;



    public UserTrainingTable() {
        exerciseZone = new ArrayList<>();
        exerciseName = new ArrayList<>();
        exerciseRepsAndSets = new ArrayList<>();
        exerciseDetail = new ArrayList<>();
        exerciseImg1 = new ArrayList<>();
        exerciseImg2 = new ArrayList<>();

    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<String> getExerciseZone() {
        return exerciseZone;
    }

    public void setExerciseZone(ArrayList<String> exerciseZone) {
        this.exerciseZone = exerciseZone;
    }

    public int getExerciseCount() {
        return exerciseCount;
    }

    public void setExerciseCount(int exerciseCount) {
        this.exerciseCount = exerciseCount;
    }

    public int getExerciseZoneCount() {
        return exerciseZoneCount;
    }

    public void setExerciseZoneCount(int exerciseZoneCount) {
        this.exerciseZoneCount = exerciseZoneCount;
    }

    public ArrayList<String> getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(ArrayList<String> exerciseName) {
        this.exerciseName = exerciseName;
    }

    public ArrayList<String> getExerciseRepsAndSets() {
        return exerciseRepsAndSets;
    }

    public void setExerciseRepsAndSets(ArrayList<String> exerciseRepsAndSets) {
        this.exerciseRepsAndSets = exerciseRepsAndSets;
    }

    public ArrayList<String> getExerciseDetail() {
        return exerciseDetail;
    }

    public void setExerciseDetail(ArrayList<String> exerciseDetail) {
        this.exerciseDetail = exerciseDetail;
    }

    public ArrayList<String> getExerciseImg1() {
        return exerciseImg1;
    }

    public void setExerciseImg1(ArrayList<String> exerciseImg1) {
        this.exerciseImg1 = exerciseImg1;
    }

    public ArrayList<String> getExerciseImg2() {
        return exerciseImg2;
    }

    public void setExerciseImg2(ArrayList<String> exerciseImg2) {
        this.exerciseImg2 = exerciseImg2;
    }
}
