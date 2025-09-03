package com.example.dicodingstory.ui.upload

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.dicodingstory.R
import com.example.dicodingstory.data.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadViewModel(
    context: Context,
    private val storyRepository: StoryRepository
) : ViewModel() {
    val errorMessage = context.getString(R.string.token_not_found)
    fun addNewStory(file: MultipartBody.Part, description: RequestBody, lat: RequestBody?, lon: RequestBody?) = storyRepository.addNewStory(file, description, lat, lon, errorMessage)
}