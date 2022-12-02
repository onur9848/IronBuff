package com.senerunosoft.ironbuff.table;

import java.util.ArrayList;
import java.util.Map;

public class UserTrainingTable {

    private String docID;
    private String date;
    private ArrayList<String> exerciseZone;
    private int exerciseCount;
    private int exerciseZoneCount;
    private ArrayList<String> exerciseName;
    private ArrayList<String> exerciseRepsAndSets;
    private ArrayList<String> exerciseDetail;


    public UserTrainingTable() {

    }

    public UserTrainingTable(Map param) {
        this.date = param.get("date").toString();
        this.exerciseZone = (ArrayList<String>) param.get("exerciseZone");
        this.exerciseCount = Integer.parseInt(param.get("exerciseCount").toString());
        this.exerciseZoneCount = Integer.parseInt(param.get("exerciseZoneCount").toString());
        this.exerciseName = (ArrayList<String>) param.get("exerciseName");
        this.exerciseRepsAndSets = (ArrayList<String>) param.get("exerciseRepsAndSets");
        this.exerciseDetail = (ArrayList<String>) param.get("exerciseDetail");
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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
}
