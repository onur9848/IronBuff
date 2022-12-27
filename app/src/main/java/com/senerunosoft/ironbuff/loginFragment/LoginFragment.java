package com.senerunosoft.ironbuff.loginFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.senerunosoft.ironbuff.activity.MainMenuActivity;
import com.senerunosoft.ironbuff.R;
import com.senerunosoft.ironbuff.databinding.FragmentLoginBinding;
import org.jetbrains.annotations.NotNull;

public class LoginFragment extends Fragment {

    FragmentLoginBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore firestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
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
        binding.forgetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections action = LoginFragmentDirections.gotoForgetpassword();
                Navigation.findNavController(view).navigate(action);
            }
        });

        //Giriş yap butonu
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailtext, passwordtext;
                emailtext = binding.loginEmail.getText().toString();
                passwordtext = binding.loginPassword.getText().toString();
                if (emailtext.isEmpty() || passwordtext.isEmpty()) {
                    Toast.makeText(getContext(), "Lütfen E-mail ve Şifreyi giriniz.", Toast.LENGTH_SHORT).show();
                } else {
                    auth.signInWithEmailAndPassword(emailtext, passwordtext).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
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
                            Log.d(getTag(), "onFailure: "+e.getLocalizedMessage());
                        }
                    });
                }

            }
        });

        //kayıt ol butonu
        binding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections action = LoginFragmentDirections.gotoSignup();
                Navigation.findNavController(view).navigate(action);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}