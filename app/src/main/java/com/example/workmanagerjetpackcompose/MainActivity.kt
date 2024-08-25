package com.example.workmanagerjetpackcompose

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.workmanagerjetpackcompose.ui.theme.WorkManagerJetpackComposeTheme
import com.example.workmanagerjetpackcompose.workers.CompressPhotoWorker

class MainActivity : ComponentActivity() {

    private lateinit var workManager: WorkManager
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        workManager = WorkManager.getInstance(applicationContext)

        enableEdgeToEdge()
        setContent {
            WorkManagerJetpackComposeTheme {
                val workerResult = mainViewModel.workerId?.let { id ->
                    workManager.getWorkInfoByIdLiveData(id).observeAsState().value
                }

                LaunchedEffect(key1 = workerResult?.outputData) {
                    if (workerResult?.outputData != null) {
                        val filePath =
                            workerResult.outputData.getString(CompressPhotoWorker.KEY_RESULT_PATH)
                                ?: ""

                        filePath.let {
                            val bitmap = BitmapFactory.decodeFile(it)
                            mainViewModel.updateCompressedBitmap(bitmap)
                        }

                    }
                }


                Column(
                    modifier = Modifier.fillMaxSize().background(Color.Red).statusBarsPadding().navigationBarsPadding(),
                    horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                    mainViewModel.unCompressedUri?.let {
                        Log.d("UncompressedUri",it.toString())
                        Text(text = "UnCompressed Photo")
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(it)
                                .crossfade(true)
                                .transformations(CircleCropTransformation())
                                .build(),
                            contentDescription = "Google Image",
                            modifier = Modifier.size(200.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    mainViewModel.compressedBitmap?.let {
                        Log.d("CompressedPhoto",it.toString())
                        Text(text = "Compressed Photo")
                        Image(bitmap = it.asImageBitmap(), contentDescription = "")
                    }
                }

            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
//        handleIntent(intent)
        val uri =Uri.parse("https://im.rediff.com/cricket/2021/jan/29umpire-anil.jpg?w=670&h=900")
        Log.d("ImageUri",uri.toString())
        mainViewModel.updateUnCompressedUri(uri)
        val request = OneTimeWorkRequestBuilder<CompressPhotoWorker>().setConstraints(
            Constraints.Builder().setRequiresStorageNotLow(true).build()
        ).setInputData(
            workDataOf(
                CompressPhotoWorker.KEY_CONTENT_URI to uri.toString(),
                CompressPhotoWorker.KEY_COMPRESSION_THRESHOLD to 524 * 20L
            )
        ).build()

        mainViewModel.updateWorkerId(request.id)

        workManager.enqueue(request)
    }
    private fun handleIntent(intent: Intent?):Uri? {
        if (intent?.action == Intent.ACTION_SEND) {
            val type = intent.type
            Log.d("IntentType",type.toString())
            if (type != null && (type.startsWith("text/") || type.startsWith("image/"))) {
                if (type.startsWith("text/")) {
                    // Handle text/plain (URL sent as text)
                    val url = intent.getStringExtra(Intent.EXTRA_TEXT)
                    Log.d("HandleIntentUri",  url?.let { Uri.parse(it).toString() }.toString())
                    return  url?.let { Uri.parse(it) }
                } else if (type.startsWith("image/")) {
                    // Handle image/* (image URL sent)
                    val uri = intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
                    Log.d("HandleIntentUri", uri?.toString().toString())
                    return uri
                }
            }
        }
        return null
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WorkManagerJetpackComposeTheme {
        Greeting("Android")
    }
}