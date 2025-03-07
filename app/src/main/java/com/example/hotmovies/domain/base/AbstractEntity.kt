package com.example.hotmovies.domain.base

interface Entity : DomainObject {
    val id: Int
}

abstract class AbstractEntity protected constructor(final override val id: Int) : Entity {
    sealed class Exceptions(msg: String) : Exception(msg) {
        data object InvalidIdException :
            Exceptions("Invalid entity ID, value cannot be zero or negative.")
    }

    init {
        if (id <= 0) throw Exceptions.InvalidIdException
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AbstractEntity

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}