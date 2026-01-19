package ru.ugrasu.eljunior.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ru.ugrasu.eljunior.data.api.MoodleApi
import ru.ugrasu.eljunior.data.model.SiteInfo
import ru.ugrasu.eljunior.data.model.TokenResponse
import ru.ugrasu.eljunior.data.model.UserProfile
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

@Singleton
class AuthRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val moodleApi: MoodleApi
) {
    companion object {
        private val TOKEN_KEY = stringPreferencesKey("moodle_token")
        private val USER_ID_KEY = intPreferencesKey("user_id")
        private val USERNAME_KEY = stringPreferencesKey("username")
        private val FIRST_NAME_KEY = stringPreferencesKey("first_name")
        private val LAST_NAME_KEY = stringPreferencesKey("last_name")
        private val FULL_NAME_KEY = stringPreferencesKey("full_name")
        private val AVATAR_URL_KEY = stringPreferencesKey("avatar_url")
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[TOKEN_KEY] != null
    }

    val currentUser: Flow<UserProfile?> = context.dataStore.data.map { preferences ->
        val userId = preferences[USER_ID_KEY] ?: return@map null
        UserProfile(
            id = userId,
            username = preferences[USERNAME_KEY] ?: "",
            firstName = preferences[FIRST_NAME_KEY] ?: "",
            lastName = preferences[LAST_NAME_KEY] ?: "",
            fullName = preferences[FULL_NAME_KEY] ?: "",
            avatarUrl = preferences[AVATAR_URL_KEY]
        )
    }

    suspend fun getToken(): String? {
        return context.dataStore.data.first()[TOKEN_KEY]
    }

    suspend fun getUserId(): Int? {
        return context.dataStore.data.first()[USER_ID_KEY]
    }

    suspend fun login(username: String, password: String): Result<UserProfile> {
        return try {
            // Step 1: Get token
            val tokenResponse = moodleApi.getToken(username, password)

            if (!tokenResponse.isSuccessful) {
                return Result.failure(Exception("Ошибка сервера: ${tokenResponse.code()}"))
            }

            val tokenBody = tokenResponse.body()
            if (tokenBody?.token == null) {
                val errorMessage = tokenBody?.error ?: "Неверный логин или пароль"
                return Result.failure(Exception(errorMessage))
            }

            val token = tokenBody.token

            // Step 2: Get site info
            val siteInfoResponse = moodleApi.getSiteInfo(token)

            if (!siteInfoResponse.isSuccessful) {
                return Result.failure(Exception("Не удалось получить данные пользователя"))
            }

            val siteInfo = siteInfoResponse.body()
                ?: return Result.failure(Exception("Пустой ответ от сервера"))

            // Step 3: Save to DataStore
            val userProfile = UserProfile.fromSiteInfo(siteInfo)
            saveUserData(token, userProfile)

            Result.success(userProfile)
        } catch (e: Exception) {
            Result.failure(Exception("Ошибка подключения: ${e.localizedMessage}"))
        }
    }

    private suspend fun saveUserData(token: String, user: UserProfile) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
            preferences[USER_ID_KEY] = user.id
            preferences[USERNAME_KEY] = user.username
            preferences[FIRST_NAME_KEY] = user.firstName
            preferences[LAST_NAME_KEY] = user.lastName
            preferences[FULL_NAME_KEY] = user.fullName
            user.avatarUrl?.let { preferences[AVATAR_URL_KEY] = it }
        }
    }

    suspend fun logout() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    suspend fun refreshUserProfile(): Result<UserProfile> {
        val token = getToken() ?: return Result.failure(Exception("Не авторизован"))

        return try {
            val response = moodleApi.getSiteInfo(token)
            if (response.isSuccessful && response.body() != null) {
                val userProfile = UserProfile.fromSiteInfo(response.body()!!)
                saveUserData(token, userProfile)
                Result.success(userProfile)
            } else {
                Result.failure(Exception("Не удалось обновить профиль"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserProfile(): UserProfile? {
        return currentUser.first()
    }
}
