package com.example.newcalendarlibrary.events_notes.create_notes

import com.example.newcalendarlibrary.room.notes.Note

object Constants {
    // Define a Kotlin object named Constants to hold constant values.

    const val NOTES_TABLE_NAME = "notes"
    const val USERS_TABLE_NAME = "users"
    const val DATABASE_NAME = "appDatabase"
    // Define constant strings for table names and database name.

    fun noteDetailNavigation(noteId: Int) = "noteDetail/$noteId"
    // Define a function that generates a navigation path for displaying note details based on a note ID.

    fun noteEditNavigation(noteId: Int) = "editNote/$noteId"
    // Define a function that generates a navigation path for editing a note based on a note ID.

    val noteDetailPlaceHolder =
        Note(description = "Cannot find note details", id = 0, title = "Cannot find note details")
    // Define a placeholder Note object with predefined values for a missing or invalid note.
}