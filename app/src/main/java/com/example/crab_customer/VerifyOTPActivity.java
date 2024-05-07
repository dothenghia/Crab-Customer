package com.example.crab_customer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class VerifyOTPActivity extends AppCompatActivity {

    String phoneNumber;
    Long timeoutSeconds = 60L;
    String verificationCode;
    EditText otpInput;
    ImageView backBtn;
    TextView phoneNumberTextView, resendOtpTextView;
    Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifyotp);
        FirebaseApp.initializeApp(this);
        backBtn = findViewById(R.id.otp_backbtn);
        phoneNumberTextView = findViewById(R.id.otp_subtitle);
        otpInput = findViewById(R.id.otp_phone_number);
        resendOtpTextView = findViewById(R.id.otp_resend);
        submitBtn = findViewById(R.id.otp_submit);

        phoneNumber = getIntent().getExtras().getString("phone");
        phoneNumberTextView.setText(phoneNumber);
        sendOtp(phoneNumber,false);

        submitBtn.setOnClickListener(v -> {
            String otp = otpInput.getText().toString().trim();
            if (!otp.isEmpty()) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otp);
                signInWithPhoneAuthCredential(credential);
            } else {
                Toast.makeText(VerifyOTPActivity.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
            }
        });

        resendOtpTextView.setOnClickListener((v)->{
            sendOtp(phoneNumber,true);
        });
        backBtn.setOnClickListener(v -> finish());

    }
    void sendOtp(String phoneNumber, boolean isResend) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber, // Phone number to verify
                60,          // Timeout duration
                TimeUnit.SECONDS, // Unit of timeout
                this,        // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential credential) {
                        signInWithPhoneAuthCredential(credential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Toast.makeText(VerifyOTPActivity.this, "Verification failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                        VerifyOTPActivity.this.verificationCode = verificationId;
                        if (isResend) {
                            startResendTimer();
                        }
                    }
                });
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

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        Log.d("signIn", "signInWithCredential:success");
                        FirebaseUser user = task.getResult().getUser();
                        saveLoginState(true);
                        Intent intent = new Intent(VerifyOTPActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        Log.w("signIn", "signInWithCredential:failure", task.getException());
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {

                            Toast.makeText(VerifyOTPActivity.this, "Invalid verification code.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void saveLoginState(boolean isLoggedIn) {
        SharedPreferences sharedPreferences = getSharedPreferences("loginState", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", isLoggedIn);
        editor.apply();
    }

}
