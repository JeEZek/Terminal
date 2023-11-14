package com.pomaskin.terminal.presentation.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pomaskin.terminal.presentation.getApplicationComponent
import com.pomaskin.terminal.ui.theme.TerminalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TerminalTheme {

                val component = getApplicationComponent()
                val viewModel: TerminalViewModel = viewModel(factory = component.getViewModelFactory())
                val screenState = viewModel.state.collectAsState(TerminalScreenState.Initial)

                when (val currentState = screenState.value) {
                    is TerminalScreenState.Content -> {
                        Log.d("MainActivity", currentState.barList.toString())
                    }
                    is TerminalScreenState.Initial -> {}
                }
            }
        }
    }
}