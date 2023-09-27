package com.example.newcalendarlibrary.room.notes

import android.content.Context
import com.example.newcalendarlibrary.room.AppDatabase
import kotlinx.coroutines.flow.Flow

// Repository class responsible for providing access to Note data
class NotesRepository(context: Context) : NoteDAO {

    // Initialize the NoteDAO by obtaining an instance of the database
    private val noteDao = AppDatabase.AppDb.getInstance(context).noteDao()

    // Override function to retrieve a list of notes by user ID
    override suspend fun getNotesByUserId(userId: Int): List<Note> {
        return noteDao.getNotesByUserId(userId)
    }

    // Override function to retrieve a specific note by its ID
    override suspend fun getNoteById(id: Int): Note? {
        return noteDao.getNoteById(id)
    }

    // Override function to retrieve all notes
    override fun getNotes(): List<Note> {
        return noteDao.getNotes()
    }

    // Override function to delete a note from the database
    override fun deleteNote(note: Note): Int {
        return noteDao.deleteNote(note)
    }

    // Override function to update a note in the database
    override fun updateNote(note: Note): Int {
        return noteDao.updateNote(note)
    }

    // Override function to insert a new note into the database
    override fun insertNote(note: Note) {
        return noteDao.insertNote(note)
    }
}