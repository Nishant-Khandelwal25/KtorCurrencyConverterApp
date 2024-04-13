package com.example.ktorcurrencyconverterapp.repository

import com.example.ktorcurrencyconverterapp.models.request.CurrencyData
import com.example.ktorcurrencyconverterapp.models.response.ApiResponse
import kotlinx.coroutines.flow.Flow

interface CurrencyConverterRepository {
    suspend fun convertCurrency(currencyData: CurrencyData): Flow<ApiResponse>
}