package com.example.newcalendarlibrary.room.events

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
@Dao
interface EventDao {

    // Insert or update an event
    @Upsert
    suspend fun insertEvent(event: Event)

    // Update an existing event
    @Update
    suspend fun updateEvent(event: Event)

    // Delete an event
    @Delete
    suspend fun deleteEvent(event: Event)

    // Query to get events ordered by title
    @Query("SELECT * FROM event ORDER BY title ASC")
    fun getEventOrderedByTitle(): Flow<List<Event>>

    // Query to get events ordered by description
    @Query("SELECT * FROM event ORDER BY description ASC")
    fun getEventOrderedByDescription(): Flow<List<Event>>

    // Query to get events for a specific user
    @Query("SELECT * FROM event WHERE user=:user")
    fun getEvent(user: String): Flow<List<Event>>

    // Query to get an event by its ID
    @Query("SELECT * FROM event WHERE id=:id")
    fun getEvent(id: Int): Flow<Event>

    // Query to get events within a specified time range for a user
    @Query("SELECT * FROM event WHERE startTime BETWEEN :startTime AND :endTime AND user=:user")
    fun getEvent(startTime: Long, endTime: Long, user: String): Flow<List<Event>>
}