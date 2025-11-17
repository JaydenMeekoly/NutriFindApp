package com.example.nutrifindapp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.nutrifindapp.R
import com.example.nutrifindapp.navigation.Screen

@Composable
fun BottomNavBar(
    currentRoute: String?,
    onItemSelected: (Screen) -> Unit
) {
    // Include all main navigation items
    val navItems = listOf(
        Triple(Screen.Main, "Home", Icons.Default.Home),
        Triple(Screen.Recipe, "Search", Icons.Default.Search),
        Triple(Screen.RecommendedRecipes, "Recommended", Icons.Default.Star),
        Triple(Screen.Favourites, "Favourites", Icons.Default.Favorite)
    )
    
    NavigationBar {
        navItems.forEach { (screen, title, icon) ->
            val isSelected = when (screen) {
                is Screen.Main -> currentRoute == Screen.Main.route
                is Screen.Recipe -> currentRoute?.startsWith(Screen.Recipe.route) == true
                is Screen.RecommendedRecipes -> currentRoute == Screen.RecommendedRecipes.route
                is Screen.Favourites -> currentRoute == Screen.Favourites.route
                else -> false
            }
            
            NavigationBarItem(
                icon = { Icon(icon, contentDescription = title) },
                label = { Text(title) },
                selected = isSelected,
                onClick = { onItemSelected(screen) }
            )
        }
    }
}
