package com.pomaskin.terminal.domain.repository

import com.pomaskin.terminal.domain.entity.Bar
import kotlinx.coroutines.flow.Flow

interface TerminalRepository {

    fun getBarsList(): Flow<List<Bar>>
}