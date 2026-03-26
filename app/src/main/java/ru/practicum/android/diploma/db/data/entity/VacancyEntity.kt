package ru.practicum.android.diploma.db.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

// Все сложносоставные поля решил пока хранить как строки, т.к. они нужны только для отображения
@Entity(tableName = "favorite_vacancies_table")
data class VacancyEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val salary: String,
    val address: String,
    val experience: String,
    val schedule: String,
    val employment: String,
    val contacts: String,
    val description: String,
    val employer: String,
    val area: String,
    val skills: List<String>,
    val url: String,
    val industry: String
)

class Converters {
    @TypeConverter
    fun fromList(list: List<String>): String = Json.encodeToString(list)

    @TypeConverter
    fun toList(string: String): List<String> = Json.decodeFromString<List<String>>(string)
}
