package com.example.nutrifindapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_list")
data class ShoppingListItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val amount: String = "",
    val unit: String = "",
    val isChecked: Boolean = false,
    val recipeId: Int? = null,
    val recipeTitle: String? = null,
    val addedAt: Long = System.currentTimeMillis()
)
