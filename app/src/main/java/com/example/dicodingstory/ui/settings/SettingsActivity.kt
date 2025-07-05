package com.example.dicodingstory.ui.settings

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.ConfigurationCompat
import com.example.dicodingstory.R
import com.example.dicodingstory.databinding.ActivitySettingsBinding
import java.util.Locale

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocale()
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.materialToolbar
        val tvAccountName = binding.tvAccountName
        val clLanguage = binding.clLanguage

        val languages = arrayOf("English", "Indonesia")

        val settingsViewModelFactory: SettingsViewModelFactory = SettingsViewModelFactory.getInstance(this)
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

        clLanguage.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.select_language))
                setSingleChoiceItems(languages, -1) { dialog, selection ->
                    when (selection) {
                        0 -> {
                            setLocale("en")
                        }
                        1 -> {
                            setLocale("in")
                        }
                    }
                    recreate()
                    dialog.dismiss()
                }
                create()
                show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setLocale(localeToSet: String) {
        val localeListToSet = LocaleList(Locale(localeToSet))
        LocaleList.setDefault(localeListToSet)

        resources.configuration.setLocales(localeListToSet)
        resources.updateConfiguration(resources.configuration, resources.displayMetrics)

        val sharedPref = getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
        sharedPref.putString("locale_to_set", localeToSet)
        sharedPref.apply()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun loadLocale() {
        val sharedPref = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val localeToSet: String = sharedPref.getString("locale_to_set", "")!!
        setLocale(localeToSet)
    }
}