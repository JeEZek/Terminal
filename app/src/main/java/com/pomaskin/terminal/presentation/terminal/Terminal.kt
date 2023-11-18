package com.pomaskin.terminal.presentation.terminal

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.TransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pomaskin.terminal.presentation.getApplicationComponent
import kotlin.math.roundToInt

//TODO change all floats to px

private const val MIN_VISIBLE_BARS_COUNT = 20

@Composable
fun Terminal() {
    val component = getApplicationComponent()
    val viewModel: TerminalViewModel = viewModel(factory = component.getViewModelFactory())
    val screenState = viewModel.state.collectAsState(TerminalScreenState.Initial)

    TerminalContent(screenState)
}

@OptIn(ExperimentalTextApi::class)
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

            val textMeasurer = rememberTextMeasurer()

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .padding(
                        top = 32.dp,
                        bottom = 32.dp
                    )
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
                bars.firstOrNull()?.let {
                    drawPrices(
                        max = max,
                        min = min,
                        lastPrice = it.close,
                        pxPerPoint = pxPerPoint,
                        textMeasurer = textMeasurer
                    )
                }
            }
        }

        is TerminalScreenState.Initial -> {}
    }
}

@OptIn(ExperimentalTextApi::class)
private fun DrawScope.drawPrices(
    max: Float,
    min: Float,
    lastPrice: Float,
    pxPerPoint: Float,
    textMeasurer: TextMeasurer
) {
    //max
    val maxPriceOffsetY = 0f
    drawDashedLine(
        start = Offset(0f, maxPriceOffsetY),
        end = Offset(size.width, maxPriceOffsetY)
    )
    drawTextPrice(
        textMeasurer = textMeasurer,
        price = max,
        offsetY = maxPriceOffsetY
    )

    //last price
    val lastPriceOffsetY = size.height - ((lastPrice - min) * pxPerPoint)
    drawDashedLine(
        start = Offset(0f, lastPriceOffsetY),
        end = Offset(size.width, lastPriceOffsetY)
    )
    drawTextPrice(
        textMeasurer = textMeasurer,
        price = lastPrice,
        offsetY = lastPriceOffsetY
    )

    //min
    val minPriceOffsetY = size.height
    drawDashedLine(
        start = Offset(0f, minPriceOffsetY),
        end = Offset(size.width, minPriceOffsetY)
    )
    drawTextPrice(
        textMeasurer = textMeasurer,
        price = min,
        offsetY = minPriceOffsetY
    )
}

@OptIn(ExperimentalTextApi::class)
private fun DrawScope.drawTextPrice(
    textMeasurer: TextMeasurer,
    price: Float,
    offsetY: Float
) {
    val textLayoutResult = textMeasurer.measure(
        text = price.toString(),
        style = TextStyle(
            color = Color.White,
            fontSize = 12.sp
        )
    )
    drawText(
        textLayoutResult = textLayoutResult,
        topLeft = Offset(size.width - textLayoutResult.size.width, offsetY)
    )
}

private fun DrawScope.drawDashedLine(
    color: Color = Color.White,
    start: Offset,
    end: Offset,
    strokeWidth: Float = 1.dp.toPx()
) {
    drawLine(
        color = color,
        start = start,
        end = end,
        strokeWidth = strokeWidth,
        pathEffect = PathEffect.dashPathEffect(
            intervals = floatArrayOf(
                4.dp.toPx(), 4.dp.toPx()
            )
        )
    )
}