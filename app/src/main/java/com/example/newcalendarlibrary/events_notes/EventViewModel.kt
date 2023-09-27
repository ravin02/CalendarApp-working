package com.example.newcalendarlibrary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newcalendarlibrary.create_notes.AppointmentState
import com.example.newcalendarlibrary.room.events.Event
import com.example.newcalendarlibrary.room.events.EventDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
// ViewModel class for handling events related to appointments
class EventViewModel(private val eventDao: EventDao) : ViewModel() {

    // Mutable state flow to hold the sort type for events
    private val _sortType = MutableStateFlow(SortType.TITLE)

    // Flow of appointments based on the selected sort type
    private val _appointments = _sortType.flatMapLatest { sortType ->
        when (sortType) {
            SortType.TITLE -> eventDao.getEventOrderedByTitle()
            SortType.DESCRIPTION -> eventDao.getEventOrderedByDescription()
        }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    }

    // Mutable state flow to hold the current state related to appointments
    private val _state = MutableStateFlow(AppointmentState())

    // Combine state and appointment data to provide a unified state for the UI
    val state = combine(_state, _sortType, _appointments) { state, sortType, appointment ->
        state.copy(appointment = appointment, sortType = sortType)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppointmentState())

    // Function to handle appointment events
    fun onEvent(event: AppointmentEvent) {
        when (event) {
            // Handle hiding the dialog for appointments
            AppointmentEvent.HideDialog -> {
                _state.update { it.copy(isAddingEvent = false) }
            }

            // Handle showing the dialog for appointments
            AppointmentEvent.ShowDialog -> {
                _state.update { it.copy(isAddingEvent = true) }
            }

            // Handle saving an appointment
            AppointmentEvent.SaveAppointment -> {
                val title = state.value.title
                val description = state.value.description

                if (title.isBlank() || description.isBlank()) {
                    return
                }

                val eventSaved = Event(title = title, description = description)
                // Uncomment to insert the event into the database
                // viewModelScope.launch { eventDao.insertEvent(event = eventSaved) }

                _state.update {
                    it.copy(
                        title = "",
                        description = "",
                        isAddingEvent = false
                    )
                }
            }

            // Handle deleting an appointment
            is AppointmentEvent.DeleteAppointment -> {
                viewModelScope.launch { eventDao.deleteEvent(event = event.appointment) }
            }

            // Handle setting the title for an appointment
            is AppointmentEvent.SetTitle -> {
                _state.update {
                    it.copy(
                        title = event.title
                    )
                }
            }

            // Handle setting the description for an appointment
            is AppointmentEvent.SetDescription -> {
                _state.update { it.copy(description = event.description) }
            }

            else -> { /* Handle other cases if needed */ }
        }
    }
}