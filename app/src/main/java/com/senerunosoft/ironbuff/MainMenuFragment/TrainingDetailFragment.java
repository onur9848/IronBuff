package com.senerunosoft.ironbuff.MainMenuFragment;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;
import com.senerunosoft.ironbuff.MainMenuFragment.adapter.TrainingDetailAdapter;
import com.senerunosoft.ironbuff.R;
import com.senerunosoft.ironbuff.databinding.FragmentTrainingDetailBinding;
import com.senerunosoft.ironbuff.table.UserTrainingTable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TrainingDetailFragment extends Fragment {

    FragmentTrainingDetailBinding binding;
    private static final String COLLECTION_USER_TABLE = "userTable";
    private static final String COLLECTION_USER_EXERCISE_TABLE = "exerciseTable";
    private String DOC_ID;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    DocumentReference userProramDocRef;
    UserTrainingTable trainingProgram;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTrainingDetailBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DOC_ID = getArguments().getString("DocId");
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        trainingProgram = new UserTrainingTable();
        userProramDocRef = firestore.collection(COLLECTION_USER_TABLE).document(auth.getCurrentUser().getUid()).collection(COLLECTION_USER_EXERCISE_TABLE).document(DOC_ID);
        getProgramDetail();

    }

    private void getProgramDetail() {
        userProramDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                trainingProgram = task.getResult().toObject(UserTrainingTable.class);
                getExerciseDetail();
            }
        });
    }

    private void getExerciseDetail() {

        TrainingDetailAdapter adapter = new TrainingDetailAdapter(getContext(),trainingProgram,binding.fragmentTrainingDetailViewpager);
        binding.fragmentTrainingDetailViewpager.setAdapter(adapter);



    }


}
