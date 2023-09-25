package com.example.newcalendarlibrary.calendar

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newcalendarlibrary.AddEventScreen
import com.example.newcalendarlibrary.AppointmentEvent
import com.example.newcalendarlibrary.room.events.Event
import com.himanshoe.kalendar.ui.firey.DaySelectionMode
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import com.example.newcalendarlibrary.R
import com.example.newcalendarlibrary.create_notes.AppointmentState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    state: AppointmentState,
    onEvent: (AppointmentEvent) -> Unit,
    navigateToItemUpdate: (Int) -> Unit
) {
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

        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                    },
                    onEvent = onEvent
                )
            }
            if (state.isAddingEvent) {
                AddEventScreen(state = state, onEvent = onEvent)

            }

            HomeBody(
                onEvent = onEvent,
                itemList = state.appointment,
                onItemClick = navigateToItemUpdate,
                modifier = modifier.padding(top = 20.dp)
            )

        }
    }
}

@Composable
private fun HomeBody(
    onEvent: (AppointmentEvent) -> Unit,
    itemList: List<Event>, onItemClick: (Int) -> Unit, modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxWidth()
    ) {
        if (itemList.isEmpty()) {
            Text(
                text = "Please click on the date to add event",
                textAlign = TextAlign.Center,
            )
        } else {
            InventoryList(
                itemList = itemList,
                onItemClick = { onItemClick(it.id) },
                onEvent = onEvent
            )
        }
    }

}

@Composable
private fun InventoryList(
    onEvent: (AppointmentEvent) -> Unit,
    itemList: List<Event>, onItemClick: (Event) -> Unit, modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        items(items = itemList, key = { it.id }) { item ->
            EventCard(
                appointment = item,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        onItemClick(item)
                        // Toast.makeText(context, "${item.id} is clicked", Toast.LENGTH_SHORT).show()
                    },
                onEvent = onEvent
            )
        }
    }
}


@Composable
private fun EventCard(
    modifier: Modifier = Modifier,
    appointment: Event,
    onEvent: (AppointmentEvent) -> Unit,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = 16.dp,
        backgroundColor = MaterialTheme.colors.background
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = modifier
                    .weight(1f)
            ) {
                Text(text = appointment.title, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = appointment.description,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Light,
                )
            }

            IconButton(onClick = {
                onEvent(AppointmentEvent.DeleteAppointment(appointment))
            }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete contact"
                )
            }

            //export button here
            ExportIconButton(
                onClick = {
                    /** Using Coroutine Dispatcher.Main to run long running File I/O process
                    in the Main thread*/
                    //In order to understand this you need to remove the coroutine scope and try to
                    // run the app without using the coroutineScope. You will notice Huge performance
                    // difference

                    coroutineScope.launch(Dispatchers.Main) {
                        exportEventToICS(context, appointment)

                    }
                },
                contentDescription = "Export appointment"
            )
        }
    }
}

@Composable
 fun ExportIconButton(
    onClick: () -> Unit,
    contentDescription: String
) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(id = R.drawable.export),
            contentDescription = contentDescription,
            modifier = Modifier.size(25.dp)
        )
    }
}

private fun exportEventToICS(context: Context, event: Event) {
    val fileName = event.title.plus(".ics")
    val dir = File(context.filesDir, "AppointmentExport")

    if (!dir.exists()) {
        dir.mkdir()
    }

    val file = File(dir, fileName)
    val outputStream = FileOutputStream(file)
    val icsContent = buildICSContent(event)

    try {
        outputStream.write(icsContent.toByteArray())
        outputStream.close()
        Toast.makeText(context, "Event exported to $fileName", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(context, "Error exporting event", Toast.LENGTH_SHORT).show()
    }
}

private fun buildICSContent(event: Event): String {
//    val cal = Calendar.getInstance()
//    val dateFormat = "yyyyMMdd'T'HHmmss'Z'"
//    val startTime = event.startTime.format(dateFormat)
//    val endTime = event.endTime.format(dateFormat)

    // This is the ics file content and you can adjust it accordingly
    val icsContent = """
        BEGIN:VCALENDAR
        VERSION:2.0
        PRODID:-//My Calendar//EN
        BEGIN:VEVENT
        SUMMARY:${event.title}
        DESCRIPTION:${event.description}
        END:VEVENT
        END:VCALENDAR
    """.trimIndent()

    return icsContent
}





