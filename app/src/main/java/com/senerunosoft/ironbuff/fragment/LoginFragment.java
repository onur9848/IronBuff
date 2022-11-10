package com.senerunosoft.ironbuff.fragment;

import android.content.Intent;
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
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.senerunosoft.ironbuff.activity.MainMenuActivity;
import com.senerunosoft.ironbuff.R;
import org.jetbrains.annotations.NotNull;

public class LoginFragment extends Fragment {


    EditText email, password;
    FirebaseAuth auth;
    Button forget_password_button, login_button, signup_button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        forget_password_button = view.findViewById(R.id.forget_password_button);
        login_button = view.findViewById(R.id.login_button);
        signup_button = view.findViewById(R.id.signup_button);
        auth = FirebaseAuth.getInstance();

        email = view.findViewById(R.id.login_email);
        password = view.findViewById(R.id.login_password);

        buttonProcess();
    }

    private void finishActivity() {
        //fragment sonlandırma
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    private void buttonProcess() {

        //şifremi unuttum butonu
        forget_password_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections action = LoginFragmentDirections.gotoForgetpassword();
                Navigation.findNavController(view).navigate(action);
            }
        });

        //Giriş yap butonu
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailtext, passwordtext;
                emailtext = email.getText().toString();
                passwordtext = password.getText().toString();
                if (emailtext.isEmpty() || passwordtext.isEmpty()) {
                    Toast.makeText(getContext(), "Lütfen E-mail ve Şifreyi giriniz.", Toast.LENGTH_SHORT).show();
                } else {
                    auth.signInWithEmailAndPassword(emailtext,passwordtext).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
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

            }
        });

        //kayıt ol butonu
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections action = LoginFragmentDirections.gotoSignup();
                Navigation.findNavController(view).navigate(action);
            }
        });

    }
}