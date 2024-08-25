package com.example.workmanagerwithapicallandroom.data.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.example.workmanagerwithapicallandroom.data.local.QuoteDao
import com.example.workmanagerwithapicallandroom.data.mappers.toDomain
import com.example.workmanagerwithapicallandroom.data.remote.ApiService
import com.google.gson.Gson
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

const val ONE_TIME_WORK_REQUEST = "ONE_TIME_WORK_REQUEST"
const val QUOTE="QUOTE"

//difference between normal injection constructor and assisted constructor is that @assisted paramter (dependencies are created and is injected at run time) while creating the object of this worker while other parameter are injected by dagger hilt
@HiltWorker
class FetchWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParameters: WorkerParameters,
    private val apiService: ApiService,
    private val quoteDao: QuoteDao,
) :
    CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        println("Entered Here")
        return try {
            val response = apiService.getQuotes().toDomain(ONE_TIME_WORK_REQUEST)
            Log.d("ApiResponse",response.toString())
            quoteDao.insert(response)
            val data = Data.Builder()
                .putString(QUOTE, Gson().toJson(response)).build()
            Result.success(data)
        } catch (e: Exception) {
            Result.failure()
        }
    }
}