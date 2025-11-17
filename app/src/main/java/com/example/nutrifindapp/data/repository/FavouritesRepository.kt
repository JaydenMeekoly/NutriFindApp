package com.example.nutrifindapp.data.repository

import com.example.nutrifindapp.data.local.FavouriteRecipe
import com.example.nutrifindapp.data.local.FavouriteRecipeDao
import com.example.nutrifindapp.data.model.Recipe
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavouritesRepository @Inject constructor(
    private val favouriteRecipeDao: FavouriteRecipeDao
) {
    fun getAllFavourites(): Flow<List<FavouriteRecipe>> {
        return favouriteRecipeDao.getAllFavourites()
    }

    fun isFavourite(recipeId: Int): Flow<Boolean> {
        return favouriteRecipeDao.isFavourite(recipeId)
    }

    suspend fun addFavourite(recipe: Recipe) {
        val favouriteRecipe = FavouriteRecipe(
            id = recipe.id,
            title = recipe.title,
            image = recipe.image,
            readyInMinutes = recipe.readyInMinutes,
            servings = recipe.servings,
            summary = recipe.summary
        )
        favouriteRecipeDao.insertFavourite(favouriteRecipe)
    }

    suspend fun removeFavourite(recipeId: Int) {
        favouriteRecipeDao.deleteFavouriteById(recipeId)
    }

    suspend fun toggleFavourite(recipe: Recipe) {
        val existing = favouriteRecipeDao.getFavouriteById(recipe.id)
        if (existing != null) {
            favouriteRecipeDao.deleteFavouriteById(recipe.id)
        } else {
            addFavourite(recipe)
        }
    }
}
