package ru.practicum.android.diploma.feature.filter.presentation.states

sealed class FilterRegionState {
    object Loading : FilterRegionState()
    data class Content(val regions: List<String>) : FilterRegionState()
    data class Error(val errorMessage: String) : FilterRegionState()
}
