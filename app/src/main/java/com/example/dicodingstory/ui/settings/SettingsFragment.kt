package com.example.dicodingstory.ui.settings

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.example.dicodingstory.R
import com.example.dicodingstory.utils.UserPreferences
import com.example.dicodingstory.utils.dataStore

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        val userPreferences = UserPreferences.getInstance(requireContext().dataStore)

        val settingsViewModelFactory: SettingsViewModelFactory = SettingsViewModelFactory.getInstance(requireActivity(), userPreferences)
        val settingsViewModel: SettingsViewModel = ViewModelProvider(this, settingsViewModelFactory)[SettingsViewModel::class.java]

        val languageKey = getString(R.string.key_language)
        val languagePref = findPreference<ListPreference>(languageKey)

        languagePref?.setOnPreferenceChangeListener { _, newValue ->
            val language = newValue as String
            settingsViewModel.saveLanguageSetting(language)

            val intent = requireActivity().intent
            requireActivity().finish()
            startActivity(intent)
            requireActivity().overridePendingTransition(0,0)
            true
        }
    }
}