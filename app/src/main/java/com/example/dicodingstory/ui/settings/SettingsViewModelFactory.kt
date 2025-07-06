package com.example.dicodingstory.ui.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dicodingstory.data.StoryRepository
import com.example.dicodingstory.di.Injection
import com.example.dicodingstory.utils.UserPreferences

class SettingsViewModelFactory private constructor(private val storyRepository: StoryRepository, private val preferences: UserPreferences) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(storyRepository, preferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: SettingsViewModelFactory? = null
        fun getInstance(context: Context, preferences: UserPreferences): SettingsViewModelFactory = instance ?: synchronized(this) {
            instance ?: SettingsViewModelFactory(Injection.provideRepository(context), preferences)
        }.also { instance = it }
    }
}