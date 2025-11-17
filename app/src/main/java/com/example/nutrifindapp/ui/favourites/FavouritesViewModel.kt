package com.example.nutrifindapp.ui.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutrifindapp.data.local.FavouriteRecipe
import com.example.nutrifindapp.data.repository.FavouritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val favouritesRepository: FavouritesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<FavouritesUiState>(FavouritesUiState.Loading)
    val uiState: StateFlow<FavouritesUiState> = _uiState.asStateFlow()

    init {
        loadFavourites()
    }

    private fun loadFavourites() {
        viewModelScope.launch {
            try {
                favouritesRepository.getAllFavourites().collect { favourites ->
                    _uiState.value = if (favourites.isEmpty()) {
                        FavouritesUiState.Empty
                    } else {
                        FavouritesUiState.Success(favourites)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = FavouritesUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun removeFavourite(recipeId: Int) {
        viewModelScope.launch {
            try {
                favouritesRepository.removeFavourite(recipeId)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}

sealed class FavouritesUiState {
    object Loading : FavouritesUiState()
    object Empty : FavouritesUiState()
    data class Success(val favourites: List<FavouriteRecipe>) : FavouritesUiState()
    data class Error(val message: String) : FavouritesUiState()
}
