package io.rndev.data

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.rndev.domain.AccountsRepository
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
        repository: AccountsRepositoryImpl
    ): AccountsRepository
}

@Module
@InstallIn(SingletonComponent::class)
object ProvideAccountRetrofitModule {
    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AccountsApiService {
        return retrofit.create(AccountsApiService::class.java)
    }
}
