package com.nabil.smartkrishi.features.profile

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.nabil.smartkrishi.ui.theme.SmartKrishiTheme

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartKrishiTheme {
                ProfileScreen(
                    onBackClick = {
                        finish() // This will close the activity.
                    },
                    onSaveClick = { uiState ->
                        Toast.makeText(
                            this,
                            "Profile saved for ${uiState.name} (${uiState.phoneNumber}) - ${uiState.selectedLocation}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }
        }
    }
}
