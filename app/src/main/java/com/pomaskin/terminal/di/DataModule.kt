package com.pomaskin.terminal.di

import com.pomaskin.terminal.data.network.ApiFactory
import com.pomaskin.terminal.data.network.ApiService
import com.pomaskin.terminal.data.repository.TerminalRepositoryImpl
import com.pomaskin.terminal.domain.repository.TerminalRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindRepository(impl: TerminalRepositoryImpl): TerminalRepository

    companion object {

        @ApplicationScope
        @Provides
        fun provideApiService(): ApiService {
            return ApiFactory.apiService
        }
    }
}