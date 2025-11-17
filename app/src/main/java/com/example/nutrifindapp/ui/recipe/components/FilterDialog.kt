package com.example.nutrifindapp.ui.recipe.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.nutrifindapp.ui.recipe.model.CuisineOptions
import com.example.nutrifindapp.ui.recipe.model.DietOptions
import com.example.nutrifindapp.ui.recipe.model.SearchFilters

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDialog(
    filters: SearchFilters,
    onDismiss: () -> Unit,
    onApply: (SearchFilters) -> Unit,
    onClear: () -> Unit
) {
    var currentFilters by remember { mutableStateOf(filters) }
    var selectedTab by remember { mutableIntStateOf(0) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Filter Recipes") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                TabRow(selectedTabIndex = selectedTab) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("Diet") }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("Cuisine") }
                    )
                    Tab(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        text = { Text("Other") }
                    )
                }

                when (selectedTab) {
                    0 -> DietFilters(
                        currentFilters = currentFilters,
                        onFilterChange = { currentFilters = it }
                    )
                    1 -> CuisineFilters(
                        currentFilters = currentFilters,
                        onFilterChange = { currentFilters = it }
                    )
                    2 -> OtherFilters(
                        currentFilters = currentFilters,
                        onFilterChange = { currentFilters = it }
                    )
                }
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = { 
                    onClear()
                    currentFilters = SearchFilters()
                }) {
                    Text("Clear All")
                }
                Row {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { onApply(currentFilters) }) {
                        Text("Apply")
                    }
                }
            }
        }
    )
}

@Composable
private fun DietFilters(
    currentFilters: SearchFilters,
    onFilterChange: (SearchFilters) -> Unit
) {
    val diets = listOf(
        DietOptions.NONE to "All Diets",
        DietOptions.VEGETARIAN to "Vegetarian",
        DietOptions.VEGAN to "Vegan",
        DietOptions.GLUTEN_FREE to "Gluten Free",
        DietOptions.KETO to "Keto",
        DietOptions.PALEO to "Paleo"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        diets.forEach { (diet, label) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onFilterChange(currentFilters.copy(diet = diet))
                    }
                    .padding(vertical = 8.dp)
            ) {
                RadioButton(
                    selected = currentFilters.diet == diet,
                    onClick = { onFilterChange(currentFilters.copy(diet = diet)) }
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun CuisineFilters(
    currentFilters: SearchFilters,
    onFilterChange: (SearchFilters) -> Unit
) {
    val cuisines = listOf(
        CuisineOptions.NONE to "All Cuisines",
        CuisineOptions.ITALIAN to "Italian",
        CuisineOptions.ASIAN to "Asian",
        CuisineOptions.MEXICAN to "Mexican",
        CuisineOptions.INDIAN to "Indian",
        CuisineOptions.AMERICAN to "American"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 300.dp)
    ) {
        items(cuisines) { (cuisine, label) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onFilterChange(currentFilters.copy(cuisine = cuisine))
                    }
                    .padding(vertical = 8.dp)
            ) {
                RadioButton(
                    selected = currentFilters.cuisine == cuisine,
                    onClick = { onFilterChange(currentFilters.copy(cuisine = cuisine)) }
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun OtherFilters(
    currentFilters: SearchFilters,
    onFilterChange: (SearchFilters) -> Unit
) {
    var maxReadyTimeText by remember { mutableStateOf(currentFilters.maxReadyTime?.toString() ?: "") }
    var minCaloriesText by remember { mutableStateOf(currentFilters.minCalories?.toString() ?: "") }
    var maxCaloriesText by remember { mutableStateOf(currentFilters.maxCalories?.toString() ?: "") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Max Ready Time
        Text(
            text = "Max Cooking Time (minutes)",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = maxReadyTimeText,
            onValueChange = { value ->
                maxReadyTimeText = value
                val minutes = value.toIntOrNull()
                onFilterChange(currentFilters.copy(maxReadyTime = minutes))
            },
            label = { Text("e.g., 30") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Calories Range
        Text(
            text = "Calories Range",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedTextField(
                value = minCaloriesText,
                onValueChange = { value ->
                    minCaloriesText = value
                    val calories = value.toIntOrNull()
                    onFilterChange(currentFilters.copy(minCalories = calories))
                },
                label = { Text("Min") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.weight(1f)
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            OutlinedTextField(
                value = maxCaloriesText,
                onValueChange = { value ->
                    maxCaloriesText = value
                    val calories = value.toIntOrNull()
                    onFilterChange(currentFilters.copy(maxCalories = calories))
                },
                label = { Text("Max") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.weight(1f)
            )
        }
    }
}
