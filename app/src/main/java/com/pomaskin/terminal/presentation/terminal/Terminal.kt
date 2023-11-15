package com.pomaskin.terminal.presentation.terminal

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.TransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
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
            var visibleBarsCount by remember { mutableStateOf(100) }
            var terminalWidth by remember { mutableStateOf(0f) }
            val barWidth by remember { derivedStateOf { terminalWidth / visibleBarsCount } }
            var scrolledBy by remember { mutableStateOf(0f) }
            val visibleBars by remember {
                derivedStateOf {
                    val startedIndex = (scrolledBy / barWidth)
                        .roundToInt()
                        .coerceAtLeast(0)
                    val endIndex = (startedIndex + visibleBarsCount)
                        .coerceAtMost(currentState.barList.size)
                    currentState.barList.subList(startedIndex, endIndex)
                }
            }
            val transformableState = TransformableState { zoomChange, panChange, _ ->
                visibleBarsCount = (visibleBarsCount / zoomChange).roundToInt()
                    .coerceIn(MIN_VISIBLE_BARS_COUNT, currentState.barList.size)

                scrolledBy = (scrolledBy + panChange.x)
                    .coerceIn(0f, currentState.barList.size * barWidth - terminalWidth)
            }
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .transformable(transformableState)
            ) {
                terminalWidth = size.width
                val max = visibleBars.maxOf { it.high }
                val min = visibleBars.minOf { it.low }
                val pxPerPoint = size.height / (max - min)
                translate(left = scrolledBy) {
                    currentState.barList.forEachIndexed { index, bar ->
                        val offsetX = size.width - index * barWidth
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
                            strokeWidth = barWidth / 2
                        )
                    }
                }
            }
        }
        is TerminalScreenState.Initial -> {}
    }
}