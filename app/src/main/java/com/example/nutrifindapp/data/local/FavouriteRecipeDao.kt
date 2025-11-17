package com.example.nutrifindapp.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteRecipeDao {
    @Query("SELECT * FROM favourite_recipes ORDER BY addedAt DESC")
    fun getAllFavourites(): Flow<List<FavouriteRecipe>>

    @Query("SELECT * FROM favourite_recipes WHERE id = :recipeId")
    suspend fun getFavouriteById(recipeId: Int): FavouriteRecipe?

    @Query("SELECT EXISTS(SELECT 1 FROM favourite_recipes WHERE id = :recipeId)")
    fun isFavourite(recipeId: Int): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavourite(recipe: FavouriteRecipe)

    @Delete
    suspend fun deleteFavourite(recipe: FavouriteRecipe)

    @Query("DELETE FROM favourite_recipes WHERE id = :recipeId")
    suspend fun deleteFavouriteById(recipeId: Int)
}
