package com.example.newcalendarlibrary.room.events

import androidx.room.Database
import androidx.room.RoomDatabase
// Define a Room database with specified entity(Event), version, and exportSchema settings
@Database(entities = [Event::class], version = 1, exportSchema = false)
abstract class EventDatabase : RoomDatabase() {

    // Declare an abstract property for accessing the EventDao
    abstract val eventDao: EventDao
}