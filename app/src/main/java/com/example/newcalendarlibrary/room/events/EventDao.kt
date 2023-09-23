package com.example.newcalendarlibrary.room.events

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEvent(event: Event)
    @Update
    suspend fun updateEvent(event: Event)
    @Delete
    suspend fun deleteEvent(event: Event)

    @Query("SELECT * FROM event ORDER BY title ASC")
    fun getEventOrderedByTitle(): Flow<List<Event>>

    @Query("SELECT * FROM event ORDER BY description ASC")
    fun getEventOrderedByDescription(): Flow<List<Event>>

}