package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.db.data.FavoriteRepositoryImpl
import ru.practicum.android.diploma.feature.favorite.domain.api.FavoriteRepository

val repositoryModule = module {

    single<FavoriteRepository> {
        FavoriteRepositoryImpl(get())
    }

}
