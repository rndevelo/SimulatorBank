package io.rndev.auth_data.network

import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}

@kotlinx.serialization.Serializable
data class LoginRequest(
    val username: String,
    val password: String
)

@kotlinx.serialization.Serializable
data class LoginResponse(
    val access_token: String,
    val refresh_token: String,
    val user: UserDto
)

@kotlinx.serialization.Serializable
data class UserDto(
    val id: String,
    val username: String,
    val name: String
)

//Last api
//
//
//interface AuthService {
//
//    @GET("discover/movie?sort_by=popularity.desc")
//    suspend fun fetchPopularMovies(@Query("region") region: String): RemoteResult
//
//    @GET("movie/{id}")
//    suspend fun fetchMovieById(@Path("id") id: Int): RemoteMovie
//
//}
