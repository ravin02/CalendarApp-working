package com.example.newcalendarlibrary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.newcalendarlibrary.calendar.CalendarScreen
import com.example.newcalendarlibrary.ui.theme.NewCalendarLibraryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewCalendarLibraryTheme {
                //You can Comment/unComment below Composable functions to test it on Emulator or device
               //AddEventScreen()
                CalendarScreen()
            }
        }
    }
}

