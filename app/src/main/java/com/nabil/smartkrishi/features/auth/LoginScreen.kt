package com.nabil.smartkrishi.features.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nabil.smartkrishi.R

private val LoginBackgroundColor = Color(0xFF2E5A42)
private val InputFieldBgColor = Color(0xFFC2D16C)
private val ButtonBgColor = Color(0xFFC5D17C)

/**
 * Top Header Card with rounded bottom corners and logo for Login Screen.
 */
@Composable
fun LoginHeader(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(320.dp),
        color = Color.White,
        shape = RoundedCornerShape(bottomStart = 60.dp, bottomEnd = 60.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.smart_krishi_logo),
                contentDescription = "Smart Krishi Logo",
                modifier = Modifier.size(220.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}

/**
 * Custom Language Switch Component ("EN" / "BN").
 */
@Composable
fun LanguageSwitch(
    isEnglish: Boolean,
    onLanguageToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = if (isEnglish) "EN" else "BN",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(end = 6.dp)
        )
        Switch(
            checked = isEnglish,
            onCheckedChange = onLanguageToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = ButtonBgColor,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.Gray
            )
        )
    }
}

/**
 * Phone Number Input Field Component for Login.
 */
@Composable
fun LoginPhoneInputField(
    phoneValue: String,
    onPhoneChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "Enter phone number"
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(InputFieldBgColor, shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Country Code Prefix
        Text(
            text = "+880 ⌵",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .height(28.dp)
                .width(1.dp)
                .background(Color.White)
        )
        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            contentAlignment = Alignment.CenterStart
        ) {
            if (phoneValue.isEmpty()) {
                Text(
                    text = hint,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 16.sp
                )
            }
            BasicTextField(
                value = phoneValue,
                onValueChange = onPhoneChange,
                singleLine = true,
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                cursorBrush = SolidColor(Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = hint }
            )
        }
    }
}

/**
 * Get OTP Action Button.
 */
@Composable
fun GetOtpButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = ButtonBgColor),
        shape = RoundedCornerShape(28.dp),
        modifier = modifier
            .testTag("btn_get_otp")
            .width(160.dp)
            .height(55.dp)
    ) {
        Text(
            text = "Get OTP",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Main Login / Main Screen Composable.
 */
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    phoneValue: String = "",
    errorMessage: String? = null,
    isEnglish: Boolean = true,
    onPhoneChange: (String) -> Unit = {},
    onLanguageToggle: (Boolean) -> Unit = {},
    onGetOtpClick: () -> Unit = {},
    onSignUpClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(LoginBackgroundColor)
    ) {
        // Bottom Illustration background image shifted down to bottom of screen
        Image(
            painter = painterResource(id = R.drawable.farming_illustration),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(377.dp)
                .offset(y = 50.dp)
                .alpha(0.3f)
        )

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top White Header Card
            LoginHeader()

            // Scrollable Form Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                // Login Title and Language Switch Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Login",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                    LanguageSwitch(
                        isEnglish = isEnglish,
                        onLanguageToggle = onLanguageToggle
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Phone Field Label
                Text(
                    text = "Phone",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Phone Input Field
                LoginPhoneInputField(
                    phoneValue = phoneValue,
                    onPhoneChange = onPhoneChange
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Don't have an account? Sign up link
                TextButton(
                    onClick = onSignUpClick,
                    modifier = Modifier.align(Alignment.Start)
                ) {
                    Text(
                        text = "Don't have an account? Sign up",
                        color = Color.White,
                        fontSize = 15.sp,
                        textDecoration = TextDecoration.Underline
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Get OTP Button
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    GetOtpButton(
                        onClick = onGetOtpClick
                    )
                }

                // Error Message if present
                if (!errorMessage.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = errorMessage,
                        color = Color(0xFFFF6B6B),
                        fontSize = 16.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}
