package com.example.dicodingstory.ui.login

import androidx.lifecycle.ViewModel
import com.example.dicodingstory.data.StoryRepository

class LoginViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun loginAccount(email: String, password: String) = storyRepository.loginAccount(email, password)
}