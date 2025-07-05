package com.example.dicodingstory.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class AccountEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "userId")
    var id: String = "",

    @ColumnInfo(name = "name")
    var name: String? = null,
)
