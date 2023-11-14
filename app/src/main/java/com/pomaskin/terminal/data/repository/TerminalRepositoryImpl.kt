package com.pomaskin.terminal.data.repository

import com.pomaskin.terminal.data.mapper.TerminalMapper
import com.pomaskin.terminal.data.network.ApiFactory
import com.pomaskin.terminal.data.network.ApiService
import com.pomaskin.terminal.domain.entity.Bar
import com.pomaskin.terminal.domain.repository.TerminalRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import javax.inject.Inject

class TerminalRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val mapper: TerminalMapper
) : TerminalRepository {

    override fun getBarsList(): Flow<List<Bar>> = flow {
        val barList = apiService.loadBars()
        emit(mapper.mapResultToBars(barList))
    }.retry {
        delay(RETRY_TIMEOUT_MILLIS)
        true
    }

    companion object {

        private const val RETRY_TIMEOUT_MILLIS = 3000L
    }
}