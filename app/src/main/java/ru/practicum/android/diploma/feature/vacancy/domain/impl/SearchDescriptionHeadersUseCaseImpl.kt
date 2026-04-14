package ru.practicum.android.diploma.feature.vacancy.domain.impl

import ru.practicum.android.diploma.feature.vacancy.domain.api.HeaderSearchRepository
import ru.practicum.android.diploma.feature.vacancy.domain.api.SearchDescriptionHeadersUseCase

class SearchDescriptionHeadersUseCaseImpl(private val repository: HeaderSearchRepository) :
    SearchDescriptionHeadersUseCase {
    override fun execute(description: String): List<IntRange>? {
        return repository.getHeaders(description)
    }
}
