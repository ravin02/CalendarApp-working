package com.example.newcalendarlibrary.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Users::class], version = 1, exportSchema = false)
abstract class UsersDatabase : RoomDatabase() {

    abstract val userDao: UsersDao
}
