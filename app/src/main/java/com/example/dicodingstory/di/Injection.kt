package com.example.dicodingstory.di

import android.content.Context
import com.example.dicodingstory.data.StoryRepository
import com.example.dicodingstory.data.local.room.StoryDatabase
import com.example.dicodingstory.data.remote.retrofit.ApiConfig
import com.example.dicodingstory.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val apiService = ApiConfig.getApiService()
        val database = StoryDatabase.getInstance(context)
        val dao = database.storyDao()
        val appExecutors = AppExecutors()
        return StoryRepository.getInstance(apiService, dao, appExecutors)
    }
}