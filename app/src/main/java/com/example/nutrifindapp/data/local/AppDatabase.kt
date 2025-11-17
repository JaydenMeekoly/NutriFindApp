package com.example.nutrifindapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [FavouriteRecipe::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favouriteRecipeDao(): FavouriteRecipeDao
}
