package com.pomaskin.terminal.di

import android.content.Context
import com.pomaskin.terminal.presentation.ViewModelFactory
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        DataModule::class,
        ViewModelModule::class
    ]
)
interface ApplicationComponent {

    fun getViewModelFactory(): ViewModelFactory

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance context: Context
        ): ApplicationComponent
    }
}