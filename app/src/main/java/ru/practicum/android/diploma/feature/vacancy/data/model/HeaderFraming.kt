package ru.practicum.android.diploma.feature.vacancy.data.model

enum class HeaderFraming : HeaderFramingRegex {
    DOUBLE_LEFT_NEW_LINE {
        override val regex: Regex = Regex("\\n\\n[^\\n]+?\\n")
    },
    DOUBLE_RIGHT_NEW_LINE {
        override val regex: Regex = Regex("\\n[^\\n]+?\\n\\n")
    },
    DOUBLE_NEW_LINE {
        override val regex: Regex = Regex("\\n\\n[^\\n]+?\\n\\n")
    },
    SINGLE_NEW_LINE {
        override val regex: Regex = Regex("\\n[^\\n]+?\\n")
    }
}
