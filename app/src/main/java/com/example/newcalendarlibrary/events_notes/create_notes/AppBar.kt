package com.example.newcalendarlibrary.events_notes.create_notes

import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun AppBar(
    title: String,
    onIconClick: (() -> Unit)?,
    icon: @Composable (() -> Unit)?,
    iconState: MutableState<Boolean>
) {
    // Create a Composable function named AppBar with the specified parameters.

    TopAppBar(
        // Use a TopAppBar Composable from the Jetpack Compose library.

        title = { Text(title, fontWeight = FontWeight.Bold) },
        // Set the title of the TopAppBar to the provided title string, with bold font.

        backgroundColor = Color.White,
        // Set the background color of the TopAppBar to white.

        actions = {
            // Define actions for the TopAppBar.

            IconButton(
                onClick = {
                    onIconClick?.invoke()
                },
                // Set the onClick behavior for the IconButton to invoke the provided onIconClick callback.

                content = {
                    if (iconState.value){
                        icon?.invoke()
                    }
                    // If the iconState value is true, invoke the provided icon Composable.
                }
            )
        }
    )
}