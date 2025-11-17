package com.example.nutrifindapp.data.model

import android.util.Log
import androidx.compose.runtime.Immutable

@Immutable
data class Recipe(
    val id: Int,
    val title: String,
    val image: String? = null,
    val summary: String = "",
    val readyInMinutes: Int = 0,
    val servings: Int = 0,
    val sourceUrl: String? = null,
    val extendedIngredients: List<Ingredient> = emptyList(),
    val analyzedInstructions: List<AnalyzedInstruction> = emptyList(),
    val nutrition: Nutrition? = null
) {
    // Secondary constructor to help with debugging
    init {
        Log.d("Recipe", "Created Recipe: id=$id, title='$title'")
    }
    
    override fun toString(): String {
        return "Recipe(id=$id, title='$title', ingredients=${extendedIngredients.size}, " +
               "instructions=${analyzedInstructions.flatMap { it.steps }.size} steps)"
    }
}

@Immutable
data class Ingredient(
    val id: Int,
    val name: String,
    val amount: Double,
    val unit: String,
    val original: String
)

@Immutable
data class AnalyzedInstruction(
    val name: String,
    val steps: List<Step>
)

@Immutable
data class Step(
    val number: Int,
    val step: String,
    val ingredients: List<Ingredient>,
    val equipment: List<Equipment>
)

@Immutable
data class Equipment(
    val id: Int,
    val name: String
)

@Immutable
data class Nutrition(
    val nutrients: List<Nutrient>,
    val ingredients: List<NutritionIngredient>
)

@Immutable
data class Nutrient(
    val name: String,
    val amount: Double,
    val unit: String,
    val percentOfDailyNeeds: Double
)

@Immutable
data class NutritionIngredient(
    val id: Int,
    val name: String,
    val amount: Double,
    val unit: String,
    val nutrients: List<Nutrient>
)

data class RecipeSearchResponse(
    val results: List<RecipeSearchResult> = emptyList(),
    val offset: Int = 0,
    val number: Int = 0,
    val totalResults: Int = 0
) {
    override fun toString(): String {
        return "RecipeSearchResponse(results=${results.size}, offset=$offset, number=$number, total=$totalResults)"
    }
}

data class RecipeSearchResult(
    val id: Int,
    val title: String,
    val image: String? = null,
    val imageType: String? = null,
    val nutrition: Nutrition? = null
) {
    override fun toString(): String {
        return "RecipeSearchResult(id=$id, title='$title')"
    }
}
