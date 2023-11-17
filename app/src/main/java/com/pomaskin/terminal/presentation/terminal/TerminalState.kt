package com.pomaskin.terminal.presentation.terminal

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import com.pomaskin.terminal.domain.entity.Bar
import kotlinx.parcelize.Parcelize
import kotlin.math.roundToInt

@Parcelize
@Immutable
data class TerminalState(
    val barList: List<Bar>,
    var visibleBarsCount: Int = 100,
    var terminalWidth: Float = 0f,
    var scrolledBy: Float = 0f
) : Parcelable {

    val barWidth: Float
        get() = terminalWidth / visibleBarsCount

    val visibleBars: List<Bar>
        get() {
            val startedIndex = (scrolledBy / barWidth).roundToInt().coerceAtLeast(0)
            val endIndex = (startedIndex + visibleBarsCount).coerceAtMost(barList.size)
            return barList.subList(startedIndex, endIndex)
        }
}

@Composable
fun rememberTerminalState(bars: List<Bar>): MutableState<TerminalState> = rememberSaveable {
    mutableStateOf(TerminalState(bars))
}