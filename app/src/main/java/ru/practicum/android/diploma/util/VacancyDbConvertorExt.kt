package ru.practicum.android.diploma.util

import ru.practicum.android.diploma.db.data.entity.VacancyEntity
import ru.practicum.android.diploma.feature.search.domain.model.Vacancy

fun Vacancy.toDbEntity(): VacancyEntity {
    // !! нормальная реализация, когда будет класс Vacancy
    return VacancyEntity(
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        emptyList(),
        "",
        ""
    )
}

fun VacancyEntity.toVacancy(): Vacancy {
    // !! нормальная реализация
    return Vacancy()
}
