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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;
import com.senerunosoft.ironbuff.R;
import com.senerunosoft.ironbuff.databinding.FragmentMyProfileBinding;
import org.jetbrains.annotations.NotNull;


public class MyProfileFragment extends Fragment {

    FragmentMyProfileBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore firestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMyProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // Inflate the layout for this fragment
        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        binding.myProfilImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDataUser();
            }
        });

    }

    private void getDataUser() {
    firestore.collection("userTable").whereEqualTo("E-mail",auth.getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
            if (task.isSuccessful()){
                for (QueryDocumentSnapshot doc:task.getResult()){
                    Log.d(getTag(),doc.getId()+"->"+doc.getData()+"yapacağınız kodunu mk");
                }
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