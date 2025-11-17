package com.example.nutrifindapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        FavouriteRecipe::class,
        RecipeHistory::class,
        ShoppingListItem::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favouriteRecipeDao(): FavouriteRecipeDao
    abstract fun recipeHistoryDao(): RecipeHistoryDao
    abstract fun shoppingListDao(): ShoppingListDao
}
