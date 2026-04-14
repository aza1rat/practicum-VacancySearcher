package ru.practicum.android.diploma.feature.vacancy.data.impl

import android.util.Log
import ru.practicum.android.diploma.feature.vacancy.data.HeaderSearcher
import ru.practicum.android.diploma.feature.vacancy.domain.api.HeaderSearchRepository

class HeaderSearchRepositoryImpl(private val searcher: HeaderSearcher) : HeaderSearchRepository {
    override fun getHeaders(description: String): List<IntRange>? {
        try {
            val headers = searcher.getHeaders(description)
            return headers
        } catch (e: IllegalArgumentException) {
            Log.e(TAG, e.message.toString())
        }
        return null
    }

    companion object {
        private const val TAG = "HeaderSearchRepository"
    }
}
