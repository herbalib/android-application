package com.rickyandrean.herbapedia.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.rickyandrean.herbapedia.model.Authentication
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthenticationPreference private constructor(private val dataStore: DataStore<Preferences>) {
    fun getAuthentication(): Flow<Authentication> {
        return dataStore.data.map { preferences ->
            Authentication(
                preferences[NAME] ?: "",
                preferences[TOKEN] ?: ""
            )
        }
    }

    suspend fun login(authentication: Authentication) {
        dataStore.edit { preferences ->
            preferences[NAME] = authentication.name
            preferences[TOKEN] = authentication.token
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[NAME] = ""
            preferences[TOKEN] = ""
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AuthenticationPreference? = null
        private val NAME = stringPreferencesKey("name")
        private val TOKEN = stringPreferencesKey("token")

        fun getInstance(dataStore: DataStore<Preferences>): AuthenticationPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = AuthenticationPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}