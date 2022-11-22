package com.senerunosoft.ironbuff.AdminPage;

import android.net.Uri;
import android.os.Bundle;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.senerunosoft.ironbuff.AdminPage.Adapter.UserListAdapter;
import com.senerunosoft.ironbuff.R;
import com.senerunosoft.ironbuff.databinding.FragmentAdminBinding;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AdminFragment extends Fragment {

    private static final String COLLECTION_USER_TABLE = "userTable";
    FragmentAdminBinding binding;
    FirebaseFirestore firestore;
    List<String> username;
    List<Uri> userImg;
    List<String> userDocId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAdminBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firestore = FirebaseFirestore.getInstance();
        username = new ArrayList<>();
        userDocId = new ArrayList<>();
        userImg = new ArrayList<>();
        getUserList();
        clickAction();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void getUserList() {
        firestore.collection(COLLECTION_USER_TABLE).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {

                        username.add(doc.get("UserName").toString());
                        userImg.add(Uri.parse((String) doc.get("imageUrl").toString()));
                        userDocId.add(doc.getId());
                        doc.getData();
                    }
                    UserListAdapter adapter = new UserListAdapter(getContext(), username, userDocId, userImg);
                    binding.userList.setAdapter(adapter);


                }
            }
        });
    }

    private void clickAction() {
        binding.addExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections directions = AdminFragmentDirections.gotoAdminAddExercise();
                Navigation.findNavController(view).navigate(directions.getActionId());
            }
        });
    }

}