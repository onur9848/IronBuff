package com.senerunosoft.ironbuff.MainMenuFragment;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;
import com.senerunosoft.ironbuff.MainMenuFragment.adapter.TrainingViewPageAdapter;
import com.senerunosoft.ironbuff.databinding.FragmentTrainingProgramBinding;
import com.senerunosoft.ironbuff.table.UserTable;
import com.senerunosoft.ironbuff.table.UserTrainingTable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TrainingProgramFragment extends Fragment {
    private static final String COLLECTION_USER_TABLE = "userTable";
    private static final String COLLECTION_USER_EXERCISE_TABLE = "exerciseTable";

    FragmentTrainingProgramBinding binding;
    UserTable user = new UserTable();
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    List<UserTrainingTable> trainingTables;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTrainingProgramBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        trainingTables = new ArrayList<>();


        firestore.collection(COLLECTION_USER_TABLE).document(auth.getCurrentUser().getUid()).collection(COLLECTION_USER_EXERCISE_TABLE).orderBy("date").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    trainingTables = task.getResult().toObjects(UserTrainingTable.class);
                    getCardView();
                }

            }
        });
    }
    private void trainingSettings(){
    binding.trainingSettings.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TrainingSettingsFragment trainingSettingsFragment = new TrainingSettingsFragment();
            trainingSettingsFragment.show();
        }
    });
    }

    private void getCardView() {
        TrainingViewPageAdapter adapter = new TrainingViewPageAdapter(getContext(), trainingTables);
        binding.trainingRecyclerView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.trainingRecyclerView.setLayoutManager(linearLayoutManager);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}