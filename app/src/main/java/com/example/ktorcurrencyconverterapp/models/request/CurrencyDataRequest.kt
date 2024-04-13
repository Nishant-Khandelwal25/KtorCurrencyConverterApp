package com.example.ktorcurrencyconverterapp.models.request

data class CurrencyData(
    val baseCurrency: String,
    val conversionCurrency: String,
    val baseAmount: Double
)
