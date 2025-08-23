package com.example.dicodingstory.ui.settings

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.dicodingstory.R
import com.example.dicodingstory.databinding.ActivitySettingsBinding
import com.example.dicodingstory.utils.LocaleHelper
import com.example.dicodingstory.utils.UserPreferences
import com.example.dicodingstory.utils.dataStore

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun attachBaseContext(newBase: Context) {
        val language = LocaleHelper.getSavedLanguage(newBase)
        val context = LocaleHelper.setLocale(newBase, language)
        super.attachBaseContext(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userPreferences = UserPreferences.getInstance(this.dataStore)

        val toolbar = binding.materialToolbar
        val tvAccountName = binding.tvAccountName

        val settingsViewModelFactory: SettingsViewModelFactory = SettingsViewModelFactory.getInstance(this,
            userPreferences as UserPreferences
        )
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