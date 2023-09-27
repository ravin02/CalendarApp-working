
import androidx.compose.runtime.MutableState
import com.example.newcalendarlibrary.calendar.KalendarEvent
import com.himanshoe.kalendar.ui.firey.DaySelectionMode
import com.himanshoe.kalendar.ui.firey.KalendarSelectedDayRange
import kotlinx.datetime.LocalDate

/**
 * Internal function invoked when a day is clicked.
 *
 * @param date The clicked date.
 * @param events The events associated with the clicked date.
 * @param daySelectionMode The day selection mode.
 * @param selectedRange The state holding the selected day range.
 * @param onRangeSelected Callback invoked when a range of days is selected.
 * @param onDayClick Callback invoked when a day is clicked.
 */
internal fun onDayClicked(
    date: LocalDate,
    events: List<KalendarEvent>,
    daySelectionMode: DaySelectionMode,
    selectedRange: MutableState<KalendarSelectedDayRange?>,
    onRangeSelected: (KalendarSelectedDayRange, List<KalendarEvent>) -> Unit = { _, _ -> },
    onDayClick: (LocalDate, List<KalendarEvent>) -> Unit = { _, _ -> }
) {
    // Handle the day click based on the day selection mode
    when (daySelectionMode) {
        DaySelectionMode.Single -> {
            // Invoke the callback for a single day click
            onDayClick(date, events)
        }

        DaySelectionMode.Range -> {
            // Handle day click for range selection mode
            val range = selectedRange.value
            if (range != null) {
                // Update the selected range based on the current state
                selectedRange.value = when {
                    range.isEmpty() != false -> KalendarSelectedDayRange(start = date, end = date)
                    range.isSingleDate() -> KalendarSelectedDayRange(start = range.start, end = date)
                    else -> KalendarSelectedDayRange(start = date, end = date)
                }
            }

            selectedRange.value?.let { rangeDates ->
                // Retrieve events within the selected date range
                val selectedEvents = events
                    .filter { it.date in (rangeDates.start..rangeDates.end) }
                    .toList()

                // Invoke the callback with the selected range and associated events
                onRangeSelected(rangeDates, selectedEvents)
            }
        }
    }
}
