package com.example.newcalendarlibrary.room.di

import com.example.newcalendarlibrary.room.AppDatabase
import com.example.newcalendarlibrary.room.events.EventDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

// This Kotlin file defines a Dagger Hilt module using the @Module annotation.
// It specifies that the provided dependencies will be available in the SingletonComponent.

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {

    // Provides a method to obtain an EventDao instance by injecting an AppDatabase instance.
    // The EventDao is a DAO (Data Access Object) used to access and manipulate Event data in the database.

    @Provides
    fun provideEventDao(
        database: AppDatabase
    ): EventDao = database.eventDao()

}