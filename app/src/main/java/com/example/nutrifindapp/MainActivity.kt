package com.example.nutrifindapp

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.FragmentActivity
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.nutrifindapp.data.repository.AuthRepository
import com.example.nutrifindapp.navigation.AppNavigation
import com.example.nutrifindapp.ui.auth.AuthViewModel
import com.example.nutrifindapp.ui.settings.SettingsViewModel
import com.example.nutrifindapp.ui.theme.NutriFindAppTheme
import com.example.nutrifindapp.utils.BiometricAuthManager
import com.example.nutrifindapp.utils.BiometricResult
import com.example.nutrifindapp.utils.LocaleHelper
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    private val settingsViewModel: SettingsViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()
    
    @Inject
    lateinit var biometricManager: BiometricAuthManager
    
    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val userPreferences by settingsViewModel.userPreferences.collectAsState()
            val authUiState by authViewModel.uiState.collectAsState()
            var biometricAuthenticated by remember { mutableStateOf(false) }
            var showBiometricPrompt by remember { mutableStateOf(false) }
            
            // Apply language when it changes
            LaunchedEffect(userPreferences.languageCode) {
                applyLanguage(userPreferences.languageCode)
            }
            
            // Handle biometric authentication on app launch
            LaunchedEffect(authUiState.isAuthenticated, userPreferences.biometricEnabled) {
                if (authUiState.isAuthenticated && 
                    userPreferences.biometricEnabled && 
                    !biometricAuthenticated &&
                    biometricManager.isBiometricAvailable()) {
                    showBiometricPrompt = true
                }
            }
            
            // Show biometric prompt
            LaunchedEffect(showBiometricPrompt) {
                if (showBiometricPrompt) {
                    biometricManager.authenticate(
                        activity = this@MainActivity,
                        title = getString(R.string.biometric_prompt_title),
                        subtitle = getString(R.string.biometric_prompt_subtitle),
                        negativeButtonText = getString(R.string.biometric_prompt_cancel)
                    ) { result ->
                        when (result) {
                            is BiometricResult.Success -> {
                                biometricAuthenticated = true
                                showBiometricPrompt = false
                            }
                            is BiometricResult.Failed -> {
                                // User can retry
                            }
                            is BiometricResult.Error,
                            is BiometricResult.Cancelled -> {
                                // User cancelled, sign them out
                                authViewModel.signOut()
                                showBiometricPrompt = false
                            }
                        }
                    }
                }
            }
            
            NutriFindAppTheme(darkTheme = userPreferences.isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(
                        biometricAuthenticated = biometricAuthenticated || !userPreferences.biometricEnabled,
                        authRepository = authRepository,
                        biometricManager = biometricManager
                    )
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