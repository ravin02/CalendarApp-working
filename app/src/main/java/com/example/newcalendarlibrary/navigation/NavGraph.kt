package com.example.newcalendarlibrary.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.newcalendarlibrary.calendar.CalendarScreen
import com.example.newcalendarlibrary.events.AddEventScreen
import com.example.newcalendarlibrary.events.AppointmentEvent
import com.example.newcalendarlibrary.events.AppointmentState
import com.example.newcalendarlibrary.events.event_update.EventUpdateScreen
import com.example.newcalendarlibrary.register.LoginScreen
import com.example.newcalendarlibrary.register.SignUpScreen
import com.example.newcalendarlibrary.room.events.EventDao
import com.example.newcalendarlibrary.userRepository

@Composable
fun NavGraph(
    navController: NavHostController, state: AppointmentState,
    onEvent: (AppointmentEvent) -> Unit,
    eventDao: EventDao
) {

    NavHost(navController = navController, startDestination = Screens.LoginScreen.route) {

        composable(route = Screens.SignUpScreen.route) {
            SignUpScreen(userRepository = userRepository, navController = navController)
        }

        composable(route = Screens.LoginScreen.route) {
            LoginScreen(userRepository = userRepository, navController = navController)
        }

        composable(route = Screens.CalendarScreen.route) {
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
                navArgument("id"){
                    type = NavType.IntType
                }
            )
            ) { navBackStackEntry ->
            val itemId = navBackStackEntry.arguments!!.getInt("id")
            EventUpdateScreen(itemId = itemId, onEvent = onEvent, state = state, navController = navController, eventDao = eventDao)
        }
    }

}