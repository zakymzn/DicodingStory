package com.example.dicodingstory.ui.upload

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dicodingstory.data.StoryRepository
import com.example.dicodingstory.di.Injection

class UploadViewModelFactory private constructor(
    private val context: Context,
    private val storyRepository: StoryRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UploadViewModel::class.java)) {
            return UploadViewModel(context, storyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: UploadViewModelFactory? = null
        fun getInstance(context: Context): UploadViewModelFactory = instance ?: synchronized(this) {
            instance ?: UploadViewModelFactory(
                context.applicationContext,
                Injection.provideRepository(context.applicationContext)
            )
        }.also { instance = it }
    }
}