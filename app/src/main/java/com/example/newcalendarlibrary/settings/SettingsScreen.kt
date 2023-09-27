package com.example.newcalendarlibrary.settings

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.newcalendarlibrary.color_picker.ColorViewModel
import com.example.newcalendarlibrary.color_picker.ColourButton
import com.example.newcalendarlibrary.color_picker.colors
import com.example.newcalendarlibrary.color_picker.colourSaver
import com.example.newcalendarlibrary.ui.viewmodel.SettingViewModel

// Composable function for the Settings screen
@Composable
fun SettingsScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    colors: List<Color>,
    onColorSelected: (Color) -> Unit,
    viewModel: SettingViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    // Create a Surface composable for the settings screen
    Surface(modifier = modifier.fillMaxSize()) {

        // Create a Column composable to organize content vertically
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Display a text message at the top of the screen
            Text(
                text = "Please select a color for your Home Screen",
                fontFamily = FontFamily.SansSerif,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(top = 40.dp, start = 20.dp)
            )
            // Create a Card composable for color selection
            Card(
                modifier = modifier
                    .wrapContentSize()
                    .padding(start = 10.dp, end = 10.dp)
            ) {
                val context= LocalView.current.context
                // Initialize the selected color state
                var selected by remember {
                    mutableIntStateOf(viewModel.setting)
                }
                // Create a ColourButton composable for color selection
                ColourButton(
                    selected = selected,
                    colors = colors,
                    onColorSelected = {
                        selected = it
                        onColorSelected(colors[it])
                        viewModel.storeSetting(it)
                        // Display a toast message to notify the user
                        Toast.makeText(context, "Restart the app to apply changes", Toast.LENGTH_SHORT).show()
                    },
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp),
                )
            }
        }
    }
}