package io.rndev.detail.data

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.rndev.detail.domain.DetailRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class BindsDetailDataModule {

    @Binds
    abstract fun bindRemoteAuthDataSource(
        remoteDataSource: DetailRemoteDataSourceImpl
    ): DetailRemoteDataSource

    @Binds
    abstract fun bindAuthRepository(
        repository: DetailRepositoryImpl
    ): DetailRepository
}

@Module
@InstallIn(SingletonComponent::class)
object ProvideDetailRetrofitModule {
    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): DetailApiService {
        return retrofit.create(DetailApiService::class.java)
    }
}
