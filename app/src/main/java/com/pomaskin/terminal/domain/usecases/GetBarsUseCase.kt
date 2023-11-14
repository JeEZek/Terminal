package com.pomaskin.terminal.domain.usecases

import com.pomaskin.terminal.data.repository.TerminalRepositoryImpl
import com.pomaskin.terminal.domain.entity.Bar
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBarsUseCase @Inject constructor(
    private val repository: TerminalRepositoryImpl

) {

    operator fun invoke(): Flow<List<Bar>> {
        return repository.getBarsList()
    }
}