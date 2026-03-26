package ru.practicum.android.diploma.di

import androidx.room.Room
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.db.data.AppDatabase
import ru.practicum.android.diploma.feature.search.domain.api.VacancyApiService
import ru.practicum.android.diploma.util.AuthInterceptor

const val VACANCY_BASE_URL = "https://android-diploma.education-services.ru/"

val dataModule = module {

    single<VacancyApiService> {
        Retrofit.Builder()
            .baseUrl(VACANCY_BASE_URL)
            .client(OkHttpClient.Builder().addInterceptor(AuthInterceptor(BuildConfig.API_ACCESS_TOKEN)).build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VacancyApiService::class.java)
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db").build()
    }

    single {
        (get() as AppDatabase).getVacancyDao()
    }
}
