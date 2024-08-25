package com.example.workmanagerwithapicallandroom.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.workmanagerwithapicallandroom.domain.models.Quote
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao {

    @Insert
    suspend fun insert(quote: Quote)

    @Query("SELECT * from Quote order by time desc")
    fun getAllQuotes() : Flow<List<Quote>>
}