package com.example.newcalendarlibrary.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Users")
data class Users(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val name : String,
    val userName : String,
    val password : String,
    val confirm_Password : String? = null // nullable for loginScreen
)