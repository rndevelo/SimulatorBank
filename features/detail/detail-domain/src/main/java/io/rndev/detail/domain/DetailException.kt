package io.rndev.detail.domain

sealed class DetailException(message: String? = null, cause: Throwable? = null) : Throwable(message, cause) {
    // Errores de Red/Conexión
    data class NetworkError(override val cause: Throwable? = null) : Exception("Error de red. Verifica tu conexión.")
    data class ServerError(val errorCode: Int, val errorMessage: String?) : Exception("Error del servidor: $errorCode - $errorMessage")

    // Errores Específicos de Autenticación
    object InvalidCredentials : Exception("Las credenciales proporcionadas son incorrectas.")
    object UserNotFound : Exception("El usuario no existe.")
    object AccountDataError : Exception("Error al procesar los datos del titular.")
    object TransactionsDataError : Exception("Error al procesar la lista de transacciones.")
    object BalancesDataError : Exception("Error al procesar la lista de saldos.")
    object PartyDataError : Exception("Error al procesar los datos del titular.")



    object WeakPassword : Exception("La contraseña es demasiado débil.")
    object TokenExpired : Exception("La sesión ha expirado. Por favor, inicia sesión de nuevo.")
    object OperationNotPermitted : Exception("Operación no permitida.")

    // Error Genérico
    data class UnknownError(val detailedMessage: String?, override val cause: Throwable? = null) : Exception("Ha ocurrido un error inesperado.")
}
