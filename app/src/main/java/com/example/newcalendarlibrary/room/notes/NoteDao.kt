package com.example.newcalendarlibrary.room.notes

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.newcalendarlibrary.room.events.Event
import kotlinx.coroutines.flow.Flow
@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNote(note: Note)
    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM note ORDER BY title ASC")
    fun getNoteOrderedByTitle(): Flow<List<Note>>

    @Query("SELECT * FROM note ORDER BY description ASC")
    fun getNoteOrderedByDescription(): Flow<List<Note>>

}