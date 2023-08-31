package com.example.newcalendarlibrary.room

class UserRepository(private val userDao: UsersDao) {
    suspend  fun insertUser(user: Users) {
        userDao.insertUser(user)
    }

    suspend  fun getUserInfo(userName: String, password: String, name: String): Users? {
        return userDao.queryUser(userName = userName, password = password, name = name)
    }


}