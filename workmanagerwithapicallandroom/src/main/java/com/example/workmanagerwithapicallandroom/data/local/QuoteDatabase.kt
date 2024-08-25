package com.example.workmanagerwithapicallandroom.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.workmanagerwithapicallandroom.domain.models.Quote
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Database(entities = [Quote::class], version = 1, exportSchema = false)
abstract class QuoteDatabase : RoomDatabase() {

    companion object {
        fun getInstance(context: Context) =
            Room.databaseBuilder(context, QuoteDatabase::class.java, "quote_db").build()
    }

    abstract fun getQuoteDao() : QuoteDao


}