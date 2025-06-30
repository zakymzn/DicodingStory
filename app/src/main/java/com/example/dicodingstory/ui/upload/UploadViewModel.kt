package com.example.dicodingstory.ui.upload

import androidx.lifecycle.ViewModel
import com.example.dicodingstory.data.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun addNewStory(file: MultipartBody.Part, description: RequestBody) = storyRepository.addNewStory(file, description)
}