package com.example.dicodingstory.ui.story

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dicodingstory.data.StoryRepository
import com.example.dicodingstory.di.Injection

class StoryViewModelFactory private constructor(private val storyRepository: StoryRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            return StoryViewModel(storyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: StoryViewModelFactory? = null
        fun getInstance(context: Context): StoryViewModelFactory = instance ?: synchronized(this) {
            instance ?: StoryViewModelFactory(Injection.provideRepository(context))
        }.also { instance = it }
    }
}