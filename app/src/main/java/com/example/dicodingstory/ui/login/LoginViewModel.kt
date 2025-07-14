package com.example.dicodingstory.ui.login

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.dicodingstory.R
import com.example.dicodingstory.data.StoryRepository

class LoginViewModel(
    context: Context,
    private val storyRepository: StoryRepository
) : ViewModel() {
    val errorMessage = context.getString(R.string.login_failed)
    fun loginAccount(email: String, password: String) = storyRepository.loginAccount(email, password, errorMessage)
}