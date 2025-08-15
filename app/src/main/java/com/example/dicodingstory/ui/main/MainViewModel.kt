package com.example.dicodingstory.ui.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.dicodingstory.R
import com.example.dicodingstory.data.StoryRepository
import com.example.dicodingstory.data.local.entity.StoryEntity
import com.example.dicodingstory.data.remote.response.ListStoryItem

class MainViewModel(
    context: Context,
    private val storyRepository: StoryRepository
) : ViewModel() {
    val errorMessage = context.getString(R.string.token_not_found)
//    fun getAllStories() = storyRepository.getAllStories(errorMessage)
    fun getAllStories(): LiveData<PagingData<StoryEntity>> = storyRepository.getAllStories().cachedIn(viewModelScope)
}