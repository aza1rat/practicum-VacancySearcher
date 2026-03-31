package ru.practicum.android.diploma.feature.search.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.query
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.feature.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.feature.search.presentation.model.SearchState
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.util.debounce

class SearchViewModel(private val searchInteractor: SearchInteractor) : ViewModel() {
    private val searchState = MutableLiveData<SearchState>()
    fun observeSearchState(): LiveData<SearchState> = searchState

    private var searchText = ""
    private var listVisible = false
    private var currentPage = 1
    private var maxPage = -1
    private val searchDebounce = debounce<String>(DEBOUNCE_DELAY, viewModelScope, true) { query ->
        if (query.isNotEmpty() && query.isNotBlank())
            doRequest(query)

    }

    fun onSearchTextChanged(text: String) {
        if (text == searchText) return
        searchText = text
        if (text.isEmpty()) {
            searchState.value = SearchState.Idle
            listVisible = false
        }
        if (text.isNotEmpty() && !listVisible)
            searchState.value = SearchState.InputStarted
        searchDebounce.invoke(text)
    }

    private fun doRequest(query: String) {
        searchState.value = SearchState.Loading
        viewModelScope.launch {
            searchInteractor.searchVacancies(query,null,currentPage).collect { vacancyList ->
                when(vacancyList) {
                    is Resource.Success -> {
                        val data = vacancyList.data!!
                        if (data.vacancies.isEmpty()) {
                            searchState.value = SearchState.EmptyResultError
                        } else {
                            searchState.value = SearchState.Result(data.vacancies, data.found)
                            maxPage = vacancyList.data.pages
                            listVisible = true
                        }
                    }
                    is Resource.Error -> {

                    }
                }
            }
        }
    }

    companion object {
        private const val DEBOUNCE_DELAY = 2000L
    }
}
