package com.nabil.smartkrishi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        Button getOtpButton = findViewById(R.id.btn_get_otp);
        Button signupButton = findViewById(R.id.btn_signup_link);
        SwitchCompat langSwitch = findViewById(R.id.lang_switch);

        // Set an OnClickListener on the button
        getOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the function to navigate to the next activity
                openOtpVerificationActivity();
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 3. Call the function to navigate to the Signup activity
                openSignupActivity();
            }
        });

        langSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Handle the switch state change here
            if (isChecked) {
                        
            }

        });
    }

    /**
     * Navigates from MainActivity to the OTP verification activity.
     */
    private void openOtpVerificationActivity() {
        // Create an Intent to start the new activity
        // Replace OtpVerificationActivity.class with the actual name of your next activity
        Intent intent = new Intent(MainActivity.this, OtpVerification.class);
        startActivity(intent);
    }

    /**
     * Creates an Intent to navigate from the current activity to SignupActivity.
     */
    private void openSignupActivity() {
        // Create an Intent to specify which activity to start
        Intent intent = new Intent(MainActivity.this, SignUp.class);
        startActivity(intent);
    }

}