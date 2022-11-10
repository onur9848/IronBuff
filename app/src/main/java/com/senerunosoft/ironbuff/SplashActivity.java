package com.senerunosoft.ironbuff;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {
    RelativeLayout rellay1;
    Button login_button, signup_button, forgetpassword_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tanimla();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                rellay1.setVisibility(View.VISIBLE);

            }
        }, 3000);
        buttonProcess();
    }

    private void buttonProcess() {
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        forgetpassword_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    private void tanimla() {
        rellay1 = findViewById(R.id.rellay1);
        login_button= findViewById(R.id.login_button);
        signup_button = findViewById(R.id.signup_button);
        forgetpassword_button = findViewById(R.id.forget_password_button);
    }
}