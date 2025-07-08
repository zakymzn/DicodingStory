package com.example.dicodingstory.ui.settings

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.example.dicodingstory.R
import com.example.dicodingstory.utils.UserPreferences
import com.example.dicodingstory.utils.dataStore
import kotlinx.coroutines.launch

class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var userPreferences: UserPreferences

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        userPreferences = UserPreferences.getInstance(requireContext().dataStore)

        val languageKey = getString(R.string.key_language)
        val languagePref = findPreference<ListPreference>(languageKey)
        languagePref?.setOnPreferenceChangeListener { _, newValue ->
            lifecycleScope.launch {
                userPreferences.saveLanguageSetting(newValue.toString())

                val intent = requireActivity().intent
                requireActivity().finish()
                startActivity(intent)
                requireActivity().overridePendingTransition(0, 0)
            }
            true
        }
    }
}