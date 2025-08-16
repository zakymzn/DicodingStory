package com.example.dicodingstory.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.dicodingstory.data.local.entity.StoryEntity
import com.example.dicodingstory.data.remote.retrofit.ApiService
import com.example.dicodingstory.utils.UserPreferences
import kotlinx.coroutines.flow.first

class StoryPagingSource(
    private val apiService: ApiService,
    private val preferences: UserPreferences
) : PagingSource<Int, StoryEntity>() {

    override fun getRefreshKey(state: PagingState<Int, StoryEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryEntity> {

        val token = preferences.getSessionToken().first()

        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getAllStories(
                token = "Bearer $token",
                page = position,
                size = params.loadSize
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

            LoadResult.Page(
                data = stories,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.listStory.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}