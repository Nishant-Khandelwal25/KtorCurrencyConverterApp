package com.example.ktorcurrencyconverterapp.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ktorcurrencyconverterapp.models.request.CurrencyData
import com.example.ktorcurrencyconverterapp.models.response.ApiResponse
import com.example.ktorcurrencyconverterapp.repository.CurrencyConverterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val repository: CurrencyConverterRepository
) : ViewModel() {
    val currencyData = MutableStateFlow(ApiResponse(false, "", 0.0))

    var amount by mutableStateOf("")
        private set

    var currencies = listOf("INR", "USD", "EUR", "AUSD", "DIN", "YEN", "DIR")
    var selectedConversionCurrency by mutableStateOf(currencies.first())
        private set

    var selectedBaseCurrency by mutableStateOf(currencies.first())
        private set

    var conversionCurrencyExpanded by mutableStateOf(false)
    var baseCurrencyExpanded by mutableStateOf(false)

    fun convertCurrency() {
        viewModelScope.launch {
            repository.convertCurrency(
                CurrencyData(
                    selectedBaseCurrency,
                    selectedConversionCurrency,
                    amount.toDouble()
                )
            )
                .collectLatest {
                    currencyData.value = it
                }
        }
    }

    fun updateAmount(value: String) {
        amount = value
    }

    fun updateConversionCurrency(value: String) {
        selectedConversionCurrency = value
    }

    fun updateBaseCurrency(value: String) {
        selectedBaseCurrency = value
    }
}