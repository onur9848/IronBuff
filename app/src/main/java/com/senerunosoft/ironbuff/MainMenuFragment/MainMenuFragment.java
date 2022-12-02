package com.senerunosoft.ironbuff.MainMenuFragment;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.viewpager2.widget.ViewPager2;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.senerunosoft.ironbuff.MainMenuFragment.adapter.BodyMeasurementPageAdapter;
import com.senerunosoft.ironbuff.databinding.FragmentMainMenuBinding;
import com.senerunosoft.ironbuff.table.UserMeasurementTable;
import org.eazegraph.lib.models.PieModel;
import org.jetbrains.annotations.NotNull;

import java.util.*;


public class MainMenuFragment extends Fragment {

    private static final String COLLECTION_USER_TABLE = "userTable";
    private static final String COLLECTION_BODY_MEASUREMENT_TABLE = "bodyMeasurement";
    FragmentMainMenuBinding binding;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    FirebaseStorage storage;
    ViewPager2 measurementViewCard;

    List<UserMeasurementTable> userMeasurementTables;
    int i = 0;


    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainMenuBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        //root.findViewById(R.layout.add_body_measurement);
        userMeasurementTables = new ArrayList<>();

        getBodymeasurement();
        pieCharts();

    }

    private TypedValue value;

    private void pieCharts() {
        value = new TypedValue();
        Resources.Theme theme = getContext().getTheme();
        theme.resolveAttribute(com.google.android.material.R.attr.colorPrimary, value, true);

        proteinPieChart();
        carbonhydratPieChart();
        fatPieChart();
        caloriePieChart();
    }

    private void fatPieChart() {

        float dailyFat = 100, Fat = 15;
        dailyFat = dailyFat - Fat;
        PieModel model = new PieModel("Karbonhidrat", Fat, Color.parseColor("#C5120B"));
        PieModel model1 = new PieModel("DailyMacro", dailyFat, value.data);
        binding.fatPieChart.addPieSlice(model);
        binding.fatPieChart.addPieSlice(model1);
        binding.fatPieChart.startAnimation();
    }

    private void carbonhydratPieChart() {

        float dailyCarbonhydrat = 100, Carbonhydrat = 90;
        dailyCarbonhydrat = dailyCarbonhydrat - Carbonhydrat;
        PieModel model = new PieModel("Karbonhidrat", Carbonhydrat, Color.parseColor("#2F61AD"));
        PieModel model1 = new PieModel("DailyMacro", dailyCarbonhydrat, value.data);
        binding.carbonhydratPieChart.addPieSlice(model);
        binding.carbonhydratPieChart.addPieSlice(model1);
        binding.carbonhydratPieChart.startAnimation();

    }

    private void proteinPieChart() {

        float dailyprotein = 100, protein = 15;
        dailyprotein = dailyprotein - protein;
        PieModel model = new PieModel("Protein", protein, Color.parseColor("#F5D20D"));
        PieModel model1 = new PieModel("DailyMacro", dailyprotein, value.data);
        binding.proteinPieChart.addPieSlice(model);
        binding.proteinPieChart.addPieSlice(model1);
        binding.proteinPieChart.startAnimation();

    }

    private void caloriePieChart() {
        PieModel model = new PieModel("Calorie", 25, Color.YELLOW);
        PieModel model1 = new PieModel("totalCalorie", 100, value.data);


        binding.caloriePieChart.addPieSlice(model);
        binding.caloriePieChart.addPieSlice(model1);
        binding.caloriePieChart.startAnimation();


    }

    private void getBodymeasurement() {

        CollectionReference colref = firestore.collection(COLLECTION_USER_TABLE).document(auth.getCurrentUser().getUid()).collection(COLLECTION_BODY_MEASUREMENT_TABLE);

        colref.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if (error == null) {
                    userMeasurementTables = new ArrayList<>();
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map map = snapshot.getData();
                        UserMeasurementTable table = new UserMeasurementTable(map);
                        table.setDate(snapshot.getDate("date"));
                        userMeasurementTables.add(table);
                    }
                    if (userMeasurementTables.isEmpty()){
                        userMeasurementTables = new ArrayList<>();
                        userMeasurementTables.add(new UserMeasurementTable(Calendar.getInstance().getTime()));
                    }
                    binding.mainMenuShowMeasurement.setAdapter(new BodyMeasurementPageAdapter(getContext(), userMeasurementTables, measurementViewCard,getChildFragmentManager()));
                }
                else {
                    Toast.makeText(getContext(), "deneme", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        colref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                userMeasurementTables = new ArrayList<>();
//                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
//                    Map map = doc.getData();
//                    UserMeasurementTable table = new UserMeasurementTable(map);
//                    table.setDate(doc.getDate("date"));
//                    userMeasurementTables.add(table);
//                }
//
//                binding.mainMenuShowMeasurement.setAdapter(new BodyMeasurementPageAdapter(getContext(), userMeasurementTables, measurementViewCard));
//            }
//        });


    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}