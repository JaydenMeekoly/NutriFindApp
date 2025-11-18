package com.example.nutrifindapp.data.preferences

data class UserPreferences(
    val isDarkMode: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val showNutritionInfo: Boolean = true,
    val languageCode: String = "en", // "en", "af", "zu"
    val isFirstLaunch: Boolean = true, // Show welcome screen on first launch
    val biometricEnabled: Boolean = false // Enable biometric authentication
)
