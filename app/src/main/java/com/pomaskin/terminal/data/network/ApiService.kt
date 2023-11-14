package com.pomaskin.terminal.data.network

import com.pomaskin.terminal.data.model.BarsResultDto
import retrofit2.http.GET

interface ApiService {

    @GET("aggs/ticker/AAPL/range/1/hour/2022-01-09/2023-01-09?adjusted=true&sort=asc&limit=50000&apiKey=NueSP7SWQx39jU3Erw2E7_3qq7muKKvu")
    suspend fun loadBars(): BarsResultDto
}