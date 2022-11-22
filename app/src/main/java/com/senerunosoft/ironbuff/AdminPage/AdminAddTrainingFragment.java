package com.senerunosoft.ironbuff.AdminPage;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Toast;
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
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;

public class AdminAddTrainingFragment extends Fragment {
    private static final String COLLECTION_EXERCISE_TABLE = "exerciseTable";
    private static final String[] DOCUMENT_IN_EXERCISE_TABLE = {"exerciseFreeWeight", "exerciseMachine", "exerciseZone"};

    FragmentAdminAddTrainingBinding binding;
    ArrayList<String> exerciseZone,exerciseFreeWeight,exerciseMachine;
    Map<String, Object> map;
    FirebaseFirestore firestore;

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
        spinnderlist();
        getMainTargetSpinner();
        selectCheckButton();
    }

    private void changeFirebaseTable() {
        firestore.collection(COLLECTION_EXERCISE_TABLE).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
            }
        });
    }

    private void getMainTargetSpinner() {
        firestore.collection(COLLECTION_EXERCISE_TABLE).document(DOCUMENT_IN_EXERCISE_TABLE[2]).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    map = task.getResult().getData();
                    exerciseZone = (ArrayList<String>) map.get("exerciseZone");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, exerciseZone);
                    binding.spinner.setAdapter(adapter);

                }
            }
        });


    }
    private void spinnderlist(){
        CollectionReference reference = firestore.collection(COLLECTION_EXERCISE_TABLE);

        reference.document(DOCUMENT_IN_EXERCISE_TABLE[0]).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    map =task.getResult().getData();
                    exerciseFreeWeight = (ArrayList<String>) map.get("freeWeightList");
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item,exerciseFreeWeight);
                    binding.weightSpinner.setAdapter(adapter);
                    Toast.makeText(getContext(), "deneme", Toast.LENGTH_SHORT).show();


                }

            }
        });

        reference.document(DOCUMENT_IN_EXERCISE_TABLE[1]).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                map =task.getResult().getData();
                exerciseMachine = (ArrayList<String>) map.get("machineList");
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item,exerciseMachine);
                binding.machineSpinner.setAdapter(adapter);
                Toast.makeText(getContext(), "deneme", Toast.LENGTH_SHORT).show();

            }
        });



    }

    private void addItemWeightType(){
        final Dialog dialog = new Dialog(getContext());

        binding.addFreeWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(getActivity());


            }
        });

        binding.addMachineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}