package com.example.newcalendarlibrary.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

val PREF_TAG="user"  // Constant value for preference tag related to user
val SETTING_TAG="setting"  // Constant value for preference tag related to settings

@Singleton  // Indicates that this class is a Singleton and will be managed by the dependency injection framework
class MyPreference @Inject constructor(@ApplicationContext context : Context){
    val prefs = context.getSharedPreferences("config",MODE_PRIVATE)  // Initialize SharedPreferences using the application context and "config" as the preference file name

    // Function to retrieve the user data from SharedPreferences
    fun getUser(): String {
        return prefs.getString(PREF_TAG, "")!!  // Retrieve the user data from SharedPreferences using the PREF_TAG key, defaulting to an empty string if not found
    }

    // Function to set/update the user data in SharedPreferences
    fun setUser(query: String) {
        prefs.edit().putString(PREF_TAG, query).apply()  // Update the user data in SharedPreferences using the PREF_TAG key
    }

    // Function to retrieve the setting from SharedPreferences
    fun getSetting(): Int {
        return prefs.getInt(SETTING_TAG, 0)!!  // Retrieve the setting from SharedPreferences using the SETTING_TAG key, defaulting to 0 if not found
    }

    // Function to set/update the setting in SharedPreferences
    fun setSetting(setting: Int) {
        prefs.edit().putInt(SETTING_TAG, setting).apply()  // Update the setting in SharedPreferences using the SETTING_TAG key
    }
}