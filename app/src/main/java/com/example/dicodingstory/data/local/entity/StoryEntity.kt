package com.example.dicodingstory.data.local.entity

import androidx.room.*

@Entity(tableName = "story")
data class StoryEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: String = "",

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "description")
    var description: String? = null,

    @ColumnInfo(name = "photoUrl")
    var photoUrl: String? = null,

    @ColumnInfo(name = "createdAt")
    var createdAt: String? = null,

    @ColumnInfo(name = "lat")
    var lat: Float? = null,

    @ColumnInfo(name = "lon")
    var lon: Float? = null
)