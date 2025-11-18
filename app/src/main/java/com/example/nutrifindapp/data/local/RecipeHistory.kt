package com.example.nutrifindapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe_history")
data class RecipeHistory(
    @PrimaryKey
    val id: Int,
    val title: String,
    val image: String?,
    val readyInMinutes: Int,
    val servings: Int,
    val viewedAt: Long = System.currentTimeMillis()
)
