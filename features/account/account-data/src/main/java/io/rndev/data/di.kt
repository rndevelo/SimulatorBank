package io.rndev.data

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.rndev.domain.AccountRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class BindsAccountDataModule {

    @Binds
    abstract fun bindRemoteAuthDataSource(
        remoteDataSource: AccountRemoteDataSourceImpl
    ): AccountRemoteDataSource

    @Binds
    abstract fun bindAuthRepository(
        repository: AccountRepositoryImpl
    ): AccountRepository
}

@Module
@InstallIn(SingletonComponent::class)
object ProvideAccountRetrofitModule {
    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AccountApiService {
        return retrofit.create(AccountApiService::class.java)
    }
}
