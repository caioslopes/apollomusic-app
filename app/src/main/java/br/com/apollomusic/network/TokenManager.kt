package br.com.apollomusic.network

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session_prefs")

@Singleton
class TokenManager @Inject constructor(@ApplicationContext context: Context) {

    private val sessionDataStore = context.dataStore

    suspend fun saveToken(token: String) {
        sessionDataStore.edit { preferences ->
            preferences[KEY_AUTH_TOKEN] = token
        }
    }

    fun getToken(): Flow<String?> {
        return sessionDataStore.data.map { preferences ->
            preferences[KEY_AUTH_TOKEN]
        }
    }

    companion object {
        private val KEY_AUTH_TOKEN = stringPreferencesKey("auth_token")
    }
}