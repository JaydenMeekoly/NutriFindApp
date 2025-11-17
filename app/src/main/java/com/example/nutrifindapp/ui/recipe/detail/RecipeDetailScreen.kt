package com.example.nutrifindapp.ui.recipe.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.widget.Toast
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.nutrifindapp.R
import com.example.nutrifindapp.data.model.Recipe
import com.example.nutrifindapp.ui.recipe.RecipeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    recipeId: Int,
    navController: NavController,
    viewModel: RecipeViewModel = hiltViewModel()
) {
    val recipeState = viewModel.recipeDetailState.collectAsState()
    val isFavourite = viewModel.isFavourite(recipeId).collectAsState(initial = false)
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    LaunchedEffect(recipeId) {
        try {
            viewModel.getRecipeDetails(recipeId)
        } catch (e: Exception) {
            // Show error message to user
            Toast.makeText(
                context,
                "Error loading recipe details: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
            // Navigate back on error
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recipe Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    // Favourite button
                    if (recipeState.value is RecipeDetailState.Success) {
                        IconButton(
                            onClick = {
                                val recipe = (recipeState.value as RecipeDetailState.Success).recipe
                                viewModel.toggleFavourite(recipe)
                            }
                        ) {
                            Icon(
                                imageVector = if (isFavourite.value) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = if (isFavourite.value) "Remove from favourites" else "Add to favourites",
                                tint = if (isFavourite.value) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            )
        }
    ) { paddingValues ->
        when (val state = recipeState.value) {
            is RecipeDetailState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is RecipeDetailState.Success -> {
                val recipe = state.recipe
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // Recipe Image
                    item {
                        recipe.image?.let { imageUrl ->
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = imageUrl,
                                    contentScale = ContentScale.Crop
                                ),
                                contentDescription = "${recipe.title} image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp)
                                    .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }

                        // Recipe Title and Basic Info
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = recipe.title,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            // Cooking time and servings
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                InfoChip(
                                    text = "${recipe.readyInMinutes} min",
                                    icon = "⏱️"
                                )
                                InfoChip(
                                    text = "${recipe.servings} servings",
                                    icon = "👥"
                                )
                            }

                            // Summary (HTML content, we'll show a simplified version)
                            Text(
                                text = recipe.summary.replace(Regex("<[^>]*>"), ""),
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                        }
                    }

                    // Ingredients Section
                    if (recipe.extendedIngredients.isNotEmpty()) {
                        item { SectionTitle("Ingredients") }
                        items(recipe.extendedIngredients) { ingredient ->
                            IngredientItem(ingredient = ingredient)
                        }
                    }

                    // Instructions Section
                    if (recipe.analyzedInstructions.isNotEmpty()) {
                        item { SectionTitle("Instructions") }
                        recipe.analyzedInstructions.firstOrNull()?.let { instruction ->
                            items(instruction.steps) { step ->
                                InstructionStep(step = step)
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }

                    // Nutrition Section
                    recipe.nutrition?.let { nutrition ->
                        item { SectionTitle("Nutrition") }
                        val importantNutrients = nutrition.nutrients.filter { nutrient ->
                            nutrient.name in listOf(
                                "Calories",
                                "Protein",
                                "Carbohydrates",
                                "Fat",
                                "Fiber"
                            )
                        }
                        
                        items(importantNutrients) { nutrient ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = nutrient.name,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = "${nutrient.amount.toInt()} ${nutrient.unit}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
            is RecipeDetailState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error loading recipe details")
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
private fun InfoChip(text: String, icon: String) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = icon,
                modifier = Modifier.padding(end = 4.dp)
            )
            Text(text = text)
        }
    }
}

@Composable
private fun IngredientItem(ingredient: com.example.nutrifindapp.data.model.Ingredient) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("•")
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = ingredient.original,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun InstructionStep(step: com.example.nutrifindapp.data.model.Step) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "${step.number}.",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = step.step,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
    }
}

sealed class RecipeDetailState {
    object Loading : RecipeDetailState()
    data class Success(val recipe: Recipe) : RecipeDetailState()
    data class Error(val message: String) : RecipeDetailState()
}
