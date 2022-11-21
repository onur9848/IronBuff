package com.senerunosoft.ironbuff.table;

import java.util.Map;

public class UserTrainingTable {
    String[] ExerciseName;
    String[] ExerciseCount;
    String ExerciseDay;
    String DocId;

    public UserTrainingTable(){

    }
    public UserTrainingTable(Map param){
        this.ExerciseName = param.get("exerciseZone").toString().replaceAll("[\\p{Ps}\\p{Pe}]","").split(", ");;
        this.ExerciseCount = param.get("exerciseCount").toString().replaceAll("[\\p{Ps}\\p{Pe}]","").split(", ");
        this.ExerciseDay = param.get("exerciseDate").toString();
        this.DocId = param.get("docId").toString();
    }


    public String[] getExerciseName() {
        return ExerciseName;
    }

    public void setExerciseName(String[] exerciseName) {
        ExerciseName = exerciseName;
    }

    public String[] getExerciseCount() {
        return ExerciseCount;
    }

    public void setExerciseCount(String[] exrtciseCount) {
        ExerciseCount = exrtciseCount;
    }

    public String getExerciseDay() {
        return ExerciseDay;
    }

    public void setExerciseDay(String exerciseDay) {
        ExerciseDay = exerciseDay;
    }

    public String getDocId() {
        return DocId;
    }

    public void setDocId(String docId) {
        DocId = docId;
    }
}
