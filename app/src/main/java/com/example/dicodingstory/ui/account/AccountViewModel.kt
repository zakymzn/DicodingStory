package com.example.dicodingstory.ui.account

import androidx.lifecycle.ViewModel
import com.example.dicodingstory.data.StoryRepository

class AccountViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getAccount() = storyRepository.getAccount()
}