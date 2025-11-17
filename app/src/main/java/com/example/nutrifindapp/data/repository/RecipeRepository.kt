package com.example.nutrifindapp.data.repository

import com.example.nutrifindapp.data.api.SpoonacularApiService
import com.example.nutrifindapp.data.model.Recipe
import com.example.nutrifindapp.data.model.RecipeSearchResponse
import com.example.nutrifindapp.ui.recipe.model.SearchFilters
import javax.inject.Inject

class RecipeRepository @Inject constructor(
    private val apiService: SpoonacularApiService
) {
    suspend fun searchRecipes(
        query: String,
        filters: SearchFilters = SearchFilters(),
        number: Int = 20,
        offset: Int = 0
    ): Result<RecipeSearchResponse> {
        return try {
            println("Making API request with query: $query and filters: $filters")
            
            val response = apiService.searchRecipes(
                query = query,
                number = number,
                offset = offset,
                cuisine = filters.cuisine.takeIf { it.isNotBlank() },
                diet = filters.diet.takeIf { it.isNotBlank() },
                intolerances = filters.intolerances.takeIf { it.isNotEmpty() }?.joinToString(","),
                maxReadyTime = filters.maxReadyTime,
                minProtein = filters.minProtein,
                minCalories = filters.minCalories,
                maxCalories = filters.maxCalories
            )
            
            println("API Response received: ${response.results.size} recipes")
            Result.success(response)
        } catch (e: Exception) {
            println("API Error: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }
    
    suspend fun getRecipeDetails(recipeId: Int): Recipe {
        return apiService.getRecipeInformation(
            id = recipeId,
            includeNutrition = true
        )
    }

    suspend fun getRecipeInformation(recipeId: Int): Result<Recipe> {
        return try {
            val response = apiService.getRecipeInformation(
                id = recipeId
            )
            Result.success(response)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}
