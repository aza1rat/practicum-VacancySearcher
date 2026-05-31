package ru.practicum.android.diploma.feature.vacancy.data.description

import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlHandler
import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlParser
import ru.practicum.android.diploma.feature.vacancy.domain.model.VacancyFormattedDescription

class VacancyDescriptionParser {
    fun parse(description: String): VacancyFormattedDescription {
        var lastTag = ""
        var position = 0
        val result = VacancyFormattedDescription()
        val htmlHandler = KsoupHtmlHandler.Builder()
            .onOpenTag { name, _, _ ->
                lastTag = name
            }
            .onText { text ->
                result.description += modifyText(lastTag, text)
                if (lastTag == TAG_HEADER_LVL_3) {
                    result.addFormatRange(
                        VacancyFormattedDescription.TextFormatType.HEADER,
                        calculateRange(position, result.description.length)
                    )
                }
                if (lastTag == TAG_LIST_ITEM) {
                    result.addFormatRange(
                        VacancyFormattedDescription.TextFormatType.LIST_ITEM,
                        calculateRange(position, result.description.length)
                    )
                }
                position = result.description.length
            }
            .build()
        val parser = KsoupHtmlParser(htmlHandler)
        parser.write(description)
        result.description = result.description.dropLast(1)
        parser.end()
        return result
    }

    private fun modifyText(lastTag: String, source: String): String {
        return when (lastTag) {
            TAG_HEADER_LVL_2 -> ""
            TAG_HEADER_LVL_3 -> "\n$source\n"
            else -> "$source\n"
        }
    }

    private fun calculateRange(position: Int, length: Int): IntRange {
        return IntRange(position, length - 1)
    }

    companion object {
        private const val TAG_HEADER_LVL_2 = "h2"
        private const val TAG_HEADER_LVL_3 = "h3"
        private const val TAG_LIST_ITEM = "li"
    }
}
