package com.example.ktorcurrencyconverterapp.repository

import com.example.ktorcurrencyconverterapp.models.request.CurrencyData
import com.example.ktorcurrencyconverterapp.models.response.ApiResponse
import com.example.ktorcurrencyconverterapp.network.CurrencyApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CurrencyConverterRepositoryImpl @Inject constructor(
    private val apiService: CurrencyApiService
) : CurrencyConverterRepository {
    override suspend fun convertCurrency(currencyData: CurrencyData): Flow<ApiResponse> {
        return flow {
            emit(
                apiService.convertCurrency(
                    currencyData.baseCurrency,
                    currencyData.conversionCurrency,
                    currencyData.baseAmount
                )
            )
        }
    }
}