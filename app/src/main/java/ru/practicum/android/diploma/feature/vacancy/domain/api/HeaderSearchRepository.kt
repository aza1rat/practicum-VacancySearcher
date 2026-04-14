package ru.practicum.android.diploma.feature.vacancy.domain.api

interface HeaderSearchRepository {
    fun getHeaders(description: String): List<IntRange>?
}
