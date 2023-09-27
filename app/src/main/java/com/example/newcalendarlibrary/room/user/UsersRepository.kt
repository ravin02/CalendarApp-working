package com.example.newcalendarlibrary.room.user

import android.content.Context
import com.example.newcalendarlibrary.room.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

// Repository class responsible for providing access to user data
class UserRepository(context: Context) : UserDAO {

    // Initialize the UserDAO by obtaining an instance of the database
    private val userDao = AppDatabase.AppDb.getInstance(context).userDao()

    // Override function to insert a user into the database
    override suspend fun insert(user: User) = withContext(Dispatchers.IO) {
        userDao.insert(user)
    }

    // Override function to delete one or more users from the database
    override suspend fun delete(user: List<User>) = withContext(Dispatchers.IO) {
        userDao.delete(user)
    }

    // Override function to update a user's information in the database
    override suspend fun update(user: User) = withContext(Dispatchers.IO) {
        userDao.update(user)
    }

    // Override function to retrieve the ID of a user by their login value
    override suspend fun getUserId(loginValue: String): Int {
        return userDao.getUserId(loginValue)
    }

    // Override function to retrieve the name of a user by their ID
    override suspend fun getUserName(id: Int): String {
        return userDao.getUserName(id)
    }

    // Override function to retrieve a user by their login value
    override suspend fun getUserByLogin(loginValue: String): User? {
        return userDao.getUserByLogin(loginValue)
    }

    // Override function to retrieve a user by their login value and password
    override suspend fun getUserByLoginAndPassword(loginValue: String, passwordValue: String): User? {
        return userDao.getUserByLoginAndPassword(loginValue, passwordValue)
    }

    // Override function to retrieve all users as a Flow (used for observing changes in user data)
    override fun getAllUsers(): Flow<List<User>> {
        return userDao.getAllUsers()
    }

    // Override function to delete all users from the database
    override suspend fun deleteAllUsers() = withContext(Dispatchers.IO) {
        userDao.deleteAllUsers()
    }
}