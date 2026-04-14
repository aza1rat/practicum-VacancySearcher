package ru.practicum.android.diploma.feature.vacancy.data.headersearch

import ru.practicum.android.diploma.feature.vacancy.data.HeaderSearcher
import ru.practicum.android.diploma.feature.vacancy.data.model.HeaderFraming

class HeaderSearcherImpl : HeaderSearcher {
    private val regexSingleNewline = Regex("\\n[^\\n]+?:\\n")
    private val regexDoubleNewline = Regex("\\n\\n")
    private class HeaderFramingResolver(private val description: String) {
        fun resolve(colonExampleResult: MatchResult): HeaderFraming {
            require(colonExampleResult.range.first > 0) { "Invalid example: it can't be at the start of description" }
            val exampleFirstSymbol = description[colonExampleResult.range.first]
            val exampleLastSymbol = description[colonExampleResult.range.last]
            require(
                exampleFirstSymbol == '\n' && exampleLastSymbol == '\n'
            ) { "Invalid example: it should start and end with new line" }
            require(
                colonExampleResult.range.first - 1 > 0 && colonExampleResult.range.last + 1 < description.length
            ) { "Out of bounds: Cause example range" }
            val symbolBeforeExample = description[colonExampleResult.range.first + SYMBOL_LEFT_OFFSET]
            val symbolAfterExample = description[colonExampleResult.range.last + SYMBOL_RIGHT_OFFSET]
            return when {
                symbolBeforeExample == '\n' && symbolAfterExample != '\n' -> HeaderFraming.DOUBLE_LEFT_NEW_LINE
                symbolBeforeExample != '\n' && symbolAfterExample == '\n' -> HeaderFraming.DOUBLE_RIGHT_NEW_LINE
                symbolBeforeExample == '\n' && symbolAfterExample == '\n' -> HeaderFraming.DOUBLE_NEW_LINE
                else -> HeaderFraming.SINGLE_NEW_LINE
            }
        }

        fun getDoubleNewLineMaxCount(headerFraming: HeaderFraming, doubleNewLineSize: Int): Int {
            return if (headerFraming != HeaderFraming.DOUBLE_NEW_LINE) {
                doubleNewLineSize
            } else {
                doubleNewLineSize * 2
            }
        }
    }

    override fun getHeaders(description: String): List<IntRange> {
        val resolver = HeaderFramingResolver(description)
        val result = mutableListOf<IntRange>()
        val resultsSingleNewline = regexSingleNewline.findAll(description).toList()
        if (resultsSingleNewline.isEmpty()) return emptyList()
        val resultDoubleNewline = regexDoubleNewline.findAll(description).toMutableList()
        val headerFraming = resolver.resolve(resultsSingleNewline[0])
        if (resultDoubleNewline.size != resolver.getDoubleNewLineMaxCount(
                headerFraming, resultsSingleNewline.size)
        ) {
            val doubleNewlineFiltered = filterDoubleNewline(resultsSingleNewline, resultDoubleNewline, headerFraming)
            if (doubleNewlineFiltered.isNotEmpty()) {
                val firstHeaderFounded = findFirstHeader(description, doubleNewlineFiltered[0], headerFraming, result)
                findDoubleNewlineHeaders(firstHeaderFounded, description, doubleNewlineFiltered, headerFraming, result)
            }
        }
        for (resultSingle in resultsSingleNewline) {
            val doubledNewline = headerFraming == HeaderFraming.DOUBLE_NEW_LINE
            val symbolLeftOffset = if (headerFraming == HeaderFraming.DOUBLE_LEFT_NEW_LINE || doubledNewline) {
                SYMBOL_LEFT_OFFSET
            } else {
                0
            }
            val symbolRightOffset = if (headerFraming == HeaderFraming.DOUBLE_RIGHT_NEW_LINE || doubledNewline) {
                SYMBOL_RIGHT_OFFSET
            } else {
                0
            }
            result.add(
                IntRange(
                    resultSingle.range.first + symbolLeftOffset,
                    resultSingle.range.last + symbolRightOffset
                )
            )
        }
        return result
    }

    private fun filterDoubleNewline(
        resultsSingleNewline: List<MatchResult>,
        resultDoubleNewline: List<MatchResult>,
        headerFraming: HeaderFraming
    ): List<MatchResult> {
        val singleNewlineStartIndexes = resultsSingleNewline.map {
            when (headerFraming) {
                HeaderFraming.DOUBLE_LEFT_NEW_LINE, HeaderFraming.DOUBLE_NEW_LINE -> it.range.first - 1
                HeaderFraming.DOUBLE_RIGHT_NEW_LINE -> it.range.last
                HeaderFraming.SINGLE_NEW_LINE -> -1
            }
        }
        val singleNewlineEndInexes = mutableListOf(-1)
        if (headerFraming == HeaderFraming.DOUBLE_NEW_LINE) {
            for (result in resultsSingleNewline) {
                singleNewlineEndInexes.add(result.range.last + 1)
            }
        }
        return resultDoubleNewline.filter {
            it.range.first !in singleNewlineStartIndexes && it.range.last !in singleNewlineEndInexes
        }
    }

    private fun findFirstHeader(
        description: String,
        doubleNewlineFirstExample: MatchResult,
        headerFraming: HeaderFraming,
        result: MutableList<IntRange>
    ): Boolean {
        val firstPartDescription = description.take(doubleNewlineFirstExample.range.last)
        val founded = when (headerFraming) {
            HeaderFraming.DOUBLE_NEW_LINE, HeaderFraming.DOUBLE_LEFT_NEW_LINE -> {
                val spaceCount = firstPartDescription.count { it == ' ' }
                if (spaceCount < SPACE_IN_HEADER_MAX_COUNT) {
                    result.add(IntRange(0, doubleNewlineFirstExample.range.last))
                    true
                } else {
                    false
                }
            }

            else -> {
                val foundedRange = headerFraming.regex.find(firstPartDescription)
                if (foundedRange == null) {
                    result.add(IntRange(0, doubleNewlineFirstExample.range.last))
                    true
                } else {
                    false
                }
            }
        }
        return founded
    }

    private fun findDoubleNewlineHeaders(
        firstHeaderFounded: Boolean,
        description: String,
        doubleNewlineFiltered: List<MatchResult>,
        headerFraming: HeaderFraming,
        result: MutableList<IntRange>
    ) {
        val start = if (firstHeaderFounded) 1 else 0
        val stepValue = if (headerFraming == HeaderFraming.DOUBLE_NEW_LINE) 2 else 1
        for (i in start until doubleNewlineFiltered.size step stepValue) {
            if (stepValue == 2 && i + 1 < doubleNewlineFiltered.size) {
                val subDescription = description.substring(
                    doubleNewlineFiltered[i].range.first,
                    doubleNewlineFiltered[i + 1].range.last + 1
                )
                val foundedRange = headerFraming.regex.find(subDescription)
                foundedRange?.let {
                    result.add(
                        IntRange(
                            doubleNewlineFiltered[i].range.first,
                            doubleNewlineFiltered[i].range.first + foundedRange.range.last
                        )
                    )
                }
            } else {
                val foundedRange = headerFraming.regex.find(description, doubleNewlineFiltered[i].range.first)
                foundedRange?.let { result.add(it.range) }
            }
        }
    }

    companion object {
        private const val SYMBOL_LEFT_OFFSET = -1
        private const val SYMBOL_RIGHT_OFFSET = 1
        private const val SPACE_IN_HEADER_MAX_COUNT = 5
    }
}
