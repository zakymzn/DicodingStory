package com.example.dicodingstory.ui.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dicodingstory.data.StoryRepository
import com.example.dicodingstory.di.Injection

class SettingsViewModelFactory private constructor(private val storyRepository: StoryRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(storyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: SettingsViewModelFactory? = null
        fun getInstance(context: Context): SettingsViewModelFactory = instance ?: synchronized(this) {
            instance ?: SettingsViewModelFactory(Injection.provideRepository(context))
        }.also { instance = it }
    }
}