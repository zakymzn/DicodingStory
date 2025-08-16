package com.example.dicodingstory.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.*
import com.example.dicodingstory.data.local.entity.AccountEntity
import com.example.dicodingstory.data.local.entity.RemoteKeys
import com.example.dicodingstory.data.local.entity.StoryEntity

@Database(
    entities = [
        StoryEntity::class,
        AccountEntity::class,
        RemoteKeys::class
               ],
    version = 2,
    exportSchema = false
)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var instance: StoryDatabase? = null
        fun getInstance(context: Context): StoryDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    StoryDatabase::class.java, "Story.db"
                ).build()
            }
    }
}