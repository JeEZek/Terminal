package com.pomaskin.terminal.domain.entity

import android.icu.util.Calendar
import com.google.gson.annotations.SerializedName
import java.util.Date

data class Bar(
    val open: Float,
    val close: Float,
    val low: Float,
    val high: Float,
    val time: Calendar
)