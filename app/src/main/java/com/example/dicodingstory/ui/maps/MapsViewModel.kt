package com.example.dicodingstory.ui.maps

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.dicodingstory.R
import com.example.dicodingstory.data.StoryRepository

class MapsViewModel(
    context: Context,
    private val storyRepository: StoryRepository
) : ViewModel() {
    val errorMessage = context.getString(R.string.token_not_found)
    fun getAllStories() = storyRepository.getAllStories(errorMessage)
}