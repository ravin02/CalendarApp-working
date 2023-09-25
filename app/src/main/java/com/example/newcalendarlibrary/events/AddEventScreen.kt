package com.example.newcalendarlibrary.events

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newcalendarlibrary.color_picker.ColorViewModel
import com.example.newcalendarlibrary.color_picker.ColourButton
import com.example.newcalendarlibrary.color_picker.colors
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import java.util.Calendar
import java.util.Date

//@Preview
//@Composable
//fun EventScreenPreview() {
//    AddEventScreen()
//}


@Composable
fun AddEventScreen(
    modifier: Modifier = Modifier,
    state: AppointmentState,
    onEvent: (AppointmentEvent) -> Unit,
    viewModel: ColorViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    Surface(modifier = modifier.fillMaxSize()) {

//        var title by remember { mutableStateOf("") }
//        val description = remember { mutableStateOf("") }

        val currentDayCal = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val context = LocalContext.current

        // Declaring integer values
        // for year, month and day
        val mYear: Int
        val mMonth: Int
        val mDay: Int

        // Initializing a Calendar
        val mCalendar = Calendar.getInstance()

        // Fetching current year, month and day
        mYear = mCalendar.get(Calendar.YEAR)
        mMonth = mCalendar.get(Calendar.MONTH)
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

        mCalendar.time = Date()

        // Declaring a string value to
        // store date in string format
        var mDate = remember { mutableStateOf("") }

        // Declaring DatePickerDialog and setting
        // initial values as current values (present year, month and day)
        val mDatePickerDialog = DatePickerDialog(
            context,
            { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
                mDate.value = "$mDayOfMonth/${mMonth + 1}/$mYear"
            }, mYear, mMonth, mDay
        )

        val mHour = mCalendar[Calendar.HOUR_OF_DAY]
        val mMinute = mCalendar[Calendar.MINUTE]

        // Value for storing time as a string
        val mTime = remember { mutableStateOf("") }

        // Creating a TimePicker dialod
        val mTimePickerDialog = TimePickerDialog(
            context,
            { _, mHour: Int, mMinute: Int ->
                mTime.value = "$mHour:$mMinute"
            }, mHour, mMinute, false
        )

        val isDatePicked = remember { mutableStateOf(false) }
        val isTimePicked = remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { onEvent(AppointmentEvent.HideDialog) },
           // title = { Text(text = "Add Event")},
            shape = RoundedCornerShape(12.dp),
            text = {
                Column(
                    modifier = modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    OutlinedTextField(
                        value = state.title,
                        onValueChange = { onEvent(AppointmentEvent.SetTitle(it)) },
                       // label = { Text(text = "Title") },
                        placeholder = {
                            Text(
                                text = "Title", color = MaterialTheme.colors.onBackground
                            )
                        })
                    Spacer(modifier = modifier.padding(vertical = 5.dp))
                    Button(
                        onClick = {
                            mDatePickerDialog.show()
                            isDatePicked.value = true
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.background),
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(start = 0.dp, end = 0.dp)
                            .height(45.dp)
                    ) {
                        Text(
                            text = if (isDatePicked.value) mDate.value else "Please Pick a Date",
                            color = MaterialTheme.colors.onBackground
                        )

                    }
                    Spacer(modifier = modifier.padding(vertical = 5.dp))

                    Button(
                        onClick = {
                            mTimePickerDialog.show()
                            isTimePicked.value = true
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.background),
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(start = 0.dp, end = 0.dp)
                            .height(45.dp)
                    ) {

                        Text(
                            text = if (isTimePicked.value) mTime.value else "Please Pick Time",
                            color = MaterialTheme.colors.onBackground
                        )

                    }
                    Spacer(modifier = modifier.padding(vertical = 5.dp))

                    ColourButton(
                        colors = colors,
                        onColorSelected = {},
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp),
                    )
                    Spacer(modifier = modifier.padding(vertical = 5.dp))

                    OutlinedTextFieldMessage(
                        modifier
                            .fillMaxWidth()
                            .size(150.dp),
                        state,
                        onEvent
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
                        onEvent(AppointmentEvent.SaveAppointment)
                        Toast.makeText(context, "Event Saved", Toast.LENGTH_SHORT).show()
                    }) {
                        Text(text = "Save")
                    }

                }
            }
        )
    }
}


@Composable
fun OutlinedTextFieldMessage(
    modifier: Modifier,
    state: AppointmentState,
    onEvent: (AppointmentEvent) -> Unit
) {
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