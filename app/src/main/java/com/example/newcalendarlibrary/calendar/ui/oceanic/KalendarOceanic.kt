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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.himanshoe.kalendar.ui.oceanic.util.getNext7Dates
import com.himanshoe.kalendar.ui.oceanic.util.getPrevious7Dates
import com.himanshoe.kalendar.util.MultiplePreviews
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import onDayClicked
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

/**
 * Creates a composable function for the KalendarOceanic component.
 * @param modifier The modifier for styling or positioning the component.
 * @param daySelectionMode The mode for selecting days in the calendar.
 * @param currentDay The current day to highlight in the calendar.
 * @param showLabel Flag indicating whether to show labels for days of the week.
 * @param kalendarHeaderTextKonfig The configuration for styling the calendar header text.
 * @param kalendarColors The colors to be used in the calendar.
 * @param events The events to be displayed in the calendar.
 * @param labelFormat The format function for generating labels for days of the week.
 * @param kalendarDayKonfig The configuration for styling individual days in the calendar.
 * @param dayContent The content to be displayed for each day in the calendar.
 * @param headerContent The content to be displayed in the calendar header.
 * @param onDayClick The callback function when a day is clicked.
 * @param onRangeSelected The callback function when a range of days is selected.
 * @param onErrorRangeSelected The callback function when there is an error in selecting a range of days.
 */
@Composable
internal fun KalendarOceanic(
    // Parameters for configuring the calendar component
    modifier: Modifier = Modifier,                               // Modifier for styling or positioning
    daySelectionMode: DaySelectionMode = DaySelectionMode.Single, // Mode for selecting days
    currentDay: LocalDate? = null,                                // Current day to highlight
    showLabel: Boolean = true,                                    // Flag to show day labels
    kalendarHeaderTextKonfig: KalendarTextKonfig? = null,        // Configuration for header text styling
    kalendarColors: KalendarColors = KalendarColors.default(),   // Colors for the calendar
    events: KalendarEvents = KalendarEvents(),                   // Events to display in the calendar
    labelFormat: (DayOfWeek) -> String = {                        // Function for generating day labels
        it.getDisplayName(
            TextStyle.SHORT,
            Locale.getDefault()
        )
    },
    kalendarDayKonfig: KalendarDayKonfig = KalendarDayKonfig.default(),  // Configuration for styling days
    dayContent: @Composable ((LocalDate) -> Unit)? = null,        // Content for each day
    headerContent: @Composable ((Month, Int) -> Unit)? = null,    // Content for calendar header
    onDayClick: (LocalDate, List<KalendarEvent>) -> Unit = { _, _ -> },  // Callback for day click
    onRangeSelected: (KalendarSelectedDayRange, List<KalendarEvent>) -> Unit = { _, _ -> }, // Callback for range selection
    onErrorRangeSelected: (RangeSelectionError) -> Unit = {}      // Callback for error in range selection
) {

// Get the current day if not provided
    val today = currentDay ?: Clock.System.todayIn(TimeZone.currentSystemDefault())

    // Initialize the week value with the next 7 dates from today
    val weekValue = remember { mutableStateOf(today.getNext7Dates()) }

    // Get the current month and year
    val yearAndMonth = getCurrentMonthAndYear(weekValue.value)

    // Initialize selected date with today
    var selectedDate by remember { mutableStateOf(today) }

    // Calculate the index of the current month in the colors list
    val currentMonthIndex = yearAndMonth.first.value.minus(1)

    // Initialize selected date range
    val selectedRange = remember { mutableStateOf<KalendarSelectedDayRange?>(null) }
    Column(
        // Column configuration using a Modifier
        modifier = modifier
            .background(
                color = kalendarColors.color[currentMonthIndex].backgroundColor // Set background color
            )
            .wrapContentHeight()  // Wrap content height
            .fillMaxWidth()  // Fill maximum width
            .padding(all = 8.dp)  // Padding on all sides
    ) {
        // Content inside the Column

        // Check if a custom header content is provided
        if (headerContent != null) {
            headerContent(yearAndMonth.first, yearAndMonth.second) // Call the provided headerContent function
        } else {
            // If no custom header content, use the KalendarHeader component
            KalendarHeader(
                month = yearAndMonth.first,  // Current month
                year = yearAndMonth.second,  // Current year
                kalendarTextKonfig = kalendarHeaderTextKonfig ?: KalendarTextKonfig(
                    // Use provided kalendarHeaderTextKonfig or default configuration
                    kalendarTextColor = kalendarColors.color[currentMonthIndex].headerTextColor,
                    kalendarTextSize = 24.sp
                ),
                onPreviousClick = {
                    // Callback for previous button click
                    val firstDayOfDisplayedWeek = weekValue.value.first()
                    weekValue.value = firstDayOfDisplayedWeek.getPrevious7Dates()
                },
                onNextClick = {
                    // Callback for next button click
                    val lastDayOfDisplayedWeek = weekValue.value.last().plus(1, DateTimeUnit.DAY)
                    weekValue.value = lastDayOfDisplayedWeek.getNext7Dates()
                },
            )
        }

        // Add vertical spacing between header and days
        Spacer(modifier = Modifier.padding(vertical = 4.dp))

        // LazyRow to display a horizontal list of days
        LazyRow(
            modifier = Modifier.fillMaxWidth(),  // Fill maximum width
            content = {
                // Populate the LazyRow with items based on weekValue
                itemsIndexed(weekValue.value) { _, item ->
                    // Create a column for each day
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {

                        // Show the label for the day if showLabel is true
                        if (showLabel) {
                            Text(
                                modifier = Modifier,
                                color = kalendarDayKonfig.textColor,
                                fontSize = kalendarDayKonfig.textSize,
                                text = labelFormat(item.dayOfWeek),
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center
                            )
                        }

                        // Add vertical spacing between label and day content
                        Spacer(modifier = Modifier.padding(vertical = 4.dp))

                        // Check if custom dayContent is provided
                        if (dayContent != null) {
                            dayContent(item)  // Call the provided dayContent function
                        } else {
                            // If no custom dayContent, use the KalendarDay component
                            KalendarDay(
                                date = item,
                                kalendarColors = kalendarColors.color[currentMonthIndex],
                                onDayClick = { clickedDate, event ->
                                    // Callback for day click
                                    onDayClicked(
                                        clickedDate,
                                        event,
                                        daySelectionMode,
                                        selectedRange,
                                        onRangeSelected = { range, events ->
                                            // Callback for range selection
                                            if (range.end < range.start) {
                                                onErrorRangeSelected(RangeSelectionError.EndIsBeforeStart)
                                            } else {
                                                onRangeSelected(range, events)
                                            }
                                        },
                                        onDayClick = { newDate, clickedDateEvent ->
                                            // Callback for day click
                                            selectedDate = newDate
                                            onDayClick(newDate, clickedDateEvent)
                                        }
                                    )
                                },
                                selectedDate = selectedDate,
                                kalendarEvents = events,
                                kalendarDayKonfig = kalendarDayKonfig,
                                selectedRange = selectedRange.value,
                            )
                        }
                    }
                }
            }
        )
    }
}

/**
 * Calculates the current month and year based on a list of dates representing a week.
 * @param weekValue The list of dates representing a week.
 * @return A pair containing the current month and year.
 */
private fun getCurrentMonthAndYear(weekValue: List<LocalDate>): Pair<Month, Int> {
    // Get the month of the first date in the week
    val month = weekValue.first().month

    // Get the year of the first date in the week
    val year = weekValue.first().year

    // Return a pair of the current month and year
    return Pair(month, year)
}

/**
 * Preview function for KalendarOceanic composable.
 * This is used for previewing the composable in Android Studio.
 */
@MultiplePreviews
@Composable
fun KalendarOceanicPreview() {
    // Create a preview of the KalendarOceanic component
    KalendarOceanic(
        currentDay = Clock.System.todayIn(
            TimeZone.currentSystemDefault()
        ),
        kalendarHeaderTextKonfig = KalendarTextKonfig.previewDefault(),
        daySelectionMode = DaySelectionMode.Single
    )
}