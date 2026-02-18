package com.nabil.smartkrishi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    TextView errorMsg;
    EditText inputPhone;
    SwitchCompat langSwitch;
    Button signupButton, getOtpButton;
    private FirebaseAuth Auth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        getOtpButton = findViewById(R.id.btn_get_otp);
        signupButton = findViewById(R.id.btn_signup_link);
        langSwitch = findViewById(R.id.lang_switch);
        inputPhone = findViewById(R.id.input_phone_login);
        errorMsg = findViewById(R.id.tv_error_message);
        // Initialize Firebase Auth
        Auth = FirebaseAuth.getInstance();

        // Set an OnClickListener on the button 'get otp'
        getOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phone = inputPhone.getText().toString().trim();
                String phoneNumber = "+880" + phone;

                if(!phone.isEmpty()){
                    PhoneAuthOptions options = PhoneAuthOptions.newBuilder(Auth)
                            .setPhoneNumber(phoneNumber)
                            .setTimeout(60L, TimeUnit.SECONDS)
                            .setActivity(MainActivity.this)
                            .setCallbacks(mCallBacks)
                            .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);
                }
                else{
                    inputPhone.setError("Please enter phone number");
                    inputPhone.requestFocus();
                    return;
                }
            }
        });

        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signIn(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                errorMsg.setVisibility(View.VISIBLE);
                errorMsg.setText(e.getMessage());

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                // Sometimes the code doesn't detect automatically
                // Call the function to navigate to the next activity
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent OTPintent = new Intent(MainActivity.this, OtpVerification.class);
                        OTPintent.putExtra("verification_pin", s);
                        startActivity(OTPintent);
                    }
                }, 10000);
            }
        };

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUp.class);
                startActivity(intent);
            }
        });

        langSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Handle the switch state change here
            if (isChecked) {
                        
            }

        });
    }
    // ========================= Sign In ======================================
    private void signIn(PhoneAuthCredential credential) {
        Auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        sendToMain();
                    }
                    else{
                        errorMsg.setVisibility(View.VISIBLE);
                        errorMsg.setText(task.getException().getMessage());
                    }
            }
        });
    }
    // ============================================================

    // =========== onStart() uses when user is logged in previously =====================
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = Auth.getCurrentUser();

        if(currentUser != null){
            sendToMain();
        }
    }

    private void sendToMain() {
        Intent intent = new Intent(MainActivity.this, OnBoardActivity.class);
        startActivity(intent);
        finish();
    }

}