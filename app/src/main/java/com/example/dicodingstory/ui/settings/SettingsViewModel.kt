package com.example.dicodingstory.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.dicodingstory.data.StoryRepository
import com.example.dicodingstory.utils.UserPreferences
import kotlinx.coroutines.launch

class SettingsViewModel(private val storyRepository: StoryRepository, private val preferences: UserPreferences) : ViewModel() {
    fun getAccount() = storyRepository.getAccount()

    fun getLanguageSetting(): LiveData<String?> {
        return preferences.getLanguageSetting().asLiveData()
    }

    fun saveLanguageSetting(language: String) {
        viewModelScope.launch {
            preferences.saveLanguageSetting(language)
        }
    }
}