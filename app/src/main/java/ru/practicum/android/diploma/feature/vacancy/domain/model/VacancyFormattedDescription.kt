package ru.practicum.android.diploma.feature.vacancy.domain.model

class VacancyFormattedDescription {
    var description = ""
    private val _formatRanges = mutableListOf<Pair<TextFormatType, IntRange>>()
    val formatRanges: List<Pair<TextFormatType, IntRange>> get() = _formatRanges

    fun addFormatRange(textFormatType: TextFormatType, range: IntRange) {
        _formatRanges.add(
            Pair(textFormatType, range)
        )
    }

    enum class TextFormatType {
        HEADER, LIST_ITEM
    }
}
