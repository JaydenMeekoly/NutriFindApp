package com.example.nutrifindapp.di

import android.content.Context
import androidx.room.Room
import com.example.nutrifindapp.data.local.AppDatabase
import com.example.nutrifindapp.data.local.FavouriteRecipeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "nutrifind_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideFavouriteRecipeDao(database: AppDatabase): FavouriteRecipeDao {
        return database.favouriteRecipeDao()
    }
}
