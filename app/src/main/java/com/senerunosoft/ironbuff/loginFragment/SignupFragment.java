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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.senerunosoft.ironbuff.R;
import com.senerunosoft.ironbuff.activity.MainMenuActivity;
import com.senerunosoft.ironbuff.databinding.FragmentSignupBinding;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class SignupFragment extends Fragment {
    FirebaseAuth auth;
    FirebaseFirestore firestore;
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

    private void signupButton() {
        binding.signupRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                registerButtonProcess();

            }
        });
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

    private void tanimla(@NotNull View view) {
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    private void registerButtonProcess() {


        String namesurname_text, username_text, email_text, password_text, age_text, weight_text, sex_text,uri_text,height_text;
        if (checkField()) {
            namesurname_text = binding.signupNamesurname.getText().toString();
            username_text = binding.signupUsername.getText().toString();
            email_text = binding.signupEmail.getText().toString();
            password_text = binding.signupPassword.getText().toString();
            age_text = binding.signupAge.getText().toString();
            weight_text = binding.signupWeight.getText().toString();
            height_text = binding.signupHeight.getText().toString();

            sex_text = getSex();
            uri_text = "gs://iron-buff-b521e.appspot.com/image/"+username_text+"/+"+"profilimage";
            binding.profilSexRadiogroup.getCheckedRadioButtonId();


            auth.createUserWithEmailAndPassword(email_text, password_text).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    HashMap<String, Object> userData = new HashMap<>();
                    userData.put("NameSurname", namesurname_text);
                    userData.put("UserName", username_text);
                    userData.put("E-mail", email_text);
                    userData.put("Age", age_text);
                    userData.put("Weight", weight_text);
                    userData.put("Sex", sex_text);
                    userData.put("Height",height_text);
                    userData.put("imageUrl",uri_text);
                    firestore.collection("userTable").add(userData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(getContext(), "başarılı", Toast.LENGTH_SHORT).show();
                            auth.signInWithEmailAndPassword(email_text,password_text).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Intent intent = new Intent(getActivity(), MainMenuActivity.class);
                                    startActivity(intent);
                                    finishActivity();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @NotNull Exception e) {

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
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }

    }

    private String getSex() {

        int i = binding.profilSexRadiogroup.getCheckedRadioButtonId();
        RadioButton radioButton = binding.getRoot().findViewById(i);

        return radioButton.getText().toString();

    }

    private boolean checkField() {
        boolean is_namesurname, is_username, is_email, is_password, is_age, is_weight, is_sex,is_height;
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