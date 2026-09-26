package com.nabil.smartkrishi.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.nabil.smartkrishi.R
import com.nabil.smartkrishi.ui.theme.BackgroundCream
import com.nabil.smartkrishi.ui.theme.DarkGreenText
import com.nabil.smartkrishi.ui.theme.FieldBorderColor
import com.nabil.smartkrishi.ui.theme.GreenPrimary
import com.nabil.smartkrishi.ui.theme.LightTextColor
import com.nabil.smartkrishi.ui.theme.SmartKrishiTheme


@Composable
fun TopNavBar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    // --- Top Navigation Bar ---
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            onClick = onBackClick,
            shape = CircleShape,
            color = Color.White,
            shadowElevation = 2.dp,
            modifier = Modifier.size(42.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = DarkGreenText,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Text(
            text = "Edit Profile",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGreenText
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .weight(1f)
                .padding(end = 42.dp)
        )
    }
    
}

@Composable
fun ProfileAvatar(
    modifier: Modifier = Modifier,
    profileImageUri: Uri? = null,
    imagePickerLauncher: ActivityResultLauncher<String>
) {
    // --- Profile Avatar Section ---
    Box(
        modifier = modifier
            .padding(vertical = 12.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = Color.White,
            shadowElevation = 4.dp,
            modifier = Modifier.size(130.dp)
        ) {
            Box(modifier = Modifier.padding(4.dp)) {
                AsyncImage(
                    model = profileImageUri ?: R.drawable.user_icon_dr, // Placeholder image
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            }
        }

        // Camera Badge Button
        Surface(
            onClick = {
                imagePickerLauncher.launch("image/*")
            },
            shape = CircleShape,
            color = GreenPrimary,
            shadowElevation = 4.dp,
            modifier = Modifier
                .size(38.dp)
                .align(Alignment.BottomEnd)
                .offset(x = (-2).dp, y = (-2).dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Outlined.PhotoCamera,
                    contentDescription = "Change Profile Picture",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

    }
}

@Composable
fun InputFields(
    modifier: Modifier = Modifier,
    name: String,
    phoneNumber: String,
    selectedLocation: String,
    onNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onVillageChange: (String) -> Unit,
    isLocationDropdownExpanded: Boolean = false,
    onToggleDropdown: (Boolean) -> Unit,
    locations: List<String> = emptyList()
) {
    // --- Full Name Input ---
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Full Name",
            style = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGreenText
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = name,
            onValueChange = {
                onNameChange(it) // callback function to update the name
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null,
                    tint = DarkGreenText
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = GreenPrimary,
                unfocusedBorderColor = FieldBorderColor,
                focusedTextColor = LightTextColor,
                unfocusedTextColor = LightTextColor
            ),
            singleLine = true,
            textStyle = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium)
        )
    }

    // --- Phone Number Input ---
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Phone Number",
            style = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGreenText
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = {
                onPhoneChange(it) // callback function to update the phone number
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            leadingIcon = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 12.dp, end = 8.dp)
                ) {
                    Text(
                        text = "+880",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = DarkGreenText
                        )
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    VerticalDivider(
                        modifier = Modifier.height(22.dp),
                        color = Color(0xFFD1D5DB)
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = GreenPrimary,
                unfocusedBorderColor = FieldBorderColor,
                focusedTextColor = LightTextColor,
                unfocusedTextColor = LightTextColor
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true,
            textStyle = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium)
        )
    }

    Spacer(modifier = Modifier.height(20.dp))

    // --- Village / Location Input ---
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Village / Location",
            style = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGreenText
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedLocation,
                onValueChange = {
                    onVillageChange(it) // callback function to update the village
                },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = null,
                        tint = DarkGreenText
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = DarkGreenText
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = GreenPrimary,
                    unfocusedBorderColor = FieldBorderColor,
                    focusedTextColor = LightTextColor,
                    unfocusedTextColor = LightTextColor
                ),
                singleLine = true,
                textStyle = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium)
            )

            // Overlay box to capture clicks anywhere of the location dropdown field
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(14.dp))
                    .clickable {
                        onToggleDropdown(true) // if the user clicks on the box, open the dropdown
                    }
            )

            DropdownMenu(
                expanded = isLocationDropdownExpanded,
                onDismissRequest = {
                    onToggleDropdown(false) // if the user clicks outside the dropdown, close it
                },
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .background(Color.White)
            ) {
                locations.forEach { location ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = location,
                                style = TextStyle(
                                    fontSize = 15.sp,
                                    color = LightTextColor,
                                    fontWeight = if (location == selectedLocation) FontWeight.Bold else FontWeight.Normal
                                )
                            )
                        },
                        onClick = {
                            onVillageChange(location) // update the selected location using the onVillageChange callback function
                            onToggleDropdown(false) // close the dropdown after selecting a location using callback function
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun SaveChangeButton(
    modifier: Modifier = Modifier,
    onSaveClick: () -> Unit
) {
    // --- Save Changes Button ---
    Button(
        onClick = {
            onSaveClick() // callback function to save changes. Saves the changes to the database which have currently in the uiState
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = GreenPrimary,
            contentColor = Color.White
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        )
    ) {
        Text(
            text = "Save Changes",
            style = TextStyle(
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

// Stateful ProfileScreen: It holds and controls all the state of the screen
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel(),
    onSaveClick: (ProfileUiState) -> Unit,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ProfileContent(
        uiState = uiState,
        onNameChange = viewModel::updateName,
        onPhoneChange = viewModel::updatePhone,
        onVillageChange = viewModel::updateVillage,
        onProfileImageChange= viewModel::updateProfileImage,
        onToggleDropdown = viewModel::toggleLocationDropdown,
        onSaveClick = {
            onSaveClick(uiState)
        } ,
        onBackClick = onBackClick
    )
}

// Stateless ProfileContent: It only renders the screen
@Composable
fun ProfileContent(
    uiState: ProfileUiState,
    onNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onVillageChange: (String) -> Unit,
    onProfileImageChange: (Uri) -> Unit,
    onToggleDropdown: (Boolean) -> Unit,
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit = {}
) {
    val locations = remember {
        listOf(
            "Rahitpur, Panchagar",
            "Tetulia, Panchagar",
            "Boda, Panchagar",
            "Atwari, Panchagar",
            "Debiganj, Panchagar"
        )
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
             onProfileImageChange(it)
        }
    }

    // ========== UI Content =========
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundCream)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Top Navigation bar
            TopNavBar(
                onBackClick = {
                    onBackClick()
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Profile Avatar
            ProfileAvatar(
                modifier = Modifier.align(Alignment.CenterHorizontally), // to center the profile avatar of it's parent
                profileImageUri = uiState.profileImageUri,
                imagePickerLauncher = imagePickerLauncher
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Input Fields
            InputFields(
                name = uiState.name,
                phoneNumber = uiState.phoneNumber,
                selectedLocation = uiState.selectedLocation,
                onNameChange = onNameChange,
                onPhoneChange = onPhoneChange,
                onVillageChange = onVillageChange,
                isLocationDropdownExpanded = uiState.isLocationDropdownExpanded,
                onToggleDropdown = onToggleDropdown,
                locations = locations
            )

            Spacer(modifier = Modifier.height(60.dp))

            // Save Changes Button
            SaveChangeButton(
                onSaveClick = {
                    onSaveClick() // callback function to save changes. This function will not take any parameters because it doesn't need to know about the state of the screen
                },
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}


// ============== Previews ==============
@Preview(showBackground = true)
@Composable
private fun TopNavBarPreview() {
    SmartKrishiTheme {
        TopNavBar()
    }
}

@Preview
@Composable
private fun ProfileAvatarPreview() {
    SmartKrishiTheme {
        ProfileAvatar(
            profileImageUri = null,
            imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {}
        )

    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(
        onSaveClick = {},
        onBackClick = {}
    )
}
