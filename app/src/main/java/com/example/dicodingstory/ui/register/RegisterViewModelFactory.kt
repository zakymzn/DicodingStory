package com.example.dicodingstory.ui.register

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dicodingstory.data.StoryRepository
import com.example.dicodingstory.di.Injection

class RegisterViewModelFactory private constructor(private val storyRepository: StoryRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(storyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: RegisterViewModelFactory? = null
        fun getInstance(context: Context): RegisterViewModelFactory = instance ?: synchronized(this) {
            instance ?: RegisterViewModelFactory(Injection.provideRepository(context))
        }.also { instance = it }
    }
}