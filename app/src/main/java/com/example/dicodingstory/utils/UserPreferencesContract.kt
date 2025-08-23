package com.example.dicodingstory.utils

import kotlinx.coroutines.flow.Flow

interface UserPreferencesContract {
    fun getSessionToken(): Flow<String?>
    fun getLanguageSetting(): Flow<String>
    suspend fun saveSessionToken(token: String)
    suspend fun clearSessionToken()
    suspend fun saveLanguageSetting(language: String)
}