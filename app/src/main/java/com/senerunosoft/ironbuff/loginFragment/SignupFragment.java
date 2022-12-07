package com.senerunosoft.ironbuff.loginFragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.senerunosoft.ironbuff.R;
import com.senerunosoft.ironbuff.activity.MainMenuActivity;
import com.senerunosoft.ironbuff.databinding.FragmentSignupBinding;
import com.senerunosoft.ironbuff.table.UserTable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class SignupFragment extends Fragment {
    private static final String COLLECTION_NAME_USER = "userTable";
    UserTable user = new UserTable();
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    FirebaseStorage storage;
    FragmentSignupBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignupBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tanimla(view);
        signupButton();


    }


    private void finishActivity() {
        //fragment sonlandırma
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void signupButton() {
        binding.signupRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkField()) {
                    Uri uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/iron-buff-b521e.appspot.com/o/ironbuff%2Fprofil.png?alt=media&token=118a4b1d-59c7-4298-9f9e-c51082596b4d");
                    user.setNameSurname(binding.signupNamesurname.getText().toString());
                    user.setUserName(binding.signupUsername.getText().toString());
                    user.setE_mail(binding.signupEmail.getText().toString());
                    user.setAge(binding.signupAge.getText().toString());
                    user.setWeight(binding.signupWeight.getText().toString());
                    user.setHeight(binding.signupHeight.getText().toString());
                    user.setSex(getSex());
                    user.setImageUrl(uri.toString());
                    user.setAdmin(false);
//                    registerButtonProcess(uri);
                    registerButtonProcessV2();
                }


            }
        });
    }

    private void uploadimage() {
        /*
        String url = "image/" + user.getUserName() + "/profilimage";
        StorageReference reference = storage.getReference().child(url);

        UploadTask uploadTask = reference.putFile(Uri.parse("android.resource://com.senerunosoft.ironbuff/" + R.drawable.profil));
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                StorageReference newRef = storage.getReference(url);
                newRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        registerButtonProcess(uri);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                    }
                });

            }
        });*/
    }

    private void tanimla(@NotNull View view) {
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }
    private void registerButtonProcessV2(){
        auth.createUserWithEmailAndPassword(user.getE_mail(),binding.signupPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                signin();
                user.setDocId(authResult.getUser().getUid());
                userDetailAddFirestore();
                Intent intent = new Intent(getActivity(), MainMenuActivity.class);
                startActivity(intent);
                finishActivity();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(getContext(), "Error: "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void signin(){
        auth.signInWithEmailAndPassword(user.getE_mail(),binding.signupPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {

            }
        });
    }
    private void userDetailAddFirestore(){
        firestore.collection(COLLECTION_NAME_USER).document(user.getDocId()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        });
    }


    private void registerButtonProcess(Uri uri) {


        auth.createUserWithEmailAndPassword(binding.signupEmail.getText().toString(),
                binding.signupPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                HashMap<String, Object> userData = new HashMap<>();
                userData.put("NameSurname", user.getNameSurname());
                userData.put("UserName", user.getUserName());
                userData.put("E-mail", user.getE_mail());
                userData.put("Age", user.getAge());
                userData.put("Weight", user.getWeight());
                userData.put("Sex", user.getSex());
                userData.put("Height", user.getHeight());
                userData.put("imageUrl", uri); user.setImageUrl(uri.toString());

                auth.signInWithEmailAndPassword(user.getE_mail(), binding.signupPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        firestore.collection(COLLECTION_NAME_USER).document(authResult.getUser().getUid()).set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        Intent intent = new Intent(getActivity(), MainMenuActivity.class);
                        startActivity(intent);
                        finishActivity();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    private String getSex() {

        int i = binding.profilSexRadiogroup.getCheckedRadioButtonId();
        RadioButton radioButton = binding.getRoot().findViewById(i);

        return radioButton.getText().toString();

    }

    private boolean checkField() {
        boolean is_namesurname, is_username, is_email, is_password, is_age, is_weight, is_sex, is_height;
        is_namesurname = binding.signupNamesurname.getText().toString().isEmpty();
        is_username = binding.signupUsername.getText().toString().isEmpty();
        is_email = binding.signupEmail.getText().toString().isEmpty();
        is_password = binding.signupPassword.getText().toString().isEmpty();
        is_age = binding.signupAge.getText().toString().isEmpty();
        is_weight = binding.signupWeight.getText().toString().isEmpty();
        is_height = binding.signupHeight.getText().toString().isEmpty();
        is_sex = getSex().isEmpty();

        if (is_username && is_namesurname && is_email && is_password && is_age && is_weight && is_sex && is_height)
            return false;
        else
            return true;
    }
}