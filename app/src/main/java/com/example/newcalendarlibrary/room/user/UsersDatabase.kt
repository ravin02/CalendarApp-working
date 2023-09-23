package com.example.newcalendarlibrary.room.user

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.newcalendarlibrary.room.user.Users
import com.example.newcalendarlibrary.room.user.UsersDao

@Database(entities = [Users::class], version = 1, exportSchema = false)
abstract class UsersDatabase : RoomDatabase() {

    abstract val userDao: UsersDao
}