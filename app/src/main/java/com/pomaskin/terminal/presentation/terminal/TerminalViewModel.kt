package com.pomaskin.terminal.presentation.terminal

import androidx.lifecycle.ViewModel
import com.pomaskin.terminal.domain.usecases.GetBarsUseCase
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class TerminalViewModel @Inject constructor(
    private val getBarsUseCase: GetBarsUseCase
): ViewModel() {

    val state = getBarsUseCase()
        .map { TerminalScreenState.Content(it) as TerminalScreenState }
        .onStart { TerminalScreenState.Initial }
}