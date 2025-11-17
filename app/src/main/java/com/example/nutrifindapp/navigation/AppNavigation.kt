package com.example.nutrifindapp.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.nutrifindapp.ui.components.BottomNavBar
import com.example.nutrifindapp.ui.favourites.FavouritesScreen
import com.example.nutrifindapp.ui.history.RecipeHistoryScreen
import com.example.nutrifindapp.ui.main.MainScreen
import com.example.nutrifindapp.ui.recommended.RecommendedRecipesScreen
import com.example.nutrifindapp.ui.recipe.RecipeScreen
import com.example.nutrifindapp.ui.recipe.detail.RecipeDetailScreen
import com.example.nutrifindapp.ui.settings.SettingsScreen
import com.example.nutrifindapp.ui.shoppinglist.ShoppingListScreen
import kotlinx.coroutines.launch

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object Recipe : Screen("recipe")
    object RecommendedRecipes : Screen("recommended")
    object Favourites : Screen("favourites")
    object Settings : Screen("settings")
    object ShoppingList : Screen("shopping_list")
    object RecipeHistory : Screen("recipe_history")
    object RecipeDetail : Screen("recipe_detail/{recipeId}")

    companion object {
        const val RECIPE_ID_ARG = "recipeId"
        const val RECIPE_DETAIL_ROUTE = "recipe_detail/{$RECIPE_ID_ARG}"

        fun getRecipeDetailRoute(recipeId: Int) = "recipe_detail/$recipeId"
    }
}

data class DrawerMenuItem(
    val route: String,
    val title: String,
    val icon: ImageVector
)

val drawerMenuItems = listOf(
    DrawerMenuItem(Screen.Settings.route, "Settings", Icons.Default.Settings),
    DrawerMenuItem(Screen.ShoppingList.route, "Shopping List", Icons.Default.ShoppingCart),
    DrawerMenuItem(Screen.RecipeHistory.route, "Recipe History", Icons.Default.History)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Only show bottom nav on main screens (not Settings, ShoppingList, or RecipeHistory)
    val showBottomNav = when (currentRoute) {
        Screen.Main.route,
        Screen.Recipe.route,
        Screen.RecommendedRecipes.route,
        Screen.Favourites.route -> true
        else -> false
    }

    // Get current bottom nav item route
    val currentBottomNavRoute = when {
        currentRoute?.startsWith(Screen.Recipe.route) == true -> Screen.Recipe.route
        currentRoute?.startsWith(Screen.RecommendedRecipes.route) == true -> Screen.RecommendedRecipes.route
        currentRoute?.startsWith(Screen.Favourites.route) == true -> Screen.Favourites.route
        else -> Screen.Main.route
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    "NutriFindApp",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleLarge
                )
                HorizontalDivider()
                drawerMenuItems.forEach { item ->
                    NavigationDrawerItem(
                        icon = { Icon(item.icon, contentDescription = null) },
                        label = { Text(item.title) },
                        selected = currentRoute == item.route,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                            }
                            navController.navigate(item.route) {
                                launchSingleTop = true
                            }
                        },
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            }
        }
    ) {
        Scaffold(
            bottomBar = {
                if (showBottomNav) {
                    BottomNavBar(
                        currentRoute = currentBottomNavRoute,
                        onMenuClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        },
                        onItemSelected = { screen ->
                            when (screen) {
                                is Screen.Main -> {
                                if (currentRoute != Screen.Main.route) {
                                    navController.navigate(Screen.Main.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                            inclusive = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }

                            is Screen.Recipe -> {
                                if (currentRoute != Screen.Recipe.route) {
                                    navController.navigate(Screen.Recipe.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                            inclusive = false
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }

                            is Screen.RecommendedRecipes -> {
                                if (currentRoute != Screen.RecommendedRecipes.route) {
                                    navController.navigate(Screen.RecommendedRecipes.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                            inclusive = false
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }

                            is Screen.Favourites -> {
                                if (currentRoute != Screen.Favourites.route) {
                                    navController.navigate(Screen.Favourites.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                            inclusive = false
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }

                            is Screen.Settings -> {
                                if (currentRoute != Screen.Settings.route) {
                                    navController.navigate(Screen.Settings.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                            inclusive = false
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }

                            // Add an else branch to handle any potential future Screen types
                            else -> { /* No-op */ }
                        }
                    }
                )
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Main.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Main.route) {
                MainScreen(
                    onGetStarted = {
                        navController.navigate(Screen.Recipe.route) {
                            popUpTo(Screen.Main.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Recipe.route) {
                RecipeScreen(
                    onRecipeClick = { recipeId ->
                        navController.navigate(Screen.getRecipeDetailRoute(recipeId))
                    }
                )
            }

            composable(Screen.RecommendedRecipes.route) {
                RecommendedRecipesScreen(
                    onRecipeClick = { recipeId ->
                        navController.navigate(Screen.getRecipeDetailRoute(recipeId))
                    }
                )
            }

            composable(Screen.Favourites.route) {
                FavouritesScreen(
                    onRecipeClick = { recipeId ->
                        navController.navigate(Screen.getRecipeDetailRoute(recipeId))
                    }
                )
            }

            composable(Screen.Settings.route) {
                SettingsScreen()
            }

            composable(Screen.ShoppingList.route) {
                ShoppingListScreen()
            }

            composable(Screen.RecipeHistory.route) {
                RecipeHistoryScreen(
                    onRecipeClick = { recipeId ->
                        navController.navigate(Screen.getRecipeDetailRoute(recipeId))
                    }
                )
            }

            composable(
                route = Screen.RECIPE_DETAIL_ROUTE,
                arguments = listOf(
                    navArgument(Screen.RECIPE_ID_ARG) {
                        type = NavType.IntType
                    }
                )
            ) { backStackEntry ->
                val recipeId = backStackEntry.arguments?.getInt(Screen.RECIPE_ID_ARG) ?: return@composable
                RecipeDetailScreen(
                    recipeId = recipeId,
                    navController = navController
                )
            }
        }
    }
    }
}