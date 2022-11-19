package com.senerunosoft.ironbuff.table;

import java.util.Map;

public class UserTrainingTable {
    String ExerciseName;
    String ExrtciseCount;

    public UserTrainingTable(){

    }
    public UserTrainingTable(Map param){
        this.ExerciseName = param.get("exerciseZone").toString();
        this.ExrtciseCount = param.get("exerciseCount").toString();
    }


    public String getExerciseName() {
        return ExerciseName;
    }

    public void setExerciseName(String exerciseName) {
        ExerciseName = exerciseName;
    }

    public String getExrtciseCount() {
        return ExrtciseCount;
    }

    public void setExrtciseCount(String exrtciseCount) {
        ExrtciseCount = exrtciseCount;
    }
}
