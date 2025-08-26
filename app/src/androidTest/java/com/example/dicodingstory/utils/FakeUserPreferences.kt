import com.example.dicodingstory.utils.UserPreferencesContract
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeUserPreferences : UserPreferencesContract {

    private val _sessionToken = MutableStateFlow<String?>(null)
    private val _languageSetting = MutableStateFlow("en")

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

    fun setSessionToken(token: String?) {
        _sessionToken.value = token
    }

    fun setLanguageSetting(language: String) {
        _languageSetting.value = language
    }

    fun clearAll() {
        _sessionToken.value = null
        _languageSetting.value = "en"
    }
}