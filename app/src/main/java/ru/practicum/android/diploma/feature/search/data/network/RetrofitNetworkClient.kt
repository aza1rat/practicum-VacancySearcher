package ru.practicum.android.diploma.feature.search.data.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.feature.search.data.NetworkClient
import ru.practicum.android.diploma.feature.search.data.dto.RequestDto
import ru.practicum.android.diploma.feature.search.data.dto.Response
import ru.practicum.android.diploma.feature.search.domain.api.VacancyApiService
import ru.practicum.android.diploma.util.ConnectionChecker

class RetrofitNetworkClient(
    private val connectionChecker: ConnectionChecker,
    private val vacancyApiService: VacancyApiService
) : NetworkClient {

    override suspend fun doRequest(dto: RequestDto): Response {
        if (!connectionChecker.isConnected()) {
            return Response().apply { code = NO_INTERNET_CODE }
        }
        if (dto !is RequestDto.WithParams && dto !is RequestDto.WithPathId) {
            return Response().apply { code = BAD_REQUEST_CODE }
        }
        return withContext(Dispatchers.IO) {
            try {
                val response = when (dto) {
                    is RequestDto.WithParams -> vacancyApiService.searchVacancies(dto.params)
                    is RequestDto.WithPathId -> {/*
                        реализация поиска по id вакансии для эпика 2
                        vacancyDetailsApiService.getVacancy(dto.id)
                    */}
                }
                Response().apply {
                    code = SUCCESS_CODE
                }
            } catch (e: Throwable) {
                Response().apply {
                    code = SERVER_ERROR_CODE
                }
            }
        }
    }

    companion object {
        private const val NO_INTERNET_CODE = -1
        private const val BAD_REQUEST_CODE = 400
        private const val SUCCESS_CODE = 200
        private const val SERVER_ERROR_CODE = 500
    }
}
