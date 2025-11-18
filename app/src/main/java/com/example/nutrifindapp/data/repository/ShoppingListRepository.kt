package com.example.nutrifindapp.data.repository

import com.example.nutrifindapp.data.local.ShoppingListDao
import com.example.nutrifindapp.data.local.ShoppingListItem
import com.example.nutrifindapp.data.model.Ingredient
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShoppingListRepository @Inject constructor(
    private val shoppingListDao: ShoppingListDao
) {
    fun getAllItems(): Flow<List<ShoppingListItem>> {
        return shoppingListDao.getAllItems()
    }

    fun getUncheckedItems(): Flow<List<ShoppingListItem>> {
        return shoppingListDao.getUncheckedItems()
    }

    suspend fun addItem(item: ShoppingListItem) {
        shoppingListDao.insertItem(item)
    }

    suspend fun addIngredientsFromRecipe(
        ingredients: List<Ingredient>,
        recipeId: Int,
        recipeTitle: String
    ) {
        val items = ingredients.map { ingredient ->
            ShoppingListItem(
                name = ingredient.name,
                amount = ingredient.amount.toString(),
                unit = ingredient.unit,
                recipeId = recipeId,
                recipeTitle = recipeTitle
            )
        }
        shoppingListDao.insertItems(items)
    }

    suspend fun updateItem(item: ShoppingListItem) {
        shoppingListDao.updateItem(item)
    }

    suspend fun toggleItemChecked(item: ShoppingListItem) {
        shoppingListDao.updateItem(item.copy(isChecked = !item.isChecked))
    }

    suspend fun deleteItem(item: ShoppingListItem) {
        shoppingListDao.deleteItem(item)
    }

    suspend fun deleteCheckedItems() {
        shoppingListDao.deleteCheckedItems()
    }

    suspend fun clearAll() {
        shoppingListDao.clearAll()
    }
}
