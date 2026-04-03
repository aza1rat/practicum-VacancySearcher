package ru.practicum.android.diploma.feature.filter.data.dao

sealed interface RegionRequestDto {

    interface WithoutName : RegionRequestDto


    class AllRegions : RegionRequestDto, WithoutName
}
