package ru.practicum.android.diploma.feature.filter.data.impl

import com.google.gson.Gson
import ru.practicum.android.diploma.feature.filter.data.StorageClient
import ru.practicum.android.diploma.feature.filter.data.storage.SharedPrefsStorageClient
import ru.practicum.android.diploma.feature.filter.domain.api.FiltersGettingRepository
import ru.practicum.android.diploma.feature.filter.domain.model.AreaCountry
import ru.practicum.android.diploma.feature.filter.domain.model.AreaRegion
import ru.practicum.android.diploma.feature.filter.domain.model.FilterIndustry
import ru.practicum.android.diploma.feature.filter.domain.model.Filters
import java.lang.reflect.Type

class FiltersGettingRepositoryImpl(
    private val storageClient: StorageClient<String>,
    private val gson: Gson,
    private val areaCountryType: Type,
    private val areaRegionType: Type,
    private val industryType: Type
) : FiltersGettingRepository {
    override fun getCountry(): AreaCountry? {
        val str = storageClient.getData<String>(SharedPrefsStorageClient.AREA_COUNTRY_KEY)
        return if (str != null) {
            gson.fromJson<AreaCountry>(str, areaCountryType)
        } else {
            null
        }
    }

    override fun getRegion(): AreaRegion? {
        val str = storageClient.getData<String>(SharedPrefsStorageClient.AREA_REGION_KEY)
        return if (str != null) {
            gson.fromJson<AreaRegion>(str, areaRegionType)
        } else {
            null
        }
    }

    override fun getIndustry(): FilterIndustry? {
        val str = storageClient.getData<String>(SharedPrefsStorageClient.INDUSTRY_KEY)
        return if (str != null) {
            gson.fromJson<FilterIndustry>(str, industryType)
        } else {
            null
        }
    }

    override fun getSalary(): Int? {
        return storageClient.getData<Int>(SharedPrefsStorageClient.SALARY_KEY)
    }

    override fun getIsOnlyWithSalary(): Boolean? {
        return storageClient.getData<Boolean>(SharedPrefsStorageClient.ONLY_WITH_SALARY_KEY)
    }

    override fun getAllFilters(): Filters? {
        var nullable = true
        val areaCountry = getCountry()
        val areaRegion = getRegion()
        val industry = getIndustry()
        val salary = getSalary()
        val isOnlyWithSalary = getIsOnlyWithSalary()
        val filterList = listOf(
            areaCountry,
            areaRegion,
            industry,
            salary,
            isOnlyWithSalary
        )
        filterList.forEach {
            if (it != null) nullable = false
        }
        return if (nullable) {
            null
        } else {
            Filters(
                areaCountry = areaCountry,
                areaRegion = areaRegion,
                industry = industry,
                salary = salary,
                isOnlyWithSalary = isOnlyWithSalary
            )
        }
    }

}
