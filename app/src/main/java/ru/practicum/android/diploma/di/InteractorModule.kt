package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.feature.favorite.domain.api.FavoriteInteractor
import ru.practicum.android.diploma.feature.favorite.domain.impl.FavoriteInteractorImpl

val interactorModule = module {

    single<FavoriteInteractor> {
        FavoriteInteractorImpl(get())
    }
}
