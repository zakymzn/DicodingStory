package com.example.dicodingstory.ui.register

import androidx.lifecycle.ViewModel
import com.example.dicodingstory.data.StoryRepository

class RegisterViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun registerAccount(name: String, email: String, password: String) = storyRepository.registerAccount(name, email, password)
}