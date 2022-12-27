package com.senerunosoft.ironbuff.AdminPage;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.*;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.*;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.senerunosoft.ironbuff.R;
import com.senerunosoft.ironbuff.databinding.FragmentAdminAddTrainingBinding;
import com.senerunosoft.ironbuff.table.ExerciseTable;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class AdminAddTrainingFragment extends Fragment {
    private static final String COLLECTION_EXERCISE_TABLE = "exerciseTable";
    private static final String DOCUMENT_IN_EXERCISE_TABLE = "exercise";

    FragmentAdminAddTrainingBinding binding;
    ArrayList<String> exerciseZone, exerciseFreeWeight, exerciseMachine;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> permissionLauncher;
    BottomSheetDialog bottomSheetDialog;
    Map<String, Object> map;
    FirebaseFirestore firestore;
    Uri imageData;
    ExerciseTable exercise;
    FirebaseStorage storage;
    int i = 0;

    LinearLayout openCamera, openGallery;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAdminAddTrainingBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firestore = FirebaseFirestore.getInstance();
        exerciseZone = new ArrayList<>();
        exerciseMachine = new ArrayList<>();
        exerciseFreeWeight = new ArrayList<>();
        exercise = new ExerciseTable();
        bottomSheetDialog = new BottomSheetDialog(getContext());
        storage = FirebaseStorage.getInstance();

        getImage();
        changeFirebaseTable();
        selectCheckButton();
        saveExercise();
    }

    private void registerLauncher() {
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intentFromResult = result.getData();
                    if (intentFromResult != null) {
                        imageData = intentFromResult.getData();
                        uploadImage();
                        bottomSheetDialog.hide();

                    }
                }
            }
        });
        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if (result) {
                    Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(intentToGallery);
                } else {
                    Toast.makeText(getContext(), "Permission Needed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getImage() {
        registerLauncher();
        binding.addTrainingImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDiaolog();
                i = 1;
            }
        });
        binding.addTrainingImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDiaolog();
                i = 2;
            }
        });
    }

    private void showDiaolog() {
        bottomSheetDialog.setContentView(R.layout.image_uploaded_bottom_sheet_diaolog);
        openCamera = bottomSheetDialog.findViewById(R.id.open_camera);
        openGallery = bottomSheetDialog.findViewById(R.id.open_gallery);
        bottomSheetDialog.show();
        clickAction();
    }

    private void clickAction() {
        openGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.cancel();
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        Snackbar.make(view, "Needed permission for gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                            }
                        }).show();
                    } else {
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                } else {
                    bottomSheetDialog.hide();
                    Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(intentToGallery);

                }
            }
        });


    }

    private void uploadImage() {
        String uuid = UUID.randomUUID().toString();
        String imageUrl = "exerciseImg/" + uuid.toUpperCase(Locale.ROOT).substring(0, 10).replace("-", "");
        if (imageData != null) {
            if (i == 1) {
                imageUrl += "/img1";
                exercise.setExerciseImg1(imageData.toString());
                UploadTask img1Task = storage.getReference(imageUrl).putFile(imageData);
                String finalImageUrl = imageUrl;
                img1Task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        StorageReference newReg = storage.getReference(finalImageUrl);
                        newReg.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.get().load(uri).into(binding.addTrainingImage1);
                                exercise.setExerciseImg1(uri.toString());
                            }
                        });
                    }
                });
            } else {
                imageUrl += "/img2";
                exercise.setExerciseImg2(imageData.toString());
                UploadTask img2Task = storage.getReference(imageUrl).putFile(imageData);
                String finalImageUrl1 = imageUrl;
                img2Task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        StorageReference newRef = storage.getReference(finalImageUrl1);
                        newRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.get().load(uri).into(binding.addTrainingImage2);
                                exercise.setExerciseImg2(uri.toString());
                            }
                        });
                    }
                });
            }


        }
    }

    private void saveExercise() {
        binding.addTrainingSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exercise.setExerciseName(binding.addExerciseName.getText().toString());
                exercise.setExerciseTarget(binding.exerciseZoneSpinner.getSelectedItem().toString());
                exercise.setExerciseDetail(binding.addExerciseDetail.getText().toString());
                getWeightType();
                addNewExercise();

            }
        });


    }

    private void addNewExercise() {

        DocumentReference docref = firestore.collection(COLLECTION_EXERCISE_TABLE).document(DOCUMENT_IN_EXERCISE_TABLE);

        docref.collection(exercise.getExerciseTarget()).add(exercise).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentReference> task) {

            }
        });

    }

    private void getWeightType() {
        RadioButton button = binding.freeOrMachineRadiogroup.findViewById(binding.freeOrMachineRadiogroup.getCheckedRadioButtonId());
        exercise.setExerciseWeightType(button.getText().toString());
        if (binding.selectedFreeWeight.getVisibility() == View.VISIBLE) {
            exercise.setExerciseWeightName(binding.freeWeightSpinner.getSelectedItem().toString());
        } else {
            exercise.setExerciseWeightName(binding.machineSpinner.getSelectedItem().toString());
        }
    }

    private void changeFirebaseTable() {
        firestore.collection(COLLECTION_EXERCISE_TABLE).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                getSpinners();
            }
        });
    }

    private void getSpinners() {
        firestore.collection(COLLECTION_EXERCISE_TABLE).document(DOCUMENT_IN_EXERCISE_TABLE).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    map = task.getResult().getData();
                    if (map != null) {
                        exerciseZone = (ArrayList<String>) map.get("exerciseZone");
                        exerciseMachine = (ArrayList<String>) map.get("exerciseMachine");
                        exerciseFreeWeight = (ArrayList<String>) map.get("exerciseFreeWeight");

                        ArrayAdapter<String> zoneAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, exerciseZone);
                        binding.exerciseZoneSpinner.setAdapter(zoneAdapter);

                        ArrayAdapter<String> machineAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, exerciseMachine);
                        binding.machineSpinner.setAdapter(machineAdapter);

                        ArrayAdapter<String> freeWeightAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, exerciseFreeWeight);
                        binding.freeWeightSpinner.setAdapter(freeWeightAdapter);
                        addItemWeightType();
                    }

                }
            }
        });
    }

    private void addItemWeightType() {
        binding.addFreeWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = new EditText(getContext());
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Yeni Ağırlık gir");
                builder.setView(editText);
                builder.setNegativeButton("İptal", null);
                builder.setPositiveButton("Kaydet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String freeWeight = editText.getText().toString();
                        Toast.makeText(getContext(), freeWeight, Toast.LENGTH_SHORT).show();
                        exerciseFreeWeight.add(freeWeight);
                        updateFreeWeight(exerciseFreeWeight);
                    }
                });
                builder.show();
            }
        });
        binding.addMachineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = new EditText(getContext());
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Yeni Ağırlık gir");
                builder.setView(editText);
                builder.setNegativeButton("İptal", null);
                builder.setPositiveButton("Kaydet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newMachine = editText.getText().toString();
                        Toast.makeText(getContext(), newMachine, Toast.LENGTH_SHORT).show();
                        exerciseMachine.add(newMachine);
                        updateMachine(exerciseMachine);
                    }
                });
                builder.show();


            }
        });


    }

    private void updateFreeWeight(ArrayList<String> freeWeight) {

        firestore.collection(COLLECTION_EXERCISE_TABLE).document(DOCUMENT_IN_EXERCISE_TABLE).update("exerciseFreeWeight", freeWeight).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
            }
        });
    }

    private void updateMachine(ArrayList<String> machineList) {
        firestore.collection(COLLECTION_EXERCISE_TABLE).document(DOCUMENT_IN_EXERCISE_TABLE).update("exerciseMachine", machineList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
            }
        });
    }

    private void selectCheckButton() {
        binding.freeOrMachineRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (binding.freeWeight.getId() == i) {
                    Log.d(getTag(), "onCheckedChanged: Free Weight");
                    binding.selectedMachine.setVisibility(View.GONE);
                    binding.selectedFreeWeight.setVisibility(View.VISIBLE);

                } else {
                    Log.d(getTag(), "onCheckedChanged: Machine");
                    binding.selectedFreeWeight.setVisibility(View.GONE);
                    binding.selectedMachine.setVisibility(View.VISIBLE);
                }
            }
        });
    } //Show Edittext by Weight Type

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}