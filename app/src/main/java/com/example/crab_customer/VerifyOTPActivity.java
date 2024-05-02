package com.example.crab_customer;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class VerifyOTPActivity extends AppCompatActivity {

    String phoneNumber;
    Long timeoutSeconds = 60L;
    String verificationCode;
    EditText otpInput;
    ImageView backBtn;
    TextView phoneNumberTextView, resendOtpTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifyotp);

        backBtn = findViewById(R.id.otp_backbtn);
        phoneNumberTextView = findViewById(R.id.otp_subtitle);
        otpInput = findViewById(R.id.otp_phone_number);
        resendOtpTextView = findViewById(R.id.otp_resend);

        phoneNumber = getIntent().getExtras().getString("phone");
        phoneNumberTextView.setText(phoneNumber);
        sendOtp(phoneNumber,false);

        /*resendOtpTextView.setOnClickListener((v)->{
            sendOtp(phoneNumber,true);
        });*/
        backBtn.setOnClickListener(v -> finish());

    }
    void sendOtp(String phoneNumber,boolean isResend){
        startResendTimer();
    }
    void startResendTimer(){
        resendOtpTextView.setEnabled(false);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeoutSeconds--;
                resendOtpTextView.setText("Resend OTP in "+timeoutSeconds +"s");
                if(timeoutSeconds<=0){
                    timeoutSeconds =60L;
                    timer.cancel();
                    runOnUiThread(() -> {
                        resendOtpTextView.setEnabled(true);
                    });
                }
            }
        },0,1000);
    }
}
