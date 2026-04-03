package ru.practicum.android.diploma.feature.filter.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.feature.filter.presentation.states.FilterRegionState

class FilterRegionViewmodel() : ViewModel() {

    private val regionLiveData = MutableLiveData<FilterRegionState>()
    fun observeRegionLiveData(): LiveData<FilterRegionState> = regionLiveData

    fun getRegions(){

    }
}
