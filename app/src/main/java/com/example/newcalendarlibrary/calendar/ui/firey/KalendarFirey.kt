/*
 * Copyright 2023 Kalendar Contributors (https://www.himanshoe.com). All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */



import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.newcalendarlibrary.AppointmentEvent
import com.example.newcalendarlibrary.calendar.KalendarEvent
import com.example.newcalendarlibrary.calendar.KalendarEvents
import com.example.newcalendarlibrary.calendar.color.KalendarColors
import com.himanshoe.kalendar.ui.component.day.KalendarDay
import com.himanshoe.kalendar.ui.component.day.KalendarDayKonfig
import com.himanshoe.kalendar.ui.component.header.KalendarHeader
import com.himanshoe.kalendar.ui.component.header.KalendarTextKonfig
import com.himanshoe.kalendar.ui.firey.DaySelectionMode
import com.himanshoe.kalendar.ui.firey.KalendarSelectedDayRange
import com.himanshoe.kalendar.ui.firey.RangeSelectionError
import com.himanshoe.kalendar.ui.oceanic.util.isLeapYear
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDate
import kotlinx.datetime.todayIn

private val WeekDays = listOf("M", "T", "W", "T", "F", "S", "S")
/**
 * Internal composable function representing the Kalendar component.
 *
 * @param currentDay The current selected day in the Kalendar.
 * @param daySelectionMode The day selection mode in the Kalendar.
 * @param modifier The modifier for styling or positioning the Kalendar.
 * @param showLabel Determines whether to show labels in the Kalendar.
 * @param kalendarHeaderTextKonfig The configuration for the Kalendar header text.
 * @param kalendarColors The colors configuration for the Kalendar.
 * @param events The events associated with the Kalendar.
 * @param kalendarDayKonfig The configuration for each day in the Kalendar.
 * @param dayContent Custom content for rendering each day in the Kalendar.
 * @param headerContent Custom content for rendering the header of the Kalendar.
 * @param onDayClick Callback invoked when a day is clicked.
 * @param onRangeSelected Callback invoked when a range of days is selected.
 * @param onErrorRangeSelected Callback invoked when an error occurs during range selection.
 */
@Composable
fun KalendarFirey(
    currentDay: LocalDate?,  // The current selected day in the Kalendar
    daySelectionMode: DaySelectionMode,  // The day selection mode in the Kalendar
    modifier: Modifier = Modifier,  // Modifier for styling or positioning the Kalendar
    showLabel: Boolean = true,  // Determines whether to show labels in the Kalendar
    kalendarHeaderTextKonfig: KalendarTextKonfig? = null,  // Configuration for the Kalendar header text
    kalendarColors: KalendarColors = KalendarColors.default(),  // Colors configuration for the Kalendar
    events: KalendarEvents = KalendarEvents(),  // Events associated with the Kalendar
    kalendarDayKonfig: KalendarDayKonfig = KalendarDayKonfig.default(),  // Configuration for each day in the Kalendar
    dayContent: (@Composable (LocalDate) -> Unit)? = null,  // Custom content for rendering each day in the Kalendar
    headerContent: (@Composable (Month, Int) -> Unit)? = null,  // Custom content for rendering the header of the Kalendar
    onDayClick: (LocalDate, List<KalendarEvent>) -> Unit = { _, _ -> },  // Callback invoked when a day is clicked
    onRangeSelected: (KalendarSelectedDayRange, List<KalendarEvent>) -> Unit = { _, _ -> },  // Callback invoked when a range of days is selected
    onErrorRangeSelected: (RangeSelectionError) -> Unit = {},  // Callback invoked when an error occurs during range selection
    onEvent: (AppointmentEvent) -> Unit  // Callback invoked when an appointment event occurs
) {
    // Initialize today's date using the provided currentDay or the current system date.
    val today = currentDay ?: Clock.System.todayIn(TimeZone.currentSystemDefault())

    // Initialize selectedRange with a nullable KalendarSelectedDayRange.
    val selectedRange = remember { mutableStateOf<KalendarSelectedDayRange?>(null) }

    // Initialize selectedDate with today's date.
    val selectedDate = remember { mutableStateOf(today) }

    // Initialize displayedMonth and displayedYear with today's month and year.
    val displayedMonth = remember { mutableStateOf(today.month) }
    val displayedYear = remember { mutableStateOf(today.year) }

    // Extract currentMonth and currentYear from their respective states.
    val currentMonth = displayedMonth.value
    val currentYear = displayedYear.value

    // Calculate the index of the current month.
    val currentMonthIndex = currentMonth.value.minus(1)

    // Calculate the default header color based on the current month.
    val defaultHeaderColor = KalendarTextKonfig.default(
        color = kalendarColors.color[currentMonthIndex].headerTextColor
    )

    // Use the provided kalendarHeaderTextKonfig or the default header color.
    val newHeaderTextKonfig = kalendarHeaderTextKonfig ?: defaultHeaderColor

    // Calculate the number of days in the current month.
    val daysInMonth = currentMonth.length(currentYear.isLeapYear())

    // Pad the month value to ensure it has at least two digits.
    val monthValue = currentMonth.value.toString().padStart(2, '0')

    // Calculate the start day of the month in LocalDate format.
    val startDayOfMonth = "$currentYear-$monthValue-01".toLocalDate()

    // Calculate the day of the week for the start day of the month.
    val firstDayOfMonth = startDayOfMonth.dayOfWeek

    // Retrieve the current context using LocalContext.
    val context = LocalContext.current

    // Create a Column composable that represents the main UI structure.
    Column(
        modifier = modifier
            .background(
                color = kalendarColors.color[currentMonthIndex].backgroundColor
            )
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(all = 8.dp)
    ) {
        // Render header content or the KalendarHeader composable.
        if (headerContent != null) {
            headerContent(currentMonth, currentYear)
        } else {
            KalendarHeader(
                month = currentMonth,
                year = currentYear,
                kalendarTextKonfig = newHeaderTextKonfig,
                onPreviousClick = {
                    displayedYear.value -= if (currentMonth == Month.JANUARY) 1 else 0
                    displayedMonth.value -= 1
                },
                onNextClick = {
                    displayedYear.value += if (currentMonth == Month.DECEMBER) 1 else 0
                    displayedMonth.value += 1
                },
            )
        }

        // Add a spacer with vertical padding.
        Spacer(modifier = Modifier.padding(vertical = 4.dp))

        // Render a LazyVerticalGrid with day labels and calendar days.
        LazyVerticalGrid(
            modifier = Modifier.fillMaxWidth(),
            columns = GridCells.Fixed(7),
            content = {
                // Render week day labels if showLabel is true.
                if (showLabel) {
                    items(WeekDays) { item ->
                        Text(
                            modifier = Modifier,
                            color = kalendarDayKonfig.textColor,
                            fontSize = kalendarDayKonfig.textSize,
                            text = item,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Render calendar days for the current month.
                items((getFirstDayOfMonth(firstDayOfMonth)..daysInMonth).toList()) {
                    if (it > 0) {
                        val day = calculateDay(it, currentMonth, currentYear)

                        // Render day content or the KalendarDay composable.
                        if (dayContent != null) {
                            dayContent(day)
                        } else {
                            KalendarDay(
                                date = day,
                                selectedDate = selectedDate.value,
                                kalendarColors = kalendarColors.color[currentMonthIndex],
                                kalendarEvents = events,
                                kalendarDayKonfig = kalendarDayKonfig,
                                selectedRange = selectedRange.value,
                                onDayClick = { clickedDate, event ->
                                    onDayClicked(
                                        clickedDate,
                                        event,
                                        daySelectionMode,
                                        selectedRange,
                                        onRangeSelected = { range, events ->
                                            if (range.end < range.start) {
                                                onErrorRangeSelected(RangeSelectionError.EndIsBeforeStart)
                                            } else {
                                                onRangeSelected(range, events)
                                            }
                                        },
                                        onDayClick = { newDate, clickedDateEvent ->
                                            selectedDate.value = newDate
                                            onDayClick(newDate, clickedDateEvent)
                                        }
                                    )
                                    onEvent(AppointmentEvent.ShowDialog)
                                }
                            )
                        }
                    }
                }
            }
        )
    }
}

/**
 * Calculates the offset to determine the first day of the month based on the provided first day of the month.
 *
 * @param firstDayOfMonth The first day of the month.
 * @return The offset value representing the first day of the month.
 */
private fun getFirstDayOfMonth(firstDayOfMonth: DayOfWeek) = -(firstDayOfMonth.value).minus(2)

/**
 * Calculates a LocalDate object based on the provided day, current month, and current year.
 *
 * @param day The day of the month.
 * @param currentMonth The current month.
 * @param currentYear The current year.
 * @return The LocalDate object representing the specified day, month, and year.
 */
private fun calculateDay(day: Int, currentMonth: Month, currentYear: Int): LocalDate {
    val monthValue = currentMonth.value.toString().padStart(2, '0')
    val dayValue = day.toString().padStart(2, '0')
    return "$currentYear-$monthValue-$dayValue".toLocalDate()
}

//@Composable
//@MultiplePreviews
//private fun KalendarFireyPreview() {
//    KalendarFirey(
//        currentDay = Clock.System.todayIn(
//            TimeZone.currentSystemDefault()
//        ),
//        kalendarHeaderTextKonfig = KalendarTextKonfig.previewDefault(),
//        daySelectionMode = DaySelectionMode.Range,
//        showDialog = false,
//    )
//}