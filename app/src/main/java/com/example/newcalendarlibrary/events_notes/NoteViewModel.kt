package com.example.newcalendarlibrary.events_notes

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.newcalendarlibrary.NoteEvent
import com.example.newcalendarlibrary.SortType
import com.example.newcalendarlibrary.create_notes.AppointmentState
import com.example.newcalendarlibrary.create_notes.NoteState
import com.example.newcalendarlibrary.room.AppDatabase
import com.example.newcalendarlibrary.room.events.Event
import com.example.newcalendarlibrary.room.notes.Note
import com.example.newcalendarlibrary.room.notes.NotesRepository
import com.example.newcalendarlibrary.room.user.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ViewModel class for handling notes-related operations
class NotesViewModel(
    private val repo: NotesRepository,
    private val userRepo: UserRepository
) : ViewModel() {

    // Mutable state for the user ID
    var userId = mutableStateOf(0)

    // Mutable state for holding a list of notes
    val notes = mutableStateOf<List<Note>>(emptyList())

    // Private variable to hold the username
    private var username = ""

    // Initialization block to fetch initial notes
    init {
        viewModelScope.launch {
            notes.value = repo.getNotesByUserId(userId.value)
            println("init $userId")
        }
    }

    // Function to get the username for a given user ID
    fun getUserName(userId: Int): String {
        viewModelScope.launch {
            username = userRepo.getUserName(userId)
        }
        return username
    }

    // Function to get notes by user ID
    private suspend fun getNotesByUserId(userId: Int): List<Note> {
        return repo.getNotesByUserId(userId)
    }

    // Function to reload notes for a specific user ID
    fun reloadNotes(userId: Int) {
        viewModelScope.launch {
            notes.value = repo.getNotesByUserId(userId)
        }
    }

    // Function to delete a note
    fun deleteNotes(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteNote(note)
            notes.value = getNotesByUserId(userId.value)
        }
    }

    // Function to update a note
    fun updateNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.updateNote(note)
            notes.value = getNotesByUserId(userId.value)
        }
    }

    // Function to create a new note
    fun createNote(title: String, note: String, userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val newNote = Note(title = title, description = note, userId = userId)
            repo.insertNote(newNote)
            notes.value = getNotesByUserId(userId = userId)
        }
    }

    // Function to get a note by its ID
    suspend fun getNote(noteId: Int): Note? {
        return repo.getNoteById(noteId)
    }
}

/**
 * Factory for creating the NotesViewModel with the required parameters
 */
@Suppress("UNCHECKED_CAST")
class NotesViewModelFactory(
    private val repo: NotesRepository,
    private val userRepo: UserRepository,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NotesViewModel(
            repo = repo, userRepo = userRepo,
        ) as T
    }
}



