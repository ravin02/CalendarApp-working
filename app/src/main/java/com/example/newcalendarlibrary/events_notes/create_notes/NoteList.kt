package com.example.newcalendarlibrary.events_notes.create_notes


import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.newcalendarlibrary.AppointmentEvent
import com.example.newcalendarlibrary.events_notes.NotesViewModel
import com.example.newcalendarlibrary.register.UserViewModel
import com.example.newcalendarlibrary.room.notes.Note
import com.example.newcalendarlibrary.ui.theme.NewCalendarLibraryTheme
import com.example.newcalendarlibrary.R
import com.example.newcalendarlibrary.calendar.EventCard
import com.example.newcalendarlibrary.color_picker.colors
import com.example.newcalendarlibrary.create_notes.AppointmentState
import com.example.newcalendarlibrary.home.NoteCard
import com.example.newcalendarlibrary.navigation.Screens
import com.example.newcalendarlibrary.ui.viewmodel.AddEventViewModel
import com.example.newcalendarlibrary.ui.viewmodel.SettingViewModel
import com.example.newcalendarlibrary.utils.RadioButtonsNavigation


// Suppressing lint warnings related to specific annotations and parameters
@SuppressLint(
    "UnusedMaterial3ScaffoldPaddingParameter", "UnusedMaterialScaffoldPaddingParameter",
    "StateFlowValueCalledInComposition", "QueryPermissionsNeeded"
)
@Composable
fun NoteList(
    navController: NavHostController,  // Navigation controller for navigation within the app
    notesViewModel: NotesViewModel,  // ViewModel for managing notes-related data and actions
    userId: Int,  // User ID for identifying the user associated with the notes
    state: AppointmentState,  // State related to an appointment
    onEvent: (AppointmentEvent) -> Unit,  // Callback for handling appointment events
    navigateToItemUpdate: (Int) -> Unit,  // Callback for navigating to update an item
    viewModel: AddEventViewModel = hiltViewModel(),  // ViewModel for adding an event (defaulting to Hilt dependency injection)
    settingViewModel: SettingViewModel = hiltViewModel(),  // ViewModel for settings (defaulting to Hilt dependency injection)
) {
    // Local ViewModel for managing user-related data and actions
    val userViewModel: UserViewModel = viewModel()

    // Retrieve the onBackPressedDispatcher from the local context
    val backPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    // Mutable state to manage if a dialog is open or closed
    val dialogOpen = remember { mutableStateOf(false) }

    // Mutable state to store text for deletion
    val deleteText = remember {
        mutableStateOf("")
    }

    // Mutable state to store the note query
    val noteQuery = remember {
        mutableStateOf("")
    }

    // Mutable state to store notes for deletion
    val notesDelete = remember {
        mutableStateOf(listOf<Note>())
    }

    // Mutable state to manage the current user's ID
    notesViewModel.userId = remember {
        mutableStateOf(userId)
    }

    // Mutable state to manage if a dialog should be open or closed
    val openDialog = remember { mutableStateOf(false) }

    // Retrieve the notes from the notesViewModel
    val notes = notesViewModel.notes

    // Retrieve the username associated with the given user ID
    val username = notesViewModel.getUserName(userId)

    // Retrieve the current context
    val context = LocalContext.current

    // Mutable state to manage whether the sorting of dates is changed
    val sortDateChange = remember { mutableStateOf(true) }

    // Define the URL you want to open
    val url = "https://www.google.com"

// Handle the click event for the Floating Action Button (FOB)
    val onFobClick = {
        // Create an intent to view the specified URL
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        // Specify the package name for Google Chrome (com.android.chrome)
        intent.setPackage("com.android.chrome")

        // Check if there's an activity to handle the intent
        if (intent.resolveActivity(context.packageManager) != null) {
            // If Google Chrome is installed, open the URL using Google Chrome
            context.startActivity(intent)
        } else {
            // If Google Chrome is not installed, open the URL in the default browser
            val defaultBrowserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            defaultBrowserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(defaultBrowserIntent)
        }
    }

// Set up a callback to handle the back button press
    DisposableEffect(key1 = backPressedDispatcher) {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                dialogOpen.value = true
            }
        }

        backPressedDispatcher?.addCallback(callback)

        onDispose {
            callback.remove()
        }
    }

// Display a dialog for logging out when dialogOpen value is true
    if (dialogOpen.value) {
        LogoutDialog(
            onConfirmLogout = {
                // Perform logout actions and navigate to the login screen
                userViewModel.logout()
                navController.navigate(Screens.LoginScreen.route)
                dialogOpen.value = false
            },
            onDismiss = {
                dialogOpen.value = false
            }
        )
    }

// Manage the state for whether something is currently playing
    var isPlaying by remember {
        mutableStateOf(true)
    }

// Manage the speed state for a particular action (e.g., animation speed)
    var speed by remember {
        mutableStateOf(1f)
    }

// Apply the NewCalendarLibraryTheme
    NewCalendarLibraryTheme {
        // Set up the main surface with a background color based on the settingViewModel's setting
        Surface(
            modifier = Modifier.fillMaxSize().background(colors[settingViewModel.setting]),
            color = colors[settingViewModel.setting]
        ) {
            // Set up the Scaffold with a top bar (AppBar) and a floating action button
            Scaffold(
                topBar = {
                    // Configure the AppBar with a title and an icon (logout)
                    AppBar(
                        title = "Welcome $username!",
                        onIconClick = {
                            // Handle the click on the icon (logout)
                            dialogOpen.value = true
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.logout),
                                contentDescription = "logout",
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        iconState = remember { mutableStateOf(true) }
                    )
                },
                floatingActionButton = {
                    // Configure the Floating Action Button (FAB) with an icon and click event
                    FloatingActionButton(
                        onClick = onFobClick,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_travel_explore_24),
                            contentDescription = null
                        )
                    }
                }
            ) {
                // Content inside the Scaffold
                Column(
                    modifier = Modifier.fillMaxSize().background(colors[settingViewModel.setting]),
                ) {
                    // Retrieve today and this week events from view model as state
                    val today by viewModel.todayEvent.collectAsStateWithLifecycle()
                    val thisWeek by viewModel.weekEvent.collectAsStateWithLifecycle()

                    // Display radio buttons for navigation
                    RadioButtonsNavigation(
                        navController = navController,
                        notesViewModel = notesViewModel,
                        userId = userId,
                        state = state,
                        onEvent = onEvent,
                        navigateToItemUpdate = navigateToItemUpdate
                    )

                    // Manage the selected tab index for Today and This Week tabs
                    var tabIndex by remember { mutableStateOf(0) }

                    // Define the tabs ("Today" and "This Week")
                    val tabs = listOf("Today", "This Week")

                    // Display a TabRow and a LazyColumn with events based on the selected tab
                    ElevatedCard(
                        modifier = Modifier.height(300.dp),
                    ) {
                        TabRow(selectedTabIndex = tabIndex) {
                            tabs.forEachIndexed { index, title ->
                                Tab(
                                    text = { Text(title) },
                                    selected = tabIndex == index,
                                    onClick = { tabIndex = index }
                                )
                            }
                        }

                        LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(
                                items = if (tabIndex == 0) today else thisWeek,
                                key = { it.id }
                            ) { item ->
                                // Display event cards for each event
                                EventCard(
                                    appointment = item,
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .clickable {

                                        },
                                    onEvent = onEvent,
                                    onDeleteEvent = viewModel::deleteEvent
                                )
                            }
                        }
                    }
// Set up the UI components for displaying a list of notes and a search bar
                    SearchBar(noteQuery)
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.End
                    ) {
                        // Display a clickable icon for sorting notes by date
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.sort),
                            contentDescription = "sort",
                            modifier = Modifier
                                .padding(12.dp)
                                .clickable {
                                    // Toggle the sorting order and reload the notes accordingly
                                    sortDateChange.value = sortDateChange.value.not()
                                    notesViewModel.reloadNotes(userId)
                                }
                        )
                    }

// Display the list of notes with relevant parameters passed
                    NotesList(
                        notes = notes.value,
                        query = noteQuery,
                        openDialog = openDialog,
                        deleteText = deleteText,
                        navController = navController,
                        notesToDelete = notesDelete,
                        sortDateChange = sortDateChange
                    )}

// Display the delete dialog for confirming deletion of notes
                DeleteDialog(
                    openDialog = openDialog,
                    text = deleteText,
                    notesToDelete = notesDelete,
                    action = {
                        // Perform the deletion of notes when confirmed
                        notesDelete.value.forEach {
                            notesViewModel.deleteNotes(it)
                        }
                    }
                )
            }
        }
    }
}
@Composable
fun SearchBar(query: MutableState<String>) {
    // Column composable to arrange UI components vertically
    Column(Modifier.padding(top = 12.dp, start = 12.dp, end = 12.dp, bottom = 12.dp)) {
        // TextField for entering the search query
        TextField(
            value = query.value,  // The value of the TextField
            placeholder = { Text("Search note...") },  // Placeholder text when TextField is empty
            maxLines = 1,  // Maximum lines allowed in the TextField
            onValueChange = { query.value = it },  // Callback when the value changes
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))  // Rounded corners for TextField
                .fillMaxWidth(),  // Fill the maximum width available

            // Customizing TextField colors
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
                focusedIndicatorColor = Color.Black,
                focusedLabelColor = Color.Black,
                cursorColor = Color.Black
            ),

            // Trailing icon in the TextField (clears the search query)
            trailingIcon = {
                AnimatedVisibility(
                    visible = query.value.isNotEmpty(),  // Show icon if query is not empty
                    enter = fadeIn(),  // Fade in animation for the icon
                    exit = fadeOut()  // Fade out animation for the icon when not visible
                ) {
                    // IconButton with a clear icon to reset the search query
                    IconButton(onClick = { query.value = "" }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.plus),
                            contentDescription = "search"
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun NotesList(
    notes: List<Note>,  // List of notes to display
    openDialog: MutableState<Boolean>,  // State for managing if a dialog is open
    query: MutableState<String>,  // Search query for filtering notes
    deleteText: MutableState<String>,  // Text for deletion confirmation
    navController: NavController,  // Navigation controller
    notesToDelete: MutableState<List<Note>>,  // Notes selected for deletion
    sortDateChange: MutableState<Boolean>  // State for managing sorting order
) {
    // Filter and sort the notes based on the search query and sorting order
    val queriedNotes = if (query.value.isEmpty()) {
        if (sortDateChange.value) {
            notes.sortedByDescending { it.dateUpdated }
        } else {
            notes.sortedBy { it.dateUpdated }
        }
    } else {
        notes.filter { it.description.contains(query.value) || it.title.contains(query.value) }
    }

    if (queriedNotes.isEmpty()) {
        // Display a message when no notes are found
        Text(
            text = "No notes found. Please add a new note.",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )
        // Display an option to create a new note
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(start = 20.dp)
                .clickable {
                    navController.navigate("createnote_page")
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_library_add_24),
                contentDescription = "add notes button",
            )
            Card(Modifier.wrapContentSize(), elevation = 6.dp) {
                Text(text = "Create Notes", textAlign = TextAlign.Center)
            }
        }
    } else {
        // Display a LazyColumn with the queried notes
        LazyColumn(
            contentPadding = PaddingValues(12.dp),
            modifier = Modifier
        ) {
            itemsIndexed(queriedNotes) { _, note ->
                // Display each note item in the list
                NoteListItem(
                    note,
                    openDialog,
                    deleteText = deleteText,
                    navController,
                    notesToDelete = notesToDelete,
                    noteBackGround = Color.Black
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                )
            }
            // Display a FloatingActionButton for creating a new note
            item {
                NotesFloatingActionButton(
                    contentDescription = "create notes",
                    action = { navController.navigate("createnote_page") },
                    icon = R.drawable.note_add_icon
                )
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteListItem(
    note: Note,  // Note data for this item
    openDialog: MutableState<Boolean>,  // State for managing if a dialog is open
    deleteText: MutableState<String>,  // Text for deletion confirmation
    navController: NavController,  // Navigation controller
    noteBackGround: Color,  // Background color for the note item
    notesToDelete: MutableState<List<Note>>  // Notes selected for deletion
) {
    // Box composable to contain the note item with specified height and rounded corners
    return Box(
        modifier = Modifier
            .height(120.dp)
            .clip(RoundedCornerShape(12.dp))
    ) {
        // Column composable to arrange UI components vertically
        Column(
            modifier = Modifier
                .background(noteBackGround)  // Background color for the note item
                .fillMaxWidth()
                .height(120.dp)
                .combinedClickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = false),
                    onClick = {
                        // Navigate to the note detail page on click
                        if (note.id != 0) {
                            navController.navigate(Constants.noteDetailNavigation(note.id))
                        }
                    },
                    onLongClick = {
                        // Show delete confirmation dialog on long click
                        if (note.id != 0) {
                            openDialog.value = true
                            println(note.id)
                            deleteText.value = "Are you sure you want to delete this note ?"
                            notesToDelete.value = mutableListOf(note)
                        }
                    }
                )
        ) {
            // Display the note title and description
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = note.title,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
                )
                Text(
                    text = note.description,
                    color = Color.White,
                    fontSize = 16.sp,
                    maxLines = 2,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
            // Display the note's update date
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.BottomEnd)
            ) {
                Text(
                    text = note.dateUpdated,
                    fontSize = 14.sp,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                )
            }
        }
    }
}


// Composable function to create a FloatingActionButton for creating notes
@Composable
fun NotesFloatingActionButton(contentDescription: String, icon: Int, action: () -> Unit) {
    // Define a FloatingActionButton with specified onClick action and appearance
    return FloatingActionButton(
        onClick = { action.invoke() },  // Execute the provided action on button click
        backgroundColor = Color.Black,  // Set the background color of the button
        contentColor = Color.White,  // Set the content color (icon color) of the button
        modifier = Modifier
            .border(2.dp, Color.White, CircleShape)  // Apply a border to the button
    ) {
        // Display an icon within the FloatingActionButton
        Icon(
            ImageVector.vectorResource(id = icon),  // Use the provided icon resource
            contentDescription = contentDescription,  // Provide content description for accessibility
            tint = Color.White  // Apply a tint to the icon (color it white)
        )
    }
}

// Composable function to create a deletion confirmation dialog
@Composable
fun DeleteDialog(
    openDialog: MutableState<Boolean>,  // State for managing dialog visibility
    text: MutableState<String>,  // Text to display in the dialog
    action: () -> Unit,  // Action to perform on "Yes" button click
    notesToDelete: MutableState<List<Note>>  // Notes selected for deletion
) {
    // Display the AlertDialog only when openDialog is true
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false  // Close the dialog on dismiss request
            },
            title = {
                Text(text = "Delete Note")  // Display the dialog title
            },
            text = {
                Column {
                    Text(text.value)  // Display the provided text in the dialog
                }
            },
            buttons = {
                Row(
                    modifier = Modifier.padding(all = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column {
                        // "Yes" button to confirm deletion
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Black,
                                contentColor = Color.White
                            ),
                            onClick = {
                                action.invoke()  // Execute the provided action (deletion)
                                openDialog.value = false  // Close the dialog
                                notesToDelete.value = mutableListOf()  // Clear notes to delete
                            }
                        ) {
                            Text("Yes")  // Display button label as "Yes"
                        }

                        Spacer(modifier = Modifier.padding(12.dp))  // Spacer for separation

                        // "No" button to cancel deletion
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Black,
                                contentColor = Color.White
                            ),
                            onClick = {
                                openDialog.value = false  // Close the dialog
                                notesToDelete.value = mutableListOf()  // Clear notes to delete
                            }
                        ) {
                            Text("No")  // Display button label as "No"
                        }
                    }
                }
            }
        )
    }
}

// Composable function to create a logout confirmation dialog
@Composable
fun LogoutDialog(
    onConfirmLogout: () -> Unit,  // Action to perform on logout confirmation
    onDismiss: () -> Unit  // Action to perform on dialog dismissal (cancel)
) {
    // Display an AlertDialog for logout confirmation
    AlertDialog(
        onDismissRequest = onDismiss,  // Execute onDismiss action on dismiss request
        title = {
            Text(text = "Logout", fontWeight = FontWeight.Bold)  // Display dialog title
        },
        text = {
            Text("Are you sure you want to logout?", fontWeight = FontWeight.SemiBold)  // Display dialog text
        },
        confirmButton = {
            // "Logout" button to confirm logout
            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
                onClick = onConfirmLogout
            ) {
                Text("Logout", color = Color.White)  // Display button label as "Logout"
            }
        },
        dismissButton = {
            // "Cancel" button to cancel logout
            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
                onClick = onDismiss
            ) {
                Text("Cancel", color = Color.White)  // Display button label as "Cancel"
            }
        }
    )
}