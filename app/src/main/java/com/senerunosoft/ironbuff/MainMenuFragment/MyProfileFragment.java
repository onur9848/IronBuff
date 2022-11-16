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
import com.senerunosoft.ironbuff.databinding.FragmentMyProfileBinding;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;


public class MyProfileFragment extends Fragment {

    FragmentMyProfileBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    FirebaseStorage storage;
    ActivityResultLauncher<Intent> activityResultLauncher;
    BottomSheetDialog bottomSheetDialog;

    Uri imageData;
    ActivityResultLauncher<String> permissionLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMyProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // Inflate the layout for this fragment
        return root;
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        bottomSheetDialog = new BottomSheetDialog(getContext());
        getDataUser();

        registerLauncher();


        binding.myProfilImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialog();

            }
        });

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

    LinearLayout openCamera, openGallery;

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

    @Override
    public void onResume() {
        getDataUser();
        super.onResume();
    }

    private void uploadImage() {


        String url = "image/"+binding.profilUsername.getText().toString()+"/profilimage";
        StorageReference storageRef = storage.getReference().child(url);


        UploadTask uploadTask = storageRef.putFile(imageData);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getContext(), "Başarılı", Toast.LENGTH_SHORT).show();
                binding.myProfilImage.setImageURI(imageData);

            }
        });
    }




    private void getDataUser() {
        firestore.collection("userTable").whereEqualTo("E-mail", auth.getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        binding.profilNamesurname.setText(doc.getData().get("NameSurname").toString());
                        binding.profilUsername.setText(doc.getData().get("UserName").toString());
                        binding.profilEmail.setText(doc.getData().get("E-mail").toString());
                        binding.profilAge.setText(doc.getData().get("Age").toString());
                        binding.profilSex.setText(doc.getData().get("Sex").toString());
                        binding.profilWeight.setText(doc.getData().get("Weight").toString());
                        binding.profilHeight.setText(doc.getData().get("Height").toString());
                        String url = doc.getData().get("imageUrl").toString();
                        Picasso.get().load(url).into(binding.myProfilImage);


                    }
                }
            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}