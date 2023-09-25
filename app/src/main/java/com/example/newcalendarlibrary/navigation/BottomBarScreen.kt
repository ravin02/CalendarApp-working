package com.example.newcalendarlibrary.navigation
import com.example.newcalendarlibrary.R
sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: Int,
    val icon_focused: Int
){

    // for home
    object Home: BottomBarScreen(
        route = "home_screen",
        title = "Home",
        icon = R.drawable.baseline_home_24,
        icon_focused = R.drawable.baseline_home_24

    )

    // for report
    object Calendar: BottomBarScreen(
        route = "calendar_screen",
        title = "Kalendar",
        icon = R.drawable.baseline_calendar_month_24,
        icon_focused = R.drawable.baseline_calendar_month_24
    )

    object Settings: BottomBarScreen(
        route = "settings_screen",
        title = "Settings",
        icon = R.drawable.baseline_settings_24,
        icon_focused = R.drawable.baseline_settings_24
    )



}