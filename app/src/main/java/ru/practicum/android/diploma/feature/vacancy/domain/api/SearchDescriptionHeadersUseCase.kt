package ru.practicum.android.diploma.feature.vacancy.domain.api

interface SearchDescriptionHeadersUseCase {
    fun execute(description: String): List<IntRange>?
}
