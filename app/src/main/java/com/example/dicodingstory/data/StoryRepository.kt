package com.example.dicodingstory.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.dicodingstory.data.local.entity.StoryEntity
import com.example.dicodingstory.data.local.room.StoryDao
import com.example.dicodingstory.data.remote.response.StoryErrorResponse
import com.example.dicodingstory.data.remote.response.StoryLoginResponse
import com.example.dicodingstory.data.remote.retrofit.ApiService
import com.example.dicodingstory.utils.AppExecutors
import com.example.dicodingstory.utils.SessionManager

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val session: SessionManager,
    private val storyDao: StoryDao,
    private val appExecutors: AppExecutors
) {
    fun loginAccount(email: String, password: String): LiveData<Result<StoryLoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.loginAccount(email, password)
            val error = response.error
            val message = response.message
            val loginResult = response.loginResult
            val result = StoryLoginResponse(
                loginResult,
                error,
                message
            )
            session.saveSessionToken(loginResult?.token!!)
            emit(Result.Success(result))
        } catch (e: Exception) {
            Log.d("StoryRepository", "loginAccount: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun registerAccount(name: String, email: String, password: String): LiveData<Result<StoryErrorResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.registerAccount(name, email, password)
            val error = response.error
            val message = response.message
            val result = StoryErrorResponse(
                error,
                message
            )
            emit(Result.Success(result))
        } catch (e: Exception) {
            Log.d("StoryRepository", "RegisterAccount: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getAllStories(): LiveData<Result<List<StoryEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getAllStories()
            val stories = response.listStory
            val storyList = stories.map { story ->
                StoryEntity(
                    story.id!!,
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

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            apiService: ApiService,
            session: SessionManager,
            storyDao: StoryDao,
            appExecutors: AppExecutors
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, session, storyDao, appExecutors)
            }.also { instance = it }
    }
}