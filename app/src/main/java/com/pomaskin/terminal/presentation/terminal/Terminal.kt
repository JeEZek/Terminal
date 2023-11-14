package com.pomaskin.terminal.presentation.terminal

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pomaskin.terminal.presentation.getApplicationComponent

@Composable
fun Terminal() {
    val component = getApplicationComponent()
    val viewModel: TerminalViewModel = viewModel(factory = component.getViewModelFactory())
    val screenState = viewModel.state.collectAsState(TerminalScreenState.Initial)

    TerminalContent(screenState)
}

@Composable
fun TerminalContent(
    screenState: State<TerminalScreenState>
) {
    when (val currentState = screenState.value) {
        is TerminalScreenState.Content -> {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                val max = currentState.barList.maxOf { it.high }
                val min = currentState.barList.minOf { it.low }
                val barWidth = size.width / currentState.barList.size
                val pxPerPoint = size.height / (max - min)
                currentState.barList.forEachIndexed { index, bar ->
                    val offsetX = index * barWidth
                    drawLine(
                        color = Color.White,
                        start = Offset(offsetX, size.height - ((bar.low - min) * pxPerPoint)),
                        end = Offset(offsetX, size.height - ((bar.high - min) * pxPerPoint)),
                        strokeWidth = 1f
                    )
                }
            }
        }
        is TerminalScreenState.Initial -> {}
    }
}