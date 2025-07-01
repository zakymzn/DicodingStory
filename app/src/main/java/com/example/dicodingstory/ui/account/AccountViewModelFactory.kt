package com.example.dicodingstory.ui.account

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dicodingstory.data.StoryRepository
import com.example.dicodingstory.di.Injection

class AccountViewModelFactory private constructor(private val storyRepository: StoryRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccountViewModel::class.java)) {
            return AccountViewModel(storyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: AccountViewModelFactory? = null
        fun getInstance(context: Context): AccountViewModelFactory = instance ?: synchronized(this) {
            instance ?: AccountViewModelFactory(Injection.provideRepository(context))
        }.also { instance = it }
    }
}