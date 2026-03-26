package ru.practicum.android.diploma.db.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.db.data.dao.VacancyDao
import ru.practicum.android.diploma.feature.favorite.domain.api.FavoriteRepository
import ru.practicum.android.diploma.feature.search.domain.model.Vacancy
import ru.practicum.android.diploma.util.toDbEntity
import ru.practicum.android.diploma.util.toVacancy

class FavoriteRepositoryImpl(private val vacancyDao: VacancyDao) : FavoriteRepository {
    override suspend fun addToFavorite(vacancy: Vacancy) {
        vacancyDao.insert(vacancy.toDbEntity())
    }

    override suspend fun removeFavoriteById(id: String) {
        vacancyDao.remove(id)
    }

    override suspend fun getFavorites(offset: Int, limit: Int): Flow<List<Vacancy>> = flow {
        val vacancies = vacancyDao.getAllByPage(offset, limit)
        emit(vacancies.map { it.toVacancy() })
    }

    override suspend fun getVacancyById(id: String): Vacancy {
        return vacancyDao.getById(id).toVacancy()
    }

    override suspend fun isFavorite(id: String): Boolean {
        return vacancyDao.isFavorite(id)
    }
}
