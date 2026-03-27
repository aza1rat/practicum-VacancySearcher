package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.feature.vacancy.domain.api.VacancyInteractor
import ru.practicum.android.diploma.feature.vacancy.domain.impl.VacancyInteractorImpl

val interactorModule = module {
    single<VacancyInteractor> {
        VacancyInteractorImpl(get())
    }

}
