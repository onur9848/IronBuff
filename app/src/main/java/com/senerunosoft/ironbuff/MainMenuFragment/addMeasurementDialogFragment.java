package com.senerunosoft.ironbuff.MainMenuFragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.senerunosoft.ironbuff.R;
import com.senerunosoft.ironbuff.databinding.FragmentAddMeasurementDialogBinding;
import com.senerunosoft.ironbuff.table.UserMeasurementTable;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.*;


public class addMeasurementDialogFragment extends DialogFragment {

    private static final String COLLECTION_USER_TABLE = "userTable";
    private static final String COLLECTION_BODY_MEASUREMENT_TABLE = "bodyMeasurement";
    FragmentAddMeasurementDialogBinding binding;
    UserMeasurementTable table;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    CollectionReference colref;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = FragmentAddMeasurementDialogBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        table = new UserMeasurementTable();
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        colref = firestore.collection(COLLECTION_USER_TABLE).document(auth.getCurrentUser().getUid()).collection(COLLECTION_BODY_MEASUREMENT_TABLE);

        binding.addMeasurementOkeyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    getNewBodyMeasurement();
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        binding.addMeasurementCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDestroyView();
            }
        });

    }

    private void getNewBodyMeasurement() throws IllegalAccessException {
        table.setChest(binding.addMeasurementChestText.getText().toString());
        table.setHips(binding.addMeasurementHipsText.getText().toString());
        table.setLeftArm(binding.addMeasurementLArmText.getText().toString());
        table.setRightArm(binding.addMeasurementRArmText.getText().toString());
        table.setWaist(binding.addMeasurementWaistText.getText().toString());
        table.setLeftCalf(binding.addMeasurementLCalfText.getText().toString());
        table.setRightCalf(binding.addMeasurementRCalfText.getText().toString());
        table.setLeftThigh(binding.addMeasurementLThighText.getText().toString());
        table.setRightThigh(binding.addMeasurementRThighText.getText().toString());
        table.setWeight(binding.addMeasurementWeightText.getText().toString());
        table.setDate(Date.from(Instant.now()));
        boolean checkFields = binding.addMeasurementChestText.getText().toString().isEmpty() || binding.addMeasurementHipsText.getText().toString().isEmpty() ||
                binding.addMeasurementLArmText.getText().toString().isEmpty() || binding.addMeasurementRArmText.getText().toString().isEmpty() || binding.addMeasurementWaistText.getText().toString().isEmpty() ||
                binding.addMeasurementLCalfText.getText().toString().isEmpty() || binding.addMeasurementRCalfText.getText().toString().isEmpty() || binding.addMeasurementLThighText.getText().toString().isEmpty() ||
                binding.addMeasurementRThighText.getText().toString().isEmpty() || binding.addMeasurementWeightText.getText().toString().isEmpty();

        Map map = new HashMap();
        Field[] fields = table.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(table);
            map.put(field.getName(), value);
        }
        if (!checkFields) {
            colref.add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<DocumentReference> task) {
                    if (task.isSuccessful()){
                        onDestroyView();

                    }
                }
            });

        }else{
            Toast.makeText(getContext(), "test", Toast.LENGTH_SHORT).show();
        }


    }


}