package ru.practicum.android.diploma.feature.filter.domain.api

import ru.practicum.android.diploma.feature.filter.domain.model.Filters

interface FilterSettingsInteractor {
    fun getAllFilters(): Filters?

    fun setSalary(salary: Int)

    fun setIsOnlyWithSalary(isOnlyWithSalary: Boolean)
}
