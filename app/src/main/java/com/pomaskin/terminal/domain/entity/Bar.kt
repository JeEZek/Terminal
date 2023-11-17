package com.pomaskin.terminal.domain.entity

import android.icu.util.Calendar
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Bar(
    val open: Float,
    val close: Float,
    val low: Float,
    val high: Float,
    val time: Calendar
) : Parcelable