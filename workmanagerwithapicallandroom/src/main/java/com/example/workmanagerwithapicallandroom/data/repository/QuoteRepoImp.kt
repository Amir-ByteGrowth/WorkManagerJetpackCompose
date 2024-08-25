package com.example.workmanagerwithapicallandroom.data.repository

import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequest.Companion.MIN_PERIODIC_INTERVAL_MILLIS
import androidx.work.WorkManager
import com.example.workmanagerwithapicallandroom.data.local.QuoteDao
import com.example.workmanagerwithapicallandroom.data.mappers.PeriodicWorker
import com.example.workmanagerwithapicallandroom.data.worker.FetchWorker
import com.example.workmanagerwithapicallandroom.domain.models.Quote
import com.example.workmanagerwithapicallandroom.domain.repository.QuoteRepository
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit

class QuoteRepoImp(
    private val workManager: WorkManager,
    private val quoteDao: QuoteDao,
) : QuoteRepository {
    override fun getQuote() {
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val workRequest =
            OneTimeWorkRequestBuilder<FetchWorker>().setConstraints(constraints).build()
        workManager.enqueue(workRequest)

    }

    override fun getAllQQuotes(): Flow<List<Quote>> = quoteDao.getAllQuotes()

    override fun setPeriodicWorkRequest() {
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val periodicWorkRequest = PeriodicWorkRequest.Builder(
            PeriodicWorker::class.java,
            MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MILLISECONDS
        ).setConstraints(constraints).build()

        workManager.enqueueUniquePeriodicWork(
            "periodic_work",
            ExistingPeriodicWorkPolicy.UPDATE,
            periodicWorkRequest
        )



    }
}