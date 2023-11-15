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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
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
            var visibleBarsCount by remember {
                mutableStateOf(100)
            }
            val transformableState = TransformableState { zoomChange, panChange, rotationChange ->
                visibleBarsCount = (visibleBarsCount / zoomChange).roundToInt()
                    .coerceIn(MIN_VISIBLE_BARS_COUNT, currentState.barList.size)
            }

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .transformable(transformableState)
            ) {
                val max = currentState.barList.maxOf { it.high }
                val min = currentState.barList.minOf { it.low }
                val barWidth = size.width / visibleBarsCount
                val pxPerPoint = size.height / (max - min)
                currentState.barList.take(visibleBarsCount).forEachIndexed { index, bar ->
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
        is TerminalScreenState.Initial -> {}
    }
}