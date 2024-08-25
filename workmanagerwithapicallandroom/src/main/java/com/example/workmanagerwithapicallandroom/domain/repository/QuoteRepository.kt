package com.example.workmanagerwithapicallandroom.domain.repository

import com.example.workmanagerwithapicallandroom.domain.models.Quote
import kotlinx.coroutines.flow.Flow

interface QuoteRepository {

    fun getQuote()

    fun getAllQQuotes() : Flow<List<Quote>>

    fun setPeriodicWorkRequest()

}