package com.senerunosoft.ironbuff.AdminPage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.*;
import com.senerunosoft.ironbuff.databinding.FragmentAdminAddTrainingBinding;
import com.senerunosoft.ironbuff.table.ExerciseTable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminAddTrainingFragment extends Fragment {
    private static final String COLLECTION_EXERCISE_TABLE = "exerciseTable";
    private static final String DOCUMENT_IN_EXERCISE_TABLE = "exercise";

    FragmentAdminAddTrainingBinding binding;
    ArrayList<String> exerciseZone, exerciseFreeWeight, exerciseMachine;
    Map<String, Object> map;
    FirebaseFirestore firestore;
    ExerciseTable exercise;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAdminAddTrainingBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firestore = FirebaseFirestore.getInstance();
        exerciseZone = new ArrayList<>();
        exerciseMachine = new ArrayList<>();
        exerciseFreeWeight = new ArrayList<>();
        exercise = new ExerciseTable();
        changeFirebaseTable();
        selectCheckButton();
        saveExercise();
    }

    private void saveExercise() {
        binding.addTrainingSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exercise.setExerciseName(binding.addExerciseName.getText().toString());
                exercise.setExerciseTarget(binding.exerciseZoneSpinner.getSelectedItem().toString());
                exercise.setExerciseDetail(binding.addExerciseDetail.getText().toString());
                getWeightType();
                addNewExercise();

            }
        });


    }

    private void addNewExercise() {
        Map exerciseMap =new HashMap();
        exerciseMap.put("exerciseName",exercise.getExerciseName());
        exerciseMap.put("exerciseTarget",exercise.getExerciseTarget());
        exerciseMap.put("exerciseDetail",exercise.getExerciseDetail());
        exerciseMap.put("exerciseWeightType",exercise.getExerciseWeightType());
        exerciseMap.put("exerciseWeight&MachineName",exercise.getExerciseWeightName());

        DocumentReference docref = firestore.collection(COLLECTION_EXERCISE_TABLE).document(DOCUMENT_IN_EXERCISE_TABLE);

        firestore.collection(COLLECTION_EXERCISE_TABLE).document(DOCUMENT_IN_EXERCISE_TABLE).collection(exercise.getExerciseTarget()).add(exerciseMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentReference> task) {

            }
        });

    }

    private void getWeightType() {
        RadioButton button = binding.freeOrMachineRadiogroup.findViewById(binding.freeOrMachineRadiogroup.getCheckedRadioButtonId());
        exercise.setExerciseWeightType(button.getText().toString());
        if (binding.selectedFreeWeight.getVisibility() == View.VISIBLE) {
            exercise.setExerciseWeightName(binding.freeWeightSpinner.getSelectedItem().toString());
        } else {
            exercise.setExerciseWeightName(binding.machineSpinner.getSelectedItem().toString());
        }
    }

    private void changeFirebaseTable() {
        firestore.collection(COLLECTION_EXERCISE_TABLE).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                getSpinners();
            }
        });
    }

    private void getSpinners() {
        firestore.collection(COLLECTION_EXERCISE_TABLE).document(DOCUMENT_IN_EXERCISE_TABLE).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    map = task.getResult().getData();
                    if (map != null) {
                        exerciseZone = (ArrayList<String>) map.get("exerciseZone");
                        exerciseMachine = (ArrayList<String>) map.get("exerciseMachine");
                        exerciseFreeWeight = (ArrayList<String>) map.get("exerciseFreeWeight");

                        ArrayAdapter<String> zoneAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, exerciseZone);
                        binding.exerciseZoneSpinner.setAdapter(zoneAdapter);

                        ArrayAdapter<String> machineAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, exerciseMachine);
                        binding.machineSpinner.setAdapter(machineAdapter);

                        ArrayAdapter<String> freeWeightAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, exerciseFreeWeight);
                        binding.freeWeightSpinner.setAdapter(freeWeightAdapter);
                        addItemWeightType();
                    }

                }
            }
        });
    }

    private void addItemWeightType() {
        binding.addFreeWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = new EditText(getContext());
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Yeni Ağırlık gir");
                builder.setView(editText);
                builder.setNegativeButton("İptal", null);
                builder.setPositiveButton("Kaydet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String freeWeight = editText.getText().toString();
                        Toast.makeText(getContext(), freeWeight, Toast.LENGTH_SHORT).show();
                        exerciseFreeWeight.add(freeWeight);
                        updateFreeWeight(exerciseFreeWeight);
                    }
                });
                builder.show();
            }
        });
        binding.addMachineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = new EditText(getContext());
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Yeni Ağırlık gir");
                builder.setView(editText);
                builder.setNegativeButton("İptal", null);
                builder.setPositiveButton("Kaydet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newMachine = editText.getText().toString();
                        Toast.makeText(getContext(), newMachine, Toast.LENGTH_SHORT).show();
                        exerciseMachine.add(newMachine);
                        updateMachine(exerciseMachine);
                    }
                });
                builder.show();


            }
        });


    }

    private void updateFreeWeight(ArrayList<String> freeWeight) {

        firestore.collection(COLLECTION_EXERCISE_TABLE).document(DOCUMENT_IN_EXERCISE_TABLE).update("exerciseFreeWeight", freeWeight).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
            }
        });
    }

    private void updateMachine(ArrayList<String> machineList) {
        firestore.collection(COLLECTION_EXERCISE_TABLE).document(DOCUMENT_IN_EXERCISE_TABLE).update("exerciseMachine", machineList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
            }
        });
    }

    private void selectCheckButton() {
        binding.freeOrMachineRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (binding.freeWeight.getId() == i) {
                    Log.d(getTag(), "onCheckedChanged: Free Weight");
                    binding.selectedMachine.setVisibility(View.GONE);
                    binding.selectedFreeWeight.setVisibility(View.VISIBLE);

                } else {
                    Log.d(getTag(), "onCheckedChanged: Machine");
                    binding.selectedFreeWeight.setVisibility(View.GONE);
                    binding.selectedMachine.setVisibility(View.VISIBLE);
                }
            }
        });
    } //Show Edittext by Weight Type

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}