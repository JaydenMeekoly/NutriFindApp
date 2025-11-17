package com.example.nutrifindapp.ui.recipe.model

data class SearchFilters(
    val cuisine: String = "",
    val diet: String = "",
    val intolerances: List<String> = emptyList(),
    val maxReadyTime: Int? = null,
    val minProtein: Int? = null,
    val minCalories: Int? = null,
    val maxCalories: Int? = null,
    val includeIngredients: List<String> = emptyList(),
    val excludeIngredients: List<String> = emptyList()
) {
    val isDefault: Boolean
        get() = this == SearchFilters()

    fun toQueryMap(): Map<String, String> {
        return buildMap {
            if (cuisine.isNotBlank()) put("cuisine", cuisine)
            if (diet.isNotBlank()) put("diet", diet)
            if (intolerances.isNotEmpty()) put("intolerances", intolerances.joinToString(","))
            maxReadyTime?.let { put("maxReadyTime", it.toString()) }
            minProtein?.let { put("minProtein", it.toString()) }
            minCalories?.let { put("minCalories", it.toString()) }
            maxCalories?.let { put("maxCalories", it.toString()) }
            if (includeIngredients.isNotEmpty()) put("includeIngredients", includeIngredients.joinToString(","))
            if (excludeIngredients.isNotEmpty()) put("excludeIngredients", excludeIngredients.joinToString(","))
        }
    }
}

object IntoleranceOptions {
    const val DAIRY = "dairy"
    const val EGG = "egg"
    const val GLUTEN = "gluten"
    const val GRAIN = "grain"
    const val PEANUT = "peanut"
    const val SEAFOOD = "seafood"
    const val SESAME = "sesame"
    const val SHELLFISH = "shellfish"
    const val SOY = "soy"
    const val TREE_NUT = "tree nut"
    const val WHEAT = "wheat"
}
