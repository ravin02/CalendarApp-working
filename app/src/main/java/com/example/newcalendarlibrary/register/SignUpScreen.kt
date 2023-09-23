package com.example.newcalendarlibrary.register

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.example.newcalendarlibrary.room.user.Users
import kotlinx.coroutines.launch


@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    userRepository: UserRepository,
    navController: NavController
) {

    var name by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    //Animation specs
    var isPlaying by remember {
        mutableStateOf(true)
    }
    var speed by remember {
        mutableStateOf(1f)
    }

    val signUp by rememberLottieComposition(

        LottieCompositionSpec
            // here `code` is the file name of lottie file
            // use it accordingly
            .RawRes(R.raw.sign_up)
    )

    // to control the animation
    val progress by animateLottieCompositionAsState(
        // pass the composition created above
        signUp,

        // Iterates Forever
        iterations = LottieConstants.IterateForever,

        // pass isPlaying we created above,
        // changing isPlaying will recompose
        // Lottie and pause/play
        isPlaying = isPlaying,

        // pass speed we created above,
        // changing speed will increase Lottie
        speed = speed,

        // this makes animation to restart
        // when paused and play
        // pass false to continue the animation
        // at which it was paused
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
                // LottieAnimation
                // Pass the composition
                // and the progress state
                LottieAnimation(
                    signUp,
                    progress,
                    modifier = Modifier.size(100.dp)
                )

            }
            Text(
                text = "Sign-Up",
                textAlign = TextAlign.Center,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
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

            Row(horizontalArrangement = Arrangement.Center, modifier = modifier.fillMaxWidth()) {
                Icon(
                    painter = painterResource(id = R.drawable.info),
                    contentDescription = "info Icon",
                    modifier = modifier.size(40.dp)
                )
                Spacer(modifier = modifier.width(15.dp))
                Text(text = "Password must consist of at least\neight characters, which must contain\none capital letter ")
            }

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it

                },
                label = { Text(text = "Re-type Password") },
                placeholder = {
                    Text(
                        text = confirmPassword, color = MaterialTheme.colors.onBackground
                    )
                },
                isError = confirmPassword != password,
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Spacer(modifier = modifier.padding(vertical = 10.dp))

            Button(onClick = {
                coroutineScope.launch {
                    onSaveClick(
                        name = name,
                        userName = userName,
                        password = password,
                        confirmPassword = confirmPassword,
                        userRepository = userRepository,
                        context = context,
                        navController = navController
                    )
                }

            },
                modifier = modifier.fillMaxWidth().padding(start = 100.dp, end = 100.dp)
            ) {
                Text(text = "Save")

            }
        }
    }
}

private suspend fun onSaveClick(
    name: String,
    userName: String,
    password: String,
    confirmPassword: String,
    userRepository: UserRepository,
    context: Context,
    navController : NavController
) {
    val nameEmpty = name.isEmpty()
    val userNameEmpty = userName.isEmpty()
    var passwordEmpty = password.isEmpty()
    var confirmPasswordEmpty = confirmPassword.isEmpty()

    if (nameEmpty || userNameEmpty || passwordEmpty || confirmPasswordEmpty) {
        showEmptyFieldsToast(context = context)
    } else if (password != confirmPassword) {
        Toast.makeText(context, "Password does not match", Toast.LENGTH_SHORT).show()
    } else {
        insertUser(name, userName, password, confirmPassword, userRepository)
        Toast.makeText(context, "Saved in DB", Toast.LENGTH_SHORT).show()
        navController.navigate(Screens.LoginScreen.route)

    }
}

 fun showEmptyFieldsToast(context: Context) {
    Toast.makeText(context, "Please make sure to fill all fields", Toast.LENGTH_SHORT).show()
}

private suspend fun insertUser(
    name: String,
    userName: String,
    password: String,
    confirmPassword: String,
    userRepository: UserRepository
) {
    val newUser = Users(
        name = name,
        userName = userName,
        password = password,
        confirm_Password = confirmPassword
    )
    userRepository.insertUser(newUser)

}

