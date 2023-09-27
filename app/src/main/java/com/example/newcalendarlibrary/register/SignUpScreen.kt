package com.example.newcalendarlibrary.register

import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.newcalendarlibrary.R
import com.example.newcalendarlibrary.navigation.Screens
import com.example.newcalendarlibrary.ui.theme.primaryColor
import com.example.newcalendarlibrary.ui.theme.textWhiteColor
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

// Define a Composable function for the registration page

@Composable
fun RegisterPage(
    navController: NavController,
    userViewModel: UserViewModel = viewModel()
) {
    // Define mutable state variables for various form fields and their error states

    val nameValue = remember { mutableStateOf("") }
    val loginValue = remember { mutableStateOf("") }
    val passwordValue = remember { mutableStateOf("") }
    val confirmPasswordValue = remember { mutableStateOf("") }
    val passwordVisibility = remember { mutableStateOf(false) }
    val confirmPasswordVisibility = remember { mutableStateOf(false) }

    val nameErrorState = remember { mutableStateOf(false) }
    val loginErrorState = remember { mutableStateOf(false) }
    val passwordErrorState = remember { mutableStateOf(false) }
    val confirmPasswordErrorState = remember { mutableStateOf(false) }

    // Get the current context
    val context = LocalContext.current

    // Initialize a variable to check if a user with the same login already exists
    var userExist: Boolean

    // Initialize a variable to track if all form fields are filled
    var allFields = true

    // Create the UI layout for the registration page using Compose
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Create a LazyColumn to hold the UI elements vertically centered
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    // Title text for the registration page
                    Text(
                        text = "Welcome!",
                        fontSize = 30.sp,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )

                    )
                    Spacer(modifier = Modifier.padding(5.dp))
                    // Text field for user login
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        OutlinedTextField(
                            value = loginValue.value,
                            onValueChange = {

                                if (loginErrorState.value) {
                                    loginErrorState.value = false
                                }
                                loginValue.value = it
                                runBlocking {
                                    userExist =
                                        userViewModel.checkIfUserExists(loginValue.value)
                                }
                            },

                            isError = loginErrorState.value,
                            label = {

                                if (loginErrorState.value) {
                                    Text(text = "Required", color = Color.Red)
                                } else {
                                    Text(text = "Username")
                                }
                            },
                            placeholder = { Text(text = "Username") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(0.8f),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                textColor = Color.Black,
                                focusedBorderColor = Color.Black,
                                focusedLabelColor = Color.Black
                            )
                        )

                        // Text field for name of user
                        OutlinedTextField(
                            value = nameValue.value,
                            onValueChange = {
                                if (nameErrorState.value) {
                                    nameErrorState.value = false
                                }
                                nameValue.value = it
                            },

                            isError = nameErrorState.value,
                            label = {

                                if (nameErrorState.value) {
                                    Text(text = "Required", color = Color.Red)
                                } else {
                                    Text(text = "Name")
                                }
                            },
                            placeholder = { Text(text = "Name") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(0.8f),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                textColor = Color.Black,
                                focusedBorderColor = Color.Black,
                                focusedLabelColor = Color.Black
                            )
                        )
                        // Text field for password

                        OutlinedTextField(
                            value = passwordValue.value,
                            onValueChange = {
                                if (passwordErrorState.value) {
                                    passwordErrorState.value = false
                                }
                                passwordValue.value = it
                            },
                            isError = passwordErrorState.value,
                            label = {
                                if (passwordErrorState.value) {
                                    Text(text = "Required", color = Color.Red)
                                } else {
                                    Text(text = "Password")
                                }
                            },
                            placeholder = { Text(text = "Password") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(0.8f),
                            trailingIcon = {
                                IconButton(onClick = {
                                    passwordVisibility.value = !passwordVisibility.value
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.password_hide),
                                        contentDescription = null,
                                        tint = if (passwordVisibility.value) primaryColor else Color.Gray
                                    )
                                }
                            },
                            visualTransformation = if (passwordVisibility.value) VisualTransformation.None
                            else PasswordVisualTransformation(),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                textColor = Color.Black,
                                focusedBorderColor = Color.Black,
                                focusedLabelColor = Color.Black
                            )
                        )
                        // Text field for confirming password
                        OutlinedTextField(
                            value = confirmPasswordValue.value,
                            onValueChange = {
                                if (confirmPasswordErrorState.value) {
                                    confirmPasswordErrorState.value = false
                                }
                                confirmPasswordValue.value = it
                            },
                            isError = confirmPasswordErrorState.value,
                            label = {
                                if (confirmPasswordErrorState.value) {
                                    Text(text = "Required", color = Color.Red)
                                } else {
                                    Text(text = "Re-enter password")
                                }
                            },
                            placeholder = { Text(text = "Re-enter password") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(0.8f),
                            trailingIcon = {
                                IconButton(onClick = {
                                    confirmPasswordVisibility.value =
                                        !confirmPasswordVisibility.value
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.password_hide),
                                        contentDescription = null,
                                        tint = if (confirmPasswordVisibility.value) primaryColor else Color.Gray
                                    )
                                }
                            },
                            visualTransformation = if (confirmPasswordVisibility.value) VisualTransformation.None
                            else PasswordVisualTransformation(),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                textColor = Color.Black,
                                focusedBorderColor = Color.Black,
                                focusedLabelColor = Color.Black
                            )
                        )
                        Spacer(modifier = Modifier.padding(10.dp))
                        // Sign-up button
                        Button(
                            // This is a button that performs an action when clicked.
                            onClick = {
                                runBlocking {
                                    // This block runs a coroutine to perform the following operation.
                                    userExist =
                                        userViewModel.checkIfUserExists(loginValue.value)
                                }

                                // Check if there are no errors in input fields and set 'allFields' to true if all conditions are met.
                                if (!loginErrorState.value && !nameErrorState.value && !passwordErrorState.value && !confirmPasswordErrorState.value) {
                                    allFields = true
                                }

                                // Check if the login input field is empty and set 'loginErrorState' to true and 'allFields' to false if empty.
                                if (loginValue.value.isEmpty()) {
                                    loginErrorState.value = true
                                    allFields = false
                                }

                                // Check if the password input field is empty and set 'passwordErrorState' to true and 'allFields' to false if empty.
                                if (passwordValue.value.isEmpty()) {
                                    passwordErrorState.value = true
                                    allFields = false
                                }

                                // Check if the name input field is empty and set 'nameErrorState' to true and 'allFields' to false if empty.
                                if (nameValue.value.isEmpty()) {
                                    nameErrorState.value = true
                                    allFields = false
                                }

                                // Check if the confirmPassword input field is empty and set 'confirmPasswordErrorState' to true and 'allFields' to false if empty.
                                if (confirmPasswordValue.value.isEmpty()) {
                                    confirmPasswordErrorState.value = true
                                    allFields = false
                                }

                                // Display a toast message if not all fields are filled.
                                if (!allFields) {
                                    Toast.makeText(
                                        context,
                                        "Please fill all fields",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }

                                // Check if the password and confirmPassword do not match and display a toast message if they don't match.
                                if (passwordValue.value != confirmPasswordValue.value) {
                                    Toast.makeText(
                                        context,
                                        "Passwords aren't matching, please try to type them again",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                // Check if the password doesn't meet length and capitalization requirements and display a toast message if not met.
                                else if (passwordValue.value.length < 8 || passwordValue.value == passwordValue.value.lowercase()) {
                                    Toast.makeText(
                                        context,
                                        "Passwords length must be more than 8 characters and with at least one capital letter",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                // Check if the user with the given login already exists and display a toast message if they do.
                                else if (userExist) {
                                    Toast.makeText(
                                        context,
                                        "User with login '${loginValue.value}' already exists! Please choose a different login.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    // If all conditions are met and 'allFields' is true, register the user and display a success message.
                                    if (allFields) {
                                        userViewModel.registerUser(
                                            nameValue.value,
                                            loginValue.value,
                                            passwordValue.value,
                                            confirmPasswordValue.value
                                        )
                                        Toast.makeText(
                                            context,
                                            "User '${nameValue.value}' registered successfully!",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        // Navigate to the login screen.
                                        navController.navigate(Screens.LoginScreen.route)
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .height(50.dp)
                                .clip(CircleShape),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
                        ) {
                            Text(text = "Sign Up", fontSize = 20.sp, color = textWhiteColor)
                        }

                        Spacer(modifier = Modifier.padding(20.dp))

                        // Text that allows navigation to the login screen when clicked.
                        Text(
                            text = "Login Instead",
                            modifier = Modifier.clickable(onClick = {
                                navController.navigate(Screens.LoginScreen.route) {
                                    launchSingleTop = true
                                }
                            })
                        )

                        Spacer(modifier = Modifier.padding(20.dp))


                    }
                }
            }
        }
    }
}


