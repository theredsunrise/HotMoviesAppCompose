package com.example.hotmovies.domain

import com.example.hotmovies.domain.base.Validators
import com.example.hotmovies.domain.base.ValueObject

class LoginUserName private constructor(val value: String) : ValueObject {
    sealed class Exceptions(msg: String) : Exception(msg) {
        class InvalidInputException(msg: String) : Exceptions(msg)
    }

    companion object {
        operator fun invoke(value: String): LoginUserName {
            try {
                Validators.validateLength(value, 7..20)
                return LoginUserName(value)
            } catch (e: Exception) {
                throw Exceptions.InvalidInputException(e.localizedMessage.orEmpty())
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LoginUserName

        return value == other.value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}