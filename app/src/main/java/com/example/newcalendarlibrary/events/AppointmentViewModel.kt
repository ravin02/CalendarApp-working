package com.example.newcalendarlibrary.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newcalendarlibrary.room.events.Event
import com.example.newcalendarlibrary.room.events.EventDao
import com.example.newcalendarlibrary.room.notes.Note
import com.example.newcalendarlibrary.room.notes.NoteDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppointmentViewModel(private val eventDao: EventDao) : ViewModel() {

    private val _sortType = MutableStateFlow(SortType.TITLE)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _appointments = _sortType.flatMapLatest { sortType ->
        when (sortType) {
            SortType.TITLE -> eventDao.getEventOrderedByTitle()
            SortType.DESCRIPTION -> eventDao.getEventOrderedByDescription()
        }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    }
    private val _state = MutableStateFlow(AppointmentState())

    val state = combine(_state, _sortType, _appointments) { state, sortType, appointment ->
        state.copy(appointment = appointment, sortType = sortType)

    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppointmentState())


    fun onEvent(event: AppointmentEvent) {
        when (event) {

            AppointmentEvent.HideDialog -> {
                _state.update { it.copy(isAddingEvent = false) }
            }

            AppointmentEvent.ShowDialog -> {
                _state.update { it.copy(isAddingEvent = true) }
            }

            AppointmentEvent.SaveAppointment -> {
                val title = state.value.title
                val description = state.value.description

                if (title.isBlank() || description.isBlank()) {
                    return
                }

                val eventSaved = Event(title = title, description = description)
                viewModelScope.launch { eventDao.insertEvent(event = eventSaved) }

                _state.update {
                    it.copy(
                        title = "",
                        description = "",
                        isAddingEvent = false
                    )
                }
            }


            is AppointmentEvent.DeleteAppointment -> {
                viewModelScope.launch { eventDao.deleteEvent(event = event.appointment) }
            }

            is AppointmentEvent.SetTitle -> {
                _state.update { it.copy(
                    title = event.title
                    ) }
            }

            is AppointmentEvent.SetDescription -> {
                _state.update { it.copy(description = event.description) }
            }

        }


    }
}


class NoteViewModel(private val noteDao: NoteDao) : ViewModel() {

    private val _sortType = MutableStateFlow(SortType.TITLE)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _notes = _sortType.flatMapLatest { sortType ->
        when (sortType) {
            SortType.TITLE -> noteDao.getNoteOrderedByTitle()
            SortType.DESCRIPTION -> noteDao.getNoteOrderedByDescription()
        }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    }
    private val _state = MutableStateFlow(NoteState())

    val state = combine(_state, _sortType, _notes) { state, sortType, note ->
        state.copy(notes = note, sortType = sortType)

    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteState())


    fun onEvent(event: NoteEvent) {
        when (event) {

            NoteEvent.HideDialog -> {
                _state.update { it.copy(isAddingNote = false) }
            }

            NoteEvent.ShowDialog -> {
                _state.update { it.copy(isAddingNote = true) }
            }

            NoteEvent.SaveNote -> {
                val title = state.value.title
                val description = state.value.description

                if (title.isBlank() || description.isBlank()) {
                    return
                }

                val noteSaved = Note(title = title, description = description)
                viewModelScope.launch { noteDao.insertNote(note = noteSaved) }

                _state.update {
                    it.copy(
                        title = "",
                        description = "",
                        isAddingNote = false
                    )
                }
            }


            is NoteEvent.DeleteNote-> {
                viewModelScope.launch { noteDao.deleteNote(note = event.note) }
            }

            is NoteEvent.SetTitle -> {
                _state.update { it.copy(
                    title = event.title
                ) }
            }

            is NoteEvent.SetDescription -> {
                _state.update { it.copy(description = event.description) }
            }

        }


    }
}