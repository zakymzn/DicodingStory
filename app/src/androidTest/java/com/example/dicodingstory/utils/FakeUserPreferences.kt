import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.dicodingstory.utils.UserPreferencesContract
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

/**
 * A fake implementation of UserPreferences for testing purposes.
 * This class mimics the behavior of the real UserPreferences but uses
 * in-memory StateFlows instead of DataStore for easier control in tests.
 */
class FakeUserPreferences : UserPreferencesContract { // Assuming you might want an interface

    private val _sessionToken = MutableStateFlow<String?>(null)
    private val _languageSetting = MutableStateFlow("en") // Default to "en" as in original

    override fun getSessionToken(): Flow<String?> {
        return _sessionToken.asStateFlow()
    }

    override fun getLanguageSetting(): Flow<String> {
        return _languageSetting.asStateFlow()
    }

    override suspend fun saveSessionToken(token: String) {
        _sessionToken.value = token
    }

    override suspend fun clearSessionToken() {
        _sessionToken.value = null
    }

    override suspend fun saveLanguageSetting(language: String) {
        _languageSetting.value = language
    }

    // --- Helper methods for testing ---

    /**
     * Directly sets the session token for testing.
     */
    fun setSessionToken(token: String?) {
        _sessionToken.value = token
    }

    /**
     * Directly sets the language setting for testing.
     */
    fun setLanguageSetting(language: String) {
        _languageSetting.value = language
    }

    /**
     * Resets all preferences to their default test state.
     */
    fun clearAll() {
        _sessionToken.value = null
        _languageSetting.value = "en"
    }
}