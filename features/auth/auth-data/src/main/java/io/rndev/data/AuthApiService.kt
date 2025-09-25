package io.rndev.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthDto>
}

//REQUEST
@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)

//RESPONSE
@Serializable
data class AuthDto(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("refresh_token")
    val refreshToken: String,
    @SerialName("user")
    val user: UserDto
)

@Serializable
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
