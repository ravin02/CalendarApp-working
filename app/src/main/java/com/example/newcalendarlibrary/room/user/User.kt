package com.example.newcalendarlibrary.room.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Define an Entity class for representing a User in the database
@Entity(tableName = "users")
data class User(
    // Primary key with auto-generation (nullable)
    @PrimaryKey(autoGenerate = true) val id: Int? = null,

    // User's name
    @ColumnInfo(name = "nameValue") val nameValue: String,

    // User's login or username
    @ColumnInfo(name = "loginValue") val loginValue: String,

    // User's password (should be securely hashed in a real application)
    @ColumnInfo(name = "passwordValue") val passwordValue: String
)

