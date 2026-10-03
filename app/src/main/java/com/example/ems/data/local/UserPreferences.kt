package com.example.ems.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_preferences")

class UserPreferences(private val context: Context) {
    companion object {
        private val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        private val AUTH_TOKEN = stringPreferencesKey("auth_token")
    }

    val isDarkModeFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_DARK_MODE] ?: false
    }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_DARK_MODE] = enabled
        }
    }

    val authTokenFlow: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[AUTH_TOKEN]
    }

    suspend fun setAuthToken(token: String?) {
        context.dataStore.edit { preferences ->
            if (token != null) {
                preferences[AUTH_TOKEN] = token
            } else {
                preferences.remove(AUTH_TOKEN)
            }
        }
    }
}
