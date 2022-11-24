package com.senerunosoft.ironbuff.table;

import java.util.ArrayList;
import java.util.Map;

public class UserTrainingTable {

    private String date;
    private ArrayList<String> exerciseZone;
    private int exerciseCount;
    private int exerciseZoneCount;
    private ArrayList<String> exerciseName;
    private ArrayList<String> exerciseRepsAndSets;


    public UserTrainingTable(){

    }
//    public UserTrainingTable(Map param){
//        this.ExerciseName = param.get("exerciseZone").toString().replaceAll("[\\p{Ps}\\p{Pe}]","").split(", ");;
//        this.ExerciseCount = param.get("exerciseCount").toString().replaceAll("[\\p{Ps}\\p{Pe}]","").split(", ");
//        this.ExerciseDay = param.get("exerciseDate").toString();
//        this.DocId = param.get("docId").toString();
//    }


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
}
