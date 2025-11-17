package com.example.nutrifindapp

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.nutrifindapp.navigation.AppNavigation
import com.example.nutrifindapp.ui.settings.SettingsViewModel
import com.example.nutrifindapp.ui.theme.NutriFindAppTheme
import com.example.nutrifindapp.utils.LocaleHelper
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val userPreferences by settingsViewModel.userPreferences.collectAsState()
            
            // Apply language when it changes
            LaunchedEffect(userPreferences.languageCode) {
                applyLanguage(userPreferences.languageCode)
            }
            
            NutriFindAppTheme(darkTheme = userPreferences.isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(newBase)
    }

    private fun applyLanguage(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}