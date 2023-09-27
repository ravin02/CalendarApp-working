package com.example.newcalendarlibrary.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

// Annotate the ViewModel with HiltViewModel for dependency injection
@HiltViewModel
class SettingViewModel @Inject constructor(
    private val preference: MyPreference
) : ViewModel() {
    // Get the current setting from preferences
    val setting = preference.getSetting()

    // Function to store the setting in preferences
    fun storeSetting(setting: Int) {
        viewModelScope.launch {
            preference.setSetting(setting)
        }
    }
}