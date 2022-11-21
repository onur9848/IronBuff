package com.senerunosoft.ironbuff.table;

public class TrainingProgramTable {

    private String ExerciseTarget;
    private String ExerciseName;
    private String ExerciseImg1;
    private String ExerciseImg2;
    private String ExerciseSetsReps;

    public TrainingProgramTable(){

    }
    public TrainingProgramTable(String exerciseTarget, String exerciseName, String exerciseImg1, String exerciseImg2, String exerciseSetsReps) {
        ExerciseTarget = exerciseTarget;
        ExerciseName = exerciseName;
        ExerciseImg1 = exerciseImg1;
        ExerciseImg2 = exerciseImg2;
        ExerciseSetsReps = exerciseSetsReps;
    }

    public String getExerciseTarget(){
        return ExerciseTarget;
    }
    public void setExerciseTarget(String exerciseTarget){
        ExerciseTarget = exerciseTarget;
    }


    public String getExerciseName() {
        return ExerciseName;
    }

    public void setExerciseName(String exerciseName) {
        ExerciseName = exerciseName;
    }

    public String getExerciseImg1() {
        return ExerciseImg1;
    }

    public void setExerciseImg1(String exerciseImg1) {
        ExerciseImg1 = exerciseImg1;
    }

    public String getExerciseImg2() {
        return ExerciseImg2;
    }

    public void setExerciseImg2(String exerciseImg2) {
        ExerciseImg2 = exerciseImg2;
    }

    public String getExerciseSetsReps() {
        return ExerciseSetsReps;
    }

    public void setExerciseSetsReps(String exerciseSetsReps) {
        ExerciseSetsReps = exerciseSetsReps;
    }
}
