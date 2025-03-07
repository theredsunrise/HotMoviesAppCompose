package com.example.hotmovies.domain

import com.example.hotmovies.domain.base.Validators
import com.example.hotmovies.domain.base.ValueObject

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
                throw Exceptions.InvalidInputException(e.localizedMessage.orEmpty())
            }
        }
    }
}