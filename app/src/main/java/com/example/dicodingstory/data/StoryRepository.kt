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
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

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
            val loginResult = response.loginResult

            if (response.error == true || loginResult?.token == null) {
                emit(Result.Error("Login gagal"))
            } else {
                session.saveSessionToken(loginResult.token)
                emit(Result.Success(response))
            }
        } catch (e: HttpException) {
            Log.e("StoryRepository", "loginAccount: ${e.message.toString()}")
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, StoryErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage.toString()))
        }
    }

    fun registerAccount(name: String, email: String, password: String): LiveData<Result<StoryErrorResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.registerAccount(name, email, password)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            Log.e("StoryRepository", "registerAccount: ${e.message.toString()}")
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, StoryErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage.toString()))
        }
    }

    fun getAllStories(): LiveData<Result<List<StoryEntity>>> = liveData {
        emit(Result.Loading)
        val token = session.getSessionToken().first()

        if (token.isNullOrEmpty()) {
            emit(Result.Error("Token tidak ditemukan"))
            return@liveData
        }

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
            storyDao.deleteAll()
            storyDao.insertStory(storyList)
        } catch (e: HttpException) {
            Log.e("StoryRepository", "getAllStories: ${e.message.toString()}")
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, StoryErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage.toString()))
        }
        val localData: LiveData<Result<List<StoryEntity>>> = storyDao.getAllStories().map { Result.Success(it) }
        emitSource(localData)
    }

    fun addNewStory(file: MultipartBody.Part, description: RequestBody): LiveData<Result<StoryErrorResponse>> = liveData {
        emit(Result.Loading)
        val token = session.getSessionToken().first()

        if (token.isNullOrEmpty()) {
            emit(Result.Error("Token tidak ditemukan"))
            return@liveData
        }

        try {
            val response = apiService.addNewStory(file, description)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            Log.e("StoryRepository", "addNewStory: ${e.message.toString()}")
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, StoryErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage.toString()))
        }
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