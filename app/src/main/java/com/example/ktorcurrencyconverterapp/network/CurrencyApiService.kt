package com.example.ktorcurrencyconverterapp.network

import com.example.ktorcurrencyconverterapp.models.response.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApiService {
    @GET("/convert-currency")
    suspend fun convertCurrency(
        @Query("baseCurrency") baseCurrency: String,
        @Query("conversionCurrency") conversionCurrency: String,
        @Query("amount") amount: Double
    ): ApiResponse
}