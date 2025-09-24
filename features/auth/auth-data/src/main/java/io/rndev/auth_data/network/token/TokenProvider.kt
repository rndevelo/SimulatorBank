package io.rndev.auth_data.network.token

interface TokenProvider {
    fun getToken(): String?
    fun saveToken(token: String)
}
