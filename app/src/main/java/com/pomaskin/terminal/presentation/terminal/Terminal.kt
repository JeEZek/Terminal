package com.pomaskin.terminal.presentation.terminal

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.TransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.layout.onSizeChanged
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pomaskin.terminal.presentation.getApplicationComponent
import kotlin.math.roundToInt

private const val MIN_VISIBLE_BARS_COUNT = 20

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

            val bars = currentState.barList
            var terminalState by rememberTerminalState(bars)

            val transformableState = TransformableState { zoomChange, panChange, _ ->
                val visibleBarsCount = (terminalState.visibleBarsCount / zoomChange).roundToInt()
                    .coerceIn(MIN_VISIBLE_BARS_COUNT, bars.size)

                val scrolledBy = (terminalState.scrolledBy + panChange.x)
                    .coerceIn(0f, bars.size * terminalState.barWidth - terminalState.terminalWidth)

                terminalState = terminalState.copy(
                    visibleBarsCount = visibleBarsCount,
                    scrolledBy = scrolledBy
                )
            }

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .transformable(transformableState)
                    .onSizeChanged {
                        terminalState = terminalState.copy(terminalWidth = it.width.toFloat())
                    }
            ) {
                val max = terminalState.visibleBars.maxOf { it.high }
                val min = terminalState.visibleBars.minOf { it.low }
                val pxPerPoint = size.height / (max - min)
                translate(left = terminalState.scrolledBy) {
                    bars.forEachIndexed { index, bar ->
                        val offsetX = size.width - index * terminalState.barWidth
                        drawLine(
                            color = Color.White,
                            start = Offset(offsetX, size.height - ((bar.low - min) * pxPerPoint)),
                            end = Offset(offsetX, size.height - ((bar.high - min) * pxPerPoint)),
                            strokeWidth = 1f
                        )
                        drawLine(
                            color = if (bar.close > bar.open) Color.Green else Color.Red,
                            start = Offset(offsetX, size.height - ((bar.open - min) * pxPerPoint)),
                            end = Offset(offsetX, size.height - ((bar.close - min) * pxPerPoint)),
                            strokeWidth = terminalState.barWidth / 2
                        )
                    }
                }
            }
        }
        is TerminalScreenState.Initial -> {}
    }
}