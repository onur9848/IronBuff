package com.senerunosoft.ironbuff.MainMenuFragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;
import com.google.firebase.firestore.EventListener;
import com.senerunosoft.ironbuff.MainMenuFragment.adapter.AddMealListAdapter;
import com.senerunosoft.ironbuff.MainMenuFragment.adapter.SelectedMealListAdapter;
import com.senerunosoft.ironbuff.R;
import com.senerunosoft.ironbuff.activity.MainMenuActivity;
import com.senerunosoft.ironbuff.databinding.FragmentAddFoodMealBinding;
import com.senerunosoft.ironbuff.table.FoodTable;
import com.senerunosoft.ironbuff.table.MealFoodList;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;


public class AddFoodMealFragment extends Fragment {
    private static final String COLLECTION_FOOD_LIST = "FoodList";
    private static final String COLLECTION_USER_TABLE = "userTable";
    private static final String COLLECTION_FOOD_TABLE = "foodTable";
    FragmentAddFoodMealBinding binding;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    List<FoodTable> foodTables, selectedList;
    MealFoodList mealFoodList;
    LinearLayout layout;
    BottomSheetDialog addFoodDialog;
    Button addFoodButton;
    TextView foodName, foodCalorie, foodFat, foodProtein, foodCarbonhydrat, foodGram;
    FoodTable selectFood;
    String label, dateString, mapKey;
    SimpleDateFormat sdf;
    Date date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAddFoodMealBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        layout = binding.listSelectedFood;
        addFoodDialog = new BottomSheetDialog(getContext());
        selectedList = new ArrayList<>();
        sdf = new SimpleDateFormat("dd.MM.yyyy");
        date = Date.from(Instant.now());
        dateString = sdf.format(date);
        binding.addFoodButton.setEnabled(false);
        mealFoodList = new MealFoodList();

        label = getArguments().getString("label");
        mapKey = getArguments().getString("database");


        ((MainMenuActivity) getActivity()).getSupportActionBar().setTitle(label);

        getFoodList();
        rotateImage();
        addSelectFoodButton();
    }


    private void getFoodList() {
        firestore.collection(COLLECTION_FOOD_LIST).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if (error == null) {
                    foodTables = value.toObjects(FoodTable.class);
                    searchList();
                    setFoodList(foodTables);
                }

            }
        });

    }

    private void searchList() {

        binding.addMealSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                List<FoodTable> searchList = new ArrayList<>();

                for (FoodTable tb : foodTables) {
                    if (tb.getFoodName().toLowerCase().indexOf(s, 0) >= 0) {
                        searchList.add(tb);
                    }
                }


                setFoodList(searchList);

                return false;
            }
        });


    }

    private void rotateImage() {
        layout.setVisibility(View.GONE);
        binding.showSelectedFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (binding.listSelectedFood.getVisibility() == View.GONE) {
                    layout.setVisibility(View.VISIBLE);

                    animate(0);
                    layout.animate().translationY(0).setDuration(250).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);

                        }
                    });

                } else {
                    animate(180);
                    layout.animate().translationY(layout.getHeight()).setDuration(250).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            binding.listSelectedFood.setVisibility(View.GONE);
                        }
                    });

                }


            }
        });
    }

    private void animate(float current) {

        Animation a = new RotateAnimation(current, current + 180,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        a.setDuration(250);
        a.setFillEnabled(true);
        a.setFillAfter(true);
        binding.showSelectedFood.startAnimation(a);

    }

    private void setFoodList(List<FoodTable> searchList) {

        AddMealListAdapter adapter = new AddMealListAdapter(getContext(), (ArrayList<FoodTable>) searchList);
        binding.addFoodList.setAdapter(adapter);

        binding.addFoodList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FoodTable foodTable = searchList.get(i);
                showAddMealBottom(foodTable);


            }
        });


    }

    @SuppressLint("SetTextI18n")
    private void showAddMealBottom(FoodTable foodTable) {

        addFoodDialog.setContentView(R.layout.add_meal_bottom_sheet_dialog);

        selectFood = foodTable;
        com.shawnlin.numberpicker.NumberPicker numberPicker;
        numberPicker = addFoodDialog.findViewById(R.id.bottom_sheet_number_picker);
        foodName = addFoodDialog.findViewById(R.id.bottom_sheet_food_name);
        foodCalorie = addFoodDialog.findViewById(R.id.bottom_sheet_calorie);
        foodFat = addFoodDialog.findViewById(R.id.bottom_sheet_fat);
        foodProtein = addFoodDialog.findViewById(R.id.bottom_sheet_protein);
        foodCarbonhydrat = addFoodDialog.findViewById(R.id.bottom_sheet_carbonhydrat);
        foodGram = addFoodDialog.findViewById(R.id.bottom_sheet_gram);
        addFoodButton = addFoodDialog.findViewById(R.id.add_select_food);


        foodName.setText(foodTable.getFoodName());
        foodCalorie.setText(foodTable.getFoodCalorie() + " Kcal");
        foodFat.setText(foodTable.getFoodFat() + " g");
        foodProtein.setText(foodTable.getFoodProtein() + " g");
        foodCarbonhydrat.setText(foodTable.getFoodCarbonhydrat() + " g");
        foodGram.setText(foodTable.getFoodGram() + " g");
        numberPicker.setValue((int) foodTable.getFoodGram());

        addFoodDialog.show();

        numberPicker.setOnValueChangedListener(new com.shawnlin.numberpicker.NumberPicker.OnValueChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onValueChange(com.shawnlin.numberpicker.NumberPicker picker, int oldVal, int gram) {
                foodName.setText(foodTable.getFoodName());
                float value = (float) gram, fat, protein, carbonhydrat, calorie;
                fat = (foodTable.getFoodFat() * value) / foodTable.getFoodGram();
                protein = (foodTable.getFoodProtein() * value) / foodTable.getFoodGram();
                carbonhydrat = (foodTable.getFoodCarbonhydrat() * value) / foodTable.getFoodGram();
                calorie = (foodTable.getFoodCalorie() * value) / foodTable.getFoodGram();

                selectFood = new FoodTable(foodTable.getFoodName(), round(carbonhydrat), round(fat), round(protein), round(calorie), gram);

                foodCalorie.setText(round(calorie) + " Kcal");
                foodFat.setText(round(fat) + " g");
                foodProtein.setText(round(protein) + " g");
                foodCarbonhydrat.setText(round(carbonhydrat) + " g");
                foodGram.setText(gram + " g");
                addSelectedItem(selectFood);

            }
        });
        addSelectedItem(selectFood);

    }

    private void addSelectedItem(FoodTable selectFood) {


        addFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.addFoodButton.setEnabled(true);
                binding.addFoodSelectedFoodCount.setVisibility(View.VISIBLE);
                selectedList.add(selectFood);
                addFoodDialog.dismiss();
                setSelectedList();
            }
        });
    }

    private void setSelectedList() {

        SelectedMealListAdapter adapter = new SelectedMealListAdapter(((MainMenuActivity) getContext()), (ArrayList<FoodTable>) selectedList);
        binding.selectedFoodList.setAdapter(adapter);
        float totalcalorie = 0;
        for (FoodTable table : selectedList) {
            totalcalorie += table.getFoodCalorie();
        }
        binding.addFoodTotalCalorie.setText("Toplam: " + round(totalcalorie) + " Kcal");
        binding.addFoodSelectedFoodCount.setText(selectedList.size() + "");

        binding.selectedFoodList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedList.remove(i);
                setSelectedList();
                if (selectedList.size() == 0) {
                    binding.addFoodButton.setEnabled(false);
                    binding.addFoodSelectedFoodCount.setVisibility(View.GONE);
                }
            }
        });

    }

    private void addSelectFoodButton() {

        binding.addFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDatabase();
            }
        });
    }

    private void setDatabase() {
        DocumentReference reference = firestore.collection(COLLECTION_USER_TABLE).document(auth.getCurrentUser().getUid())
                .collection(COLLECTION_FOOD_TABLE).document(dateString);
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    mealFoodList = task.getResult().toObject(MealFoodList.class);
                    if (mealFoodList == null) {
                        mealFoodList = new MealFoodList();
                    }
                    addlist();
                    reference.set(mealFoodList).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {

                        }
                    });
                    getActivity().onBackPressed();

                }
            }
        });
    }

    private void addlist() {
        if (mapKey.equals(getString(R.string.databaseBreakfast))) {
            if (mealFoodList.getBreakfastList() != null) {
                selectedList.addAll(mealFoodList.getBreakfastList());
            }
            mealFoodList.setBreakfastList(selectedList);

        } else if (mapKey.equals(getString(R.string.databaseLunch))) {
            if (mealFoodList.getLunchList() != null) {
                selectedList.addAll(mealFoodList.getLunchList());
            }
            mealFoodList.setLunchList(selectedList);

        } else if (mapKey.equals(getString(R.string.databaseDinner))) {
            if (mealFoodList.getDinnerList() != null) {
                selectedList.addAll(mealFoodList.getDinnerList());
            }
            mealFoodList.setDinnerList(selectedList);

        } else if (mapKey.equals(getString(R.string.databaseExtra))) {
            if (mealFoodList.getExtraList() != null) {
                selectedList.addAll(mealFoodList.getExtraList());
            }
            mealFoodList.setExtraList(selectedList);
        }

    }

    float round(float sayi) {
        sayi = (float) (Math.round(sayi * 100.0) / 100.0);
        return sayi;
    }


}