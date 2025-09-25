package io.rndev.core.common

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class CommonModule {

    @Binds
    abstract fun bindTokenProvider(
        tokenProvider: SimpleTokenStore
    ): TokenProvider
}
