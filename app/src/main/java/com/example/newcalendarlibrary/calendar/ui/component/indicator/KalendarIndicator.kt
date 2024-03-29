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

package com.himanshoe.kalendar.ui.component.indicator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**

Composable function that renders a Kalendar indicator.*
@param index The index of the indicator.
@param size The size of the indicator.
@param color The color of the indicator.
@param modifier The modifier for the indicator.
 */
@Composable
fun KalendarIndicator(
    index: Int,  // The index of the indicator
    size: Dp,  // The size of the indicator
    color: Color,  // The color of the indicator
    modifier: Modifier = Modifier,  // Modifier for the indicator
) {
    // Create a Box composable to represent the indicator
    Box(
        modifier = modifier
            .padding(horizontal = 1.dp)  // Add horizontal padding to the indicator
            .clip(shape = CircleShape)  // Clip the indicator to a circular shape
            .background(color = color.copy(alpha = (index + 1) * 0.3f))  // Set the background color with adjusted alpha based on index
            .size(size = size.div(12))  // Set the size of the indicator based on the provided size
    )
}