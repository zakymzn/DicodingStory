package com.example.dicodingstory.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dicodingstory.data.local.entity.AccountEntity
import com.example.dicodingstory.data.local.entity.StoryEntity

@Dao
interface StoryDao {
    @Query("SELECT * FROM story ORDER BY createdAt DESC")
    fun getAllStories(): LiveData<List<StoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<StoryEntity>)

    @Query("DELETE FROM story")
    suspend fun deleteAll()

    @Query("SELECT * FROM settings")
    fun getAccount(): LiveData<AccountEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: AccountEntity)

    @Query("SELECT * FROM story ORDER BY createdAt DESC")
    fun getAllStoryWidget(): List<StoryEntity>
}