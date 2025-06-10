package com.example.dicodingstory.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.dicodingstory.data.local.entity.StoryEntity
import com.example.dicodingstory.data.local.room.StoryDao
import com.example.dicodingstory.data.remote.retrofit.ApiService
import com.example.dicodingstory.utils.AppExecutors

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val storyDao: StoryDao,
    private val appExecutors: AppExecutors
) {
    fun getAllStories(): LiveData<Result<List<StoryEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getAllStories("token")
            val stories = response.listStory
            val storyList = stories.map { story ->
                StoryEntity(
                    story.id,
                    story.name,
                    story.description,
                    story.photoUrl,
                    story.createdAt,
                    story.lat,
                    story.lon
                )
            }
            storyDao.insertStory(storyList)
        } catch (e: Exception) {
            Log.d("StoryRepository", "getAllStories: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<List<StoryEntity>>> = storyDao.getAllStories().map { Result.Success(it) }
        emitSource(localData)
    }
}