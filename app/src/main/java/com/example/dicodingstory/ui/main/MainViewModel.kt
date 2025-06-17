package com.example.dicodingstory.ui.main

import androidx.lifecycle.ViewModel
import com.example.dicodingstory.data.StoryRepository

class MainViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getAllStories() = storyRepository.getAllStories()
}