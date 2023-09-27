package com.example.newcalendarlibrary.create_notes

import com.example.newcalendarlibrary.SortType
import com.example.newcalendarlibrary.room.events.Event
import com.example.newcalendarlibrary.room.notes.Note

// Data class representing the state of appointments
data class AppointmentState(
    val appointment: List<Event> = emptyList(), // List of appointments
    val title: String = "", // Title of the appointment
    val description: String = "", // Description of the appointment
    val isAddingEvent: Boolean = false, // Flag indicating if adding a new event
    val sortType: SortType = SortType.TITLE // Sort type for appointments
)

// Data class representing the state of notes
data class NoteState(
    val notes: List<Note> = emptyList(), // List of notes
    val title: String = "", // Title of the note
    val description: String = "", // Description of the note
    val isAddingNote: Boolean = false, // Flag indicating if adding a new note
    val sortType: SortType = SortType.TITLE, // Sort type for notes
    val noteId: Long = 0, // Note ID
    val userId: Long = 0 // User ID associated with the note
)
