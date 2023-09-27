package com.example.newcalendarlibrary.room.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// Data Access Object (DAO) interface for user-related database operations
@Dao
interface UserDAO {

    // Insert a user into the database
    @Insert
    suspend fun insert(user: User)

    // Delete one or more users from the database
    @Delete
    suspend fun delete(user: List<User>)

    // Update a user's information in the database
    @Update
    suspend fun update(user: User)

    // Query to retrieve the ID of a user by their login value
    @Query("SELECT id FROM users WHERE loginValue = :loginValue")
    suspend fun getUserId(loginValue: String): Int

    // Query to retrieve the name of a user by their ID
    @Query("SELECT nameValue FROM users WHERE id = :id")
    suspend fun getUserName(id: Int): String

    // Query to retrieve a user by their login value
    @Query("SELECT * FROM users WHERE users.loginValue=:loginValue")
    suspend fun getUserByLogin(loginValue: String): User?

    // Query to retrieve a user by their login value and password
    @Query("SELECT * FROM users WHERE users.loginValue = :loginValue AND users.passwordValue = :passwordValue")
    suspend fun getUserByLoginAndPassword(loginValue: String, passwordValue: String): User?

    // Query to retrieve all users as a Flow (used for observing changes in user data)
    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<User>>

    // Query to delete all users from the database
    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()
}