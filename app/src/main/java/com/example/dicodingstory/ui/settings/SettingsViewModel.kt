package com.example.dicodingstory.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingstory.data.StoryRepository
import com.example.dicodingstory.utils.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SettingsViewModel(private val storyRepository: StoryRepository, private val userPreferences: UserPreferences) : ViewModel() {
    fun getAccount() = storyRepository.getAccount()
}