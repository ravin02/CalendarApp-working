package com.example.newcalendarlibrary.color_picker

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp


// You can add as much colors as you like
val colors = listOf(
    Color(0xFFFFFFFF),
    Color(0xFFEF9A9A),
    Color(0xFFF48FB1),
    Color(0xFF80CBC4),
    Color(0xFFA5D6A7),
    Color(0xFFFFCC80),
    Color(0xFFFFAB91),
    Color(0xFF81D4FA),
    Color(0xFFCE93D8),
    Color(0xFFB39DDB)
)

@Composable
fun ColourButton(colors: List<Color>, onColorSelected: (Int) -> Unit, modifier: Modifier = Modifier, selected: Int = 0) {
    // State variables to track color picker state and currently selected color
    var colorPickerOpen by rememberSaveable { mutableStateOf(false) }
    var currentlySelected by rememberSaveable(saver = colourSaver()) { mutableStateOf(colors[selected]) }

    // Layout for displaying the color selection button
    Box(
        modifier = modifier
            .padding(top = 16.dp, start = 5.dp, end = 5.dp, bottom = 5.dp)
            .fillMaxWidth(0.8f)
            .clip(RoundedCornerShape(20))
            .border(
                2.dp,
                MaterialTheme.colors.onBackground.copy(alpha = 0.75f),
                RoundedCornerShape(20)
            )
            .clickable {
                colorPickerOpen = true
            }
    ) {
        // Layout for arranging items horizontally
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Display text to instruct color selection
            Text(
                text = "Select colour",
            )

            // Display a canvas representing the currently selected color
            Canvas(
                modifier = modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(20))
                    .border(
                        1.dp,
                        MaterialTheme.colors.onBackground.copy(alpha = 0.75f),
                        RoundedCornerShape(20)
                    )
                    .background(currentlySelected)
                    .clickable {
                        colorPickerOpen = true
                    }
            ) {}
        }
    }


    if (colorPickerOpen) {
        // Display a color selection dialog if the color picker is open
        TwoColorDialog(
            colorList = colors,
            onDismiss = { colorPickerOpen = false }, // Handle dialog dismissal by setting colorPickerOpen to false
            currentlySelected = currentlySelected, // Pass the currently selected color to the dialog
            onColorSelected = {
                // Update the currently selected color and invoke the onColorSelected callback with the selected color index
                currentlySelected = it
                onColorSelected(colors.indexOf(it))
            }
        )
    }
}
@Composable
private fun ColorDialog(
    colorList: List<Color>, // List of colors to display in the dialog
    onDismiss: (() -> Unit), // Callback when the dialog is dismissed
    currentlySelected: Color, // Currently selected color
    onColorSelected: ((Color) -> Unit) // Callback when a color is selected
) {
    val gridState = rememberLazyGridState() // Remember the state of the lazy grid

    // Create an AlertDialog to display color options
    AlertDialog(
        shape = RoundedCornerShape(20.dp), // Rounded corners for the dialog
        backgroundColor = MaterialTheme.colors.background, // Set the dialog's background color
        contentColor = MaterialTheme.colors.onBackground, // Set the content color
        onDismissRequest = onDismiss, // Set the callback for dismissal
        text = {
            // Display a grid of colors for selection
            LazyVerticalGrid(
                columns = GridCells.Fixed(3), // Display in 3 columns
                state = gridState // Set the grid state
            ) {
                // Iterate through the color list and display each color
                items(colorList) { color ->
                    var borderWidth = 0.dp
                    if (currentlySelected == color) {
                        borderWidth = 2.dp // Highlight the selected color
                    }

                    // Display individual color as a clickable element
                    Canvas(modifier = Modifier
                        .padding(16.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .border(
                            borderWidth,
                            MaterialTheme.colors.onBackground.copy(alpha = 0.75f),
                            RoundedCornerShape(20.dp)
                        )
                        .background(color) // Set the color background
                        .requiredSize(70.dp) // Set the required size
                        .clickable {
                            // Trigger the onColorSelected callback and dismiss the dialog
                            onColorSelected(color)
                            onDismiss()
                        }
                    ) {
                        // Content of the Canvas, in this case, displaying the color
                    }
                }
            }
        },
        confirmButton = {} // No confirm button in this dialog
    )
}


@Composable
private fun TwoColorDialog(
    colorList: List<Color>, // List of colors to display in the dialog
    onDismiss: (() -> Unit), // Callback when the dialog is dismissed
    currentlySelected: Color, // Currently selected color
    onColorSelected: ((Color) -> Unit) // Callback when a color is selected
) {
    val gridState = rememberLazyGridState() // Remember the state of the lazy grid

    // Create an AlertDialog to display color options
    AlertDialog(
        shape = RoundedCornerShape(20.dp), // Rounded corners for the dialog
        backgroundColor = MaterialTheme.colors.background, // Set the dialog's background color
        contentColor = MaterialTheme.colors.onBackground, // Set the content color
        onDismissRequest = onDismiss, // Set the callback for dismissal
        text = {
            // Display a grid of colors for selection
            LazyVerticalGrid(
                columns = GridCells.Fixed(3), // Display in 3 columns
                state = gridState // Set the grid state
            ) {
                // Iterate through the color list and display each color
                items(colorList) { color ->
                    var borderWidth = 0.dp
                    if (currentlySelected == color) {
                        borderWidth = 2.dp // Highlight the selected color
                    }

                    // Display individual color as a clickable element
                    Canvas(modifier = Modifier
                        .padding(16.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .border(
                            borderWidth,
                            MaterialTheme.colors.onBackground.copy(alpha = 0.75f),
                            RoundedCornerShape(20.dp)
                        )
                        // .background(color) // Background color (commented out)
                        .requiredSize(70.dp) // Set the required size
                        .clickable {
                            // Trigger the onColorSelected callback and dismiss the dialog
                            onColorSelected(color)
                            onDismiss()
                        }
                    ) {
                        val canvasWidth = size.width
                        val canvasHeight = size.height

                        // Draw two paths to create a "two-color" effect
                        drawPath(Path().apply {
                            moveTo(0f, 0f)
                            lineTo(canvasWidth, 0f)
                            lineTo(0f, canvasHeight)
                            close()
                        }, color = color)

                        drawPath(Path().apply {
                            moveTo(canvasWidth, 0f)
                            lineTo(0f, canvasHeight)
                            lineTo(canvasWidth, canvasHeight)
                            close()
                        }, color = color.copy(alpha = 0.6f))

                    }
                }
            }
        },
        confirmButton = {} // No confirm button in this dialog
    )
}

// Utility function to save and restore Color state
fun colourSaver() = Saver<MutableState<Color>, String>(
    save = { state -> state.value.toHexString() },
    restore = { value -> mutableStateOf(value.toColor()) }
)

// Extension function to convert Color to its hexadecimal representation
fun Color.toHexString(): String {
    // Convert the Color to its hexadecimal representation
    return String.format(
        "#%02x%02x%02x%02x", (this.alpha * 255).toInt(),
        (this.red * 255).toInt(), (this.green * 255).toInt(), (this.blue * 255).toInt()
    )
}

// Extension function to convert hexadecimal string to Color
fun String.toColor(): Color {
    // Convert the hexadecimal string to a Color object
    return Color(android.graphics.Color.parseColor(this))
}
