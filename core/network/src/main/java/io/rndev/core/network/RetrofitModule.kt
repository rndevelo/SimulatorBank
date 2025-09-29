package io.rndev.core.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.rndev.core.common.TokenProvider
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    // Base URL apuntando al Mockoon sandbox
    private const val BASE_URL =
        "http://10.23.16.14:3000/sandbox/uk.openbanking.accountinfo/oauth2/v3.1.5/"

    // Kotlinx JSON
    @Provides
    @Singleton
    fun provideJson(): Json = Json { ignoreUnknownKeys = true }

    // OkHttpClient con interceptores
    @Provides
    @Singleton
    fun provideOkHttpClient(tokenProvider: TokenProvider): OkHttpClient {
        // Logging de request/response para debug
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // Interceptor que añade token como query param
        val authInterceptor = Interceptor { chain ->
            val request = chain.request()
            val token = tokenProvider.getToken() ?: "fake-jwt-token-123"
            val newUrl = request.url.newBuilder()
                .addQueryParameter("api_key", token)
                .build()

            val newRequest = request.newBuilder().url(newUrl).build()
            chain.proceed(newRequest)
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)       // primero logging
            .addInterceptor(authInterceptor) // luego interceptor de token
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(json: Json, client: OkHttpClient): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }
}
