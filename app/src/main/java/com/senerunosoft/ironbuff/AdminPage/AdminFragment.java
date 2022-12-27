package com.senerunosoft.ironbuff.AdminPage;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.firestore.*;
import com.google.firebase.storage.FirebaseStorage;
import com.senerunosoft.ironbuff.AdminPage.Adapter.UserListAdapter;
import com.senerunosoft.ironbuff.R;
import com.senerunosoft.ironbuff.databinding.FragmentAdminBinding;
import com.senerunosoft.ironbuff.table.FoodTable;
import com.senerunosoft.ironbuff.table.UserTable;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AdminFragment extends Fragment {

    private static final String COLLECTION_USER_TABLE = "userTable";
    FragmentAdminBinding binding;
    FirebaseFirestore firestore;
    List<UserTable> userList;

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
                    List<UserTable> userList = task.getResult().toObjects(UserTable.class);
                    UserListAdapter adapter = new UserListAdapter(getContext(), userList);
                    binding.userList.setAdapter(adapter);

                    binding.userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Toast.makeText(getContext(), "position" + userList.size(), Toast.LENGTH_SHORT).show();

                            Bundle bundle = new Bundle();
                            bundle.putString("DocId", userList.get(i).getDocId());
                            NavDirections directions = AdminFragmentDirections.adminToAddUserTrainingProgram();
                            Navigation.findNavController(view).navigate(directions.getActionId(), bundle);
                        }
                    });

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