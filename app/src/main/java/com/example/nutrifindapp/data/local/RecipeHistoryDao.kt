package com.example.nutrifindapp.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeHistoryDao {
    @Query("SELECT * FROM recipe_history ORDER BY viewedAt DESC LIMIT 20")
    fun getRecentRecipes(): Flow<List<RecipeHistory>>

    @Query("SELECT * FROM recipe_history WHERE id = :recipeId")
    suspend fun getRecipeHistory(recipeId: Int): RecipeHistory?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipeHistory(recipe: RecipeHistory)

    @Query("DELETE FROM recipe_history WHERE id = :recipeId")
    suspend fun deleteRecipeHistory(recipeId: Int)

    @Query("DELETE FROM recipe_history")
    suspend fun clearHistory()
}
