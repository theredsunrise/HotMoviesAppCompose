package com.example.hotmovies.domain.base

object Validators {
    sealed class Exceptions(msg: String) : Exception(msg) {
        data class ShortLengthException(val length: Int) :
            Exceptions("Invalid length, must be at least $length characters long.")

        data class LongLengthException(val length: Int) :
            Exceptions("Invalid length, must be less than $length characters long.")
    }

    fun validateLength(value: String, range: ClosedRange<Int>) {
        if (value.length < range.start) {
            throw Exceptions.ShortLengthException(range.start)
        }
        if (value.length > range.endInclusive) {
            throw Exceptions.LongLengthException(range.endInclusive)
        }
    }
}