package com.example.newcalendarlibrary.room.events

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Event(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    var title : String,
    val description : String
)
