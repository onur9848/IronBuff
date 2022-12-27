package com.senerunosoft.ironbuff.MainMenuFragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.senerunosoft.ironbuff.databinding.FragmentAddMeasurementDialogBinding;
import com.senerunosoft.ironbuff.table.UserMeasurementTable;
import org.jetbrains.annotations.NotNull;

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
    UserMeasurementTable oldTable;

    public addMeasurementDialogFragment(UserMeasurementTable table) {
        oldTable = table;
    }

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

        setHintText();
        buttonProcess();

    }

    private void setHintText() {
        binding.addMeasurementWeightText.setHint(oldTable.getWeight() + " kg");
        binding.addMeasurementChestText.setHint(oldTable.getChest() + " cm");
        binding.addMeasurementLArmText.setHint(oldTable.getLeftArm() + " cm");
        binding.addMeasurementRArmText.setHint(oldTable.getRightArm() + " cm");
        binding.addMeasurementWaistText.setHint(oldTable.getWaist() + " cm");
        binding.addMeasurementHipsText.setHint(oldTable.getHips() + " cm");
        binding.addMeasurementLThighText.setHint(oldTable.getLeftThigh() + " cm");
        binding.addMeasurementRThighText.setHint(oldTable.getRightThigh() + " cm");
        binding.addMeasurementLCalfText.setHint(oldTable.getLeftCalf() + " cm");
        binding.addMeasurementRCalfText.setHint(oldTable.getRightCalf() + " cm");

    }

    private void buttonProcess() {
        binding.addMeasurementOkeyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNewBodyMeasurement();
            }
        });
        binding.addMeasurementCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                onDestroyView();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void getNewBodyMeasurement() {
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

        CheckFields();


        table.setDate(Date.from(Instant.now()));

        colref.add(table).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    dismiss();
                    onDestroyView();

                }
            }
        });
        Map<String, Object> weight = new HashMap<String, Object>();
        weight.put("weight", table.getWeight());
        firestore.collection(COLLECTION_USER_TABLE).document(auth.getCurrentUser().getUid()).update(weight).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull @NotNull Task task) {
            }
        });


    }

    private void CheckFields() {
        if (table.getChest().isEmpty())
            table.setChest(oldTable.getChest());

        if (table.getHips().isEmpty())
            table.setHips(oldTable.getHips());

        if (table.getLeftArm().isEmpty())
            table.setLeftArm(oldTable.getLeftArm());

        if (table.getRightArm().isEmpty())
            table.setRightArm(oldTable.getRightArm());

        if (table.getWaist().isEmpty())
            table.setWaist(oldTable.getWaist());

        if (table.getLeftCalf().isEmpty())
            table.setLeftCalf(oldTable.getLeftCalf());

        if (table.getRightCalf().isEmpty())
            table.setRightCalf(oldTable.getRightCalf());

        if (table.getLeftThigh().isEmpty())
            table.setLeftThigh(oldTable.getLeftThigh());

        if (table.getRightThigh().isEmpty())
            table.setRightThigh(oldTable.getRightThigh());

        if (table.getWeight().isEmpty())
            table.setWeight(oldTable.getWeight());
    }


}