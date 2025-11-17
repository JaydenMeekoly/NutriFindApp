package com.example.nutrifindapp.ui.recommended

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutrifindapp.data.api.SpoonacularApiService
import com.example.nutrifindapp.data.model.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "RecommendedRecipesVM"

@HiltViewModel
class RecommendedRecipesViewModel @Inject constructor(
    private val apiService: SpoonacularApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow<RecommendedRecipesUiState>(RecommendedRecipesUiState.Loading)
    val uiState: StateFlow<RecommendedRecipesUiState> = _uiState.asStateFlow()

    init {
        loadRecommendedRecipes()
    }

    fun loadRecommendedRecipes() {
        viewModelScope.launch {
            Log.d(TAG, "Loading recommended recipes...")
            _uiState.value = RecommendedRecipesUiState.Loading
            try {
                Log.d(TAG, "Making API call to get random recipes")
                val response = apiService.getRandomRecipes(number = 10)
                Log.d(TAG, "API response received: $response")
                
                // The response is already a List<Recipe> wrapped in a map with key "recipes"
                val recipes = response["recipes"] ?: emptyList()
                Log.d(TAG, "Extracted ${recipes.size} recipes")
                
                if (recipes.isNotEmpty()) {
                    Log.d(TAG, "Successfully loaded ${recipes.size} recipes")
                    _uiState.value = RecommendedRecipesUiState.Success(recipes)
                } else {
                    Log.d(TAG, "No recipes found in the response")
                    _uiState.value = RecommendedRecipesUiState.Error("No recipes found")
                }
            } catch (e: Exception) {
                val errorMsg = "Failed to load recipes: ${e.message ?: "Unknown error"}"
                Log.e(TAG, errorMsg, e)
                _uiState.value = RecommendedRecipesUiState.Error(errorMsg)
            }
        }
    }
}

sealed class RecommendedRecipesUiState {
    object Loading : RecommendedRecipesUiState()
    data class Success(val recipes: List<Recipe>) : RecommendedRecipesUiState()
    data class Error(val message: String) : RecommendedRecipesUiState()
}
