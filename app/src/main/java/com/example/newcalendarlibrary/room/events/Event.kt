package com.example.newcalendarlibrary.room.events

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event")
data class Event(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,             // Unique identifier for the event, auto-generated
    var title: String,           // Title or name of the event
    val description: String,     // Description or details about the event
    val startTime: Long = 0L,    // Start time of the event in milliseconds since epoch
    val endTime: Long = 0L,      // End time of the event in milliseconds since epoch
    val color: Int = 0,          // Color associated with the event (possibly for UI/display purposes)
    val user: String = ""        // User associated with the event (if applicable)
)
