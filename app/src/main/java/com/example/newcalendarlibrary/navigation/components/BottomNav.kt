package com.example.newcalendarlibrary.navigation.components

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomNavigation
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.example.newcalendarlibrary.AppointmentEvent
import com.example.newcalendarlibrary.NoteEvent
import com.example.newcalendarlibrary.create_notes.AppointmentState
import com.example.newcalendarlibrary.create_notes.NoteState
import com.example.newcalendarlibrary.events_notes.NotesViewModel
import com.example.newcalendarlibrary.navigation.HomeNavGraph
import com.example.newcalendarlibrary.navigation.Screens
import com.example.newcalendarlibrary.room.events.EventDao
import com.example.newcalendarlibrary.ui.theme.Purple200

// This annotation suppresses lint warnings related to unused parameters.
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BottomNav(
    onEvent: (AppointmentEvent) -> Unit,
    eventDao: EventDao,
    state: AppointmentState,
    navController: NavHostController = rememberNavController(),
    notesViewModel: NotesViewModel
) {
    // The BottomNav composable function is the entry point for rendering the bottom navigation.
    // It receives several parameters like event handlers, state, navigation controller, and view models.

    Scaffold {
        // Scaffold is a Material Design component that provides basic structure for the screen.
        // It typically contains the app bar, floating action button, and the main content area.

        HomeNavGraph(
            state = state,
            onEvent = onEvent,
            eventDao = eventDao,
            notesViewModel = notesViewModel,
            navController = navController,
            navigateToItemUpdate = { navController.navigate("${Screens.EventUpdateScreen.route}/${it}") }
        )
        // The HomeNavGraph composable is called to render the content of the screen,
        // using the provided state, event handlers, view models, and navigation controller.
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    // BottomBar is a composable function that renders the bottom navigation bar.

    val screens = listOf(
        BottomBarScreen.Home, BottomBarScreen.SettingsScreen, BottomBarScreen.Calendar
    )
    // A list of BottomBarScreen objects representing each item in the bottom navigation.

    // Retrieve the current destination from the navigation controller's back stack.
    val navStackBackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navStackBackEntry?.destination

    // Check if the current destination corresponds to an item in the bottom navigation.
    val bottomBarDestination = screens.any { it.route == currentDestination?.route }
    if (bottomBarDestination) {
        // If the current destination is in the bottom navigation, render the BottomNavigation.

        BottomNavigation {
            // BottomNavigation is a composable function for displaying the bottom navigation UI.

            Card(
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp, top = 8.dp, bottom = 8.dp),
                shape = RoundedCornerShape(15.dp),
                elevation = (6.dp)
            ) {
                // Card is used to display a card-like UI with rounded corners and elevation.

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Row is used to arrange the bottom navigation items in a horizontal row.

                    screens.forEach { screen ->
                        // Iterate over each bottom navigation item and render an AddItem.

                        AddItem(
                            screen = screen,
                            currentDestination = currentDestination,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavController
) {
    // AddItem is a composable function that renders an individual item in the bottom navigation.

    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
    // Check if the current destination corresponds to the current bottom navigation item.

    val background =
        if (selected) Purple200.copy(alpha = 0.6f) else Color.Transparent
    // Determine the background color based on whether the item is selected.

    val contentColor =
        if (selected) Color.White else Color.Black
    // Determine the content color (icon and text) based on whether the item is selected.

    Box(
        modifier = Modifier
            .height(40.dp)
            .clip(CircleShape)
            .background(background)
            .clickable(onClick = {
                navController.navigate(screen.route) {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            })
    ) {
        // Box is used to define the layout and appearance of the individual bottom navigation item.

        Row(
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Row is used to arrange the icon and text within each bottom navigation item.

            Icon(
                painter = painterResource(id = if (selected) screen.icon_focused else screen.icon),
                contentDescription = "icon",
                tint = contentColor
            )
            // Icon is used to display the icon for the bottom navigation item.

            AnimatedVisibility(visible = selected) {
                // AnimatedVisibility is used to animate the visibility of the item's title.

                Text(
                    text = screen.title,
                    color = contentColor
                )
            }
        }
    }
}