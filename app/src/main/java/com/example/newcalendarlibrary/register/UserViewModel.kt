package com.example.newcalendarlibrary.register

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.newcalendarlibrary.room.user.User
import com.example.newcalendarlibrary.room.user.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(app: Application) : AndroidViewModel(app) {

    // Repository instance for accessing user data
    private val repo = UserRepository(app.applicationContext)

    // Flag to check if the user exists
    private var userExist = false

    // MutableStateFlow to hold the currently logged-in user
    val currentUser = MutableStateFlow<User?>(null)

    // StateFlow for observing changes to the current user
    private val userFlow = currentUser.asStateFlow()

    // Function to check if the provided username and password are valid
    suspend fun checkPassword(username: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            val user = getUserByLoginAndPassword(username, password)
            user != null
        }
    }

    // Function to set the current user
    private fun setCurrentUser(user: User) {
        currentUser.value = user
    }

    // Function to check if a user with the provided login exists
    suspend fun checkIfUserExists(loginValue: String): Boolean {
        return withContext(Dispatchers.IO) {
            val user = repo.getUserByLogin(loginValue)
            user != null
        }
    }

    // Function to get a user by login and password
    private suspend fun getUserByLoginAndPassword(
        loginValue: String,
        passwordValue: String
    ): User? {
        return repo.getUserByLoginAndPassword(loginValue, passwordValue)
    }

    // Function to handle login
    suspend fun login(loginValue: String, passwordValue: String): Boolean {
        return withContext(Dispatchers.IO) {
            val user = getUserByLoginAndPassword(loginValue, passwordValue)
            if (user != null) {
                setCurrentUser(user)
                userFlow.apply {
                    currentUser.asStateFlow()
                }
                println(currentUser.value)
                true
            } else {
                false
            }
        }
    }

    // Function to logout the current user
    fun logout() {
        currentUser.value = null
    }

    // Function to register a new user
    fun registerUser(
        nameValue: String,
        loginValue: String,
        passwordValue: String,
        confirmPasswordValue: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            if (passwordValue == confirmPasswordValue) {
                userExist = false
                val user = User(
                    nameValue = nameValue,
                    loginValue = loginValue,
                    passwordValue = passwordValue
                )
                repo.insert(user)
            } else {
                userExist = true
            }
        }
    }
}