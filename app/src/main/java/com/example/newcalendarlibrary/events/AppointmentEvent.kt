package com.example.newcalendarlibrary.events

import com.example.newcalendarlibrary.room.events.Event

sealed interface AppointmentEvent {
    object SaveAppointment : AppointmentEvent
    object HideDialog : AppointmentEvent
    object ShowDialog : AppointmentEvent
    data class SetTitle(val title: String) : AppointmentEvent
    data class SetDescription(val description: String) : AppointmentEvent
    data class DeleteAppointment(val appointment: Event) : AppointmentEvent

}