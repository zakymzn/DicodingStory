package com.example.dicodingstory.ui.settings

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import android.util.Log
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.dicodingstory.R
import com.example.dicodingstory.databinding.ActivitySettingsBinding
import com.example.dicodingstory.utils.LanguageManager
import com.example.dicodingstory.utils.UserPreferences
import com.example.dicodingstory.utils.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.Locale

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
//    private lateinit var userPreferences: UserPreferences
//    private lateinit var localeListToSet: LocaleList

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userPreferences = UserPreferences.getInstance(this.dataStore)

        val toolbar = binding.materialToolbar
        val tvAccountName = binding.tvAccountName
        val clLanguage = binding.clLanguage

        val settingsViewModelFactory: SettingsViewModelFactory = SettingsViewModelFactory.getInstance(this, userPreferences)
        val settingsViewModel: SettingsViewModel by viewModels {
            settingsViewModelFactory
        }

        val languages = arrayOf("English", "Indonesia")
        val checkedItem = if (Locale.getDefault().displayLanguage == "English") 0 else 1
//        val localeSet = ""
//        localeListToSet = LocaleList(Locale(localeToSet))
//        LocaleList.setDefault(localeListToSet)
//        var localeToSet = Locale(localeSet)
//        resources.configuration.setLocale(localeToSet)
//        resources.updateConfiguration(resources.configuration, resources.displayMetrics)

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        settingsViewModel.getAccount().observe(this) { account ->
            if (account != null) {
                tvAccountName.text = account.name
            }
        }

//        clLanguage.setOnClickListener {
//            settingsViewModel.getLanguageSetting().observe(this) { language ->
//                AlertDialog.Builder(this).apply {
//                    setTitle(getString(R.string.select_language))
//                    setSingleChoiceItems(languages, checkedItem) { dialog, selection ->
//                        when (selection) {
//                            0 -> {
////                                setLocale("en")
//                                Log.d("Language", "Language : $language")
//                                settingsViewModel.saveLanguageSetting("en")
//                                localeToSet = Locale(language!!)
//                                resources.configuration.setLocale(localeToSet)
//                                resources.updateConfiguration(resources.configuration, resources.displayMetrics)
//                            }
//                            1 -> {
////                                setLocale("in")
//                                Log.d("Language", "Language : $language")
//                                settingsViewModel.saveLanguageSetting("in")
//                                localeToSet = Locale(language!!)
//                                resources.configuration.setLocale(localeToSet)
//                                resources.updateConfiguration(resources.configuration, resources.displayMetrics)
//                            }
//                        }
//                        recreate()
//                        dialog.dismiss()
//                    }
//                    create()
//                    show()
//                }
//            }
//        }
    }

    override fun attachBaseContext(newBase: Context) {
        val userPreferences = UserPreferences.getInstance(newBase.dataStore)
        runBlocking {
            val language = userPreferences.getLanguageSetting().first()
            super.attachBaseContext(LanguageManager.setLocale(newBase, language))
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