package io.rndev.domain

sealed class AccountsException(message: String? = null, cause: Throwable? = null) : Throwable(message, cause) {
    // Errores de Red/Conexión
    data class NetworkError(override val cause: Throwable? = null) : Exception("Error de red. Verifica tu conexión.")
    data class ServerError(val errorCode: Int, val errorMessage: String?) : Exception("Error del servidor: $errorCode - $errorMessage")

    // Errores Específicos de Autenticación
    object InvalidCredentials : Exception("Las credenciales proporcionadas son incorrectas.")
    object UserNotFound : Exception("El usuario no existe.")
    object ReadAccountsError : Exception("Error al procesar la lista de cuentas.")

    // Error Genérico
    data class UnknownError(val detailedMessage: String?, override val cause: Throwable? = null) : Exception("Ha ocurrido un error inesperado.")
}
