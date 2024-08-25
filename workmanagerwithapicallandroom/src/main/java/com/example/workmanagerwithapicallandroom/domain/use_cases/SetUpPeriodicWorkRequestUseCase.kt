package com.example.workmanagerwithapicallandroom.domain.use_cases

import com.example.workmanagerwithapicallandroom.domain.repository.QuoteRepository
import javax.inject.Inject

class SetUpPeriodicWorkRequestUseCase @Inject constructor(private val quoteRepository: QuoteRepository) {

    operator fun invoke() = quoteRepository.setPeriodicWorkRequest()
}