package com.example.ktorcurrencyconverterapp.models.response

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("convertedAmount")
    val convertedAmount: Double
)
