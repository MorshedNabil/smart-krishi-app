package com.nabil.smartkrishi

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.nabil.smartkrishi.features.profile.ProfileActivity
import com.nabil.smartkrishi.ui.theme.SmartKrishiTheme

class OnBoardActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartKrishiTheme {
                OnBoardScreen(
                    onProfileClick = {
                        val intent = Intent(this@OnBoardActivity, ProfileActivity::class.java)
                        startActivity(intent)
                    }
                )
            }
        }
    }
}
