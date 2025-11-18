package com.example.nutrifindapp.ui.shoppinglist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutrifindapp.data.local.ShoppingListItem
import com.example.nutrifindapp.data.repository.ShoppingListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListViewModel @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ShoppingListUiState>(ShoppingListUiState.Loading)
    val uiState: StateFlow<ShoppingListUiState> = _uiState.asStateFlow()

    init {
        loadShoppingList()
    }

    private fun loadShoppingList() {
        viewModelScope.launch {
            try {
                shoppingListRepository.getAllItems().collect { items ->
                    _uiState.value = if (items.isEmpty()) {
                        ShoppingListUiState.Empty
                    } else {
                        ShoppingListUiState.Success(items)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = ShoppingListUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun toggleItemChecked(item: ShoppingListItem) {
        viewModelScope.launch {
            try {
                shoppingListRepository.toggleItemChecked(item)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun deleteItem(item: ShoppingListItem) {
        viewModelScope.launch {
            try {
                shoppingListRepository.deleteItem(item)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun deleteCheckedItems() {
        viewModelScope.launch {
            try {
                shoppingListRepository.deleteCheckedItems()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun clearAll() {
        viewModelScope.launch {
            try {
                shoppingListRepository.clearAll()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun addItem(name: String, amount: String, unit: String) {
        viewModelScope.launch {
            try {
                val item = ShoppingListItem(
                    name = name,
                    amount = amount,
                    unit = unit
                )
                shoppingListRepository.addItem(item)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}

sealed class ShoppingListUiState {
    object Loading : ShoppingListUiState()
    object Empty : ShoppingListUiState()
    data class Success(val items: List<ShoppingListItem>) : ShoppingListUiState()
    data class Error(val message: String) : ShoppingListUiState()
}
