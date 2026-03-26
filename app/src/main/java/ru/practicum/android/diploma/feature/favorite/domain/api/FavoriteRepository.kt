package ru.practicum.android.diploma.feature.favorite.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.feature.search.domain.model.Vacancy

interface FavoriteRepository {
    suspend fun addToFavorite(vacancy: Vacancy)

    suspend fun removeFavoriteById(id: String)

    suspend fun getFavorites(offset: Int, limit: Int): Flow<List<Vacancy>>

    suspend fun getVacancyById(id: String): Vacancy

    suspend fun isFavorite(id: String): Boolean
}
