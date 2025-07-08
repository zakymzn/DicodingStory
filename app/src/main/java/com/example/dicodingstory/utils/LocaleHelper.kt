package com.example.dicodingstory.utils

import android.content.Context
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.Locale

object LocaleHelper {

    fun getSavedLanguage(context: Context): String {
        val userPreferences = UserPreferences.getInstance(context.dataStore)
        return runBlocking {
            userPreferences.getLanguageSetting().first() ?: "en"
        }
    }

    fun setLocale(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        return context.createConfigurationContext(config)
    }
}