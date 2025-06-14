package com.example.dicodingstory.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class SessionManager private constructor(private val dataStore: DataStore<Preferences>) {

    fun getSessionToken(): Flow<String?> {
        return dataStore.data.map { it[TOKEN_KEY] }
    }

    suspend fun saveSessionToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    suspend fun clearSessionToken() {
        dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
        }
    }

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("session_token")

        @Volatile
        private var INSTANCE: SessionManager? = null

        fun getInstance(dataStore: DataStore<Preferences>): SessionManager {
            return INSTANCE ?: synchronized(this) {
                val instance = SessionManager(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}