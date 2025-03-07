package com.example.hotmovies.domain

import android.graphics.Bitmap
import com.example.hotmovies.domain.base.AbstractEntity

class User private constructor(
    id: Int,
    val name: String,
    val userName: String,
    val avatar: Bitmap?
) : AbstractEntity(id) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as User

        if (name != other.name) return false
        if (userName != other.userName) return false
        if (avatar != other.avatar) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + userName.hashCode()
        result = 31 * result + (avatar?.hashCode() ?: 0)
        return result
    }

    companion object {
        operator fun invoke(id: Int, name: String, userName: String, avatar: Bitmap?): User {
            return User(id, name, userName, avatar)
        }
    }
}