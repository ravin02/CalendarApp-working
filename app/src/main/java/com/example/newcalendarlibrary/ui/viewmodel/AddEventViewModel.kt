package com.example.newcalendarlibrary.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newcalendarlibrary.repository.EventRepository
import com.example.newcalendarlibrary.repository.endDate
import com.example.newcalendarlibrary.repository.startDate
import com.example.newcalendarlibrary.room.events.Event
import com.example.newcalendarlibrary.utils.MyPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEventViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    preference: MyPreference,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Get the ID from the saved state handle, default to 0 if not present
    val id = savedStateHandle.get<Int>("id") ?: 0

    // Get user from preferences
    val user = preference.getUser()

    // Function to store an event in the repository
    fun storeEvent(event: Event) {
        viewModelScope.launch {
            eventRepository.storeEvent(event)
        }
    }

    // Initialize starting and ending times with default values
    val starting = MutableStateFlow(startDate().time)
    val ending = MutableStateFlow(endDate().time)

    // Initialize the color state with 0
    var color = MutableStateFlow(0)

    // Function to update the color
    fun updateColor(colorIndex: Int) {
        color.value = colorIndex
    }

    // Function to set the starting and ending times
    fun setValue(start: Long, end: Long) {
        starting.value = start
        ending.value = end
    }

    // Retrieve today's events from the repository
    val todayEvent = eventRepository.todayEvent()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    // Retrieve all events from the repository
    val data = eventRepository.getEvent()

    // Combine events with starting time to filter events after the starting time
    val rangeEvent = data
        .combine(starting) { a, b ->
            return@combine a.filter { it.startTime > b }
        }
        // Combine filtered events with ending time to filter events before the ending time
        .combine(ending) { a, b ->
            return@combine a.filter { it.startTime < b }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    // Combine events with color to filter events with the selected color
    var colorEvent = eventRepository.getEvent()
        .combine(color) { a, b ->
            return@combine a.filter { it.color == b }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    // Retrieve a single event by ID
    var singleEvent = eventRepository.getEvent(id)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = Event(0, "", "", 0L, 0L, 0, "")
        )

    // Function to delete an event
    fun deleteEvent(event: Event) {
        colorEvent // Make sure colorEvent is used
        viewModelScope.launch {
            eventRepository.removeEvent(event)
        }
    }

    // Retrieve events for the current week
    val weekEvent = eventRepository.thisWeekEvent()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
}