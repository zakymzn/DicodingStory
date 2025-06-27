package com.example.dicodingstory.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dicodingstory.data.local.entity.StoryEntity

@Dao
interface StoryDao {
    @Query("SELECT * FROM story")
    fun getAllStories(): LiveData<List<StoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<StoryEntity>)

    @Query("DELETE FROM story")
    suspend fun deleteAll()
}