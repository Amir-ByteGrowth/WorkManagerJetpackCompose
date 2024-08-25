package com.example.workmanagerwithapicallandroom.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workmanagerwithapicallandroom.domain.models.Quote
import com.example.workmanagerwithapicallandroom.domain.use_cases.GetAllQuotesFromDbUseCase
import com.example.workmanagerwithapicallandroom.domain.use_cases.GetQuoteUseCase
import com.example.workmanagerwithapicallandroom.domain.use_cases.SetUpPeriodicWorkRequestUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
@HiltViewModel
class MainViewModel @Inject constructor(
    private val getQuoteUseCase: GetQuoteUseCase,
    private val getAllQuotesFromDbUseCase: GetAllQuotesFromDbUseCase,
    private val setUpPeriodicWorkRequestUseCase: SetUpPeriodicWorkRequestUseCase,
) : ViewModel() {

    val uiState = getAllQuotesFromDbUseCase.invoke().map { UIState(it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, UIState(emptyList()))

    init {
        setUpPeriodicWorkRequestUseCase.invoke()
    }

    fun getQuote() {
        getQuoteUseCase.invoke()
    }

}

data class UIState(val data: List<Quote>)