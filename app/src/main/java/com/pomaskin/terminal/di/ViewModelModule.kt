package com.pomaskin.terminal.di

import androidx.lifecycle.ViewModel
import com.pomaskin.terminal.presentation.terminal.TerminalViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @IntoMap
    @ViewModelKey(TerminalViewModel::class)
    @Binds
    fun bindTerminalViewModel(viewModel: TerminalViewModel): ViewModel
}