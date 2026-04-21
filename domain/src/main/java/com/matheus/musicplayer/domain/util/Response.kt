package com.matheus.musicplayer.domain.util

sealed class Response<out T : Any> {
    data class Success<T : Any>(val data: T) : Response<T>()
    data class Error(val exception: Exception) : Response<Nothing>()

    fun resultOnly(): T {
        if (this is Error) throw this.exception
        return (this as Success).data
    }

    fun resultOrNull(onError: ((Exception) -> Unit)? = null): T? {
        return when (this) {
            is Success -> data
            is Error -> {
                onError?.invoke(exception)
                null
            }
        }
    }

    fun resultWithDefault(defaultValue: () -> @UnsafeVariance T): T {
        if (this is Error) {
            return defaultValue()
        }
        return (this as Success).data
    }

    fun onSuccess(lambda: (T) -> Unit): Response<T> {
        if (this is Success) {
            try {
                lambda(this.data)
            } catch (e: Exception) {
                return Error(e)
            }
            return this
        } else return this
    }

    suspend fun <R : Any> returnOnSuccess(lambda: suspend (T) -> Response<R>): Response<R> {
        return if (this is Success) {
            try {
                lambda(this.data)
            } catch (e: Exception) {
                Error(e)
            }
        } else {
            this as Error
        }
    }

    suspend fun onSuspendSuccess(lambda: suspend (T) -> Unit): Response<T> {
        if (this is Success) {
            try {
                lambda(this.data)
            } catch (e: Exception) {
                return Error(e)
            }
            return this
        } else return this
    }

    fun onError(lambda: (Exception) -> Unit): Response<T> {
        if (this is Error) {
            try {
                lambda(this.exception)
            } catch (e: Exception) {
                return Error(e)
            }
            return this
        } else return this
    }

    suspend fun onSuspendError(lambda: suspend (Exception) -> Unit): Response<T> {
        if (this is Error) {
            try {
                lambda(this.exception)
            } catch (e: Exception) {
                return Error(e)
            }
            return this
        } else return this
    }

    suspend fun <R : Any> andThen(transform: suspend (T) -> Response<R>): Response<R> {
        return if (this is Error) this
        else transform(this.resultOnly())
    }
}