package com.example.dicodingstory.data

import FakeUserPreferences
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.dicodingstory.data.local.entity.StoryEntity
import com.example.dicodingstory.data.local.room.StoryDatabase
import com.example.dicodingstory.data.remote.retrofit.ApiService
import com.example.dicodingstory.data.remote.retrofit.FakeApiService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExperimentalPagingApi
@RunWith(AndroidJUnit4::class)
class StoryRemoteMediatorTest {
    private lateinit var mockApi: ApiService
    private lateinit var mockDb: StoryDatabase
    private lateinit var mockPreferences: FakeUserPreferences

    @Before
    fun setUp() {
        mockDb = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            StoryDatabase::class.java
        ).allowMainThreadQueries().build()
        mockApi = FakeApiService()
        mockPreferences = FakeUserPreferences()
        mockPreferences.setSessionToken("dummy-token")
    }

    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runTest {
        val remoteMediator = StoryRemoteMediator(
            mockDb,
            mockApi,
            mockPreferences
        )

        val pagingState = PagingState<Int, StoryEntity>(
            listOf(),
            null,
            PagingConfig(6),
            6
        )

        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @After
    fun tearDown() {
        mockDb.clearAllTables()
        mockDb.close()
        mockPreferences.clearAll()
    }
}
