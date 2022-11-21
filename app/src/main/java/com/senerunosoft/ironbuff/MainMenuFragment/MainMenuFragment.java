package com.senerunosoft.ironbuff.MainMenuFragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.viewpager2.widget.ViewPager2;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;
import com.google.firebase.storage.FirebaseStorage;
import com.senerunosoft.ironbuff.MainMenuFragment.adapter.BodyMeasurementPageAdapter;
import com.senerunosoft.ironbuff.databinding.FragmentMainMenuBinding;
import com.senerunosoft.ironbuff.table.UserMeasurementTable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainMenuFragment extends Fragment {

    FragmentMainMenuBinding binding;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    FirebaseStorage storage;
    ViewPager2 measurementViewCard;

    List<UserMeasurementTable> userMeasurementTables = new ArrayList<>();
    int i = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainMenuBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    public int getRandomNumber() {
        Random random = new Random();
        int a = random.nextInt(50);
        a = a + 80;
        return a;
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        measurementViewCard = binding.mainMenuShowMeasurement;
        List<String> liste = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            UserMeasurementTable userMeasurement = new UserMeasurementTable();
            userMeasurement.setChest("" + getRandomNumber());
            userMeasurement.setLeftArm("" + getRandomNumber());
            userMeasurement.setRightArm("" + getRandomNumber());
            userMeasurement.setWaist("" + getRandomNumber());
            userMeasurement.setHips("" + getRandomNumber());
            userMeasurement.setLeftThigh("" + getRandomNumber());
            userMeasurement.setRightThigh("" + getRandomNumber());
            userMeasurement.setLeftCalf("" + getRandomNumber());
            userMeasurement.setRightCalf("" + getRandomNumber());
            userMeasurement.setWeight("" + getRandomNumber());
            userMeasurementTables.add(userMeasurement);


        }
        measurementViewCard.setAdapter(new BodyMeasurementPageAdapter(getContext(), userMeasurementTables, measurementViewCard));


    }





    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}