package com.example.dicodingstory.ui.settings

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.dicodingstory.R
import com.example.dicodingstory.databinding.ActivitySettingsBinding
import com.example.dicodingstory.utils.UserPreferences
import com.example.dicodingstory.utils.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.Locale

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun attachBaseContext(newBase: Context) {
        val userPreferences = UserPreferences.getInstance(newBase.dataStore)
        val language = runBlocking { userPreferences.getLanguageSetting().first() }
        val locale = Locale(language)
        val config = newBase.resources.configuration
        Locale.setDefault(locale)
        config.setLocale(locale)
        val context = newBase.createConfigurationContext(config)
        super.attachBaseContext(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userPreferences = UserPreferences.getInstance(this.dataStore)

        val toolbar = binding.materialToolbar
        val tvAccountName = binding.tvAccountName

        val settingsViewModelFactory: SettingsViewModelFactory = SettingsViewModelFactory.getInstance(this, userPreferences)
        val settingsViewModel: SettingsViewModel by viewModels {
            settingsViewModelFactory
        }

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        settingsViewModel.getAccount().observe(this) { account ->
            if (account != null) {
                tvAccountName.text = account.name
            }
        }

        supportFragmentManager.beginTransaction().replace(R.id.settings_container, SettingsFragment()).commit()
    }
}