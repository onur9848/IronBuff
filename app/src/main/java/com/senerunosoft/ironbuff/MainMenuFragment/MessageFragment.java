package com.senerunosoft.ironbuff.MainMenuFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.View;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firestore.v1.Document;
import com.senerunosoft.ironbuff.databinding.FragmentMessageBinding;
import com.senerunosoft.ironbuff.databinding.FragmentTrainingProgramBinding;
import com.senerunosoft.ironbuff.table.UserTable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MessageFragment extends Fragment {

    private static final String COLLECTION_USER_TABLE = "userTable";

    FragmentMessageBinding binding;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    ArrayList<String> userDocId, userName;
    ArrayList<String> adminList;


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
        auth = FirebaseAuth.getInstance();
        adminList = new ArrayList<>();

        isAdminUser();
        gotoMessageScreen();

    }

    private void isAdminUser() {

        firestore.collection("adminTable").document("admins").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    adminList = (ArrayList<String>) task.getResult().get("userUid");
                    if (adminList.contains(auth.getCurrentUser().getUid())) {
                        getAdminList();
                    } else
                        getUserList();
                }
            }
        });

    }


    private void getAdminList() {

        firestore.collection(COLLECTION_USER_TABLE).whereNotEqualTo(FieldPath.documentId(), auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                userDocId = new ArrayList<>();
                userName = new ArrayList<>();
                for (QueryDocumentSnapshot doc : task.getResult()) {

                    if (!adminList.contains(doc.getId())) {
                        userDocId.add(doc.getId());
                        userName.add(doc.get("UserName").toString());
                    }
                }
                Log.d(getTag(), "onComplete: " + userDocId.toString());
                Log.d(getTag(), "onComplete: " + userName.toString());
                ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, userName);
                binding.messageUserList.setAdapter(adapter);

            }
        });

    }

    private void getUserList() {
        firestore.collection(COLLECTION_USER_TABLE).whereNotEqualTo(FieldPath.documentId(), auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                userDocId = new ArrayList<>();
                userName = new ArrayList<>();
                for (QueryDocumentSnapshot doc : task.getResult()) {

                    if (adminList.contains(doc.getId())) {
                        userDocId.add(doc.getId());
                        userName.add(doc.get("UserName").toString());
                    }
                }
                Log.d(getTag(), "onComplete: " + userDocId.toString());
                Log.d(getTag(), "onComplete: " + userName.toString());
                ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, userName);
                binding.messageUserList.setAdapter(adapter);
            }
        });


    }

    private void gotoMessageScreen() {

        binding.messageUserList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle =new Bundle();
                bundle.putString("userDocID",userDocId.get(i));
                NavDirections directions = MessageFragmentDirections.gotoSendMessageUsers();
                Navigation.findNavController(getView()).navigate(directions.getActionId(),bundle);

            }
        });


    }

    @Override

    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}