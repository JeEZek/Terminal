package com.pomaskin.terminal.data.mapper

import android.icu.util.Calendar
import com.pomaskin.terminal.data.model.BarsResultDto
import com.pomaskin.terminal.domain.entity.Bar
import java.util.Date
import javax.inject.Inject

class TerminalMapper @Inject constructor() {

    fun mapResultToBars(resultDto: BarsResultDto): List<Bar> {
        val result = mutableListOf<Bar>()

        val bars = resultDto.barList

        for (bar in bars) {
            result.add(
                Bar(
                    open = bar.open,
                    close = bar.close,
                    low = bar.low,
                    high = bar.high,
                    time = mapTimestampToCalendar(bar.time)
                )
            )
        }

        return result
    }

    private fun mapTimestampToCalendar(timestamp: Long): Calendar {
        val date = Date(timestamp)
        return Calendar.getInstance().apply {
            time = Date(timestamp)
        }
    }
}