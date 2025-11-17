package com.example.nutrifindapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_recipes")
data class FavouriteRecipe(
    @PrimaryKey
    val id: Int,
    val title: String,
    val image: String?,
    val readyInMinutes: Int,
    val servings: Int,
    val summary: String,
    val addedAt: Long = System.currentTimeMillis()
)
