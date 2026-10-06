package com.nabil.smartkrishi

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nabil.smartkrishi.features.home.HomeScreen
import com.nabil.smartkrishi.ui.theme.BackgroundCream
import com.nabil.smartkrishi.ui.theme.KantumruyPro
import com.nabil.smartkrishi.ui.theme.SmartKrishiTheme

// Sealed class representing standard Navigation Destinations
sealed class Screen(
    val route: String,
    val title: String,
    @DrawableRes val icon: Int,
    val badgeCount: Int = 0
) {
    object Home : Screen(route = "home", title = "Home", icon = R.drawable.home)
    object BankLoan : Screen(route = "bank_loan", title = "Bank Loan", icon = R.drawable.bank)
    object MyProducts : Screen(route = "my_products", title = "My Products", icon = R.drawable.product)
    object Notifications : Screen(route = "notifications", title = "Notifications", icon = R.drawable.bell, badgeCount = 12)
}

val bottomNavScreens = listOf(
    Screen.Home,
    Screen.BankLoan,
    Screen.MyProducts,
    Screen.Notifications
)

// Standalone Custom Bottom Navigation Bar matching XML design and Compose Navigation standards
@Composable
fun BottomNavBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = BackgroundCream,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        shadowElevation = 16.dp
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            contentColor = Color(0xFF2D6A4F),
            tonalElevation = 0.dp
        ) {
            bottomNavScreens.forEach { screen ->
                val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                val itemColor = if (isSelected) Color(0xFF2D6A4F) else Color(0xFF444444)

                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        if (currentDestination?.route != screen.route) {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    icon = {
                        BadgedBox(
                            badge = {
                                if (screen.badgeCount > 0) {
                                    Badge(
                                        containerColor = Color(0xFFE53935),
                                        contentColor = Color.White
                                    ) {
                                        Text(
                                            text = "${screen.badgeCount}",
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
                                painter = painterResource(id = screen.icon),
                                contentDescription = screen.title,
                                tint = itemColor,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    label = {
                        Text(
                            text = screen.title,
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

// Full OnBoard Screen Composable acting as the NavHost container
@Composable
fun OnBoardScreen(
    navController: NavHostController = rememberNavController(),
    onProfileClick: () -> Unit = {}
) {
    Scaffold(
        bottomBar = {
            BottomNavBar(navController = navController)
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    bottomPadding = paddingValues.calculateBottomPadding(),
                    onProfileClick = onProfileClick
                )
            }
            composable(Screen.BankLoan.route) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Bank Loan Screen")
                }
            }
            composable(Screen.MyProducts.route) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("My Products Screen")
                }
            }
            composable(Screen.Notifications.route) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Notifications Screen")
                }
            }
        }
    }
}

// =============== Previews ====================
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OnBoardScreenPreview() {
    SmartKrishiTheme {
        OnBoardScreen()
    }
}
