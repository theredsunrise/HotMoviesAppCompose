package com.example.hotmovies.appplication.login.interfaces

import kotlinx.coroutines.flow.Flow

interface SettingsRepositoryInterface {
    object Keys {
        const val AUTH_TOKEN_KEY = "authToken"
    }

    sealed class Exceptions(msg: String) : Exception(msg) {
        data class NoValueException(val key: String, val type: Class<Boolean>) :
            Exceptions("No value of type: ${type.name} for key: $key ")
    }

    fun store(key: String, value: String): Flow<Unit>

    fun getStringValue(key: String): Flow<String>

    fun clear(key: String): Flow<Unit>
}