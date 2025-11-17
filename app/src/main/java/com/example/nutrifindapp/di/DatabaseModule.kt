package com.example.nutrifindapp.di

import android.content.Context
import androidx.room.Room
import com.example.nutrifindapp.data.local.AppDatabase
import com.example.nutrifindapp.data.local.FavouriteRecipeDao
import com.example.nutrifindapp.data.local.RecipeHistoryDao
import com.example.nutrifindapp.data.local.ShoppingListDao
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
        )
        .fallbackToDestructiveMigration() // For development - removes old data on schema change
        .build()
    }

    @Provides
    @Singleton
    fun provideFavouriteRecipeDao(database: AppDatabase): FavouriteRecipeDao {
        return database.favouriteRecipeDao()
    }

    @Provides
    @Singleton
    fun provideRecipeHistoryDao(database: AppDatabase): RecipeHistoryDao {
        return database.recipeHistoryDao()
    }

    @Provides
    @Singleton
    fun provideShoppingListDao(database: AppDatabase): ShoppingListDao {
        return database.shoppingListDao()
    }
}
