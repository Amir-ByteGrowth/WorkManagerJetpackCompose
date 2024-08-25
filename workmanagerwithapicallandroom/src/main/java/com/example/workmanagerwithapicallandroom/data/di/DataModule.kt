package com.example.workmanagerwithapicallandroom.data.di

import android.content.Context
import androidx.work.WorkManager
import com.example.workmanagerwithapicallandroom.data.local.QuoteDao
import com.example.workmanagerwithapicallandroom.data.local.QuoteDatabase
import com.example.workmanagerwithapicallandroom.data.remote.ApiService
import com.example.workmanagerwithapicallandroom.data.repository.QuoteRepoImp
import com.example.workmanagerwithapicallandroom.domain.repository.QuoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataModule {
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://dummyjson.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): QuoteDatabase {
        return QuoteDatabase.getInstance(context)
    }

    @Provides
    fun provideQuoteDao(quoteDatabase: QuoteDatabase): QuoteDao {
        return quoteDatabase.getQuoteDao()
    }

    @Singleton
    @Provides
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }

    @Provides
    fun provideRepositoryImp(workManager: WorkManager, quoteDao: QuoteDao): QuoteRepository {
        return QuoteRepoImp(workManager, quoteDao)
    }
}