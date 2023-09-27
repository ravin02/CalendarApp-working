package com.example.newcalendarlibrary.event_update

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.newcalendarlibrary.R
import com.example.newcalendarlibrary.AppointmentEvent
import com.example.newcalendarlibrary.color_picker.ColourButton
import com.example.newcalendarlibrary.color_picker.colors
import com.example.newcalendarlibrary.create_notes.AppointmentState
import com.example.newcalendarlibrary.events_notes.CustomTimePicker
import com.example.newcalendarlibrary.events_notes.DatePicker
import com.example.newcalendarlibrary.room.events.Event
import com.example.newcalendarlibrary.ui.viewmodel.AddEventViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Currency
import java.util.Date
import java.util.Locale

// Composable function to display an event update screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventUpdateScreen(
    modifier: Modifier = Modifier,
    itemId: Int, // Identifier for the event
    onEvent: (AppointmentEvent) -> Unit,
    state: AppointmentState,
    navController: NavController,
    eventViewModel: AddEventViewModel = hiltViewModel(),
) {
    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colors.background) {

        // Collect the event from the ViewModel
        val event by eventViewModel.singleEvent.collectAsStateWithLifecycle()

        // Check if the event title is not empty
        if (event.title.isNotEmpty()) {
            Log.i("123321", "EventUpdateScreen: $event")

            // Initialize calendars for start and end times
            val startCalendar = Calendar.getInstance()
            startCalendar.timeInMillis = event.startTime

            val endCalendar = Calendar.getInstance()
            endCalendar.timeInMillis = event.endTime

            // Remembering state for date and time pickers
            val datePickerState = rememberDatePickerState(initialSelectedDateMillis = startCalendar.timeInMillis)
            val timePickerState = rememberTimePickerState(startCalendar.get(Calendar.HOUR_OF_DAY), startCalendar.get(Calendar.MINUTE))

            val endDatePickerState = rememberDatePickerState(initialSelectedDateMillis = endCalendar.timeInMillis)
            val endTimePickerState = rememberTimePickerState(endCalendar.get(Calendar.HOUR_OF_DAY), endCalendar.get(Calendar.MINUTE))

            // Remembering state for various inputs
            var title by remember { mutableStateOf(event.title) }
            var description by remember { mutableStateOf(event.description) }
            var selectedColor by remember { mutableIntStateOf(event.color) }

            // Flags to control the visibility of date and time pickers
            var showDatePicker by remember { mutableStateOf(false) }
            var showEndDatePicker by remember { mutableStateOf(false) }
            var showTimePicker by remember { mutableStateOf(false) }
            var showEndTimePicker by remember { mutableStateOf(false) }

            // Selected date and time values
            var selectedDate by remember { mutableLongStateOf(datePickerState.selectedDateMillis ?: 0) }
            var selectedEndDate by remember { mutableLongStateOf(endDatePickerState.selectedDateMillis ?: 0) }
            var selectedTime by remember { mutableLongStateOf(startCalendar.timeInMillis) }
            var selectedEndTime by remember { mutableLongStateOf(endCalendar.timeInMillis) }

            // Display date picker
            DatePicker(showDatePicker, datePickerState) {
                showDatePicker = false
                selectedDate = datePickerState.selectedDateMillis ?: 0
            }

            // Display end date picker
            DatePicker(showEndDatePicker, endDatePickerState) {
                showEndDatePicker = false
                selectedEndDate = endDatePickerState.selectedDateMillis ?: 0
            }

            // Display custom time picker for start time
            CustomTimePicker(showDatePicker = showTimePicker, datePickerState = timePickerState) {
                startCalendar.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                startCalendar.set(Calendar.MINUTE, timePickerState.minute)
                selectedTime = startCalendar.timeInMillis
                showTimePicker = false
            }

            // Display custom time picker for end time
            CustomTimePicker(showDatePicker = showEndTimePicker, datePickerState = endTimePickerState) {
                endCalendar.set(Calendar.HOUR_OF_DAY, endTimePickerState.hour)
                endCalendar.set(Calendar.MINUTE, endTimePickerState.minute)
                selectedEndTime = endCalendar.timeInMillis
                showEndTimePicker = false
            }
// Remembering state to control the visibility of the dialog
            var showDialog by remember {
                mutableStateOf(true)
            }

            if (showDialog) {
                // Displaying an AlertDialog when showDialog is true
                androidx.compose.material.AlertDialog(
                    onDismissRequest = { onEvent(AppointmentEvent.HideDialog) },
                    shape = RoundedCornerShape(12.dp),
                    text = {
                        Column(
                            modifier = modifier.fillMaxWidth().padding(horizontal = 12.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start
                        ) {
                            // Input field for the event title
                            OutlinedTextField(
                                value = title,
                                onValueChange = { title = it },
                                placeholder = {
                                    Text(
                                        text = "Title",
                                        color = MaterialTheme.colors.onBackground
                                    )
                                }
                            )

                            Spacer(modifier = modifier.padding(vertical = 5.dp))

                            // Displaying "From" section
                            Text("From")
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                TextButton(onClick = { showDatePicker = true }) {
                                    // Displaying the selected date in a button
                                    Text(
                                        text = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(Date(selectedDate))
                                    )
                                }
                                TextButton(onClick = { showTimePicker = true }) {
                                    // Displaying the selected time in a button
                                    Text(
                                        text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(selectedTime))
                                    )
                                }
                            }

                            Spacer(modifier = modifier.padding(vertical = 5.dp))

                            // Displaying "To" section
                            Text("To")
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                TextButton(onClick = { showEndDatePicker = true }) {
                                    // Displaying the selected end date in a button
                                    Text(
                                        text = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(Date(selectedEndDate))
                                    )
                                }
                                TextButton(onClick = { showEndTimePicker = true }) {
                                    // Displaying the selected end time in a button
                                    Text(
                                        text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(selectedEndTime))
                                    )
                                }
                            }



                            Spacer(modifier = modifier.padding(vertical = 5.dp))

                            ColourButton(
                                selected = selectedColor,
                                colors = colors,
                                onColorSelected = {
                                    selectedColor = it
                                },
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp, end = 16.dp)
                            )

                            Spacer(modifier = modifier.padding(vertical = 5.dp))

                            OutlinedTextField(
                                value = description,
                                onValueChange = { description = it },
                                label = { Text("Description") },
                                singleLine = false,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                textStyle = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Start
                                ),
                                modifier = modifier
                                    .height(150.dp)
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(22.dp)
                            )

                            Spacer(modifier = modifier.padding(vertical = 5.dp))

                        }
                    },

                    buttons = {
                        Box(
                            modifier = modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Button(onClick = {
                                // Obtain the current time and date and update the selected date and time
                                val calendar = Calendar.getInstance()
                                calendar.timeInMillis = datePickerState.selectedDateMillis ?: System.currentTimeMillis()
                                calendar.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                                calendar.set(Calendar.MINUTE, timePickerState.minute)

                                // Obtain the end date and time and update
                                val endCalendar = Calendar.getInstance()
                                endCalendar.timeInMillis = endDatePickerState.selectedDateMillis ?: System.currentTimeMillis()
                                endCalendar.set(Calendar.HOUR_OF_DAY, endTimePickerState.hour)
                                endCalendar.set(Calendar.MINUTE, endTimePickerState.minute)

                                // Update the event in the view model and navigate back
                                eventViewModel.storeEvent(
                                    Event(
                                        id = event.id,
                                        title = title,
                                        description = description,
                                        startTime = calendar.timeInMillis,
                                        endTime = endCalendar.timeInMillis,
                                        color = selectedColor,
                                        user = eventViewModel.user
                                    )
                                )
                                showDialog = false
                                navController.navigateUp()
                            }) {
                                Text(text = "Update")
                            }
                        }
                    }
                )
            }}}}

@Composable
private fun OutlinedTextFieldMessage(
    modifier: Modifier,
    state: AppointmentState,
    onEvent: (AppointmentEvent) -> Unit
) {
    // OutlinedTextField for displaying and editing the description
    OutlinedTextField(
        value = state.description,
        onValueChange = { onEvent(AppointmentEvent.SetDescription(it)) },
        label = { Text("Description") },
        singleLine = false,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        textStyle = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            textAlign = TextAlign.Start
        ),
        modifier = modifier.fillMaxSize(),
        shape = RoundedCornerShape(22.dp),
    )
}

@Composable
fun ItemInputForm(
    appointmentState: AppointmentState,
    modifier: Modifier = Modifier,
    onValueChange: (AppointmentState) -> Unit = {},
    enabled: Boolean = true
) {
    // Column to hold the input form for the appointment
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        // OutlinedTextField for the title
        OutlinedTextField(
            value = appointmentState.title,
            onValueChange = { onValueChange(appointmentState.copy(title = it)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        // OutlinedTextField for the description
        OutlinedTextField(
            value = appointmentState.description,
            onValueChange = { onValueChange(appointmentState.copy(description = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
    }
}