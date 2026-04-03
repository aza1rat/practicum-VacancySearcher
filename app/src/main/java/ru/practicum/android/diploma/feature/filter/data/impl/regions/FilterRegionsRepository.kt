package ru.practicum.android.diploma.feature.filter.data.impl.regions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.feature.filter.domain.api.regions.FilterRegionsRepository
import ru.practicum.android.diploma.feature.search.data.NetworkClient
import ru.practicum.android.diploma.util.Resource

class FilterRegionsRepositoryImpl(private val networkClient: NetworkClient) : FilterRegionsRepository {
    override fun getAllRegions(): Flow<Resource<List<String>>> = flow {
        networkClient
    }
}
