package com.example.dicodingstory.data.local.entity

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize

@Entity(tableName = "story")
@Parcelize
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
    var lat: Double? = null,

    @ColumnInfo(name = "lon")
    var lon: Double? = null
) : Parcelable