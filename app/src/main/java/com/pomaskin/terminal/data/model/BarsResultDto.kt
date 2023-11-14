package com.pomaskin.terminal.data.model

import com.google.gson.annotations.SerializedName

data class BarsResultDto(
    @SerializedName("results") val barList: List<BarDto>
)