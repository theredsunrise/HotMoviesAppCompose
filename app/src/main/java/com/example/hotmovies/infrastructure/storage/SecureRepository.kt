package com.example.hotmovies.infrastructure.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.hotmovies.appplication.login.interfaces.SecureRepositoryInterface
import com.example.hotmovies.appplication.login.interfaces.SecureRepositoryInterface.Exceptions.NoValueException
import com.example.hotmovies.shared.checkNotMainThread
import com.example.hotmovies.shared.fromBase64ToByteArray
import com.example.hotmovies.shared.toBase64
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SecureRepository @Inject constructor(
    private val crypto: CryptoInterface,
    private val dataStore: DataStore<Preferences>
) :
    SecureRepositoryInterface {

    init {
        println("******* ${this::class.simpleName}")
    }

    override fun getStringValue(key: String): Flow<String> = flow {
        val value = dataStore.data.map { preferences ->
            checkNotMainThread()
            val stringKey = stringPreferencesKey(key)
            val encryptedValue = preferences[stringKey]
                ?: throw NoValueException(
                    key,
                    Boolean::class.java
                )
            decrypt(encryptedValue)
        }.first()
        emit(value)
    }

    override fun store(key: String, value: String): Flow<Unit> = flow {
        dataStore.edit { preferences ->
            checkNotMainThread()
            val stringKey = stringPreferencesKey(key)
            preferences[stringKey] = encrypt(value)
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

    private fun encrypt(plainString: String): String {
        return crypto.encrypt(plainString.toByteArray(Charsets.UTF_8)).toBase64()
    }

    private fun decrypt(base64String: String): String {
        return crypto.decrypt(base64String.fromBase64ToByteArray()).toString(Charsets.UTF_8)
    }
}