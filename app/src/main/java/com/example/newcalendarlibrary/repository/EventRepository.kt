package com.example.newcalendarlibrary.repository

import com.example.newcalendarlibrary.room.events.Event
import com.example.newcalendarlibrary.room.events.EventDao
import com.example.newcalendarlibrary.utils.MyPreference
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import javax.inject.Inject


fun endDate(currentTimeMillis: Long=System.currentTimeMillis()): Date {
    // Create a Calendar instance and set its time based on the provided currentTimeMillis
    val c: Calendar = GregorianCalendar()
    c.timeInMillis=currentTimeMillis

    // Set the hour, minute, and second to the end of the day (23:59:59)
    c.set(Calendar.HOUR_OF_DAY, 23)
    c.set(Calendar.MINUTE, 59)
    c.set(Calendar.SECOND, 59)

    // Get the Date object representing the end date and return it
    val endDate: Date = c.time
    return endDate
}

fun startDate(currentTimeMillis: Long=System.currentTimeMillis()): Date {
    // Create a Calendar instance and set its time based on the provided currentTimeMillis
    val startTime: Calendar = GregorianCalendar()
    startTime.timeInMillis=currentTimeMillis

    // Set the hour, minute, and second to the start of the day (00:00:00)
    startTime.set(Calendar.HOUR_OF_DAY, 0)
    startTime.set(Calendar.MINUTE, 0)
    startTime.set(Calendar.SECOND, 0)

    // Get the Date object representing the start date and return it
    val startDate: Date = startTime.time
    return startDate
}

class EventRepository @Inject constructor(private val eventDao: EventDao,val preference: MyPreference) {
    // Function to retrieve events for the current day
    fun todayEvent (): Flow<List<Event>> {
        val startDate: Date = startDate(System.currentTimeMillis())
        val endDate: Date = endDate(System.currentTimeMillis())

        // Return events within the given date range for the current user
        return eventDao.getEvent(startDate.time, endDate.time, user=preference.getUser())
    }

    // Function to retrieve all events for the current user
    fun getEvent() = eventDao.getEvent(user=preference.getUser())

    // Function to retrieve an event by its ID
    fun getEvent(id:Int) = eventDao.getEvent(id)

    // Function to retrieve events for the current week
    fun thisWeekEvent (): Flow<List<Event>> {
        val startTime: Calendar = GregorianCalendar()
        startTime.set(Calendar.DAY_OF_WEEK, startTime.firstDayOfWeek)
        startTime.set(Calendar.HOUR_OF_DAY, 0)
        startTime.set(Calendar.MINUTE, 0)
        startTime.set(Calendar.SECOND, 0)
        val startDate: Date = startTime.time

        val c: Calendar = GregorianCalendar()
        c.set(Calendar.DAY_OF_WEEK, 6)
        c.set(Calendar.HOUR_OF_DAY, 23)
        c.set(Calendar.MINUTE, 59)
        c.set(Calendar.SECOND, 59)
        val endDate: Date = c.time

        // Return events within the current week for the current user
        return eventDao.getEvent(startDate.time, endDate.time, preference.getUser())
    }

    // Function to store an event in the database
    suspend fun storeEvent(event: Event) {
        eventDao.insertEvent(event)
    }

    // Function to remove an event from the database
    suspend fun removeEvent(event: Event) = eventDao.deleteEvent(event)
}