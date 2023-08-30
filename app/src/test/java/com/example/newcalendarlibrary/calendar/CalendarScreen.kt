package com.example.newcalendarlibrary.calendar

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.himanshoe.kalendar.ui.firey.DaySelectionMode
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn

@Composable
fun CalendarScreen(modifier: Modifier = Modifier) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        val events1 = (0..1).map {
            KalendarEvent(
                date = Clock.System.todayIn(
                    TimeZone.currentSystemDefault()
                ).plus(it, DateTimeUnit.DAY),
                eventName = it.toString(),
                eventDescription = it.toString()
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize()
                .padding(4.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = 8.dp
        ) {
            Kalendar(
                currentDay = Clock.System.todayIn(
                    TimeZone.currentSystemDefault()
                ),
                kalendarType = KalendarType.Firey,
                daySelectionMode = DaySelectionMode.Single,
                events = KalendarEvents(events1 + events1 + events1),
                onRangeSelected = { range, rangeEvents ->
                    Log.d(":SDfsdfsdfdsfsdfsdf", range.toString())
                    Log.d(":SDfsdfsdfdsfsdfsdf", rangeEvents.toString())
                }
            )
        }


    }
}