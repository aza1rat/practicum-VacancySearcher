package ru.practicum.android.diploma.feature.filter.data.dao

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.feature.search.data.dto.Response

class RegionResponse(): Response() {
    @SerializedName("id")
    val id: String? = null
    @SerializedName("parent_id")
    val parentId: String? = null
    @SerializedName("name")
    val name: String? = null
    @SerializedName("areas")
    val areas: List<RegionResponse>? = null
}
