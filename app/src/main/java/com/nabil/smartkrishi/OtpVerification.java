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
import androidx.appcompat.app.AppCompatActivity;

import com.nabil.smartkrishi.data.local.AppDatabase;
import com.nabil.smartkrishi.data.remote.RetrofitClient;
import com.nabil.smartkrishi.data.repository.UserRepository;
import com.nabil.smartkrishi.domain.model.User;

public class OtpVerification extends AppCompatActivity {

    EditText otp1, otp2, otp3, otp4, otp5, otp6;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_otp_verification);

        otp1 = findViewById(R.id.otp1);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);
        otp5 = findViewById(R.id.otp5);
        otp6 = findViewById(R.id.otp6);
        loginBtn = findViewById(R.id.btnLogin);

        // =================== Add text change listener in each edit text field ============================
        otp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 1){
                    otp2.requestFocus();
                }
            }
        });

        otp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 1){
                    otp3.requestFocus();
                } else if(s.length() == 0){
                    otp1.requestFocus();
                }
            }
        });

        otp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 1){
                    otp4.requestFocus();
                } else if(s.length() == 0){
                    otp2.requestFocus();
                }
            }
        });

        otp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 1){
                    otp5.requestFocus();
                } else if(s.length() == 0){
                    otp3.requestFocus();
                }
            }
        });

        otp5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 1){
                    otp6.requestFocus();
                } else if(s.length() == 0){
                    otp4.requestFocus();
                }
            }
        });

        otp6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                 if(s.length() == 0){
                    otp5.requestFocus();
                 }
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!otp6.getText().toString().isEmpty()){
                    String otpCode = otp1.getText().toString() + otp2.getText().toString() + otp3.getText().toString() + otp4.getText().toString() + otp5.getText().toString() + otp6.getText().toString();
                    if(!otpCode.isEmpty()){
                        verifyAndProceed();
                    } else {
                        Toast.makeText(OtpVerification.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(OtpVerification.this, "Please enter complete OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void verifyAndProceed() {
        boolean isSignup = getIntent().getBooleanExtra("is_signup", false);
        if (isSignup) {
            String firstName = getIntent().getStringExtra("first_name");
            String lastName = getIntent().getStringExtra("last_name");
            String phone = getIntent().getStringExtra("phone");
            String role = getIntent().getStringExtra("role");
            int villageId = getIntent().getIntExtra("village_id", 1);

            UserRepository userRepository =
                new UserRepository(
                    AppDatabase.Companion.getInstance(getApplicationContext()).userDao(),
                    RetrofitClient.INSTANCE.getApiService()
                );

            new Thread(() -> {
                try {
                    User user = userRepository.registerUserSync(
                        firstName, lastName, phone, role, villageId, null
                    );
                    if (user != null) {
                        runOnUiThread(() -> sendToMain());
                    } else {
                        runOnUiThread(() -> Toast.makeText(OtpVerification.this, "Registration failed. Please check your details or connection.", Toast.LENGTH_LONG).show());
                    }
                } catch (Exception e) {
                    Log.e("OtpVerification", "Registration API request failed", e);
                    runOnUiThread(() -> Toast.makeText(OtpVerification.this, "Registration failed: " + e.getMessage(), Toast.LENGTH_LONG).show());
                }
            }).start();
        } else {
            sendToMain();
        }
    }

    private void sendToMain() {
        Intent intent = new Intent(OtpVerification.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
