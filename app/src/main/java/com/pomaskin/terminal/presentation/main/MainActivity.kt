package com.pomaskin.terminal.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.pomaskin.terminal.presentation.terminal.Terminal
import com.pomaskin.terminal.ui.theme.TerminalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TerminalTheme {
                Terminal()
            }
        }
    }
}