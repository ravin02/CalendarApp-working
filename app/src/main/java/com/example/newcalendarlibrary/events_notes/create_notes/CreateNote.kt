package com.example.newcalendarlibrary.events_notes.create_notes

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.newcalendarlibrary.events_notes.NotesViewModel
import com.example.newcalendarlibrary.R
import com.example.newcalendarlibrary.ui.theme.NewCalendarLibraryTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CreateNote(
    navController: NavController,
    notesViewModel: NotesViewModel
) {
    // Mutable state for the current note's text
    val currentNote = remember {
        mutableStateOf("")
    }

    // Mutable state for the current note's title
    val currentTitle = remember {
        mutableStateOf("")
    }

    // Mutable state for the save button's enabled state
    val saveButtonState = remember {
        mutableStateOf(false)
    }

    // Mutable state for the current user
    val currentUser = remember {
        mutableStateOf(notesViewModel.userId.value)
    }

    // Get the context
    val context = LocalContext.current

    // Start composing the UI with NewCalendarLibraryTheme
    NewCalendarLibraryTheme {
        // Create a Surface with a background color
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            // Create a Scaffold to provide a top app bar
            Scaffold(
                topBar = {
                    AppBar(
                        title = "Create Note",
                        onIconClick = {
                            // When the save icon is clicked, create the note and display a toast message
                            notesViewModel.createNote(
                                currentTitle.value,
                                currentNote.value,
                                currentUser.value
                            )
                            Toast.makeText(context, "Note is saved", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        },
                        icon = {
                            // Icon for saving the note
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.save),
                                contentDescription = "save notes",
                                tint = Color.Black,
                            )
                        },
                        iconState = remember { mutableStateOf(true) }
                    )
                },
            ) {
                // Create a Column layout for the content
                Column(
                    Modifier
                        .padding(12.dp)
                        .fillMaxSize()
                ) {
                    // Create a TextField for the note title
                    TextField(
                        value = currentTitle.value,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(5.dp)),
                        onValueChange = { value ->
                            currentTitle.value = value
                            // Enable the save button if both title and note have content
                            saveButtonState.value =
                                currentTitle.value != "" && currentNote.value != ""
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            cursorColor = Color.Black,
                            focusedIndicatorColor = Color.Black,
                            focusedLabelColor = Color.Black
                        ),
                        label = { Text(text = "Title") }
                    )

                    // Add spacing between title and note fields
                    Spacer(modifier = Modifier.padding(12.dp))

                    // Create a TextField for the note content
                    TextField(
                        value = currentNote.value,
                        modifier = Modifier
                            .fillMaxHeight(0.5f)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(5.dp)),
                        onValueChange = { value ->
                            currentNote.value = value
                            // Enable the save button if both title and note have content
                            saveButtonState.value =
                                currentTitle.value != "" && currentNote.value != ""
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            cursorColor = Color.Black,
                            focusedIndicatorColor = Color.Black,
                            focusedLabelColor = Color.Black
                        ),
                        label = { Text(text = "Note") }
                    )
                }
            }
        }
    }
}