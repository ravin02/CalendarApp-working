package com.example.newcalendarlibrary.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.newcalendarlibrary.Day
import com.example.newcalendarlibrary.DaysOfWeekTitle
import com.example.newcalendarlibrary.MonthHeader
import com.example.newcalendarlibrary.widgets.HomeTopAppBar
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.daysOfWeek
import java.time.YearMonth

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController: NavController = rememberNavController()
) {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(100) } // Adjust as needed
    val endMonth = remember { currentMonth.plusMonths(100) } // Adjust as needed
    val daysOfWeek = remember { daysOfWeek() }

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = daysOfWeek.first()
    )
    
    Scaffold(topBar = {}) {
        HomeTopAppBar(
            title = "Home",
            menuContent = {
                DropdownMenuItem(onClick = { navController.navigate(Screen.TodoListScreen.route) }) {
                    Row {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Todo List",
                            modifier = Modifier.padding(4.dp)
                        )
                        Text(
                            text = "Todos", modifier = Modifier
                                .width(100.dp)
                                .padding(4.dp))
                    }
                }
            }
        )
    }





//    If you need a vertical calendar.
//    VerticalCalendar(
//        state = state,
//        dayContent = { Day(it) }
//    )
    
    Column {
        DaysOfWeekTitle(daysOfWeek = daysOfWeek) // Use the title here
        HorizontalCalendar(
            state = state,
            dayContent = { Day(it) } ,

            // Draw the day content gradient.
            monthBody = { _, content ->
                Box(
                    modifier = Modifier.background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFB2EBF2),
                                Color(0xFFB2B8F2)
                            )
                        )
                    )
                ) {
                    content() // Render the provided content!
                }
            },
            // Add the corners/borders and month width.
            monthContainer = { _, container ->
                val configuration = LocalConfiguration.current
                val screenWidth = configuration.screenWidthDp.dp
                Box(
                    modifier = Modifier
                        .width(screenWidth * 0.73f)
                        .padding(8.dp)
                        .clip(shape = RoundedCornerShape(8.dp))
                        .border(
                            color = Color.Black,
                            width = 1.dp,
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    container() // Render the provided container!
                }
            },
            monthHeader = { month ->
                // You may want to use `remember {}` here so the mapping is not done
                // every time as the days of week order will never change unless
                // you set a new value for `firstDayOfWeek` in the state.
                val daysOfWeek = month.weekDays.first().map { it.date.dayOfWeek }
                MonthHeader(daysOfWeek = daysOfWeek)
            },
        )
    }




}