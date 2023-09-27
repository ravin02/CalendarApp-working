package com.example.newcalendarlibrary.event_update

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.newcalendarlibrary.room.events.Event
import com.example.newcalendarlibrary.room.events.EventDao

// ViewModel to handle updating an event
class EventUpdateViewModel(
    private val eventDao: EventDao,
    private val itemId: Int // Identifier for the event to be updated
) : ViewModel() {
    var title by mutableStateOf("") // State for the event title
    var description by mutableStateOf("") // State for the event description

    // Function to update the event in the database
    suspend fun updateEventInDatabase(itemId: Int) {
        val eventToUpdate = Event(id = itemId, title = title, description = description)
        eventDao.updateEvent(eventToUpdate)
    }
}

// Factory to create an instance of EventUpdateViewModel
class EventUpdateViewModelFactory(
    private val eventDao: EventDao,
    private val itemId: Int // Identifier for the event to be updated
) : ViewModelProvider.Factory {

    // Create an instance of the ViewModel
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(EventUpdateViewModel::class.java)) {
            return EventUpdateViewModel(eventDao, itemId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}