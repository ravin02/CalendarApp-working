package com.example.newcalendarlibrary.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.newcalendarlibrary.widgets.ArrowBackTopAppBar

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TodoList(
    navController: NavController
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            ArrowBackTopAppBar(arrowBackClicked = { navController.popBackStack()}) {
                Text(text = "Please add a TODO")
            }
                 },
        ) {
    }
}

