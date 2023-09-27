package com.example.newcalendarlibrary.room.notes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// Define an Entity class for representing a Note in the database
@Entity(tableName = "notes", indices = [Index(value = ["id"], unique = true)])
data class Note(
    // Primary key with auto-generation
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    // User ID associated with the note
    @ColumnInfo(name = "userId") val userId: Int = 0,

    // Title of the note
    @ColumnInfo(name = "title") val title: String,

    // Description or content of the note
    @ColumnInfo(name = "note") val description: String,

    // Date when the note was last updated, with a default value
    @ColumnInfo(name = "dateUpdated") val dateUpdated: String = getDateCreated()
)

// Function to get the current date and time formatted as a string
fun getDateCreated(): String {
    return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm"))
}

/*
fun Note.getDay(): String {
    if (this.dateUpdated.isEmpty()) return ""
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
    return LocalDateTime.parse(this.dateUpdated, formatter).toLocalDate()
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
}*/
