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

class UserPreferences private constructor(private val dataStore: DataStore<Preferences>) : UserPreferencesContract {

    override fun getSessionToken(): Flow<String?> {
        return dataStore.data.map { it[TOKEN_KEY] }
    }

    override fun getLanguageSetting(): Flow<String> {
        return dataStore.data.map {
            it[LANGUAGE_KEY] ?: "en"
        }
    }

    override suspend fun saveSessionToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    override suspend fun clearSessionToken() {
        dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
        }
    }

    override suspend fun saveLanguageSetting(language: String) {
        dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = language
        }
    }

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("session_token")
        private val LANGUAGE_KEY = stringPreferencesKey("language_setting")

        @Volatile
        private var INSTANCE: UserPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferencesContract {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}