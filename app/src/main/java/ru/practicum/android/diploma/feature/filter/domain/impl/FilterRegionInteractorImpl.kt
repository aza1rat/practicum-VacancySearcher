package ru.practicum.android.diploma.feature.filter.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.feature.filter.domain.api.regions.FilterRegionsInteractor
import ru.practicum.android.diploma.feature.filter.domain.api.regions.FilterRegionsRepository
import ru.practicum.android.diploma.util.Resource

class FilterRegionInteractorImpl(private val filterRegionsRepository: FilterRegionsRepository) : FilterRegionsInteractor {
    override fun getAllRegions(): Flow<Resource<List<String>>> {
        return filterRegionsRepository.getAllRegions()
    }
}
