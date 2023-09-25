package com.example.newcalendarlibrary.navigation

sealed class Screens(val route: String) {

    object LoginScreen : Screens(route = "login_screen")
    object SignUpScreen : Screens(route = "signup_screen")
    object CalendarAppointmentScreen : Screens(route = "calendar_appointment_screen")
    object AddEventScreen : Screens(route = "add_event_screen")
    object EventUpdateScreen : Screens(route = "event_update_screen")

}