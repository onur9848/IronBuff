package com.senerunosoft.ironbuff.AdminPage;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
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
import com.google.firebase.firestore.*;
import com.senerunosoft.ironbuff.AdminPage.Adapter.ExerciseNameGridViewAdapter;
import com.senerunosoft.ironbuff.AdminPage.Adapter.GridViewAdapter;
import com.senerunosoft.ironbuff.activity.MainMenuActivity;
import com.senerunosoft.ironbuff.databinding.FragmentAdminAddUserProgramBinding;
import com.senerunosoft.ironbuff.table.ExerciseTable;
import com.senerunosoft.ironbuff.table.UserTrainingTable;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;


public class AdminAddUserProgramFragment extends Fragment {

    private static final String COLLECTION_NAME_EXERCISE_TABLE = "exerciseTable";
    private static final String COLLECTION_NAME_USER_TABLE = "userTable";
    private static final String DOCUMENT_IN_EXERCISE_TABLE = "exercise";
    FragmentAdminAddUserProgramBinding binding;
    FirebaseFirestore firestore;
    ArrayList<String> exerciseZone, exerciseName, listExercise, exerciseDetails;
    ArrayList<String>  addListView;
    List<UserTrainingTable> userTrainingTables;
    List<ExerciseTable> exerciseTables;
    DocumentReference documentReference;
    UserTrainingTable table;
    private String DOC_ID_USER;
    Calendar myCalender = Calendar.getInstance();
    int Id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAdminAddUserProgramBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firestore = FirebaseFirestore.getInstance();
        documentReference = firestore.collection(COLLECTION_NAME_EXERCISE_TABLE).document(DOCUMENT_IN_EXERCISE_TABLE);
        exerciseZone = new ArrayList<>();
        listExercise = new ArrayList<>();
        table = new UserTrainingTable();
        DOC_ID_USER = getArguments().getString("docIDUser");
        userTrainingTables = new ArrayList<>();

        showHideButton();
        getSetAndReps();
        getExerciseDate();
        addTrainingProgram();

    }

    private void getGridViews() {
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    exerciseZone = (ArrayList<String>) task.getResult().get("exerciseZone");
                    Toast.makeText(getContext(), exerciseZone.toString(), Toast.LENGTH_SHORT).show();
                    GridViewAdapter adapter = new GridViewAdapter(getContext(), exerciseZone, binding.selectedExerciseZoneText);
                    binding.addUserProgramExercisezoneGridview.setVisibility(View.VISIBLE);
                    binding.addUserProgramExercisezoneGridview.setAdapter(adapter);
                    binding.selectedExerciseZoneText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            binding.selectedExerciseName.setText("");
                            exerciseName = new ArrayList<>();
                            exerciseDetails = new ArrayList<>();
                            getExerciseName();

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }

                    });

                }
            }
        });
    }

    private void getExerciseName() {
        documentReference.collection(binding.selectedExerciseZoneText.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    exerciseTables = task.getResult().toObjects(ExerciseTable.class);

                    ExerciseNameGridViewAdapter adapter = new ExerciseNameGridViewAdapter(getContext(), exerciseTables, binding.selectedExerciseName, Id);
                    binding.addUserProgramExercisenameGridview.setVisibility(View.VISIBLE);
                    binding.addUserProgramExercisenameGridview.setAdapter(adapter);


                }

            }
        });


    }

    private void showHideButton() {
        binding.showExerciseZones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.addUserProgramExercisezoneGridview.getVisibility() == View.GONE) {
                    binding.addUserProgramExercisezoneGridview.setVisibility(View.VISIBLE);
                } else {
                    binding.addUserProgramExercisezoneGridview.setVisibility(View.GONE);
                }
            }
        });
        binding.showExercises.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.addUserProgramExercisenameGridview.getVisibility() == View.GONE) {
                    binding.addUserProgramExercisenameGridview.setVisibility(View.VISIBLE);
                } else {
                    binding.addUserProgramExercisenameGridview.setVisibility(View.GONE);
                }
            }
        });
        binding.showExerciseSetAndReps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.addUserProgramSetAndRepsGridview.getVisibility() == View.GONE) {
                    binding.addUserProgramSetAndRepsGridview.setVisibility(View.VISIBLE);
                } else {
                    binding.addUserProgramSetAndRepsGridview.setVisibility(View.GONE);
                }
            }
        });
        binding.showExerciseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.addUserProgramExercisedateGridview.getVisibility() == View.GONE) {
                    binding.addUserProgramExercisedateGridview.setVisibility(View.VISIBLE);
                } else {
                    binding.addUserProgramExercisedateGridview.setVisibility(View.GONE);
                }
            }
        });
    }

    private void getSetAndReps() {
        binding.selectedExerciseName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0)
                    binding.addUserProgramSetAndRepsGridview.setVisibility(View.VISIBLE);
                binding.selectedExerciseSetsAndReps.setText("");
                binding.gridviewAddRepsSetsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (binding.gridviewAddReps.getText().toString().isEmpty() || binding.gridviewAddSets.getText().toString().isEmpty()) {
                            Toast.makeText(getContext(), "Lütfen Değerleri giriniz", Toast.LENGTH_SHORT).show();
                        } else {
                            InputMethodManager Imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            Imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            String setAndReps = binding.gridviewAddSets.getText().toString() + "x" + binding.gridviewAddReps.getText().toString();
                            binding.selectedExerciseSetsAndReps.setText(setAndReps);
                            binding.addUserProgramSetAndRepsGridview.setVisibility(View.GONE);
                        }
                    }
                });

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    private void getExerciseDate() {
        binding.addUserProgramExercisedateGridview.setVisibility(View.VISIBLE);
        openDateDialog();
        binding.gridviewAddDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.selectedExerciseDate.setText(binding.gridviewDateText.getText());
                binding.addUserProgramExercisedateGridview.setVisibility(View.GONE);
                getGridViews();
            }
        });


    }

    private void openDateDialog() {

        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.getDefault());

        binding.gridviewDateText.setText(dateFormat.format(myCalender.getTime()));
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                myCalender.set(Calendar.YEAR, year);
                myCalender.set(Calendar.MONTH, month);
                myCalender.set(Calendar.DAY_OF_MONTH, day);
                binding.gridviewDateText.setText(dateFormat.format(myCalender.getTime()));

            }
        };

        binding.gridviewDateDiaolog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), date,
                        myCalender.get(Calendar.YEAR),
                        myCalender.get(Calendar.MONTH),
                        myCalender.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


    }

    private void addTrainingProgram() {
        boolean bool = binding.selectedExerciseDate.getText().toString().isEmpty() || binding.selectedExerciseName.getText().toString().isEmpty()
                || binding.selectedExerciseZoneText.getText().toString().isEmpty() || binding.selectedExerciseSetsAndReps.getText().toString().isEmpty();

        addListView = new ArrayList<>();

        binding.addUserExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!bool) {
                    Toast.makeText(getContext(), "Tüm alanları seçiniz", Toast.LENGTH_SHORT).show();
                } else {
                    addItem();
                    removeItem();
                }


                addDatabase();

            }
        });
    }

    private void addItem() {
        table.setDate(myCalender.getTime());
        table.getExerciseZone().add(binding.selectedExerciseZoneText.getText().toString());
        table.getExerciseName().add(binding.selectedExerciseName.getText().toString());
        table.getExerciseRepsAndSets().add(binding.selectedExerciseSetsAndReps.getText().toString());
        for (ExerciseTable exerciseTable : exerciseTables) {
            if (exerciseTable.getExerciseName().equals(binding.selectedExerciseName.getText().toString())) {
                table.getExerciseImg1().add(exerciseTable.getExerciseImg1());
                table.getExerciseImg2().add(exerciseTable.getExerciseImg2());
                table.getExerciseDetail().add(exerciseTable.getExerciseDetail());
            }
        }
        table.setExerciseZoneCount(getZone());
        table.setExerciseCount(table.getExerciseName().size());

        addListView.add(binding.selectedExerciseName.getText().toString() + ": " + binding.selectedExerciseSetsAndReps.getText().toString() + " - " + binding.selectedExerciseZoneText.getText().toString());

        listItem();
    }

    private void removeItem() {
        binding.listviewExerciseZoneAndExercise.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                table.getExerciseName().remove(i);
                table.getExerciseRepsAndSets().remove(i);
                table.getExerciseZone().remove(i);
                table.getExerciseDetail().remove(i);
                addListView.remove(i);
                listItem();
            }
        });
    }

    private void listItem() {
        binding.selectedExerciseZoneAndExercise.setText(getExerciseText());
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, addListView);
        binding.listviewExerciseZoneAndExercise.setAdapter(adapter);
    }

    private void addDatabase() {

        binding.addUserProgramButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (table.getExerciseName().size() > 0) {
                    String docId = UUID.randomUUID().toString();
                    table.setDocID(docId);
                    firestore.collection(COLLECTION_NAME_USER_TABLE).document(DOC_ID_USER).collection(COLLECTION_NAME_EXERCISE_TABLE).document(docId).set(table).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            getActivity().onBackPressed();
                        }
                    });
//                    firestore.collection(COLLECTION_NAME_USER_TABLE).document(DOC_ID_USER).collection(COLLECTION_NAME_EXERCISE_TABLE).add(table).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//                        @Override
//                        public void onComplete(@NonNull @NotNull Task<DocumentReference> task) {
//                            if (task.isSuccessful()) {
//                                Bundle bundle = new Bundle();
//                                bundle.putString("DocId", DOC_ID_USER);
//                                NavDirections directions = AdminAddUserProgramFragmentDirections.gotoAdminUserAddProgram();
//                                Navigation.findNavController(view).navigate(directions.getActionId(), bundle);
//
//                            }
//                        }
//                    });

                }
                Log.d(getTag(), "onClickUser: " + table);
            }
        });


    }

    public int getZone() {
        ArrayList<String> zonecount = new ArrayList<>();
        for (String st : table.getExerciseZone()) {
            if (!zonecount.contains(st)) {
                zonecount.add(st);
            }
        }
        return zonecount.size();
    }

    private String getExerciseText() {
        String getText;
        getText = getZone() + " zone " + table.getExerciseName().size() + " exercise";
        return getText;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}