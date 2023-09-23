package com.example.newcalendarlibrary.events

import com.example.newcalendarlibrary.room.events.Event

data class AppointmentState(
    val appointment : List<Event> = emptyList(),
    val title : String = "",
    val description : String = "",
    val isAddingEvent : Boolean = false,
    val sortType: SortType = SortType.TITLE

)