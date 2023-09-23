package com.example.newcalendarlibrary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.newcalendarlibrary.events.AppointmentViewModel
import com.example.newcalendarlibrary.navigation.NavGraph
import com.example.newcalendarlibrary.room.events.EventDatabase
import com.example.newcalendarlibrary.room.user.UserRepository
import com.example.newcalendarlibrary.room.user.UsersDatabase
import com.example.newcalendarlibrary.ui.theme.NewCalendarLibraryTheme
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

lateinit var userRepository: UserRepository

class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(applicationContext, EventDatabase::class.java, "appointment.db")
            .build()
    }

    private val viewModel by viewModels<AppointmentViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AppointmentViewModel(db.eventDao) as T
                }
            }
        }
    )


    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GlobalScope.launch(Dispatchers.IO) {
            val database =
                Room.databaseBuilder(
                    applicationContext,
                    UsersDatabase::class.java,
                    "Users-Database"
                )
                    .build()

            userRepository = UserRepository(database.userDao)
        }

//        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
//        val isFirstLaunch =  prefs.getBoolean("is_first_launch",true)
//
//        val startDestination =  if (isFirstLaunch){
//            prefs.edit().putBoolean("is_first_launch",false).apply()
//            Screens.SignUpScreen.route
//        }
//        else{
//            Screens.LoginScreen.route
//        }

        setContent {
            NewCalendarLibraryTheme {
                val navController = rememberNavController()
                val state by viewModel.state.collectAsState()
                NavGraph(
                    navController = navController,
                    state = state,
                    onEvent = viewModel::onEvent,
                    eventDao = db.eventDao
                )
            }
        }
    }
}

