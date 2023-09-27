package com.example.newcalendarlibrary.register

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.newcalendarlibrary.MainActivity
import com.example.newcalendarlibrary.R
import com.example.newcalendarlibrary.events_notes.NotesViewModel
import com.example.newcalendarlibrary.navigation.Graph
import com.example.newcalendarlibrary.navigation.Screens
import com.example.newcalendarlibrary.navigation.components.BottomBarScreen
import com.example.newcalendarlibrary.ui.theme.primaryColor
import com.example.newcalendarlibrary.ui.theme.textWhiteColor
import com.example.newcalendarlibrary.ui.viewmodel.MyUserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.system.exitProcess
private const val TAG = "Login"  // Define a constant TAG for logging purposes.

@SuppressLint("SuspiciousIndentation")  // Suppress lint warning about suspicious indentation.

@Composable
fun LoginPage(
    navController: NavController,  // NavController for navigating to different screens.
    userViewModel: UserViewModel = viewModel(),  // ViewModel for managing user data.
    notesViewModel: NotesViewModel = viewModel(),  // ViewModel for managing notes data.
    viewModel: MyUserViewModel = hiltViewModel(),  // ViewModel for user management.
) {
    // Initialize state variables to hold user input for login and password.
    val loginState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val passwordVisibility = remember { mutableStateOf(false) }

    // Get the current context and backPressedDispatcher.
    val context = LocalContext.current
    val backPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    // State variable to control the visibility of a dialog for exit confirmation.
    val dialogOpen = remember { mutableStateOf(false) }

    // State variable to control the visibility of another dialog (not used in this snippet).
    val dialogOpen1 = remember { mutableStateOf(false) }

    // Setup a callback to handle back button presses.
    DisposableEffect(key1 = backPressedDispatcher) {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                dialogOpen.value = true  // Show exit confirmation dialog on back button press.
            }
        }
        backPressedDispatcher?.addCallback(callback)
        onDispose {
            callback.remove()
        }
    }

    // Show exit confirmation dialog if dialogOpen is true.
    if (dialogOpen.value) {
        ExitDialog(
            onConfirmExit = {
                dialogOpen.value = false
                val activity = MainActivity()
                activity.finish()  // Finish the activity to exit the app.
                exitProcess(0)  // Exit the app.
            },
            onDismiss = {
                dialogOpen.value = false
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()  // Fills the maximum available size within its parent.
            .background(Color.White),  // Sets the background color to white.
        contentAlignment = Alignment.BottomCenter  // Aligns content to the bottom center of the box.
    ) {

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,  // Aligns content horizontally to the center.
            verticalArrangement = Arrangement.Center,  // Arranges content vertically at the center.
            modifier = Modifier
                .fillMaxWidth()  // Fills the maximum available width.
                .fillMaxHeight(0.55f)  // Fills 55% of the maximum available height.
                .background(Color.White)  // Sets the background color to white.
        ) {
            item {
                // Displays the "Login" text with specified font properties.
                Text(
                    text = "Login",
                    fontSize = 30.sp,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                )
                Spacer(modifier = Modifier.padding(5.dp))  // Adds space with a 5dp padding.
                // Provides an input field for the login name.
                OutlinedTextField(
                    value = loginState.value,
                    onValueChange = {
                        loginState.value = it  // Updates the loginState value on input change.
                        userViewModel.logout()  // Calls a function to handle logout in view model.
                    },
                    label = { Text(text = "Login name") },  // Label for the input field.
                    placeholder = { Text(text = "Login name") },  // Placeholder text for the input field.
                    singleLine = true,  // Restricts input to a single line.
                    modifier = Modifier.fillMaxWidth(0.88f),  // Sets the width to 88% of the available width.
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.Black,
                        focusedBorderColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        cursorColor = Color.Black
                    )  // Sets colors for the outlined text field.
                )

                OutlinedTextField(
                    value = passwordState.value,  // Binds the value to the passwordState variable.
                    onValueChange = {
                        passwordState.value = it  // Updates the passwordState value on input change.
                        userViewModel.logout()  // Calls a function to handle logout in view model.
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            passwordVisibility.value = !passwordVisibility.value  // Toggles password visibility.
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.password_hide),  // Icon for hiding/showing password.
                                contentDescription = null,
                                tint = if (passwordVisibility.value) primaryColor else Color.Gray  // Icon tint based on password visibility.
                            )
                        }
                    },
                    label = { Text("Password") },  // Label for the password input field.
                    placeholder = { Text(text = "Password") },  // Placeholder text for the password input field.
                    singleLine = true,  // Restricts input to a single line.
                    visualTransformation = if (passwordVisibility.value) VisualTransformation.None
                    else PasswordVisualTransformation(),  // Controls the visual transformation of the password input.
                    modifier = Modifier.fillMaxWidth(0.88f),  // Sets the width to 88% of the available width.
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.Black,
                        focusedBorderColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        cursorColor = Color.Black
                    )  // Sets colors for the outlined text field.
                )

                Spacer(modifier = Modifier.padding(10.dp))  // Adds space with a 10dp padding.

                Button(
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),  // Sets button colors.
                    onClick = {
                        userViewModel.logout()  // Calls a function to handle logout in view model.

                        // Check if the user exists and validate the password.
                        val userExists = runBlocking {
                            userViewModel.checkIfUserExists(loginState.value)
                        }

                        if (userExists) {
                            val validPassword = runBlocking {
                                userViewModel.checkPassword(
                                    loginState.value,
                                    passwordState.value
                                )
                            }

                            if (passwordState.value.isNotEmpty() && validPassword) {
                                // Login the user and navigate to Home screen.
                                runBlocking {
                                    userViewModel.login(
                                        loginState.value,
                                        passwordState.value
                                    )
                                }

                                userViewModel.currentUser.value!!.id?.let {
                                    notesViewModel.reloadNotes(it)
                                }

                                viewModel.storeUser(loginState.value)

                                // Navigate to the Home screen for the logged-in user.
                                val ctrl = navController.navigate("${BottomBarScreen.Home.route}/${userViewModel.currentUser.value!!.id}")
                                Log.d(TAG, "Navigation is $ctrl")
                            } else {
                                // Show a toast for incorrect login or password.
                                Toast.makeText(
                                    context,
                                    "Incorrect login or password.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            // Show a toast for a non-existing user.
                            Toast.makeText(
                                context,
                                "User doesn't exist.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.6f)  // Sets the width to 60% of the available width.
                        .height(50.dp)  // Sets the button height to 50dp.
                        .clip(CircleShape)  // Clips the button to a circular shape.
                ) {
                    Text(text = "Sign In", fontSize = 20.sp, color = textWhiteColor)  // Text content and style for the button.
                }

                Spacer(modifier = Modifier.padding(20.dp))  // Adds space with a 20dp padding.

                Text(
                    text = "Create an Account",
                    modifier = Modifier.clickable(onClick = {
                        // Navigate to the SignUpScreen when the text is clicked.
                        navController.navigate(Screens.SignUpScreen.route) {
                            launchSingleTop = true
                        }
                    })
                )
                Spacer(modifier = Modifier.padding(20.dp))  // Adds space with a 20dp padding.
            }
        }
    }
}
@Composable
fun ExitDialog(
    onConfirmExit: () -> Unit,  // Callback function for confirming the exit action.
    onDismiss: () -> Unit  // Callback function for dismissing the dialog.
) {
    // AlertDialog to confirm exiting the application.
    AlertDialog(
        onDismissRequest = onDismiss,  // Handles the dismissal of the dialog.
        title = {
            Text(text = "Exit", fontWeight = FontWeight.Bold)  // Title of the dialog.
        },
        text = {
            Text("Are you sure you want to exit?", fontWeight = FontWeight.SemiBold)  // Text content of the dialog.
        },
        confirmButton = {
            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),  // Button colors.
                onClick = onConfirmExit  // Calls the onConfirmExit callback on button click.
            ) {
                Text("Exit", color = Color.White)  // Text content and style for the exit button.
            }
        },
        dismissButton = {
            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),  // Button colors.
                onClick = onDismiss  // Calls the onDismiss callback on button click.
            ) {
                Text("Cancel", color = Color.White)  // Text content and style for the cancel button.
            }
        }
    )
}