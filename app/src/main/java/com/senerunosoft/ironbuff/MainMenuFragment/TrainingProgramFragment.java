package com.senerunosoft.ironbuff.MainMenuFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;
import com.google.firebase.storage.FirebaseStorage;
import com.senerunosoft.ironbuff.R;
import com.senerunosoft.ironbuff.adapter.TrainingViewPageAdapter;
import com.senerunosoft.ironbuff.databinding.FragmentTrainingProgramBinding;
import com.senerunosoft.ironbuff.table.TrainingProgramTable;
import com.senerunosoft.ironbuff.table.UserTable;
import com.senerunosoft.ironbuff.table.UserTrainingTable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TrainingProgramFragment extends Fragment {
    private static final String COLLECTION_USER_TABLE = "userTable";
    private static final String COLLECTION_USER_EXERCISE_TABLE = "exercisePrograms";

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


        firestore.collection(COLLECTION_USER_TABLE).document(auth.getCurrentUser().getUid()).collection(COLLECTION_USER_EXERCISE_TABLE).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Map<String, Object> hashMap = new HashMap<>();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        hashMap = doc.getData();
                        trainingTables.add(new UserTrainingTable(hashMap));
                    }
                    getCardView();

                }
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