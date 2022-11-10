package com.senerunosoft.ironbuff.fragment;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.senerunosoft.ironbuff.R;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

public class SignupFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    EditText namesurname, username, email, password;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    Button registerbuton;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tanimla(view);
        registerButton();


    }

    private void tanimla(@NotNull View view) {
        namesurname = view.findViewById(R.id.signup_namesurname);
        username = view.findViewById(R.id.signup_username);
        email = view.findViewById(R.id.signup_email);
        password = view.findViewById(R.id.signup_password);
        registerbuton = view.findViewById(R.id.register_button);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    private void registerButton() {
        registerbuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namesurname_text, username_text, email_text, password_text;
                if (checkField()){
                    namesurname_text = namesurname.getText().toString();
                    username_text = username.getText().toString();
                    email_text = email.getText().toString();
                    password_text = password.getText().toString();
                    auth.createUserWithEmailAndPassword(email_text,password_text).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            HashMap<String, Object> userData = new HashMap<>();
                            userData.put("NameSurname",namesurname_text);
                            userData.put("UserName",username_text);
                            userData.put("E-mail",email_text);
                            firestore.collection("userTable").add(userData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(getContext(), "başarılı", Toast.LENGTH_SHORT).show();
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
        });
    }

    private boolean checkField() {
        boolean is_namesurname, is_username, is_email, is_password;
        is_namesurname = namesurname.getText().toString().isEmpty();
        is_username = username.getText().toString().isEmpty();
        is_email = email.getText().toString().isEmpty();
        is_password = password.getText().toString().isEmpty();

        if (is_username && is_namesurname && is_email && is_password)
            return false;
        else
            return true;


    }
}