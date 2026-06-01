package ru.practicum.android.diploma.feature.search.data.mapper

import ru.practicum.android.diploma.feature.search.data.dto.VacancyPreviewDto
import ru.practicum.android.diploma.feature.vacancy.domain.model.Address
import ru.practicum.android.diploma.feature.vacancy.domain.model.Employer
import ru.practicum.android.diploma.feature.vacancy.domain.model.Salary
import ru.practicum.android.diploma.feature.vacancy.domain.model.Vacancy

object VacancyPreviewMapper {
    fun map(vacancyPreviewDto: VacancyPreviewDto): Vacancy {
        return Vacancy(
            id = vacancyPreviewDto.id,
            name = vacancyPreviewDto.name,
            salary = vacancyPreviewDto.salary?.let { Salary(it.id, it.currency, it.from, it.to) },
            address = vacancyPreviewDto.city?.let { Address(city = it) },
            employer = vacancyPreviewDto.company?.let { Employer(name = it, logo = vacancyPreviewDto.logo) }
        )
    }
}
