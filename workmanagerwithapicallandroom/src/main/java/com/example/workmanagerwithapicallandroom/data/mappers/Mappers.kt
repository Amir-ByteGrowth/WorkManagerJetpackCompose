package com.example.workmanagerwithapicallandroom.data.mappers

import com.example.workmanagerwithapicallandroom.data.model.QuoteDTO
import com.example.workmanagerwithapicallandroom.domain.models.Quote

fun QuoteDTO.toDomain(workType: String): Quote {
    return Quote(author, id, quote, workType)
}