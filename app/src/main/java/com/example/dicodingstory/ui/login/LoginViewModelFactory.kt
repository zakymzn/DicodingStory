package com.example.dicodingstory.ui.login

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dicodingstory.data.StoryRepository
import com.example.dicodingstory.di.Injection

class LoginViewModelFactory private constructor(
    private val context: Context,
    private val storyRepository: StoryRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(context, storyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: LoginViewModelFactory? = null
        fun getInstance(context: Context): LoginViewModelFactory = instance ?: synchronized(this) {
            instance ?: LoginViewModelFactory(
                context.applicationContext,
                Injection.provideRepository(context.applicationContext)
            )
        }.also { instance = it }
    }
}