    package com.example.nutrifindapp.data.api

import com.example.nutrifindapp.data.model.Recipe
import com.example.nutrifindapp.data.model.RecipeSearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SpoonacularApiService {
    @GET("recipes/complexSearch")
    suspend fun searchRecipes(
        @Query("query") query: String,
        @Query("number") number: Int = 20,
        @Query("offset") offset: Int = 0,
        @Query("addRecipeInformation") addRecipeInformation: Boolean = true,
        @Query("fillIngredients") fillIngredients: Boolean = true,
        @Query("addRecipeNutrition") addRecipeNutrition: Boolean = true,
        @Query("cuisine") cuisine: String? = null,
        @Query("diet") diet: String? = null,
        @Query("intolerances") intolerances: String? = null,
        @Query("maxReadyTime") maxReadyTime: Int? = null,
        @Query("minProtein") minProtein: Int? = null,
        @Query("minCalories") minCalories: Int? = null,
        @Query("maxCalories") maxCalories: Int? = null
    ): RecipeSearchResponse

    @GET("recipes/random")
    suspend fun getRandomRecipes(
        @Query("number") number: Int = 10,
        @Query("tags") tags: String? = null
    ): Map<String, List<Recipe>>

    @GET("recipes/{id}/information")
    suspend fun getRecipeInformation(
        @Path("id") id: Int,
        @Query("includeNutrition") includeNutrition: Boolean = true
    ): Recipe

    companion object {
        const val BASE_URL = "https://api.spoonacular.com/"
        // TODO: Replace with your actual API key
        const val API_KEY = "1f6633aab1b94c30b74ff9e5dbff4060"
    }
}
