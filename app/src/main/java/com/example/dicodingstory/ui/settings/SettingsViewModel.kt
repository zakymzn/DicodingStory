package com.example.dicodingstory.ui.settings

import androidx.lifecycle.ViewModel
import com.example.dicodingstory.data.StoryRepository

class SettingsViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getAccount() = storyRepository.getAccount()
}