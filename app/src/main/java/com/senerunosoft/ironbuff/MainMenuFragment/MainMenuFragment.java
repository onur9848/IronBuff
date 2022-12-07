package com.senerunosoft.ironbuff.MainMenuFragment;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.senerunosoft.ironbuff.MainMenuFragment.adapter.BodyMeasurementPageAdapter;
import com.senerunosoft.ironbuff.databinding.FragmentMainMenuBinding;
import com.senerunosoft.ironbuff.table.UserMacroDetailTable;
import com.senerunosoft.ironbuff.table.UserMeasurementTable;
import org.eazegraph.lib.models.PieModel;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;


public class MainMenuFragment extends Fragment {

    private static final String COLLECTION_USER_TABLE = "userTable";
    private static final String COLLECTION_BODY_MEASUREMENT_TABLE = "bodyMeasurement";
    private static final String COLLECTION_DAILY_MACRO = "DailyCalorie";
    private static final String COLLECTION_FOOD_TABLE = "foodTable";
    FragmentMainMenuBinding binding;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    FirebaseStorage storage;
    ViewPager2 measurementViewCard;
    float totalMacro;
    UserMacroDetailTable macroDetailTable;
    SimpleDateFormat sdfFormat;
    String dateString;
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
        userMeasurementTables = new ArrayList<>();
        sdfFormat = new SimpleDateFormat("dd.MM.yyyy");
        dateString = sdfFormat.format(Date.from(Instant.now()));
        macroDetailTable = new UserMacroDetailTable();


        getBodymeasurement();
        gotoCalorieView();
        getPieChartData();

    }

    public void gotoCalorieView() {
        binding.calorieDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections directions = MainMenuFragmentDirections.gotoCalorieFragment();
                Navigation.findNavController(getView()).navigate(directions);

            }
        });

    }
    private TypedValue value;
    private void pieCharts() {
        value = new TypedValue();
        Resources.Theme theme = getContext().getTheme();
        theme.resolveAttribute(com.google.android.material.R.attr.colorPrimary, value, true);
        totalMacro = macroDetailTable.getDailyProtein() + macroDetailTable.getDailyFat() + macroDetailTable.getDailyCarbonhydrat();
        proteinPieChart();
        carbonhydratPieChart();
        fatPieChart();
        caloriePieChart();
    }
    private void fatPieChart() {


        PieModel model = new PieModel("fat", macroDetailTable.getDailyFat(), Color.parseColor("#C5120B"));
        PieModel model1 = new PieModel("DailyMacro", totalMacro - macroDetailTable.getDailyFat(), value.data);
        binding.fatPieChart.addPieSlice(model);
        binding.fatPieChart.addPieSlice(model1);
        binding.fatPieChart.startAnimation();
        binding.mainMenuFatText.setText(round(macroDetailTable.getDailyFat()) + "g /\n" + round(totalMacro) + "g");
        float percent = (macroDetailTable.getDailyFat()/totalMacro)*100;
        binding.mainMenuFatPercent.setText(binding.mainMenuFatPercent.getText()+"\n%"+round(percent));
    }
    private void carbonhydratPieChart() {


        PieModel model = new PieModel("Karbonhidrat", macroDetailTable.getDailyCarbonhydrat(), Color.parseColor("#2F61AD"));
        PieModel model1 = new PieModel("DailyMacro", totalMacro - macroDetailTable.getDailyCarbonhydrat(), value.data);
        binding.carbonhydratPieChart.addPieSlice(model);
        binding.carbonhydratPieChart.addPieSlice(model1);
        binding.carbonhydratPieChart.startAnimation();
        binding.mainMenuCarbonhydratText.setText(round(macroDetailTable.getDailyCarbonhydrat()) + "g /\n" + round(totalMacro) + "g");
        float percent = (macroDetailTable.getDailyCarbonhydrat()/totalMacro)*100;
        binding.mainMenuCarbonhydratPercent.setText(binding.mainMenuCarbonhydratPercent.getText()+"\n%"+round(percent));

    }
    private void proteinPieChart() {

        PieModel model = new PieModel("Protein", macroDetailTable.getDailyProtein(), Color.parseColor("#F5D20D"));
        PieModel model1 = new PieModel("DailyMacro", totalMacro - macroDetailTable.getDailyProtein(), value.data);
        binding.proteinPieChart.addPieSlice(model);
        binding.proteinPieChart.addPieSlice(model1);
        binding.proteinPieChart.startAnimation();
        binding.mainMenuProteinText.setText(round(macroDetailTable.getDailyProtein()) + "g /\n" + round(totalMacro) + "g");
        float percent = (macroDetailTable.getDailyProtein()/totalMacro)*100;
        binding.mainMenuProteinPercent.setText(binding.mainMenuProteinPercent.getText()+"\n%"+round(percent));
    }
    private void caloriePieChart() {
        PieModel model = new PieModel("Calorie", macroDetailTable.getDailyCalorie(), Color.YELLOW);
        PieModel model1 = new PieModel("totalCalorie", macroDetailTable.getDailyMaxCalorie()-macroDetailTable.getDailyCalorie(), value.data);
        binding.caloriePieChart.addPieSlice(model);
        binding.caloriePieChart.addPieSlice(model1);
        binding.caloriePieChart.startAnimation();

        binding.breakfastKcalTextPiechart.setText(macroDetailTable.getDailyBreakfastCalorie()+" Kcal");
        binding.lunchKcalTextPiechart.setText(macroDetailTable.getDailyLunchCalorie()+" Kcal");
        binding.dinnerKcalTextPiechart.setText(macroDetailTable.getDailyDinnerCalorie()+" Kcal");
        binding.extraKcalTextPiechart.setText(macroDetailTable.getDailyExtraCalorie()+" Kcal");




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
                    if (userMeasurementTables.isEmpty()) {
                        userMeasurementTables = new ArrayList<>();
                        userMeasurementTables.add(new UserMeasurementTable(Calendar.getInstance().getTime()));
                    }
                    Collections.sort(userMeasurementTables);
                    BodyMeasurementPageAdapter adapter = new BodyMeasurementPageAdapter(getContext(), userMeasurementTables, measurementViewCard, getChildFragmentManager());
                    binding.mainMenuShowMeasurement.setAdapter(adapter);
                    binding.mainMenuShowMeasurement.setCurrentItem(adapter.getItemCount());
                } else {

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

    private void getPieChartData() {
        DocumentReference reference = firestore.collection(COLLECTION_USER_TABLE).document(auth.getCurrentUser().getUid());
        reference.collection(COLLECTION_DAILY_MACRO).document(dateString).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        macroDetailTable = new UserMacroDetailTable(doc.getData());
                        pieCharts();
                    }
                }
            }
        });
    }




    float round(float sayi) {
        sayi = (float) (Math.round(sayi * 100.0) / 100.0);
        return sayi;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}