package com.example.workmanagerwithapicallandroom.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Quote(
    val author: String,
    @PrimaryKey
    val id: Int,
    val quote: String,
    val workType: String = "",
    val time: Long = System.currentTimeMillis(),
)
