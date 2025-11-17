package com.example.nutrifindapp.ui.recommended

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nutrifindapp.data.model.Recipe
import com.example.nutrifindapp.ui.recipe.components.RecipeItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendedRecipesScreen(
    onRecipeClick: (Int) -> Unit,
    viewModel: RecommendedRecipesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
TopAppBar(
                title = { Text("Recommended Recipes") },
                actions = {
                    IconButton(
                        onClick = { viewModel.loadRecommendedRecipes() },
                        enabled = uiState !is RecommendedRecipesUiState.Loading
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val state = uiState) {
                is RecommendedRecipesUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is RecommendedRecipesUiState.Success -> {
                    if (state.recipes.isEmpty()) {
                        Text(
                            text = "No recipes found",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        RecipeList(
                            recipes = state.recipes,
                            onRecipeClick = onRecipeClick
                        )
                    }
                }
                is RecommendedRecipesUiState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Error: ${state.message}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadRecommendedRecipes() }) {
                            Text("Retry")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RecipeList(
    recipes: List<Recipe>,
    onRecipeClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(recipes) { recipe ->
            RecipeItem(
                recipe = recipe,
                onClick = { onRecipeClick(recipe.id) }
            )
        }
    }
}
