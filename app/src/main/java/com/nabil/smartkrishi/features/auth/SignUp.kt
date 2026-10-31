package com.nabil.smartkrishi.features.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ComposeView
import com.google.firebase.auth.FirebaseAuth
import com.nabil.smartkrishi.OtpVerification
import com.nabil.smartkrishi.R
import com.nabil.smartkrishi.ui.theme.SmartKrishiTheme

class SignUp : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mAuth = FirebaseAuth.getInstance()

        val composeView = findViewById<ComposeView>(R.id.compose_view)
        composeView.setContent {
            SmartKrishiTheme {
                SignUpScreen(
                    onSignUpClick = { formData ->
                        handleSignUp(formData)
                    }
                )
            }
        }
    }

    private fun handleSignUp(formData: SignUpFormData) {
        if (formData.phone.isBlank()) {
            Toast.makeText(this, "Please enter Phone Number", Toast.LENGTH_SHORT).show()
            return
        }
        if (formData.nidNumber.isBlank()) {
            Toast.makeText(this, "Please enter NID number", Toast.LENGTH_SHORT).show()
            return
        }
        if (formData.division.isBlank()) {
            Toast.makeText(this, "Please Select your Division", Toast.LENGTH_SHORT).show()
            return
        }
        if (formData.district.isBlank()) {
            Toast.makeText(this, "Please Select your District", Toast.LENGTH_SHORT).show()
            return
        }
        if (formData.villageName.isBlank()) {
            Toast.makeText(this, "Please enter Village name", Toast.LENGTH_SHORT).show()
            return
        }

        Toast.makeText(this, "Submitting Registration...", Toast.LENGTH_SHORT).show()
        // Proceed with registration logic
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            sendToMain()
        }
    }

    private fun sendToMain() {
        val intent = Intent(this, OtpVerification::class.java)
        startActivity(intent)
        finish()
    }
}
