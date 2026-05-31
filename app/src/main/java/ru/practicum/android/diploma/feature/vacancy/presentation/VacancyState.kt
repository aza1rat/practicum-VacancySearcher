package ru.practicum.android.diploma.feature.vacancy.presentation

import ru.practicum.android.diploma.feature.vacancy.domain.model.Vacancy
import ru.practicum.android.diploma.feature.vacancy.domain.model.VacancyFormattedDescription

sealed class VacancyState {
    object Loading : VacancyState()
    data class Content(
        val content: Vacancy,
        val formattedDescription: VacancyFormattedDescription?,
        val isFavorite: Boolean
    ) : VacancyState()
    data class Error(val errorMessage: String) : VacancyState()
}
