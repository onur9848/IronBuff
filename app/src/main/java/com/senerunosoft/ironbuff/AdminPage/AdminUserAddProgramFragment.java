package com.senerunosoft.ironbuff.AdminPage;

import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.senerunosoft.ironbuff.R;
import com.senerunosoft.ironbuff.databinding.FragmentAdminUserAddProgramBinding;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class AdminUserAddProgramFragment extends Fragment {
    private static final String COLLECTION_USER_TABLE = "userTable";
    private static final String COLLECTION_USER_EXERCISE_TABLE = "exercisePrograms";
    private static final String DOC_GET_FIELD_EXERCISEZONE ="exerciseZone";
    FragmentAdminUserAddProgramBinding binding;
    FirebaseFirestore firestore;
    private String DOC_ID;
    ArrayList<String> userTrainingID;
    ArrayList<String> docIdList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAdminUserAddProgramBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        assert getArguments() != null;
        DOC_ID = getArguments().getString("DocId");
        firestore = FirebaseFirestore.getInstance();
        userTrainingID = new ArrayList<>();
        docIdList =new ArrayList<>();
        getAndSetUserProgram();
        addNewUserProgram();


    }

    private void getAndSetUserProgram() {
        firestore.collection(COLLECTION_USER_TABLE).document(DOC_ID).collection(COLLECTION_USER_EXERCISE_TABLE).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        String st = doc.getId();
                        st += doc.get(DOC_GET_FIELD_EXERCISEZONE);
                        userTrainingID.add(st);
                        docIdList.add(doc.getId());
                    }
                    ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1,android.R.id.text1,userTrainingID);
                    binding.adminUserTrainingList.setAdapter(adapter);
                    binding.adminUserTrainingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Bundle bundle = new Bundle();
                            bundle.putString("docIDTraining",docIdList.get(i));
                            bundle.putString("docIDUser",DOC_ID);
                            NavDirections directions = AdminUserAddProgramFragmentDirections.gotoAdminUpdateUserTrainingProgram();
                            Navigation.findNavController(view).navigate(directions.getActionId(),bundle);
                        }
                    });
                }
            }
        });
    }

    private void addNewUserProgram(){
        binding.addNewProgramButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("docIDUser",DOC_ID);
                NavDirections directions = AdminUserAddProgramFragmentDirections.gotoAdminAddUserTrainingProgram();
                Navigation.findNavController(view).navigate(directions.getActionId(),bundle);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}