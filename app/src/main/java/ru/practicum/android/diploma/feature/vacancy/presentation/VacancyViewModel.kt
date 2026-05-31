package ru.practicum.android.diploma.feature.vacancy.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.feature.favorite.domain.api.FavoriteInteractor
import ru.practicum.android.diploma.feature.vacancy.domain.api.VacancyInteractor
import ru.practicum.android.diploma.feature.vacancy.domain.model.Vacancy
import ru.practicum.android.diploma.feature.vacancy.domain.model.VacancyFormattedDescription
import ru.practicum.android.diploma.util.Resource

class VacancyViewModel(
    private val vacancyInteractor: VacancyInteractor,
    private val favoriteInteractor: FavoriteInteractor
) : ViewModel() {

    private val _vacancyDetail = MutableLiveData<VacancyState>()
    fun observeVacancyDetail(): LiveData<VacancyState> = _vacancyDetail
    private var isFavorite = false
    private var formattedDescription: VacancyFormattedDescription? = null

    fun getVacancyDetail(id: String) {
        _vacancyDetail.postValue(VacancyState.Loading)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                isFavorite = favoriteInteractor.isFavorite(id)
            }

            vacancyInteractor.getVacancyDetail(id).collect {
                when (it) {
                    is Resource.Error -> _vacancyDetail.postValue(VacancyState.Error(it.message!!))
                    is Resource.Success -> _vacancyDetail.postValue(
                        VacancyState.Content(
                            it.data!!,
                            getFormattedDescription(it.data.description),
                            isFavorite
                        )
                    )
                }
            }

        }
    }

    fun getVacancyFromDatabase(id: String) {
        _vacancyDetail.postValue(VacancyState.Loading)
        viewModelScope.launch {
            var vacancy: Vacancy? = null
            withContext(Dispatchers.IO) {
                vacancy = favoriteInteractor.getFromFavoritesById(id)
            }
            vacancy?.apply {
                isFavorite = true
                _vacancyDetail.postValue(
                    VacancyState.Content(
                        this,
                        getFormattedDescription(description),
                        isFavorite
                    )
                )
            } ?: run {
                _vacancyDetail.postValue(VacancyState.Error(DATABASE_ERROR))
            }
        }
    }

    fun sendVacancyViaMessenger(url: String) {
        viewModelScope.launch {
            vacancyInteractor.sendVacancyViaMessenger(url)
        }
    }

    fun selectEmailClientAndSend(email: String) {
        viewModelScope.launch {
            vacancyInteractor.selectEmailClientAndSend(email)
        }
    }

    fun showCallAppsAndDial(phoneNumber: String) {
        viewModelScope.launch {
            vacancyInteractor.showCallAppsAndDial(phoneNumber)
        }
    }

    fun handleFavorites(vacancy: Vacancy) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (isFavorite) {
                    favoriteInteractor.removeFromFavorites(vacancy.id)
                } else {
                    favoriteInteractor.addToFavorites(vacancy)
                }
                isFavorite = favoriteInteractor.isFavorite(vacancy.id)
            }
            _vacancyDetail.postValue(VacancyState.Content(
                vacancy,
                getFormattedDescription(vacancy.description),
                isFavorite
            ))
        }
    }

    private fun getFormattedDescription(description: String?): VacancyFormattedDescription? {
        if (formattedDescription != null) {
            return formattedDescription
        } else {
            formattedDescription = description?.let {
                vacancyInteractor.getVacancyFormattedDescription(it)
            }
            return formattedDescription
        }
    }

    companion object {
        const val DATABASE_ERROR = "DATABASE_ERROR"
    }
}
