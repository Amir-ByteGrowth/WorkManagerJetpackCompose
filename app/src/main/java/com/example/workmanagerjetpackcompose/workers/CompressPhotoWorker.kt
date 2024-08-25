package com.example.workmanagerjetpackcompose.workers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import kotlin.math.roundToInt

class CompressPhotoWorker(appContext: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(appContext, workerParameters) {
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            val stringUri = inputData.getString(KEY_CONTENT_URI) ?: ""
            val threshHoldInBytes = inputData.getLong(KEY_COMPRESSION_THRESHOLD, 0L)
            val client = OkHttpClient()

            try {
                val request = Request.Builder().url(stringUri).build()
                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    val imageBytes = response.body?.bytes() // This is where the image is downloaded
                    if (imageBytes != null) {
                        // Process the image bytes (e.g., compress, save, etc.)
                        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

                        var outputBytes: ByteArray
                        var quality = 100
                        do {
                            println("DoWHilee")
                            val outputStream = ByteArrayOutputStream()
                            outputStream.use { outputStream ->
                                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
                                outputBytes = outputStream.toByteArray()
                                quality -= (quality * 0.1).roundToInt()

                            }
                        } while (outputBytes.size > threshHoldInBytes && quality > 5)

                        val file = File(applicationContext.cacheDir, "amir.jpg")
                        file.writeBytes(outputBytes)
                        println("StoredImageImage    " + file.absolutePath.toString())
                        Result.success(workDataOf(KEY_RESULT_PATH to file.absolutePath))

                    } else {
                        Result.failure()
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Result.failure()
            }


        }
        Result.failure()
    }

    companion object {
        val KEY_CONTENT_URI = "KEY_CONTENT_URI"
        val KEY_COMPRESSION_THRESHOLD = "KEY_COMPRESSION_THRESHOLD"
        val KEY_RESULT_PATH = "KEY_RESULT_PATH"
    }
}