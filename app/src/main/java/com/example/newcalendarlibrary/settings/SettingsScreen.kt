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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.newcalendarlibrary.color_picker.ColorViewModel
import com.example.newcalendarlibrary.color_picker.ColourButton
import com.example.newcalendarlibrary.color_picker.colors
import com.example.newcalendarlibrary.color_picker.colourSaver

@Composable
fun SettingsScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    colors: List<Color>,
    onColorSelected: (Color) -> Unit
) {
    val context = LocalContext.current

    Surface(modifier = modifier.fillMaxSize()) {

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Please select a color for your Home Screen",
                fontFamily = FontFamily.SansSerif,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(top = 40.dp, start = 20.dp)
            )
            Card(
                modifier = modifier
                    .wrapContentSize()
                    .padding(start = 10.dp, end = 10.dp)
            ) {
                ColourButton(
                    colors = colors,
                    onColorSelected = {
                        onColorSelected(it)
                    },
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp),
                )
            }
        }

    }
}