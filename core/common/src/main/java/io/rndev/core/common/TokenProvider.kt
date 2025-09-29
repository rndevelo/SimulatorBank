package io.rndev.core.common

interface TokenProvider {
    fun getToken(): String?
    fun saveToken(token: String)
}
