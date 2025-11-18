package com.example.nutrifindapp.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutrifindapp.data.local.RecipeHistory
import com.example.nutrifindapp.data.repository.RecipeHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeHistoryViewModel @Inject constructor(
    private val recipeHistoryRepository: RecipeHistoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<RecipeHistoryUiState>(RecipeHistoryUiState.Loading)
    val uiState: StateFlow<RecipeHistoryUiState> = _uiState.asStateFlow()

    init {
        loadHistory()
    }

    private fun loadHistory() {
        viewModelScope.launch {
            try {
                recipeHistoryRepository.getRecentRecipes().collect { history ->
                    _uiState.value = if (history.isEmpty()) {
                        RecipeHistoryUiState.Empty
                    } else {
                        RecipeHistoryUiState.Success(history)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = RecipeHistoryUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun deleteFromHistory(recipeId: Int) {
        viewModelScope.launch {
            try {
                recipeHistoryRepository.deleteFromHistory(recipeId)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            try {
                recipeHistoryRepository.clearHistory()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}

sealed class RecipeHistoryUiState {
    object Loading : RecipeHistoryUiState()
    object Empty : RecipeHistoryUiState()
    data class Success(val history: List<RecipeHistory>) : RecipeHistoryUiState()
    data class Error(val message: String) : RecipeHistoryUiState()
}
