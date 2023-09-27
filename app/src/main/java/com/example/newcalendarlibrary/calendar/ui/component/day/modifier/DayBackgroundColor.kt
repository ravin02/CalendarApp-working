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

package com.himanshoe.kalendar.ui.component.day.modifier

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.himanshoe.kalendar.ui.firey.KalendarSelectedDayRange
import kotlinx.datetime.LocalDate

// Constants for alpha values
private const val FULL_ALPHA = 1f
private const val TOWNED_DOWN_ALPHA = 0.4F

/**

A modifier that applies the background color to a composable based on the day's selection state,
selected range, and specified color.
 *
@param selected Whether the day is selected.
@param color The color to be applied as the background.
@param date The date corresponding to the day.
@param selectedRange The selected date range.
 *
@return The modified modifier.
 */
fun Modifier.dayBackgroundColor(
    selected: Boolean,  // Whether the day is selected
    color: Color,  // The color to be applied as the background
    date: LocalDate,  // The date corresponding to the day
    selectedRange: KalendarSelectedDayRange?  // The selected date range
): Modifier {
    // Check if the date is in the selected range
    val inRange = date == selectedRange?.start || date == selectedRange?.end

    // Determine the background color based on selection and date range
    val backgroundColor = when {
        selected -> color  // Use the provided color if the day is selected
        selectedRange != null && date in selectedRange.start..selectedRange.end -> {
            val alpha = if (inRange) FULL_ALPHA else TOWNED_DOWN_ALPHA
            color.copy(alpha = alpha)  // Adjust alpha based on being in range or not
        }
        else -> Color.Transparent  // Use a transparent color if not selected or in range
    }

    // Apply the background color as a modifier
    return this.then(
        background(backgroundColor)
    )
}
