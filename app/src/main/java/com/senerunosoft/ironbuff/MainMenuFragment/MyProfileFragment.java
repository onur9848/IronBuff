package com.senerunosoft.ironbuff.MainMenuFragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.senerunosoft.ironbuff.R;
import com.senerunosoft.ironbuff.activity.MainMenuActivity;
import com.senerunosoft.ironbuff.databinding.FragmentMyProfileBinding;
import com.senerunosoft.ironbuff.table.UserTable;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class MyProfileFragment extends Fragment {
    private static final String COLLECTION_NAME_USER = "userTable";
    public static final String USERNAME = "UserName";
    public static final String NAME_SURNAME = "NameSurname";
    public static final String SEX = "Sex";
    public static final String AGE = "Age";
    public static final String EMAIL = "E-mail";
    public static final String HEIGHT = "Height";
    public static final String WEIGHT = "Weight";
    public static final String IMAGE_URL = "imageUrl";

    FragmentMyProfileBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    FirebaseStorage storage;
    ActivityResultLauncher<Intent> activityResultLauncher;
    BottomSheetDialog bottomSheetDialog;

    Uri imageData;
    ActivityResultLauncher<String> permissionLauncher;
    LinearLayout openCamera, openGallery;
    List<UserTable> userTables;
    String imageUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        defVariable();
        registerLauncher();
        profilDataOnChanged();

        binding.myProfilImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialog();
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void defVariable() {
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        userTables = new ArrayList<>();
        bottomSheetDialog = new BottomSheetDialog(getContext());
        userTables = new ArrayList<>();
        imageUrl = "image/" + "username" + "/profilimage";

    }


    private void registerLauncher() {
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intentFromResult = result.getData();
                    if (intentFromResult != null) {
                        imageData = intentFromResult.getData();
                        uploadimage();
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

    private void uploadimage() {
        if (imageData != null) {

            String url = "image/" + binding.profilUsername.getText().toString() + "/" + auth.getCurrentUser().getUid();
            StorageReference reference = storage.getReference(url);
            UploadTask uploadTask = reference.putFile(imageData);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    StorageReference newRef = storage.getReference(url);
                    newRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downloadUrl = uri.toString();
                            firestore.collection(COLLECTION_NAME_USER).document(auth.getCurrentUser().getUid()).update(IMAGE_URL, uri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @NotNull Exception e) {
                                    Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });

                }
            });


        }
    }

    private void showBottomSheetDialog() {

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
                    Intent intenToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(intenToGallery);
                    bottomSheetDialog.hide();


                }
            }
        });


    }


    private void profilDataOnChanged() {
        firestore.collection(COLLECTION_NAME_USER).document(auth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(getContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    getProfilData();
                }
            }
        });

    }



    public void getProfilData() {
        firestore.collection(COLLECTION_NAME_USER).whereEqualTo(EMAIL, auth.getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
//                    Toast.makeText(getContext(), "2", Toast.LENGTH_SHORT).show();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
//                        Toast.makeText(getContext(), "for", Toast.LENGTH_SHORT).show();
                        UserTable user = new UserTable();
                        user.setNameSurname(doc.get(NAME_SURNAME).toString());
                        user.setUserName(doc.get(USERNAME).toString());
                        user.setAge(doc.get(AGE).toString());
                        user.setSex(doc.get(SEX).toString());
                        user.setHeight(doc.get(HEIGHT).toString());
                        user.setWeight(doc.get(WEIGHT).toString());
                        user.setE_mail(doc.get(EMAIL).toString());
                        user.setImageUrl(doc.get(IMAGE_URL).toString());
                        userTables.add(user);
                    }
                    setProfilData();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void setProfilData() {
        for (UserTable user : userTables) {
//            Toast.makeText(getContext(), "3", Toast.LENGTH_SHORT).show();
            binding.profilUsername.setText(user.getUserName());
            binding.profilNamesurname.setText(user.getNameSurname());
            binding.profilEmail.setText(user.getE_mail());
            binding.profilHeight.setText(user.getHeight());
            binding.profilWeight.setText(user.getWeight());
            binding.profilAge.setText(user.getAge());
            binding.profilSex.setText(user.getSex());


            Picasso.get().load(user.getImageUrl()).into(binding.myProfilImage);


        }
    }
}