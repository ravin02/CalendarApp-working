package com.example.newcalendarlibrary.calendar

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newcalendarlibrary.events.AddEventScreen
import com.example.newcalendarlibrary.events.AppointmentEvent
import com.example.newcalendarlibrary.events.AppointmentState
import com.example.newcalendarlibrary.room.events.Event
import com.himanshoe.kalendar.ui.firey.DaySelectionMode
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import com.example.newcalendarlibrary.R

//object HomeDestination : NavigationDestination {
//    override val route = "home"
//    override val titleRes = R.string.app_name
//}
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

//            LazyColumn(
//                contentPadding = PaddingValues(16.dp),
//                modifier = Modifier.fillMaxSize(),
//                verticalArrangement = Arrangement.spacedBy(16.dp)
//            ) {
//                items(state.appointment) { appointment ->
//                    EventCard(appointment = appointment, onEvent = onEvent,)
//                }
//            }

            HomeBody(onEvent = onEvent, itemList = state.appointment, onItemClick = navigateToItemUpdate)

        }


    }
}

@Composable
fun HomeBody(
    onEvent: (AppointmentEvent) -> Unit,
    itemList: List<Event>, onItemClick: (Int) -> Unit, modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        if (itemList.isEmpty()) {
            Text(
                text = "Oops!\\nNo items in the inventory.\\nTap + to add.",
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
    val context = LocalContext.current
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        items(items = itemList, key = { it.id }) { item ->
            EventCard(
                appointment = item,
                modifier = Modifier.padding(8.dp)
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
fun EventCard(
    modifier: Modifier = Modifier,
    appointment: Event,
    onEvent: (AppointmentEvent) -> Unit,
) {

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
                .padding(8.dp)
        ) {

            Column(
                modifier = modifier
                    .wrapContentSize()
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


        }
    }

}