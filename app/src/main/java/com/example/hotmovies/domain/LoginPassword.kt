package com.example.hotmovies.domain

import com.example.hotmovies.domain.base.Validators
import com.example.hotmovies.domain.base.ValueObject
import com.example.hotmovies.shared.getMessage

class LoginPassword private constructor(val value: String) : ValueObject {
    sealed class Exceptions(msg: String) : Exception(msg) {
        class InvalidInputException(msg: String) : Exceptions(msg)
    }

    companion object {
        operator fun invoke(value: String): LoginPassword {
            try {
                Validators.validateLength(value, 7..12)
                return LoginPassword(value)
            } catch (e: Exception) {
                throw Exceptions.InvalidInputException(e.getMessage())
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LoginPassword

        return value == other.value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}