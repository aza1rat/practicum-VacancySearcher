package ru.practicum.android.diploma.feature.search.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.feature.filter.domain.model.SearchFilters
import ru.practicum.android.diploma.feature.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.feature.search.domain.api.SearchRepository
import ru.practicum.android.diploma.feature.search.domain.model.Vacancy

class SearchInteractorImpl(private val repository: SearchRepository): SearchInteractor {
    override fun searchVacancies(
        expression: String,
        filter: SearchFilters?,
        pager: Int
    ): Flow<List<Vacancy>> {
        return repository.searchVacancies(expression, filter, pager).map {
            it.data ?: emptyList()
        }
    }
}
