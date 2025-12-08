package br.com.apollomusic.utils

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

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class TokenManager @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        private val FCM_TOKEN_KEY = stringPreferencesKey("fcm_token")
    }

    suspend fun saveFCMToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[FCM_TOKEN_KEY] = token
        }
    }

    val fcmToken: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[FCM_TOKEN_KEY] ?: ""
        }
}