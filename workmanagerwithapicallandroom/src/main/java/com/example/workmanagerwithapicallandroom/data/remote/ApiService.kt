package com.example.workmanagerwithapicallandroom.data.remote

import com.example.workmanagerwithapicallandroom.data.model.QuoteDTO
import retrofit2.http.GET

interface ApiService {

    @GET("quotes/random")
    suspend fun getQuotes() : QuoteDTO
}