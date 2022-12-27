package com.senerunosoft.ironbuff.MainMenuFragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.SharedElementCallback;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;
import com.google.firebase.firestore.EventListener;
import com.google.firestore.bundle.BundleElement;
import com.senerunosoft.ironbuff.MainMenuFragment.adapter.SelectedMealListAdapter;
import com.senerunosoft.ironbuff.R;
import com.senerunosoft.ironbuff.activity.MainMenuActivity;
import com.senerunosoft.ironbuff.databinding.FragmentCalorieDetailBinding;
import com.senerunosoft.ironbuff.table.FoodTable;
import com.senerunosoft.ironbuff.table.MealFoodList;
import com.senerunosoft.ironbuff.table.UserMacroDetailTable;
import com.senerunosoft.ironbuff.table.UserTable;
import org.eazegraph.lib.models.PieModel;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;


public class CalorieDetailFragment extends Fragment {

    private static final String COLLECTION_FOOD_LIST = "foodTable";
    private static final String COLLECTION_USER_TABLE = "userTable";
    String breakfastStr, lunchStr, dinnerStr, extraStr;
    FragmentCalorieDetailBinding binding;
    FirebaseFirestore firestore;
    FirebaseAuth auth;

    MealFoodList mealFoodList;
    SimpleDateFormat sdfFormat;
    String dateString;
    DocumentReference reference;
    float dailyCalorie, totalCalorie;
    Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCalorieDetailBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        defVariable();
        setPieChart();
        buttonProcess();
        getFoodLists();

    }

    @SuppressLint("SimpleDateFormat")
    private void defVariable() {
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        sdfFormat = new SimpleDateFormat("dd.MM.yyyy");
        dateString = sdfFormat.format(Date.from(Instant.now()));
        reference = firestore.collection(COLLECTION_USER_TABLE).document(auth.getCurrentUser().getUid()).collection(COLLECTION_FOOD_LIST).document(dateString);
        breakfastStr = "Breakfast";
        dinnerStr = "Dinner";
        lunchStr = "Lunch";
        extraStr = "Extra";
        context = (MainMenuActivity) getContext();
        mealFoodList = new MealFoodList();
    }

    private void buttonProcess() {
        binding.addFoodButtonBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("label", breakfastStr);
                bundle.putString("database", breakfastStr);
                navigateAddFood(bundle);
            }
        });
        binding.addFoodButtonLunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("label", lunchStr);
                bundle.putString("database", lunchStr);
                navigateAddFood(bundle);
            }
        });
        binding.addFoodButtonDinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("label", dinnerStr);
                bundle.putString("database", dinnerStr);
                navigateAddFood(bundle);
            }
        });
        binding.addFoodButtonExtra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("label", extraStr);
                bundle.putString("database", extraStr);
                navigateAddFood(bundle);
            }
        });
    }

    private void navigateAddFood(Bundle bundle) {
        NavDirections directions = CalorieDetailFragmentDirections.gotoAddMealFragment();
        Navigation.findNavController(getView()).navigate(directions.getActionId(), bundle);


    }

    private void getFoodLists() {

        reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @SuppressWarnings("unchecked")
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if (error == null) {
                    mealFoodList = value.toObject(MealFoodList.class);


                    if (getContext() != null && mealFoodList != null) {
                        setList();
                    }
                }
            }
        });


    }

    private void setList() {


        float breakfastCalorie = 0, lunchCalorie = 0, dinnerCalorie = 0, extraCalorie = 0;
        if (mealFoodList.getBreakfastList() != null) {
            SelectedMealListAdapter breakFastAdapter = new SelectedMealListAdapter(getContext(), mealFoodList.getBreakfastList());
            binding.breakfastListview.setAdapter(breakFastAdapter);
            breakfastCalorie = mealCalorie(mealFoodList.getBreakfastList());
            binding.calorieDetailBreakfastCalorie.setText(breakfastCalorie + " Kcal");

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);
            builder.setTitle("Yemeği silmek istediğinize emin misiniz");
            builder.setNegativeButton("İptal", null);

            binding.breakfastListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mealFoodList.getBreakfastList().remove(position);
                            removeItem(breakfastStr, mealFoodList.getBreakfastList());
                        }
                    });
                    builder.show();
                }
            });
        }
        if (mealFoodList.getLunchList() != null) {
            SelectedMealListAdapter LunchAdapter = new SelectedMealListAdapter(getContext(), mealFoodList.getLunchList());
            binding.lunchListview.setAdapter(LunchAdapter);
            lunchCalorie = mealCalorie(mealFoodList.getLunchList());
            binding.calorieDetailLunchCalorie.setText(lunchCalorie + " Kcal");

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);
            builder.setTitle("Yemeği silmek istediğinize emin misiniz");
            builder.setNegativeButton("İptal", null);
            binding.lunchListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mealFoodList.getLunchList().remove(position);
                            removeItem(lunchStr, mealFoodList.getLunchList());
                        }
                    });
                    builder.show();
                }
            });


        }
        if (mealFoodList.getDinnerList() != null) {
            SelectedMealListAdapter DinnerAdapter = new SelectedMealListAdapter(getContext(), mealFoodList.getDinnerList());
            binding.dinnerListview.setAdapter(DinnerAdapter);
            dinnerCalorie = mealCalorie(mealFoodList.getDinnerList());
            binding.calorieDetailDinnerCalorie.setText(dinnerCalorie + " Kcal");

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);
            builder.setTitle("Yemeği silmek istediğinize emin misiniz");
            builder.setNegativeButton("İptal", null);
            binding.dinnerListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mealFoodList.getDinnerList().remove(position);
                            removeItem(dinnerStr, mealFoodList.getDinnerList());
                        }
                    });
                    builder.show();
                }
            });
        }
        if (mealFoodList.getExtraList() != null) {
            SelectedMealListAdapter ExtraAdapter = new SelectedMealListAdapter(getContext(), mealFoodList.getExtraList());
            binding.extraListview.setAdapter(ExtraAdapter);
            extraCalorie = mealCalorie(mealFoodList.getExtraList());
            binding.calorieDetailExtraCalorie.setText(extraCalorie + " Kcal");

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);
            builder.setTitle("Yemeği silmek istediğinize emin misiniz");
            builder.setNegativeButton("İptal", null);
            binding.extraListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mealFoodList.getExtraList().remove(position);
                            removeItem(extraStr, mealFoodList.getExtraList());
                        }
                    });
                    builder.show();

                }
            });
        }

        binding.breakfastListview.setLayoutParams(listviewHeight(getValue(mealFoodList.getBreakfastList()), binding.breakfastListview));
        binding.lunchListview.setLayoutParams(listviewHeight(getValue(mealFoodList.getLunchList()), binding.lunchListview));
        binding.dinnerListview.setLayoutParams(listviewHeight(getValue(mealFoodList.getDinnerList()), binding.dinnerListview));
        binding.extraListview.setLayoutParams(listviewHeight(getValue(mealFoodList.getExtraList()), binding.extraListview));


        dailyCalorie = breakfastCalorie + lunchCalorie + dinnerCalorie + extraCalorie;
        binding.calorieDetailDailyCalorie.setText(round(dailyCalorie) + " Kcal");
        setPieChart();

    }

    private int getValue(List<FoodTable> list) {
        if (list == null) {
            return 0;
        } else {
            return list.size();
        }
    }

    private ViewGroup.LayoutParams listviewHeight(int listsize, ListView view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (listsize == 1) {
            params.height = 140;
        } else if (listsize == 2) {
            params.height = 280;
        } else if (listsize >= 3) {
            params.height = 420;
        } else {
            params.height = 0;
        }
        return params;
    }

    float round(float sayi) {
        sayi = (float) (Math.round(sayi * 100.0) / 100.0);
        return sayi;
    }

    private Float mealCalorie(List<FoodTable> list) {
        float total = 0;
        if (list != null) {
            for (FoodTable food : list) {
                total += food.getFoodCalorie();
            }
        }
        return total;
    }

    private void removeItem(String key, List<FoodTable> list) {

        reference.set(mealFoodList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {


            }
        });


    }

    private void setPieChart() {

        firestore.collection(COLLECTION_USER_TABLE).document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    UserTable table = task.getResult().toObject(UserTable.class);

                    float height = Float.parseFloat(table.getHeight());
                    float weight = Float.parseFloat(table.getWeight());
                    float age = Float.parseFloat(table.getAge());
                    String sex = table.getSex();

                    if (sex.equals("Male") || sex.equals("Erkek")) {
                        totalCalorie = (float) (66 + (13.7 * weight) + (5 * height) - (6.8 * age));
                    } else {
                        totalCalorie = (float) (655 + (9.6 * weight) + (1.8 * height) - (4.7 * age));
                    }
                    binding.calorieDetailTotalCalorie.setText(totalCalorie + " Kcal");

                    PieModel model1 = new PieModel(dailyCalorie, Color.RED);
                    PieModel model2 = new PieModel(totalCalorie - dailyCalorie, Color.DKGRAY);
                    binding.calorieDetailPiechart.clearChart();
                    binding.calorieDetailPiechart.addPieSlice(model1);
                    binding.calorieDetailPiechart.addPieSlice(model2);
                    binding.calorieDetailPiechart.setUsePieRotation(false);
                    float percent = (dailyCalorie / totalCalorie) * 100;
                    binding.calorieDetailPercent.setText("%" + round(percent));

                    if (mealFoodList != null)
                        addMacroDetailTable(weight);


                }
            }
        });
    }

    private void addMacroDetailTable(float weight) {
        float totalFat = getFatMeal(mealFoodList.getBreakfastList()) + getFatMeal(mealFoodList.getLunchList()) + getFatMeal(mealFoodList.getDinnerList()) + getFatMeal(mealFoodList.getExtraList());
        float totalProtein = getProteinMeal(mealFoodList.getBreakfastList()) + getProteinMeal(mealFoodList.getLunchList()) + getProteinMeal(mealFoodList.getDinnerList()) + getProteinMeal(mealFoodList.getExtraList());
        float totalCarbonhydrat = getCarbonhydratMeal(mealFoodList.getBreakfastList()) + getCarbonhydratMeal(mealFoodList.getLunchList()) + getCarbonhydratMeal(mealFoodList.getDinnerList()) + getCarbonhydratMeal(mealFoodList.getExtraList());
        float totalgram = getGramMeal(mealFoodList.getBreakfastList()) + getGramMeal(mealFoodList.getLunchList()) + getGramMeal(mealFoodList.getDinnerList()) + getGramMeal(mealFoodList.getExtraList());

        UserMacroDetailTable table = new UserMacroDetailTable();
        table.setDailyMaxCalorie(totalCalorie);
        table.setDailyCalorie(dailyCalorie);
        table.setDailyFat(totalFat);
        table.setDailyProtein(totalProtein);
        table.setDailyCarbonhydrat(totalCarbonhydrat);
        table.setDailyGram(totalgram);
        table.setDailyBreakfastCalorie(mealCalorie(mealFoodList.getBreakfastList()));
        table.setDailyLunchCalorie(mealCalorie(mealFoodList.getLunchList()));
        table.setDailyDinnerCalorie(mealCalorie(mealFoodList.getDinnerList()));
        table.setDailyExtraCalorie(mealCalorie(mealFoodList.getExtraList()));

        firestore.collection(COLLECTION_USER_TABLE).document(auth.getCurrentUser().getUid())
                .collection("DailyCalorie").document(dateString).set(table).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (task.isSuccessful()) ;
                    }
                });


    }

    private float getFatMeal(List<FoodTable> list) {
        float fat = 0;
        if (list != null) {

            for (FoodTable table : list)
                fat += table.getFoodFat();

        }
        return fat;
    }

    private float getProteinMeal(List<FoodTable> list) {
        float protein = 0;
        if (list != null) {

            for (FoodTable table : list)
                protein += table.getFoodProtein();

        }
        return protein;

    }

    private float getCarbonhydratMeal(List<FoodTable> list) {
        float carbonhydrat = 0;
        if (list != null) {

            for (FoodTable table : list)
                carbonhydrat += table.getFoodCarbonhydrat();

        }
        return carbonhydrat;
    }

    private float getGramMeal(List<FoodTable> list) {

        float gram = 0;
        if (list != null) {

            for (FoodTable table : list)
                gram += table.getFoodGram();

        }
        return gram;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}