package ru.practicum.android.diploma.feature.vacancy.data

interface HeaderSearcher {
    fun getHeaders(description: String): List<IntRange>
}
