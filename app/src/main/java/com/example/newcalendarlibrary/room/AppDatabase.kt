package com.example.newcalendarlibrary.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.newcalendarlibrary.room.events.Event
import com.example.newcalendarlibrary.room.events.EventDao
import com.example.newcalendarlibrary.room.notes.Note
import com.example.newcalendarlibrary.room.notes.NoteDAO
import com.example.newcalendarlibrary.room.user.User
import com.example.newcalendarlibrary.room.user.UserDAO

// Define a Room database class using annotations
@Database(entities = [User::class, Note::class, Event::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // Declare abstract functions for accessing DAOs
    abstract fun noteDao(): NoteDAO
    abstract fun userDao(): UserDAO
    abstract fun eventDao(): EventDao

    // Define a companion object to manage database instances
    object AppDb {
        private var db: AppDatabase? = null

        // Create or retrieve a database instance
        fun getInstance(context: Context): AppDatabase {
            if (db == null) {
                db = Room.databaseBuilder(context, AppDatabase::class.java, "AppDatabase")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return db!!
        }
    }
}