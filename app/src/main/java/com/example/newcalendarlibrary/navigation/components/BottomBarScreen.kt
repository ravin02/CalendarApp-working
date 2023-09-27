package com.example.newcalendarlibrary.navigation.components
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.newcalendarlibrary.R
sealed class BottomBarScreen(
    val route: String,         // Route associated with the screen
    val title: String,         // Title or display name of the screen
    val icon: Int,             // Icon resource for the screen when not focused
    val icon_focused: Int      // Icon resource for the screen when focused
) {

    // Subclass representing the Home screen
    object Home: BottomBarScreen(
        route = "home_screen",                  // Route for the Home screen
        title = "Home",                         // Title "Home"
        icon = R.drawable.baseline_home_24,    // Icon for Home screen
        icon_focused = R.drawable.baseline_home_24  // Focused icon for Home screen
    )

    // Subclass representing the Calendar screen
    object Calendar: BottomBarScreen(
        route = "calendar_screen",              // Route for the Calendar screen
        title = "Explore",                      // Title "Explore"
        icon = R.drawable.baseline_calendar_month_24,  // Icon for Calendar screen
        icon_focused = R.drawable.baseline_calendar_month_24  // Focused icon for Calendar screen
    )

    // Subclass representing the Settings screen
    object SettingsScreen: BottomBarScreen(
        route = "settings_screen",              // Route for the Settings screen
        title = "Settings",                     // Title "Settings"
        icon = R.drawable.baseline_settings_24,    // Icon for Settings screen
        icon_focused = R.drawable.baseline_settings_24  // Focused icon for Settings screen
    )

}
