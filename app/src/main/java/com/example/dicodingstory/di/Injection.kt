package com.example.dicodingstory.di

import android.content.Context
import com.example.dicodingstory.data.StoryRepository
import com.example.dicodingstory.data.local.room.StoryDatabase
import com.example.dicodingstory.data.remote.retrofit.ApiConfig
import com.example.dicodingstory.utils.AppExecutors
import com.example.dicodingstory.utils.SessionManager
import com.example.dicodingstory.utils.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val sessionManager = SessionManager.getInstance(context.dataStore)
        val session = runBlocking { sessionManager.getSessionToken().first() }
        val apiService = ApiConfig.getApiService(session)
        val database = StoryDatabase.getInstance(context)
        val dao = database.storyDao()
        val appExecutors = AppExecutors()
        return StoryRepository.getInstance(apiService, sessionManager, dao, appExecutors)
    }
}