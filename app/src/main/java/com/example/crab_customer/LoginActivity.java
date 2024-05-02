package com.example.crab_customer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.hbb20.CountryCodePicker;

public class LoginActivity extends AppCompatActivity {
    ImageView backBtn;
    CountryCodePicker countryCodePicker;
    EditText phoneInput;
    Button sendOtpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        backBtn = findViewById(R.id.login_backbtn);
        countryCodePicker = findViewById(R.id.login_country_code_picker);
        phoneInput = findViewById(R.id.login_phone_number);
        sendOtpBtn = findViewById(R.id.login_nextbtn);
        countryCodePicker.registerCarrierNumberEditText(phoneInput);
        backBtn.setOnClickListener(v -> finish());
        sendOtpBtn.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, VerifyOTPActivity.class);
            intent.putExtra("phone", countryCodePicker.getFullNumberWithPlus());
            startActivity(intent);
        });
    }
}
