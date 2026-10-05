package com.nabil.smartkrishi

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nabil.smartkrishi.features.home.HomeScreen
import com.nabil.smartkrishi.ui.theme.BackgroundCream
import com.nabil.smartkrishi.ui.theme.KantumruyPro
import com.nabil.smartkrishi.ui.theme.SimpleWhite
import com.nabil.smartkrishi.ui.theme.SmartKrishiTheme

// Data class for Bottom Navigation Items
data class BottomNavItem(
    val title: String,
    @DrawableRes val icon: Int,
    val badgeCount: Int = 0
)

val bottomNavItems = listOf(
    BottomNavItem(title = "Home", icon = R.drawable.home),
    BottomNavItem(title = "Bank Loan", icon = R.drawable.bank),
    BottomNavItem(title = "My Products", icon = R.drawable.product),
    BottomNavItem(title = "Notifications", icon = R.drawable.bell, badgeCount = 12)
)

// Standalone Custom Bottom Navigation Bar matching XML design
@Composable
fun BottomNavBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = SimpleWhite,
        shadowElevation = 16.dp
    ) {
        NavigationBar(
            containerColor = SimpleWhite,
            contentColor = Color(0xFF2D6A4F),
            tonalElevation = 0.dp
        ) {
            bottomNavItems.forEachIndexed { index, item ->
                val isSelected = selectedIndex == index
                val itemColor = if (isSelected) Color(0xFF2D6A4F) else Color(0xFF444444)

                NavigationBarItem(
                    selected = isSelected,
                    onClick = { onItemSelected(index) },
                    icon = {
                        BadgedBox(
                            badge = {
                                if (item.badgeCount > 0) {
                                    Badge(
                                        containerColor = Color(0xFFE53935),
                                        contentColor = Color.White
                                    ) {
                                        Text(
                                            text = "${item.badgeCount}",
                                            style = TextStyle(
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                    }
                                }
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = item.icon),
                                contentDescription = item.title,
                                tint = itemColor,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    label = {
                        Text(
                            text = item.title,
                            style = TextStyle(
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontFamily = KantumruyPro,
                                color = itemColor
                            )
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF2D6A4F),
                        selectedTextColor = Color(0xFF2D6A4F),
                        unselectedIconColor = Color(0xFF444444),
                        unselectedTextColor = Color(0xFF444444),
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    }
}

// Full OnBoard Screen Composable containing the layout & Navigation Bar
@Composable
fun OnBoardScreen(
    onProfileClick: () -> Unit = {}
) {
    var selectedIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        containerColor = BackgroundCream,
        bottomBar = {
            BottomNavBar(
                selectedIndex = selectedIndex,
                onItemSelected = { selectedIndex = it }
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedIndex) {
                0 -> HomeScreen(onProfileClick = onProfileClick)
                1 -> { /* Bank Loan Screen */ }
                2 -> { /* My Products Screen */ }
                3 -> { /* Notifications Screen */ }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnBoardScreenPreview() {
    SmartKrishiTheme {
        OnBoardScreen()
    }
}
