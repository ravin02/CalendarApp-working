package com.example.newcalendarlibrary.events_notes

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.newcalendarlibrary.AppointmentEvent
import com.example.newcalendarlibrary.color_picker.ColourButton
import com.example.newcalendarlibrary.color_picker.colors
import com.example.newcalendarlibrary.create_notes.AppointmentState
import com.example.newcalendarlibrary.room.events.Event
import com.example.newcalendarlibrary.ui.viewmodel.AddEventViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

//@Preview
//@Composable
//fun EventScreenPreview() {
//    AddEventScreen()
//}


// Composable function to display the Add Event screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventScreen(
    modifier: Modifier = Modifier,
    state: AppointmentState,
    onEvent: (AppointmentEvent) -> Unit,
    eventViewModel: AddEventViewModel = hiltViewModel(),
) {
    Surface(modifier = modifier.fillMaxSize()) {

        // Initialize calendars for start and end dates
        val startCalender = Calendar.getInstance()
        val endCalender = Calendar.getInstance()

        // Initialize date picker and time picker states
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())
        val timePickerState = rememberTimePickerState(startCalender.get(Calendar.HOUR_OF_DAY), startCalender.get(Calendar.MINUTE))
        val endTimePickerState = rememberTimePickerState(endCalender.get(Calendar.HOUR_OF_DAY), endCalender.get(Calendar.MINUTE))
        val endDatePickerState = rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())

        // Booleans to control the visibility of date and time pickers
        var showDatePicker by remember {
            mutableStateOf(false)
        }
        var showEndDatePicker by remember {
            mutableStateOf(false)
        }
        var showTimePicker by remember {
            mutableStateOf(false)
        }
        var showEndTimePicker by remember {
            mutableStateOf(false)
        }

        // Variables to hold selected dates and times
        var selectedDate by remember {
            mutableLongStateOf(datePickerState.selectedDateMillis ?: 0)
        }
        var selectedEndDate by remember {
            mutableLongStateOf(datePickerState.selectedDateMillis ?: 0)
        }
        var selectedTime by remember {
            mutableLongStateOf(startCalender.timeInMillis)
        }
        var selectedEndTime by remember {
            mutableLongStateOf(startCalender.timeInMillis)
        }

        // Show date picker and update selected date on selection
        DatePicker(showDatePicker, datePickerState) {
            showDatePicker = false
            selectedDate = datePickerState.selectedDateMillis ?: 0
        }

        // Show end date picker and update selected end date on selection
        DatePicker(showEndDatePicker, endDatePickerState) {
            showEndDatePicker = false
            selectedEndDate = endDatePickerState.selectedDateMillis ?: 0
        }

        // Show custom time picker and update selected time on selection
        CustomTimePicker(showDatePicker = showTimePicker, datePickerState = timePickerState) {
            startCalender.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
            startCalender.set(Calendar.MINUTE, timePickerState.minute)
            selectedTime = startCalender.timeInMillis
            showTimePicker = false
        }

        // Show custom end time picker and update selected end time on selection
        CustomTimePicker(showDatePicker = showEndTimePicker, datePickerState = endTimePickerState) {
            endCalender.set(Calendar.HOUR_OF_DAY, endTimePickerState.hour)
            endCalender.set(Calendar.MINUTE, endTimePickerState.minute)
            selectedEndTime = endCalender.timeInMillis
            showEndTimePicker = false
        }

// Mutable states for holding the title, description, and selected color
        var title by remember {
            mutableStateOf("")
        }
        var description by remember {
            mutableStateOf("")
        }
        var selectedColor by remember {
            mutableIntStateOf(0)
        }

// AlertDialog to display the Add Event screen
        androidx.compose.material.AlertDialog(
            onDismissRequest = { onEvent(AppointmentEvent.HideDialog) },
            shape = RoundedCornerShape(12.dp),
            text = {
                // Content of the AlertDialog, including various UI elements
                Column(
                    modifier = modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    // Input field for the title
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

                    // "From" section with date and time pickers
                    Text("From")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextButton(onClick = { showDatePicker = true }) {
                            Text(text = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(Date(selectedDate)))
                        }
                        TextButton(onClick = { showTimePicker = true }) {
                            Text(text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(selectedTime)))
                        }
                    }

                    // "To" section with date and time pickers
                    Text("To")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextButton(onClick = { showEndDatePicker = true }) {
                            Text(text = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(Date(selectedEndDate)))
                        }
                        TextButton(onClick = { showEndTimePicker = true }) {
                            Text(text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(selectedEndTime)))
                        }
                    }

                    // Color selection button
                    ColourButton(
                        colors = colors,
                        onColorSelected = { selectedColor = it },
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp)
                    )

                    // Input field for the description
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
                        modifier = modifier.height(150.dp).fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp)
                    )
                }
            },

// Save button section within the AlertDialog
            buttons = {
                Box(
                    modifier = modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    // Save button
                    Button(onClick = {
                        // Create Calendar instances for start and end times
                        val calendar = Calendar.getInstance()
                        calendar.timeInMillis = datePickerState.selectedDateMillis ?: System.currentTimeMillis()
                        calendar.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                        calendar.set(Calendar.MINUTE, timePickerState.minute)

                        val endCalendar = Calendar.getInstance()
                        endCalendar.timeInMillis = endDatePickerState.selectedDateMillis ?: System.currentTimeMillis()
                        endCalendar.set(Calendar.HOUR_OF_DAY, endTimePickerState.hour)
                        endCalendar.set(Calendar.MINUTE, endTimePickerState.minute)

                        // Store the event using the eventViewModel
                        eventViewModel.storeEvent(
                            Event(
                                id = 0,
                                title = title,
                                description = description,
                                startTime = calendar.timeInMillis,
                                endTime = endCalendar.timeInMillis,
                                color = selectedColor,
                                user = eventViewModel.user
                            )
                        )

                        // Hide the dialog on save
                        onEvent(AppointmentEvent.HideDialog)
                    }) {
                        Text(text = "Save")
                    }
                }
            })
    }
}



// DatePicker composable to display a Material3 DatePickerDialog
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DatePicker(
    showDatePicker: Boolean,         // Flag indicating whether the DatePicker should be shown
    datePickerState: DatePickerState, // State for the DatePicker
    onDismiss: () -> Unit             // Callback for dismissing the DatePicker
) {
    // Local variable to control the visibility of the DatePickerDialog
    var showDatePicker1 = showDatePicker

    // Show the DatePickerDialog if showDatePicker is true
    if (showDatePicker1) {
        // Display the DatePickerDialog with specified actions
        DatePickerDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back button
                showDatePicker1 = false
            },
            confirmButton = {
                // OK button to confirm the selected date
                TextButton(
                    onClick = {
                        onDismiss.invoke() // Invoke the provided callback on OK button click
                    },
                    enabled = true
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                // Cancel button to dismiss the DatePickerDialog
                TextButton(
                    onClick = {
                        showDatePicker1 = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            // Display the DatePicker using the provided state
            DatePicker(state = datePickerState)
        }
    }
}

// CustomTimePicker composable to display a custom time picker using AlertDialog
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CustomTimePicker(
    showDatePicker: Boolean,    // Flag indicating whether the CustomTimePicker should be shown
    datePickerState: TimePickerState, // State for the TimePicker
    onDismiss: () -> Unit       // Callback for dismissing the CustomTimePicker
) {
    // Local variable to control the visibility of the AlertDialog
    val showDatePicker1 = showDatePicker

    // Show the AlertDialog if showDatePicker is true
    if (showDatePicker1) {
        AlertDialog(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.surface),
            onDismissRequest = { onDismiss.invoke() },
            properties = DialogProperties(usePlatformDefaultWidth = false) // Adjust dialog properties for full width and height
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        top = 16.dp,
                        start = 12.dp,
                        end = 12.dp,
                        bottom = 0.dp
                    ), // Reduce padding to fit dialog contents into the screen
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Time picker
                TimePicker(
                    datePickerState
                )

                // Buttons
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

/*// Placeholder composable for OutlinedTextFieldMessage (add specific implementation)
@Composable
fun OutlinedTextFieldMessage(
    modifier: Modifier,
    state: AppointmentState,
    onEvent: (AppointmentEvent) -> Unit
) {

}*/

