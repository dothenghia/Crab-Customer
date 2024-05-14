package com.example.crab_customer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

public class IntroActivity extends AppCompatActivity {
    Button loginBtn, registerBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);
        SharedPreferences sharedPreferences = getSharedPreferences("loginState", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        if (isLoggedIn) {
            Intent intent = new Intent(IntroActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            setContentView(R.layout.activity_intro);

            loginBtn = findViewById(R.id.intro_btn_login);
            loginBtn.setOnClickListener(v -> {
                Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
                startActivity(intent);

            });
            registerBtn = findViewById(R.id.intro_btn_register);
            registerBtn.setOnClickListener(v -> {
                Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
                startActivity(intent);
            });
        }
    }
}
