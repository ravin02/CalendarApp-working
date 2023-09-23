package com.example.newcalendarlibrary.register

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.newcalendarlibrary.R
import com.example.newcalendarlibrary.navigation.Screens
import com.example.newcalendarlibrary.room.user.UserRepository
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    userRepository: UserRepository,
    navController: NavController
) {

    var name by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    //Animation specs
    var isPlaying by remember {
        mutableStateOf(true)
    }
    var speed by remember {
        mutableStateOf(1f)
    }

    val login by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.login))
    val progress by animateLottieCompositionAsState(
        login,
        iterations = LottieConstants.IterateForever,
        isPlaying = isPlaying,
        speed = speed,
        restartOnPlay = false
    )

    //UI
    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colors.background) {

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 100.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(contentAlignment = Alignment.TopCenter) {

                LottieAnimation(
                    login,
                    progress,
                    modifier = Modifier.size(120.dp)
                )

            }
            Text(
                text = "Login",
                textAlign = TextAlign.Center,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = modifier.padding(bottom = 20.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(text = "Name") },
                placeholder = {
                    Text(
                        text = name, color = MaterialTheme.colors.onBackground
                    )
                },
                isError = name.isEmpty()
            )
            Spacer(modifier = modifier.padding(vertical = 10.dp))

            OutlinedTextField(
                value = userName,
                onValueChange = { userName = it },
                label = { Text(text = "Username") },
                placeholder = {
                    Text(
                        text = userName, color = MaterialTheme.colors.onBackground
                    )
                },
                isError = userName.isEmpty()
            )
            Spacer(modifier = modifier.padding(vertical = 10.dp))


            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                },
                label = { Text(text = "Password") },
                placeholder = {
                    Text(
                        text = password,
                        color = MaterialTheme.colors.onBackground
                    )
                },
                isError = password.isEmpty() || password.length >= 8 || !password.any { char -> char.isUpperCase() },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Spacer(modifier = modifier.padding(vertical = 10.dp))

            Button(
                onClick = {

                    if (name.isEmpty() || userName.isEmpty() || password.isEmpty()) {
                        showEmptyFieldsToast(context = context)
                    }
                    coroutineScope.launch {
                        val user = userRepository.getUserInfo(
                            userName = userName,
                            password = password,
                            name = name
                        )
                        if (user != null) {
                            //Success
                            Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                            navController.navigate(Screens.CalendarScreen.route)
                        } else {
                            //Failure
                            Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 100.dp, end = 100.dp)
            ) {
                Text(text = "Login")
            }
            Row(horizontalArrangement = Arrangement.Center) {
                Text(text = "Don't have an account?")
                Text(
                    text = "Singup",
                    color = Color.Blue,
                    modifier = modifier.clickable { navController.navigate(Screens.SignUpScreen.route) })
            }

        }
    }
}