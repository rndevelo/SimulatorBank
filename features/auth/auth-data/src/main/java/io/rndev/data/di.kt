package io.rndev.data

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.rndev.domain.AuthRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class BindsAuthDataModule {

    @Binds
    abstract fun bindRemoteAuthDataSource(
        remoteDataSource: AuthServerDataSource
    ): AuthRemoteDataSource

    @Binds
    abstract fun bindAuthRepository(
        repository: AuthRepositoryImpl
    ): AuthRepository
}

@Module
@InstallIn(SingletonComponent::class)
object ProvideAuthRetrofitModule {
    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }
}
