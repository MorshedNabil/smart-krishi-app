package com.nabil.smartkrishi.features.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nabil.smartkrishi.R

// Data class representing the SignUp form state
data class SignUpFormData(
    val phone: String = "",
    val nidNumber: String = "",
    val division: String = "",
    val district: String = "",
    val villageName: String = "",
    val role: String = "Farmer"
)

private val SignUpBackgroundColor = Color(0xFF2E5A42)
private val InputFieldBgColor = Color(0xFFC2D16C)
private val ButtonBgColor = Color(0xFFC5D17C)

/**
 * Top Header section with rounded bottom corners and logo.
 */
@Composable
fun SignUpHeader(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp),
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
                modifier = Modifier.size(180.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}

/**
 * Reusable Form Label component.
 */
@Composable
fun FormLabel(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        color = Color.White,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier.padding(top = 16.dp, bottom = 4.dp)
    )
}

/**
 * Reusable Custom Input Text Field component matching XML InputContainerStyle.
 */
@Composable
fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    prefix: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(InputFieldBgColor, shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (prefix != null) {
            prefix()
            Spacer(modifier = Modifier.width(8.dp))
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            contentAlignment = Alignment.CenterStart
        ) {
            if (value.isEmpty()) {
                Text(
                    text = hint,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 15.sp
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal
                ),
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                cursorBrush = SolidColor(Color.White),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * Reusable Phone Number Input Field with fixed country code "+880".
 */
@Composable
fun PhoneInputField(
    phoneValue: String,
    onPhoneChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "Enter phone number"
) {
    FormTextField(
        value = phoneValue,
        onValueChange = onPhoneChange,
        hint = hint,
        keyboardType = KeyboardType.Phone,
        modifier = modifier,
        prefix = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "+880",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .height(24.dp)
                        .width(1.dp)
                        .background(Color.White)
                )
            }
        }
    )
}

/**
 * Reusable Dropdown / Spinner component for selecting Division / District.
 */
@Composable
fun FormDropdownField(
    selectedOption: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    hint: String,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(InputFieldBgColor, shape = RoundedCornerShape(16.dp))
                .clickable { expanded = true }
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (selectedOption.isNotBlank()) selectedOption else hint,
                color = if (selectedOption.isNotBlank()) Color.White else Color.White.copy(alpha = 0.7f),
                fontSize = 15.sp
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Dropdown Arrow",
                tint = Color.White
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(0.85f)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

/**
 * Role Selection Radio Group component ("Sign up as").
 */
@Composable
fun RoleSelectionGroup(
    selectedRole: String,
    onRoleSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    roles: List<String> = listOf("Farmer", "Vendor", "Buyer")
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        roles.forEach { role ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { onRoleSelected(role) }
                    .padding(end = 8.dp)
            ) {
                RadioButton(
                    selected = (selectedRole == role),
                    onClick = { onRoleSelected(role) },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color.White,
                        unselectedColor = Color.White.copy(alpha = 0.7f)
                    )
                )
                Text(
                    text = role,
                    color = Color.White,
                    fontSize = 15.sp
                )
            }
        }
    }
}

/**
 * SignUp Action Button.
 */
@Composable
fun SignUpButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = ButtonBgColor),
        shape = RoundedCornerShape(28.dp),
        modifier = modifier
            .width(180.dp)
            .height(55.dp)
    ) {
        Text(
            text = "Sign up",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Main SignUp Screen Composable.
 */
@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    divisions: List<String> = listOf("Dhaka", "Chittagong", "Khulna", "Rajshahi", "Barisal", "Sylhet", "Rangpur", "Mymensingh"),
    districts: List<String> = listOf("Dhaka", "Chittagong", "Khulna", "Rajshahi", "Barisal", "Sylhet", "Rangpur", "Mymensingh"),
    onSignUpClick: (SignUpFormData) -> Unit = {}
) {
    var phone by remember { mutableStateOf("") }
    var nidNumber by remember { mutableStateOf("") }
    var selectedDivision by remember { mutableStateOf("") }
    var selectedDistrict by remember { mutableStateOf("") }
    var villageName by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("Farmer") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SignUpBackgroundColor)
    ) {
        // Bottom Illustration background image
        Image(
            painter = painterResource(id = R.drawable.farming_illustration),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(377.dp)
                .alpha(0.2f)
        )

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top White Header Card
            SignUpHeader()

            // Scrollable Form Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                // Title
                Text(
                    text = "Sign up",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Phone Field
                FormLabel(text = "Phone")
                PhoneInputField(
                    phoneValue = phone,
                    onPhoneChange = { phone = it }
                )

                // NID Field
                FormLabel(text = "Nid")
                FormTextField(
                    value = nidNumber,
                    onValueChange = { nidNumber = it },
                    hint = "Enter NID number",
                    keyboardType = KeyboardType.Number
                )

                // Division Dropdown Field
                FormLabel(text = "Division")
                FormDropdownField(
                    selectedOption = selectedDivision,
                    options = divisions,
                    onOptionSelected = { selectedDivision = it },
                    hint = "Select Division"
                )

                // District Dropdown Field
                FormLabel(text = "District")
                FormDropdownField(
                    selectedOption = selectedDistrict,
                    options = districts,
                    onOptionSelected = { selectedDistrict = it },
                    hint = "Select District"
                )

                // Village Field
                FormLabel(text = "Village Name")
                FormTextField(
                    value = villageName,
                    onValueChange = { villageName = it },
                    hint = "Enter village name"
                )

                // Role Radio Group
                FormLabel(text = "Sign up as")
                RoleSelectionGroup(
                    selectedRole = selectedRole,
                    onRoleSelected = { selectedRole = it }
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Submit Button
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    SignUpButton(
                        onClick = {
                            onSignUpClick(
                                SignUpFormData(
                                    phone = phone,
                                    nidNumber = nidNumber,
                                    division = selectedDivision,
                                    district = selectedDistrict,
                                    villageName = villageName,
                                    role = selectedRole
                                )
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

// ================== Preview ==================
@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen()
}
