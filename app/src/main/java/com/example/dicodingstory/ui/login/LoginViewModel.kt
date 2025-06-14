package com.example.dicodingstory.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingstory.data.StoryRepository
import com.example.dicodingstory.data.remote.response.StoryLoginResponse
import kotlinx.coroutines.launch

class LoginViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun loginAccount(email: String, password: String) = storyRepository.loginAccount(email, password)
}