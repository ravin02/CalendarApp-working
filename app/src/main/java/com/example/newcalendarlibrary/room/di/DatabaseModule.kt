package com.example.newcalendarlibrary.room.di

import android.content.Context
import androidx.room.Room
import com.example.newcalendarlibrary.room.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    // Provides a singleton instance of the AppDatabase using Dagger Hilt
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        // Builds the Room database using the provided context, AppDatabase class, and database name
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "AppDatabase"
        )
            // Allows fallback to a destructive migration if necessary
            .fallbackToDestructiveMigration()
            // Builds and returns the AppDatabase instance
            .build()
    }
}