package com.example.ktorcurrencyconverterapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest


@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = hiltViewModel()
) {
    val showLoader = remember {
        mutableStateOf(false)
    }

    val convertedAmount = remember {
        mutableStateOf("")
    }

    val showError = remember {
        mutableStateOf(false)
    }

    val convertedAmountData = viewModel.currencyData

    LaunchedEffect(key1 = showLoader.value) {
        delay(1000)
        convertedAmountData.collectLatest {
            if (it.message.isNotEmpty()) convertedAmount.value =
                String.format("%.2f", it.convertedAmount)
            showLoader.value = false
        }
    }

    if (showLoader.value) LoaderView()
    else MainView(
        viewModel = viewModel,
        showLoader = showLoader,
        amount = convertedAmount,
        showError = showError
    )
}

@Composable
fun MainView(
    viewModel: MainScreenViewModel,
    showLoader: MutableState<Boolean>,
    amount: MutableState<String>,
    showError: MutableState<Boolean>
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            CurrencyDropdownBox(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                expanded = viewModel.baseCurrencyExpanded,
                onExpandedChange = { viewModel.baseCurrencyExpanded = it },
                selectedCurrency = viewModel.selectedBaseCurrency,
                currencies = viewModel.currencies,
                onCurrencySelected = {
                    viewModel.updateBaseCurrency(it)
                    amount.value = ""
                    viewModel.baseCurrencyExpanded = false
                }
            ) {
                viewModel.baseCurrencyExpanded = it
            }

            CurrencyDropdownBox(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                expanded = viewModel.conversionCurrencyExpanded,
                onExpandedChange = { viewModel.conversionCurrencyExpanded = it },
                selectedCurrency = viewModel.selectedConversionCurrency,
                currencies = viewModel.currencies,
                onCurrencySelected = {
                    viewModel.updateConversionCurrency(it)
                    amount.value = ""
                    viewModel.conversionCurrencyExpanded = false
                }
            ) {
                viewModel.conversionCurrencyExpanded = it
            }
        }
        OutlinedTextField(
            value = viewModel.amount,
            onValueChange = {
                amount.value = ""
                viewModel.updateAmount(it)
            },
            singleLine = true,
            label = {
                Text(text = "Amount in ${viewModel.selectedBaseCurrency}", fontSize = 12.sp)
            },
            isError = showError.value,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
        )
        if (viewModel.amount.isNotEmpty()) {
            Button(
                onClick = {
                    if (viewModel.amount.isNotEmpty()) {
                        showLoader.value = true
                        viewModel.convertCurrency()
                        showError.value = false
                    } else {
                        showError.value = true
                    }
                }, modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = "Convert ${viewModel.amount} ${viewModel.selectedBaseCurrency} to ${viewModel.selectedConversionCurrency}"
                )
            }
            if (amount.value.isNotEmpty()) Text(
                text = "${viewModel.amount} ${viewModel.selectedBaseCurrency} =  ${amount.value} ${viewModel.selectedConversionCurrency}",
                modifier = Modifier.padding(8.dp),
                fontWeight = FontWeight.Bold
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyDropdownBox(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    selectedCurrency: String,
    currencies: List<String>,
    onCurrencySelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    onDismiss: (Boolean) -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = expanded, onExpandedChange = onExpandedChange, modifier = modifier
    ) {
        OutlinedTextField(value = selectedCurrency,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier
                .focusProperties {
                    canFocus = false
                }
                .menuAnchor())
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { onDismiss(false) }) {
            currencies.forEach { currency ->
                DropdownMenuItem(
                    text = { Text(text = currency) },
                    onClick = { onCurrencySelected(currency) })
            }

        }
    }

    /*ExposedDropdownMenuBox(
                expanded = viewModel.baseCurrencyExpanded, onExpandedChange = {
                    viewModel.baseCurrencyExpanded = !viewModel.baseCurrencyExpanded
                }, modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {

                ExposedDropdownMenu(
                    expanded = viewModel.baseCurrencyExpanded,
                    onDismissRequest = { viewModel.baseCurrencyExpanded = false },
                ) {
                    viewModel.currencies.forEach { currency ->
                        DropdownMenuItem(text = { Text(text = currency) },
                            onClick = {
                                viewModel.updateBaseCurrency(currency)
                                amount.value = ""
                                viewModel.baseCurrencyExpanded = false
                            })
                    }
                }
            }*/

}

@Composable
fun LoaderView() {
    Column(
        modifier = Modifier.size(64.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .progressSemantics()
                .size(64.dp)
        )
    }
}