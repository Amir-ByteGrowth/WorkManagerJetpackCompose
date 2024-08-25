package com.example.workmanagerjetpackcompose

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.net.URI
import java.util.UUID

class MainViewModel :ViewModel() {
    var unCompressedUri : Uri? by mutableStateOf(null)
        private set

    var compressedBitmap : Bitmap? by mutableStateOf(null)
        private set

    var workerId : UUID? by mutableStateOf(null)
        private set

    fun updateUnCompressedUri(uri: Uri?){
        this.unCompressedUri =uri
    }
    fun updateCompressedBitmap(bitmap: Bitmap?){
        this.compressedBitmap = bitmap
    }

    fun updateWorkerId (uuid: UUID?){
        this.workerId=uuid
    }

}