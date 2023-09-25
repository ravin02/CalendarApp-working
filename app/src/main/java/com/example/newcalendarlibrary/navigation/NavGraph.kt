package com.example.newcalendarlibrary.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.newcalendarlibrary.calendar.CalendarScreen
import com.example.newcalendarlibrary.color_picker.ColorViewModel
import com.example.newcalendarlibrary.color_picker.colors
import com.example.newcalendarlibrary.events.AddEventScreen
import com.example.newcalendarlibrary.events.AppointmentEvent
import com.example.newcalendarlibrary.events.AppointmentState
import com.example.newcalendarlibrary.events.NoteEvent
import com.example.newcalendarlibrary.events.NoteState
import com.example.newcalendarlibrary.events.event_update.EventUpdateScreen
import com.example.newcalendarlibrary.home.HomeScreen
import com.example.newcalendarlibrary.register.LoginScreen
import com.example.newcalendarlibrary.register.SignUpScreen
import com.example.newcalendarlibrary.room.events.EventDao
import com.example.newcalendarlibrary.settings.SettingsScreen
import com.example.newcalendarlibrary.userRepository

@Composable
fun NavGraph(
    navController: NavHostController, state: AppointmentState,
    onEvent: (AppointmentEvent) -> Unit,
    eventDao: EventDao,
    stateNote : NoteState,
    onEventNote: (NoteEvent) -> Unit
) {

    var selectedColor by remember { mutableStateOf(colors[0]) }

    NavHost(navController = navController, startDestination = BottomBarScreen.Home.route) {
        composable(route = BottomBarScreen.Home.route) {
            HomeScreen(
                navController = navController,
                colors = colors,
                selectedColor = selectedColor,
                state = stateNote ,
                onEvent = onEventNote
            )
        }

        /*  composable(route = Screens.SignUpScreen.route) {
              SignUpScreen(userRepository = userRepository, navController = navController)
          }

          composable(route = Screens.LoginScreen.route) {
              LoginScreen(userRepository = userRepository, navController = navController)
          }*/

        composable(route = BottomBarScreen.Calendar.route) {
            CalendarScreen(
                state = state,
                onEvent = onEvent,
                navigateToItemUpdate = {
                    navController.navigate("${Screens.EventUpdateScreen.route}/${it}")
                })
        }

        composable(route = Screens.AddEventScreen.route) {
            AddEventScreen(state = state, onEvent = onEvent)
        }
        composable(
            route = "${Screens.EventUpdateScreen.route}/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                }
            )
        ) { navBackStackEntry ->
            val itemId = navBackStackEntry.arguments!!.getInt("id")
            EventUpdateScreen(
                itemId = itemId,
                onEvent = onEvent,
                state = state,
                navController = navController,
                eventDao = eventDao
            )
        }

        composable(route = BottomBarScreen.Settings.route) {
            SettingsScreen(
                navController = navController,
                colors = colors,
                onColorSelected = { selectedColor = it })
        }

    }

}