package com.example.dicodingstory.di

import android.content.Context
import com.example.dicodingstory.data.StoryRepository
import com.example.dicodingstory.data.local.room.StoryDatabase
import com.example.dicodingstory.data.remote.retrofit.ApiConfig
import com.example.dicodingstory.utils.AppExecutors
import com.example.dicodingstory.utils.UserPreferences
import com.example.dicodingstory.utils.UserSharedPreferences
import com.example.dicodingstory.utils.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val preferences = UserSharedPreferences(context)
        val session = runBlocking { preferences.getSessionToken() }
        val apiService = ApiConfig.getApiService(session ?: "")
        val database = StoryDatabase.getInstance(context)
        val dao = database.storyDao()
        val appExecutors = AppExecutors()
        return StoryRepository.getInstance(apiService, preferences, dao, appExecutors)
    }
}