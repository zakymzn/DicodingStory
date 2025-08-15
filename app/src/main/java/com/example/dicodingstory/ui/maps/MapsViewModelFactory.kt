package com.example.dicodingstory.ui.maps

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dicodingstory.data.StoryRepository
import com.example.dicodingstory.di.Injection

class MapsViewModelFactory private constructor(
    private val context: Context,
    private val storyRepository: StoryRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            return MapsViewModel(context, storyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: MapsViewModelFactory? = null
        fun getInstance(context: Context): MapsViewModelFactory = instance ?: synchronized(this) {
            instance ?: MapsViewModelFactory(
                context.applicationContext,
                Injection.provideRepository(context.applicationContext)
            )
        }.also { instance = it }
    }
}