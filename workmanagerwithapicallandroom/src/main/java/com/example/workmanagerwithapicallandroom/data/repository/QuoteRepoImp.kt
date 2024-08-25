package com.example.workmanagerwithapicallandroom.data.repository

import com.example.workmanagerwithapicallandroom.domain.models.Quote
import com.example.workmanagerwithapicallandroom.domain.repository.QuoteRepository
import kotlinx.coroutines.flow.Flow

class QuoteRepoImp :QuoteRepository {
    override fun getQuote() {
        TODO("Not yet implemented")
    }

    override fun getAllQQuotes(): Flow<List<Quote>> {
        TODO("Not yet implemented")
    }

    override fun setPeriodicWorkRequest() {
        TODO("Not yet implemented")
    }
}