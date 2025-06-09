package com.example.dicodingstory.data

import com.example.dicodingstory.data.local.room.StoryDao
import com.example.dicodingstory.data.remote.retrofit.ApiService
import com.example.dicodingstory.utils.AppExecutors

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val newsDao: StoryDao,
    private val appExecutors: AppExecutors
) {
}