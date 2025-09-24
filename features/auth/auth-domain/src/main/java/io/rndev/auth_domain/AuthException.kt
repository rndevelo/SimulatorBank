package io.rndev.auth_domain

// En tu módulo :features:auth:auth-domain (o un módulo :core:common si es compartido)
sealed class AuthException(message: String? = null, cause: Throwable? = null) : Throwable(message, cause) {
    // Errores de Red/Conexión
    data class NetworkError(override val cause: Throwable? = null) : AuthException("Error de red. Verifica tu conexión.")
    data class ServerError(val errorCode: Int, val errorMessage: String?) : AuthException("Error del servidor: $errorCode - $errorMessage")

    // Errores Específicos de Autenticación
    object InvalidCredentials : AuthException("Las credenciales proporcionadas son incorrectas.")
    object UserNotFound : AuthException("El usuario no existe.")
    object UserAlreadyExists : AuthException("Un usuario con estas credenciales ya existe.")
    object WeakPassword : AuthException("La contraseña es demasiado débil.")
    object TokenExpired : AuthException("La sesión ha expirado. Por favor, inicia sesión de nuevo.")
    object OperationNotPermitted : AuthException("Operación no permitida.")

    // Error Genérico
    data class UnknownError(val detailedMessage: String?, override val cause: Throwable? = null) : AuthException("Ha ocurrido un error inesperado.")
}
