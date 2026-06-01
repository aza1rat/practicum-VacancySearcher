package ru.practicum.android.diploma.feature.search.data.dto

import ru.practicum.android.diploma.feature.vacancy.data.dto.SalaryDto

data class VacancyPreviewDto(
    val id: String,
    val name: String,
    val company: String?,
    val city: String?,
    val salary: SalaryDto?,
    val logo: String?
)
