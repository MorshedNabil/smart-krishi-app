package com.nabil.smartkrishi;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

public class OtpVerification extends AppCompatActivity {

    EditText otp1, otp2, otp3, otp4;
    Button loginBtn;
    private FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_otp_verification);

        otp1 = findViewById(R.id.otp1);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);
        loginBtn = findViewById(R.id.btnLogin);
        fAuth = FirebaseAuth.getInstance();

        String myOtp = getIntent().getStringExtra("verification_pin");

        // =================== Add text change listener in each edit text field ============================
        // The cursor will automatically move to the next field when a number is entered
        otp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 1){
                    otp2.requestFocus();
                }

            }
        });

        otp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 1){
                    otp3.requestFocus();
                }
                else if(s.length() == 0){
                    // when user removing the number from the field go to the previous field
                    otp1.requestFocus();

                }

            }
        });

        otp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 1){
                    otp4.requestFocus();
                    //Log.d("UserOtp", OTP + "");
                }
                else if(s.length() == 0){
                    // when user removing the number from the field go to the previous field
                    otp2.requestFocus();
                }
            }
        });

        otp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                 if(s.length() == 0){
                     // when user removing the number from the field go to the previous field
                    otp3.requestFocus();
                 }
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!otp4.getText().toString().isEmpty()){
                    String OTP = "";
                    OTP = otp1.getText().toString() + otp2.getText().toString() + otp3.getText().toString() + otp4.getText().toString();

                    // OTP matching code
                    if(!OTP.isEmpty()){
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(OTP, myOtp);
                        logIn(credential);
                    }
                    else{
                        Toast.makeText(OtpVerification.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    // =========== onStart() uses when user is logged in previously =====================
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = fAuth.getCurrentUser();
        if(currentUser != null){
            sendToMain(); // if user is logged in then directly go to Home screen;
        }

    }
    private void logIn(PhoneAuthCredential credential) {
        fAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    sendToMain();
                }
                else{
                    Toast.makeText(OtpVerification.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendToMain() {
        Intent intent = new Intent(OtpVerification.this, OnBoardActivity.class);
        startActivity(intent);
        finish();
    }


}