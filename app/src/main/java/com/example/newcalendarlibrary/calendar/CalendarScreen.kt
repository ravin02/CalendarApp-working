package com.example.newcalendarlibrary.calendar

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.newcalendarlibrary.AppointmentEvent
import com.example.newcalendarlibrary.R
import com.example.newcalendarlibrary.color_picker.ColourButton
import com.example.newcalendarlibrary.color_picker.colors
import com.example.newcalendarlibrary.create_notes.AppointmentState
import com.example.newcalendarlibrary.events_notes.AddEventScreen
import com.example.newcalendarlibrary.repository.endDate
import com.example.newcalendarlibrary.repository.startDate
import com.example.newcalendarlibrary.room.events.Event
import com.example.newcalendarlibrary.ui.viewmodel.AddEventViewModel
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.YearMonth
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    state: AppointmentState,
    onEvent: (AppointmentEvent) -> Unit,
    navigateToItemUpdate: (Int) -> Unit,
    viewModel: AddEventViewModel = hiltViewModel(),
) {
    // A Composable function representing the Calendar screen.

    // Collect today's event and color event using viewModel
    val todayEvent by viewModel.rangeEvent.collectAsStateWithLifecycle()
    val colorEvent by viewModel.colorEvent.collectAsStateWithLifecycle()

    // Surface representing the entire calendar screen
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        // List of sample events for the calendar
        val events1 = (0..1).map {
            KalendarEvent(
                date = Clock.System.todayIn(
                    TimeZone.currentSystemDefault()
                ).plus(it, DateTimeUnit.DAY),
                eventName = it.toString(),
                eventDescription = it.toString()
            )
        }

        // Flag to control visibility of the time picker
        var showTimePicker by remember {
            mutableStateOf(false)
        }

        // State for the date range picker
        val rangeState = rememberDateRangePickerState(
            initialSelectedStartDateMillis = System.currentTimeMillis(),
            initialSelectedEndDateMillis = System.currentTimeMillis()
        )

        // Column to organize UI components vertically
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Button to add an event
            Button(onClick = {
                onEvent(AppointmentEvent.ShowDialog)
            }) {
                Text(text = "Add Event")
            }

            // Card containing a calendar view
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize()
                    .padding(4.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = 8.dp
            ) {


                var tabIndex by remember { mutableStateOf(0) } // Variable to track the selected tab index.

                val currentMonth = remember { YearMonth.now() } // Remember the current month.
                val startMonth = remember { currentMonth.minusMonths(100) } // Remember the start month for the calendar (100 months before the current month).
                val endMonth = remember { currentMonth.plusMonths(100) } // Remember the end month for the calendar (100 months after the current month).
                val firstDayOfWeek = remember { firstDayOfWeekFromLocale() } // Remember the first day of the week based on the locale.
                val tabs = listOf("By Time", "By Color") // List of tab titles.

                val state = rememberCalendarState(
                    startMonth = startMonth,
                    endMonth = endMonth,
                    firstVisibleMonth = currentMonth,
                    firstDayOfWeek = firstDayOfWeek
                ) // Remember the state of the calendar with specified parameters.

                val daysOfWeek = remember { daysOfWeek() } // Remember the days of the week based on the locale.

                ElevatedCard {
                    // ElevatedCard to contain the TabRow component.

                    // TabRow to display tabs and allow selecting different views (By Time, By Color).
                    androidx.compose.material3.TabRow(selectedTabIndex = tabIndex) {
                        tabs.forEachIndexed { index, title ->
                            androidx.compose.material3.Tab(
                                text = { Text(title) },
                                selected = tabIndex == index,
                                onClick = { tabIndex = index }
                            )
                        }
                    }


                    when(tabIndex){
                        0->{
                            // Initialize mutable state for start date, end date, and displayed text
                            var starting by remember {
                                mutableStateOf(startDate())
                            }
                            var ending by remember {
                                mutableStateOf(endDate())
                            }

                            var text by remember {
                                mutableStateOf("${SimpleDateFormat("dd MMM yyyy").format(starting)}- ${SimpleDateFormat("dd MMM yyyy").format(ending)}")
                            }

                            // Display the custom time picker using the provided showTimePicker and rangeState
                            CustomTimePicker(showDatePicker = showTimePicker, datePickerState = rangeState) {
                                // Update showTimePicker and log the selected start date
                                showTimePicker = false
                                Log.i("123321", "CalendarScreen: ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(rangeState.selectedStartDateMillis ?: 0))}")

                                if (rangeState.selectedStartDateMillis == null) {
                                    starting = startDate()
                                    ending = endDate()
                                } else if (rangeState.selectedEndDateMillis == null) {
                                    starting = startDate(rangeState.selectedStartDateMillis!!)
                                    ending = endDate(rangeState.selectedStartDateMillis!!)
                                } else {
                                    starting = startDate(rangeState.selectedStartDateMillis!!)
                                    ending = endDate(rangeState.selectedEndDateMillis!!)
                                }

                                // Update the displayed text with the formatted start and end dates
                                text = "${SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(starting)}- ${SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(ending)}"

                                // Set the values in the view model for the start and end times
                                viewModel.setValue(starting.time, ending.time)
                            }

                            Row(
                                // Set modifiers for the Row layout
                                modifier = Modifier
                                    .padding(4.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Display the text and style it
                                Text(text = text, fontWeight = FontWeight.Bold, fontSize = 18.sp)

                                // Display an icon button to trigger showing the time picker
                                IconButton(onClick = { showTimePicker = true }) {
                                    Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = null)
                                }
                            }

// Display the HomeBody component with specified parameters
                            HomeBody(
                                onEvent = onEvent,
                                itemList = todayEvent,
                                onItemClick = navigateToItemUpdate,
                                modifier = modifier.padding(top = 20.dp),
                                onDeleteEvent = viewModel::deleteEvent
                            )
                        }



                        1->{
                            // Case 1 specific code block

                            // Log the colorEvent for debugging purposes
                            Log.i("123321", "CalendarScreen: $colorEvent")

                            // Display a ColourButton with specified colors and an onColorSelected listener to update the color
                            ColourButton(
                                colors = colors,
                                onColorSelected = {
                                    viewModel.updateColor(it)
                                },
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp, end = 16.dp)
                            )

                            // Display the HomeBody component with specified parameters for event handling, item list, item click navigation, and event deletion
                            HomeBody(
                                onEvent = onEvent,
                                itemList = colorEvent,
                                onItemClick = navigateToItemUpdate,
                                modifier = modifier.padding(top = 20.dp),
                                onDeleteEvent = viewModel::deleteEvent
                            )
                        }
                    }}}

            if (state.isAddingEvent) {
                // Check if the application is in the process of adding an event
                // Display the AddEventScreen with the provided state and onEvent callback
                AddEventScreen(state = state, onEvent = onEvent)
            }
        }}}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun CustomTimePicker(
    showDatePicker: Boolean,
    datePickerState: DateRangePickerState,
    onDismiss: () -> Unit
) {
    var showDatePicker1 = showDatePicker

    if (showDatePicker1) {
        // Display an AlertDialog if showDatePicker is true
        AlertDialog(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.surface),
            onDismissRequest = { onDismiss.invoke() },
            properties = DialogProperties(usePlatformDefaultWidth = false) // Set this so that the dialog occupies the full width and height
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        top = 16.dp,
                        start = 12.dp,
                        end = 12.dp,
                        bottom = 0.dp
                    ), // Reduce the padding to make the dialog contents fit into the screen
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Display the DateRangePicker inside the AlertDialog
                DateRangePicker(
                    datePickerState,
                    title = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                        ) {
                            Button(onClick = { onDismiss.invoke() }) {
                                Text(text = "Done")
                            }
                        }
                    }
                )

                // Display buttons for dismissing and confirming the selection
                Row(
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center // Place buttons at the center
                ) {
                    // Dismiss button
                    TextButton(onClick = { onDismiss.invoke() }) {
                        Text(text = "Dismiss")
                    }

                    // Confirm button
                    TextButton(
                        onClick = {
                            onDismiss.invoke()
                        }
                    ) {
                        Text(text = "Confirm")
                    }
                }
            }
        }
    }
}



@Composable
private fun HomeBody(
    onEvent: (AppointmentEvent) -> Unit,
    itemList: List<Event>,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    onDeleteEvent: (Event) -> Unit
) {
    // Display a Column layout for organizing content vertically
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxWidth()
    ) {
        // Display a message if the itemList is empty
        if (itemList.isEmpty()) {
            Text(
                text = "Please click on Add Event to add an event",
                textAlign = TextAlign.Center
            )
        } else {
            // Display the InventoryList with the provided itemList and callbacks for item click and event deletion
            InventoryList(
                itemList = itemList,
                onItemClick = { onItemClick(it.id) },
                onEvent = onEvent,
                onDeleteEvent = onDeleteEvent
            )
        }
    }
}

@Composable
private fun InventoryList(
    onEvent: (AppointmentEvent) -> Unit,
    itemList: List<Event>,
    onItemClick: (Event) -> Unit,
    modifier: Modifier = Modifier,
    onDeleteEvent: (Event) -> Unit
) {
    // Display a LazyColumn for efficiently displaying a scrolling list
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        items(items = itemList, key = { it.id }) { item ->
            // Display an EventCard for each item in the itemList
            EventCard(
                appointment = item,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        onItemClick(item) // Trigger the onItemClick callback when the EventCard is clicked
                    },
                onEvent = onEvent,
                onDeleteEvent = onDeleteEvent
            )
        }
    }
}


@Composable
fun EventCard(
    modifier: Modifier = Modifier,
    appointment: Event,
    onEvent: (AppointmentEvent) -> Unit,
    onDeleteEvent: (Event) -> Unit
) {
    // Obtain the current context and coroutine scope
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Create an ElevatedCard with specified modifiers
    ElevatedCard(
        modifier = modifier.fillMaxWidth()
    ) {
        // Create a Column to organize content vertically
        Column(modifier = Modifier) {
            // Create a Row to organize content horizontally
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Display appointment title and description
                Column(
                    modifier = modifier.weight(1f)
                ) {
                    Text(text = appointment.title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = appointment.description,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Light,
                    )
                }

                // Display a circle with the appointment color
                Canvas(
                    modifier = Modifier.size(size = 20.dp)
                ) {
                    drawCircle(
                        color = colors[appointment.color]
                    )
                }

                // Display an IconButton to delete the event
                IconButton(onClick = {
                    onDeleteEvent.invoke(appointment)
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
                        /** Using Coroutine Dispatcher.Main to run long running File I/O process
                        in the Main thread*/
                        /** Using Coroutine Dispatcher.Main to run long running File I/O process
                        in the Main thread*/

                        /** Using Coroutine Dispatcher.Main to run long running File I/O process
                        in the Main thread*/
                        /** Using Coroutine Dispatcher.Main to run long running File I/O process
                        in the Main thread*/
                        /** Using Coroutine Dispatcher.Main to run long running File I/O process
                        in the Main thread*/

                        /** Using Coroutine Dispatcher.Main to run long running File I/O process
                        in the Main thread*/

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

// Display the formatted start and end times of the appointment
            Text(
                modifier = Modifier
                    .fillMaxWidth()  // Occupy the full available width
                    .padding(4.dp),  // Add padding around the text
                text = "${appointment.startTime.toDateTime()} - ${appointment.endTime.toDateTime()}",  // Display the formatted date and time range
                textAlign = TextAlign.End,  // Align the text to the end (right)
                fontSize = 10.sp)  // Set the font size
        }}}

fun Long.toDateTime():String{
    return  SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH).format(Date(this))
}

@Composable
fun ExportIconButton(
    onClick: () -> Unit,
    contentDescription: String
) {
    // Icon button triggering the export action
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(id = R.drawable.export),
            contentDescription = contentDescription,
            modifier = Modifier.size(25.dp)
        )
    }
}

// Function to export the event to ICS format
private fun exportEventToICS(context: Context, event: Event) {
    // Generate a file name based on the event title
    val fileName = event.title.plus(".ics")

    // Create a directory for the exported files if it doesn't exist
    val dir = File(context.filesDir, "AppointmentExport")
    if (!dir.exists()) {
        dir.mkdir()
    }

    // Create a file for the event in the export directory
    val file = File(dir, fileName)
    val outputStream = FileOutputStream(file)

    // Build ICS content for the event
    val icsContent = buildICSContent(event)

    try {
        // Write the ICS content to the file
        outputStream.write(icsContent.toByteArray())
        outputStream.close()

        // Display a success toast
        Toast.makeText(context, "Event exported to $fileName", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        // Display an error toast if export fails
        Toast.makeText(context, "Error exporting event", Toast.LENGTH_SHORT).show()
    }
}


private fun buildICSContent(event: Event): String {
    // Build the ICS file content with event details
    val icsContent = """
        BEGIN:VCALENDAR          // Start of the VCALENDAR section
        VERSION:2.0              // Specifies the version of the iCalendar format
        PRODID:-//My Calendar//EN // Product identifier for the calendar
        BEGIN:VEVENT              // Start of the VEVENT section
        SUMMARY:${event.title}    // Event title
        DESCRIPTION:${event.description}  // Event description
        END:VEVENT                // End of the VEVENT section
        END:VCALENDAR             // End of the VCALENDAR section
    """.trimIndent()

    return icsContent
}






