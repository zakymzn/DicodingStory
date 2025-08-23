package com.example.dicodingstory.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.dicodingstory.data.StoryRepository
import com.example.dicodingstory.data.local.entity.StoryEntity

class MainViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getAllStories(): LiveData<PagingData<StoryEntity>> = storyRepository.getAllStories().cachedIn(viewModelScope)
}