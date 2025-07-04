package com.example.dicodingstory.ui.story

import androidx.lifecycle.ViewModel
import com.example.dicodingstory.data.StoryRepository

class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getAllStories() = storyRepository.getAllStories()
}