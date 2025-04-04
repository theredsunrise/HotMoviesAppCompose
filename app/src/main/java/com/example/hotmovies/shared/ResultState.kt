package com.example.hotmovies.shared

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

@Stable
@Immutable
sealed interface ResultState<out T> {
    @Stable
    @Immutable
    data object None : ResultState<Nothing>
    @Stable
    @Immutable
    data object Progress : ResultState<Nothing>
    @Stable
    @Immutable
    data class Success<T>(val value: T) : ResultState<T>
    @Stable
    @Immutable
    data class Failure(val exception: Exception) : ResultState<Nothing>

    val isNone: Boolean
        get() {
            return when (this) {
                is None -> true
                else -> false
            }
        }

    val isProgress: Boolean
        get() {
            return when (this) {
                is Progress -> true
                else -> false
            }
        }

    val success: T?
        get() {
            return when (this) {
                is Success -> this.value
                else -> null
            }
        }

    val isSuccess: Boolean
        get() = success != null

    val isSuccessTrue: Boolean
        get() = success == true

    val isSuccessFalse: Boolean
        get() = success == false

    val failure: Exception?
        get() {
            return when (this) {
                is Failure -> this.exception
                else -> null
            }
        }

    val isFailure: Boolean
        get() = failure != null

    fun print(): String {
        return when (this) {
            is None -> "None"
            is Progress -> "Progress"
            is Success -> value.toString()
            is Failure -> {
                this.exception.message.orEmpty()
            }
        }
    }
}

fun <T> T.state(): ResultState.Success<T> = ResultState.Success(this)
fun <T> T.stateEvent(): Event<ResultState.Success<T>> = Event(ResultState.Success(this))
fun Exception.stateFailure() = ResultState.Failure(this)
fun Exception.stateEventFailure() = Event(ResultState.Failure(this))

typealias none = ResultState.None

val eventNone = Event(ResultState.None)
typealias progress = ResultState.Progress

val progressEvent: Event<progress> get() = Event(progress)

fun <T> Flow<T>.asStateResult(): Flow<ResultState<T>> {
    return this.map<T, ResultState<T>> { it.state() }
        .onStart { emit(progress) }
        .catch { e ->
            if (e !is Exception) throw e
            emit(e.stateFailure())
        }
}