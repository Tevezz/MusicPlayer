package com.matheus.musicplayer.domain.util

suspend fun <T : Any> runCatchingResponse(
    blockCatch: (suspend (e: Exception) -> Exception)? = null,
    blockTry: suspend () -> T
): Response<T> {
    return try {
        Response.Success(blockTry())
    } catch (e: Exception) {
        val exception = blockCatch?.invoke(e) ?: e
        Response.Error(exception)
    }
}