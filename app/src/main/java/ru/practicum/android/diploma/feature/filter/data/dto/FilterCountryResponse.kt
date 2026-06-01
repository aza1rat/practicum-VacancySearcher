package ru.practicum.android.diploma.feature.filter.data.dto

import ru.practicum.android.diploma.feature.search.data.dto.Response
import ru.practicum.android.diploma.feature.vacancy.data.dto.AreaDto

class FilterCountryResponse(
    val areas: List<AreaDto>
) : Response()
