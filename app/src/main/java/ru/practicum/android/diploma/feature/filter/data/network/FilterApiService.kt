package ru.practicum.android.diploma.feature.filter.data.network

import retrofit2.http.GET
import ru.practicum.android.diploma.feature.filter.data.dao.RegionDto

interface FilterApiService {

    @GET("areas")
    suspend fun getRegions(): List<RegionDto>
}
