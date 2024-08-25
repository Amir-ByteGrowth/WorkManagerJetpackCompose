package com.example.workmanagerwithapicallandroom.data.mappers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.workmanagerwithapicallandroom.data.local.QuoteDao
import com.example.workmanagerwithapicallandroom.data.remote.ApiService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


const val PERIODIC_WORK_REQUEST = "PERIODIC_WORK_REQUEST"

//difference between normal injection constructor and assisted constructor is that @assisted paramter (dependencies are created and is injected at run time) while creating the object of this worker while other parameter are injected by dagger hilt
@HiltWorker
class PeriodicWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParameters: WorkerParameters,
    private val apiService: ApiService,
    private val quoteDao: QuoteDao,
) :
    CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        return try {
            val response = apiService.getQuotes().toDomain(PERIODIC_WORK_REQUEST)
            quoteDao.insert(response)
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}