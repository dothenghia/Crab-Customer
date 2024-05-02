package com.example.crab_customer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {
    Button loginBtn, registerBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        loginBtn = findViewById(R.id.intro_btn_login);
        loginBtn.setOnClickListener(v -> {
            Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
            startActivity(intent);

        });
        registerBtn = findViewById(R.id.intro_btn_register);
        registerBtn.setOnClickListener(v -> {
            Intent intent = new Intent(IntroActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
