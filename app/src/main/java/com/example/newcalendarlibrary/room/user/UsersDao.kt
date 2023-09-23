package com.example.newcalendarlibrary.room.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.newcalendarlibrary.room.user.Users

@Dao
interface UsersDao {

    @Insert
    suspend fun insertUser(user: Users)

    @Query("SELECT * FROM Users WHERE  userName = :userName AND password = :password AND name = :name ")
    suspend fun queryUser(userName: String, password: String, name: String) : Users?


}