package com.example.newcalendarlibrary

import com.example.newcalendarlibrary.room.events.Event
import com.example.newcalendarlibrary.room.notes.Note

// Sealed interface for events related to appointments
sealed interface AppointmentEvent {
    object SaveAppointment : AppointmentEvent // Event to save an appointment
    object HideDialog : AppointmentEvent // Event to hide a dialog related to appointments
    object ShowDialog : AppointmentEvent // Event to show a dialog related to appointments
    data class SetTitle(val title: String) : AppointmentEvent // Event to set the title of an appointment
    data class SetDescription(val description: String) : AppointmentEvent // Event to set the description of an appointment
    data class DeleteAppointment(val appointment: Event) : AppointmentEvent // Event to delete an appointment
}

// Sealed interface for events related to notes
sealed interface NoteEvent {
    object SaveNote : NoteEvent // Event to save a note
    object HideDialog : NoteEvent // Event to hide a dialog related to notes
    object ShowDialog : NoteEvent // Event to show a dialog related to notes
    data class SetTitle(val title: String) : NoteEvent // Event to set the title of a note
    data class SetDescription(val description: String) : NoteEvent // Event to set the description of a note
    data class DeleteNote(val note: Note) : NoteEvent // Event to delete a note
}