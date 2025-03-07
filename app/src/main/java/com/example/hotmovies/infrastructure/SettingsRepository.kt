package com.example.hotmovies.infrastructure

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.hotmovies.appplication.login.interfaces.SettingsRepositoryInterface
import com.example.hotmovies.shared.checkNotMainThread
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class SettingsRepository(private val dataStore: DataStore<Preferences>) :
    SettingsRepositoryInterface {

    override fun getStringValue(key: String): Flow<String> {
        return dataStore.data.map { preferences ->
            checkNotMainThread()
            val stringKey = stringPreferencesKey(key)
            preferences[stringKey]
                ?: throw SettingsRepositoryInterface.Exceptions.NoValueException(
                    key,
                    Boolean::class.java
                )
        }
    }

    override fun store(key: String, value: String): Flow<Unit> = flow {
        dataStore.edit { preferences ->
            checkNotMainThread()
            val stringKey = stringPreferencesKey(key)
            preferences[stringKey] = value
        }
        emit(Unit)
    }

    override fun clear(key: String): Flow<Unit> = flow {
        dataStore.edit { preferences ->
            checkNotMainThread()
            val stringKey = stringPreferencesKey(key)
            preferences.remove(stringKey)
        }
        emit(Unit)
    }
}