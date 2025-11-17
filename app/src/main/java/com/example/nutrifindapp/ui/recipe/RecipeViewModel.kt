package com.example.nutrifindapp.ui.recipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutrifindapp.data.model.Recipe
import com.example.nutrifindapp.data.model.RecipeSearchResult
import com.example.nutrifindapp.data.repository.FavouritesRepository
import com.example.nutrifindapp.data.repository.RecipeRepository
import com.example.nutrifindapp.ui.recipe.model.SearchFilters
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val favouritesRepository: FavouritesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecipeUiState())
    val uiState: StateFlow<RecipeUiState> = _uiState.asStateFlow()
    
    private val _recipeDetailState = MutableStateFlow<com.example.nutrifindapp.ui.recipe.detail.RecipeDetailState>(
        com.example.nutrifindapp.ui.recipe.detail.RecipeDetailState.Loading
    )
    val recipeDetailState = _recipeDetailState.asStateFlow()

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun toggleFilters() {
        _uiState.update { it.copy(showFilters = !it.showFilters) }
    }
    
    fun updateFilters(filters: SearchFilters) {
        _uiState.update { it.copy(searchFilters = filters) }
    }
    
    fun clearFilters() {
        _uiState.update { it.copy(searchFilters = SearchFilters()) }
    }

    fun searchRecipes() {
        val query = _uiState.value.searchQuery.trim()
        if (query.isBlank()) {
            println("Search query is empty")
            return
        }

        println("Starting search for: $query")
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            println("Launching search coroutine")
            val filters = _uiState.value.searchFilters
            recipeRepository.searchRecipes(
                query = query,
                filters = filters
            ).fold(
                onSuccess = { response ->
                    println("Search successful, received ${response.results.size} recipes")
                    _uiState.update {
                        it.copy(
                            recipes = response.results,
                            isLoading = false,
                            error = if (response.results.isEmpty()) "No recipes found" else null
                        )
                    }
                },
                onFailure = { error ->
                    println("Search failed: ${error.message}")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "An error occurred"
                        )
                    }
                }
            )
        }
    }

    fun onRecipeSelected(recipe: RecipeSearchResult) {
        _uiState.update { it.copy(selectedRecipe = recipe) }
    }

    fun clearSelectedRecipe() {
        _uiState.update { it.copy(selectedRecipe = null) }
    }

    fun getRecipeDetails(recipeId: Int) {
        _recipeDetailState.value = com.example.nutrifindapp.ui.recipe.detail.RecipeDetailState.Loading
        
        viewModelScope.launch {
            try {
                println("Fetching recipe details for ID: $recipeId")  // Debug log
                val recipe = recipeRepository.getRecipeDetails(recipeId)
                println("Successfully fetched recipe: ${recipe.title}")  // Debug log
                _recipeDetailState.value = com.example.nutrifindapp.ui.recipe.detail.RecipeDetailState.Success(recipe)
            } catch (e: Exception) {
                val errorMsg = "Failed to load recipe details: ${e.message}"
                println(errorMsg)  // Debug log
                e.printStackTrace()  // Print stack trace for debugging
                _recipeDetailState.value = com.example.nutrifindapp.ui.recipe.detail.RecipeDetailState.Error(errorMsg)
            }
        }
    }

    fun isFavourite(recipeId: Int): Flow<Boolean> {
        return favouritesRepository.isFavourite(recipeId)
    }

    fun toggleFavourite(recipe: Recipe) {
        viewModelScope.launch {
            favouritesRepository.toggleFavourite(recipe)
        }
    }
}

data class RecipeUiState(
    val searchQuery: String = "",
    val recipes: List<RecipeSearchResult> = emptyList(),
    val selectedRecipe: RecipeSearchResult? = null,
    val recipeDetails: Recipe? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showFilters: Boolean = false,
    val searchFilters: SearchFilters = SearchFilters()
)
