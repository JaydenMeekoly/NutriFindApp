package com.example.nutrifindapp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
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
    onMenuClick: () -> Unit,
    onItemSelected: (Screen) -> Unit
) {
    // Include main navigation items (4 tabs + menu)
    val navItems = listOf(
        Triple(Screen.Main, stringResource(R.string.nav_home), Icons.Default.Home),
        Triple(Screen.Recipe, stringResource(R.string.nav_search), Icons.Default.Search),
        Triple(Screen.RecommendedRecipes, stringResource(R.string.nav_recommended), Icons.Default.Star),
        Triple(Screen.Favourites, stringResource(R.string.nav_favourites), Icons.Default.Favorite)
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
        
        // Menu button to open drawer
        NavigationBarItem(
            icon = { Icon(Icons.Default.Menu, contentDescription = "Menu") },
            label = { Text(stringResource(R.string.nav_menu)) },
            selected = false,
            onClick = onMenuClick
        )
    }
}
