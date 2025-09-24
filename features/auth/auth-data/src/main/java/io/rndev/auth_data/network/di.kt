package io.rndev.auth_data.network

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.rndev.auth_data.network.token.SimpleTokenStore
import io.rndev.auth_data.network.token.TokenProvider
import io.rndev.auth_domain.AuthRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class NetworkModule {

    @Binds
    abstract fun bindRemoteAuthDataSource(
        remoteDataSource: AuthServerDataSource
    ): AuthRemoteDataSource

    @Binds
    abstract fun bindAuthRepository(
        repository: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    abstract fun bindTokenProvider(
        tokenProvider: SimpleTokenStore
    ): TokenProvider
}
