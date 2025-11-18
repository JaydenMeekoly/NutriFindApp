package com.example.nutrifindapp.data.repository

import com.example.nutrifindapp.data.local.RecipeHistory
import com.example.nutrifindapp.data.local.RecipeHistoryDao
import com.example.nutrifindapp.data.model.Recipe
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeHistoryRepository @Inject constructor(
    private val recipeHistoryDao: RecipeHistoryDao
) {
    fun getRecentRecipes(): Flow<List<RecipeHistory>> {
        return recipeHistoryDao.getRecentRecipes()
    }

    suspend fun addToHistory(recipe: Recipe) {
        val historyItem = RecipeHistory(
            id = recipe.id,
            title = recipe.title,
            image = recipe.image,
            readyInMinutes = recipe.readyInMinutes,
            servings = recipe.servings,
            viewedAt = System.currentTimeMillis()
        )
        recipeHistoryDao.insertRecipeHistory(historyItem)
    }

    suspend fun deleteFromHistory(recipeId: Int) {
        recipeHistoryDao.deleteRecipeHistory(recipeId)
    }

    suspend fun clearHistory() {
        recipeHistoryDao.clearHistory()
    }
}
