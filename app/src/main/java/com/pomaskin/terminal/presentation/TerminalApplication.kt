package com.pomaskin.terminal.presentation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.pomaskin.terminal.di.ApplicationComponent
import com.pomaskin.terminal.di.DaggerApplicationComponent

class TerminalApplication: Application() {

    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}

@Composable
fun getApplicationComponent(): ApplicationComponent {
    return (LocalContext.current.applicationContext as TerminalApplication).component
}