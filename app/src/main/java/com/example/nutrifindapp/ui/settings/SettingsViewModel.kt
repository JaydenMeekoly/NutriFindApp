package com.example.nutrifindapp.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutrifindapp.data.preferences.SettingsRepository
import com.example.nutrifindapp.data.preferences.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val userPreferences: StateFlow<UserPreferences> = settingsRepository.userPreferencesFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserPreferences()
        )

    fun updateDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.updateDarkMode(enabled)
        }
    }

    fun updateNotifications(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.updateNotifications(enabled)
        }
    }

    fun updateShowNutritionInfo(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.updateShowNutritionInfo(enabled)
        }
    }

    fun toggleShowNutritionInfo() {
        viewModelScope.launch {
            settingsRepository.updateShowNutritionInfo(!userPreferences.value.showNutritionInfo)
        }
    }

    fun updateLanguage(languageCode: String) {
        viewModelScope.launch {
            settingsRepository.updateLanguage(languageCode)
        }
    }

    suspend fun setFirstLaunchComplete() {
        settingsRepository.setFirstLaunchComplete()
    }

    fun toggleBiometric() {
        viewModelScope.launch {
            settingsRepository.updateBiometricEnabled(!userPreferences.value.biometricEnabled)
        }
    }

    fun updateBiometric(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.updateBiometricEnabled(enabled)
        }
    }
}
