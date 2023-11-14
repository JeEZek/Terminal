package com.pomaskin.terminal.presentation.terminal

import com.pomaskin.terminal.domain.entity.Bar

sealed class TerminalScreenState {

    object Initial : TerminalScreenState()

    data class Content(val barList: List<Bar>): TerminalScreenState()
}