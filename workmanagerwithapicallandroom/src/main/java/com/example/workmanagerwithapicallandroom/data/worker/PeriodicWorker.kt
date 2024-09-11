package com.example.workmanagerwithapicallandroom.data.worker

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.workmanagerwithapicallandroom.CHANNEL
import com.example.workmanagerwithapicallandroom.R
import com.example.workmanagerwithapicallandroom.data.local.QuoteDao
import com.example.workmanagerwithapicallandroom.data.mappers.toDomain
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(
                        context,
                        POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    val notification = NotificationCompat.Builder(context, CHANNEL)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Quote's")
                        .setContentText(response.quote.plus(" ${response.author}"))
                        .build()
                    NotificationManagerCompat.from(context)
                        .notify(1, notification)
                }
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}