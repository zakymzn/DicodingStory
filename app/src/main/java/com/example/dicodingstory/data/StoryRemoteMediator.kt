package com.example.dicodingstory.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.dicodingstory.data.local.entity.StoryEntity
import com.example.dicodingstory.data.local.room.StoryDatabase
import com.example.dicodingstory.data.remote.retrofit.ApiService
import com.example.dicodingstory.utils.UserPreferences
import kotlinx.coroutines.flow.first

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val database: StoryDatabase,
    private val apiService: ApiService,
    private val preferences: UserPreferences
) : RemoteMediator<Int, StoryEntity>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StoryEntity>
    ): MediatorResult {
        val page = INITIAL_PAGE_INDEX
        val token = preferences.getSessionToken().first()

        try {
            val responseData = apiService.getAllStories(
                "Bearer $token",
                page,
                state.config.pageSize
            )
            val stories = responseData.listStory.map { story ->
                StoryEntity(
                    id = story.id!!,
                    name = story.name,
                    description = story.description,
                    photoUrl = story.photoUrl,
                    createdAt = story.createdAt,
                    lat = story.lat,
                    lon = story.lon
                )
            }
            val endOfPaginationReached = responseData.listStory.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.storyDao().deleteAll()
                }
                database.storyDao().insertStory(stories)
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}