package com.example.newcalendarlibrary.screens

sealed class Screen (val route: String){

    object MainScreen : Screen("main")

    object TodoListScreen : Screen("todo")
}