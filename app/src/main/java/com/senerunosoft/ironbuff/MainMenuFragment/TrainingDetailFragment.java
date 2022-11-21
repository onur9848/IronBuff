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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.senerunosoft.ironbuff.R;
import com.senerunosoft.ironbuff.databinding.FragmentTrainingDetailBinding;
import org.jetbrains.annotations.NotNull;

public class TrainingDetailFragment extends Fragment {

    FragmentTrainingDetailBinding binding;
    private static final String COLLECTION_USER_TABLE = "userTable";
    private static final String COLLECTION_USER_EXERCISE_TABLE = "exercisePrograms";
    private static final String COLLECTION_USER_PROGRAM_DETAIL ="exerciseDetail";
    private String DOC_ID;
    FirebaseFirestore firestore;
    FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

        getProgramDetail();

    }

    private void getProgramDetail() {
        binding.denemeText.setText(DOC_ID);
        CollectionReference reference = firestore.collection(COLLECTION_USER_TABLE).document(auth.getCurrentUser().getUid()).collection(COLLECTION_USER_EXERCISE_TABLE).document(DOC_ID).collection(COLLECTION_USER_PROGRAM_DETAIL);
        reference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot doc: task.getResult()){
                    Log.d(getTag(), "onComplete: "+doc.getData());
                }
            }
        });


    }


}
