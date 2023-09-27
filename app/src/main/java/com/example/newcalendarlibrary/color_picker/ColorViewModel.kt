package com.example.newcalendarlibrary.color_picker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel

class ColorViewModel : ViewModel() {
    // Mutable state variable to hold the selected color with an initial value of white
    var selectedColor by mutableStateOf<Color>(Color(0xFFFFFFFF))

    // Function to update the selected color
    fun updateSelectedColor(newColor: Color) {
        selectedColor = newColor // Update the selected color with the new color
    }
}