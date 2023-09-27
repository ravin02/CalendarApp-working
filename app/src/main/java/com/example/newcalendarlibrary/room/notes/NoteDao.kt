package com.example.newcalendarlibrary.room.notes

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.newcalendarlibrary.room.events.Event
import kotlinx.coroutines.flow.Flow

// Define a Data Access Object (DAO) interface for Note entities
@Dao
interface NoteDAO {

    // Query to retrieve all notes belonging to a specific user by their userId
    @Query("SELECT * FROM Notes WHERE notes.userId=:userId")
    suspend fun getNotesByUserId(userId: Int): List<Note>

    // Query to retrieve a specific note by its unique id
    @Query("SELECT * FROM Notes WHERE notes.id=:id")
    suspend fun getNoteById(id: Int): Note?

    // Query to retrieve all notes, ordered by their dateUpdated in descending order
    @Query("SELECT * FROM Notes ORDER BY dateUpdated DESC")
    fun getNotes(): List<Note>

    // Delete operation to remove a note from the database
    @Delete
    fun deleteNote(note: Note): Int

    // Update operation to modify an existing note in the database
    @Update
    fun updateNote(note: Note): Int

    // Insert operation to add a new note to the database
    @Insert
    fun insertNote(note: Note)
}
