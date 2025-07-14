package com.example.dicodingstory.utils

import android.content.Context
import androidx.core.content.edit

class UserSharedPreferences(context: Context) {

    private val preferences = context.getSharedPreferences(TOKEN_PREF, Context.MODE_PRIVATE)

    fun saveSessionToken(token: String) {
        preferences.edit {
            putString(TOKEN, token)
        }
    }

    fun getSessionToken(): String? {
        return preferences.getString(TOKEN, null)
    }

    fun clearSessionToken() {
        preferences.edit {
            remove(TOKEN)
        }
    }

    companion object {
        private const val TOKEN_PREF = "token_pref"
        private const val TOKEN = "token"
    }
}