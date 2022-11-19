package com.senerunosoft.ironbuff.MainMenuFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.View;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firestore.v1.Document;
import com.senerunosoft.ironbuff.databinding.FragmentMessageBinding;
import com.senerunosoft.ironbuff.databinding.FragmentTrainingProgramBinding;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MessageFragment extends Fragment {

    FragmentMessageBinding binding;
    FirebaseFirestore firestore;


    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = FragmentMessageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firestore = FirebaseFirestore.getInstance();
        firestore.collection("exerciseTable").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    List<String> strings = new ArrayList<>();
                    for (QueryDocumentSnapshot doc: task.getResult()){
                        Log.d(getTag(), "logMessage first: "+doc.toString());
                        strings.add(doc.getId());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,android.R.id.text1,strings);
                    binding.exerciseZone.setAdapter(adapter);
                }
            }
        });

        firestore.collection("exerciseTable").document("Back").collection("backExercise").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    List<String> strings = new ArrayList<>();
                    for (QueryDocumentSnapshot doc: task.getResult()){
                        Log.d(getTag(), "logMessage two: "+doc.toString());
                        strings.add(doc.getId()+": "+doc.get("exerciseName").toString());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,android.R.id.text1,strings);
                    binding.backExercise.setAdapter(adapter);
                }
            }
        });
        firestore.collection("exerciseTable").document("Chest").collection("chestExercise").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    List<String> strings = new ArrayList<>();
                    for (QueryDocumentSnapshot doc: task.getResult()){
                        Log.d(getTag(), "logMessage two: "+doc.toString());
                        strings.add(doc.getId()+": "+doc.get("exerciseName").toString());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,android.R.id.text1,strings);
                    binding.chestExercise.setAdapter(adapter);
                }
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}